/**
 * 
 */
package com.wurrly.server.handlers;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.lang.model.element.Modifier;
import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jsoniter.spi.TypeLiteral;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.wurrly.modules.RoutingModule;
import com.wurrly.server.Extractors;
import com.wurrly.server.ServerRequest;
import com.wurrly.server.ServerResponse;
import com.wurrly.server.endpoints.EndpointInfo;

import io.swagger.annotations.Api;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import net.openhft.compiler.CompilerUtils;

/**
 * @author jbauer
 */
public class HandlerGenerator
{

	private static Logger log = LoggerFactory.getLogger(HandlerGenerator.class.getCanonicalName());

	private static final Pattern TYPE_NAME_PATTERN = Pattern.compile("(java\\.util\\.[A-Za-z]+)<([^>]+)", Pattern.DOTALL | Pattern.UNIX_LINES);

	public enum StatementParameterType
	{
		STRING, LITERAL, TYPE, RAW
	}

	public enum TypeHandler
	{

		LongType("Long $L = $T.longValue(exchange,$S)", false, StatementParameterType.LITERAL,com.wurrly.server.Extractors.class, StatementParameterType.STRING),
		IntegerType("Integer $L = $T.integerValue(exchange,$S)", false, StatementParameterType.LITERAL,com.wurrly.server.Extractors.class, StatementParameterType.STRING),
		StringType("String $L =  $T.string(exchange,$S)", false, StatementParameterType.LITERAL,com.wurrly.server.Extractors.class, StatementParameterType.STRING),
		BooleanType("Boolean $L =  $T.booleanValue(exchange,$S)", false, StatementParameterType.LITERAL, com.wurrly.server.Extractors.class, StatementParameterType.STRING),
		FilePathType("$T $L = $T.filePath(exchange,$S)", true, java.nio.file.Path.class, StatementParameterType.LITERAL,com.wurrly.server.Extractors.class, StatementParameterType.STRING),
		AnyType("$T $L = $T.any(exchange)", true, com.jsoniter.any.Any.class, StatementParameterType.LITERAL,com.wurrly.server.Extractors.class),
		JsonIteratorType("$T $L = $T.jsonIterator(exchange)", true, com.jsoniter.JsonIterator.class, StatementParameterType.LITERAL,com.wurrly.server.Extractors.class),
		ModelType("$T $L = com.wurrly.server.Extractors.typed(exchange,$L)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.LITERAL),
		EnumType("$T $L = $T.enumValue(exchange,$T.class,$S)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL,com.wurrly.server.Extractors.class, StatementParameterType.TYPE, StatementParameterType.STRING),
		ByteBufferType("$T $L =  $T.fileBytes(exchange,$S)", false, java.nio.ByteBuffer.class, StatementParameterType.LITERAL,com.wurrly.server.Extractors.class, StatementParameterType.STRING),
		DateType("$T $L =  $T.date(exchange,$S)", false, java.util.Date.class, StatementParameterType.LITERAL, com.wurrly.server.Extractors.class, StatementParameterType.STRING),
		FloatType("Integer $L = $T.floatValue(exchange,$S)", false, StatementParameterType.LITERAL, com.wurrly.server.Extractors.class, StatementParameterType.STRING),
		DoubleType("Integer $L = $T.doubleValue(exchange,$S)", false, StatementParameterType.LITERAL, com.wurrly.server.Extractors.class, StatementParameterType.STRING),
		
		ValueOfType("$T $L = $T.valueOf($T.string(exchange,$S))", false, StatementParameterType.TYPE, StatementParameterType.LITERAL,  StatementParameterType.TYPE, com.wurrly.server.Extractors.class,   StatementParameterType.STRING),
		FromStringType("$T $L = $T.fromString($T.string(exchange,$S))", false, StatementParameterType.TYPE, StatementParameterType.LITERAL,StatementParameterType.TYPE,  com.wurrly.server.Extractors.class, StatementParameterType.STRING),

		ListType("$T<$T> $L = exchange.getQueryParameters().get($S).stream().map($T::valueOf).collect(java.util.stream.Collectors.toList())",false, java.util.List.class,StatementParameterType.RAW, StatementParameterType.LITERAL, StatementParameterType.STRING, StatementParameterType.RAW  ),
		
		HeaderValueOfType("$T $L = $T.valueOf($T.string(exchange,$S))", false, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.TYPE, com.wurrly.server.Extractors.Header.class, StatementParameterType.STRING),
		HeaderFromStringType("$T $L = $T.fromString($T.string(exchange,$S))", false, StatementParameterType.TYPE, StatementParameterType.LITERAL, StatementParameterType.TYPE,com.wurrly.server.Extractors.Header.class,  StatementParameterType.STRING),
		HeaderStringType("$T $L = $T.string(exchange,$S)", false, java.lang.String.class, StatementParameterType.LITERAL,com.wurrly.server.Extractors.Header.class, StatementParameterType.STRING),

		OptionalHeaderValueOfType("$T<$T> $L = $T.string(exchange,$S).map($T::valueOf)", false,Optional.class, StatementParameterType.RAW, StatementParameterType.LITERAL,   com.wurrly.server.Extractors.Header.Optional.class, StatementParameterType.STRING,StatementParameterType.RAW),
		OptionalHeaderFromStringType("$T<$T> $L = $T.string(exchange,$S).map($T::fromString)", false, Optional.class, StatementParameterType.RAW, StatementParameterType.LITERAL, com.wurrly.server.Extractors.Header.Optional.class,  StatementParameterType.STRING,StatementParameterType.RAW),
		OptionalHeaderStringType("$T<$T> $L = $T.string(exchange,$S)", false, Optional.class,  java.lang.String.class, StatementParameterType.LITERAL,com.wurrly.server.Extractors.Header.Optional.class, StatementParameterType.STRING),

		
		
		OptionalJsonIteratorType("$T<$T> $L = $T.jsonIterator(exchange)", true, Optional.class,   com.jsoniter.JsonIterator.class, StatementParameterType.LITERAL,com.wurrly.server.Extractors.Optional.class),
		OptionalAnyType("$T<$T> $L = $T.any(exchange)", true,  Optional.class,  com.jsoniter.any.Any.class, StatementParameterType.LITERAL,com.wurrly.server.Extractors.Optional.class),
		OptionalStringType("$T<String> $L = $T.string(exchange,$S)", false,  Optional.class,  StatementParameterType.LITERAL,com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING),
		OptionalLongType("$T<Long> $L = $T.longValue(exchange,$S)", false,   Optional.class, StatementParameterType.LITERAL, com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING),
		OptionalIntegerType("$T<Integer> $L = $T.integerValue(exchange,$S)", false,  Optional.class,  StatementParameterType.LITERAL, com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING),
		OptionalBooleanType("$T<Boolean> $L = $T.booleanValue(exchange,$S)", false,  Optional.class,  StatementParameterType.LITERAL,com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING),
		OptionalPathType("$T<$T> $L = $T.filePath(exchange,$S)", true,  Optional.class, java.nio.file.Path.class, StatementParameterType.LITERAL,com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING),
		OptionalFloatType("$T<Long> $L = $T.floatValue(exchange,$S)", false,   Optional.class, StatementParameterType.LITERAL, com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING),
		OptionalDoubleType("$T<Integer> $L = $T.doubleValue(exchange,$S)", false,  Optional.class,  StatementParameterType.LITERAL, com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING),
		
		OptionalDateType("$T<$T> $L = $T.date(exchange,$S)", false,  Optional.class, java.util.Date.class,  StatementParameterType.LITERAL, com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING),

		OptionalModelType("java.util.Optional<$L> $L = $T.typed(exchange,$L)", true,   StatementParameterType.LITERAL, StatementParameterType.LITERAL, com.wurrly.server.Extractors.Optional.class, StatementParameterType.LITERAL),

		OptionalValueOfType("$T<$T> $L = $T.string(exchange,$S).map($T::valueOf)", false, Optional.class, StatementParameterType.RAW, StatementParameterType.LITERAL,com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING, StatementParameterType.RAW),
		OptionalFromStringType("$T<$T> $L = $T.string(exchange,$S).map($T::fromString)", false, Optional.class, StatementParameterType.RAW, StatementParameterType.LITERAL, com.wurrly.server.Extractors.Optional.class, StatementParameterType.STRING, StatementParameterType.RAW),

		//OptionalEnumType("$T $L = $T.enumValue(exchange,$T.class,$S)", true, StatementParameterType.TYPE, StatementParameterType.LITERAL, com.wurrly.server.Extractors.Optional.class, StatementParameterType.RAW, StatementParameterType.STRING),

		;

		public boolean isBlocking()
		{ 
			return this.isBlocking;
		}

		public String statement()
		{

			return this.statement;
		}

		final private String statement;
		final private boolean isBlocking;
		final private Object[] parameterTypes;

		TypeHandler(String statement, boolean isBlocking, Object... types)
		{
			this.statement = statement;
			this.isBlocking = isBlocking;
			this.parameterTypes = types;
		}

		public static void addStatement(MethodSpec.Builder builder, Parameter parameter, TypeHandler handler) throws Exception
		{ 
			Object[] args = new Object[handler.parameterTypes.length];

			for (int i = 0; i < handler.parameterTypes.length; i++)
			{
				if (handler.parameterTypes[i] instanceof StatementParameterType)
				{
					String pName = parameter.getName();
					
					if( parameter.isAnnotationPresent(QueryParam.class))
					{
						QueryParam qp = parameter.getAnnotation(QueryParam.class);
						pName = qp.value();
					}
					else if( parameter.isAnnotationPresent(HeaderParam.class))
					{
						HeaderParam hp = parameter.getAnnotation(HeaderParam.class);
						pName = hp.value();
					}
					else if( parameter.isAnnotationPresent(PathParam.class))
					{
						PathParam pp = parameter.getAnnotation(PathParam.class);
						pName = pp.value();
					}
					else if( parameter.isAnnotationPresent(CookieParam.class))
					{
						CookieParam cp = parameter.getAnnotation(CookieParam.class);
						pName = cp.value();
					}
					else if( parameter.isAnnotationPresent(FormParam.class))
					{
						FormParam fp = parameter.getAnnotation(FormParam.class);
						pName = fp.value();
					}
					
					
					StatementParameterType pType = (StatementParameterType) handler.parameterTypes[i];
					switch (pType)
					{
					case LITERAL:
						args[i] = parameter.getName();
						break;
					case STRING:
						args[i] = pName;
						break;
					case TYPE:
						args[i] = parameter.getParameterizedType();
						break;
					case RAW:
					{
						Type type = parameter.getParameterizedType();
						type = extractErasedType(type);
						args[i] = type;
						break;
					}
					default:
						break;
					}
				}
				else if (handler.parameterTypes[i] instanceof Class)
				{
					Class<?> clazz = (Class<?>) handler.parameterTypes[i];

					args[i] = clazz;
					
 				}  
			}

 			builder.addStatement(handler.statement, args);
		}
		
		public static void addStatement(MethodSpec.Builder builder, Parameter parameter) throws Exception
		{
			TypeHandler handler = forType(parameter.getParameterizedType());

			addStatement(builder,parameter,handler);
			
		}

		public static TypeHandler forType(Type type)
		{
			boolean isEnum = false;
			boolean hasValueOf = false;
			boolean hasFromString = false;
			boolean isOptional = type.getTypeName().contains("java.util.Optional");
			boolean isArray = type.getTypeName().contains("java.util.List");

			if(!isOptional && !isArray)
			{
				try
				{
					Class<?> clazz = Class.forName(type.getTypeName());
	
					isEnum = clazz.isEnum();
	 
					hasValueOf = hasValueOfMethod(clazz);
					  
					hasFromString = hasFromStringMethod(clazz);
	 
				} catch (Exception e)
				{
					log.error(e.getMessage(),e);
				}
			}
			
			if( isArray && !isOptional )
			{
				return ListType;
			}
			
		//	log.debug("type: " + type.getTypeName() + " valueOf: " + hasValueOf + " fromString: " + hasFromString);

			if (type.equals(Long.class))
			{
				return LongType;
			}
			else if (type.equals(Integer.class))
			{
				return IntegerType;
			}
			else if (type.equals(Float.class))
			{
				return FloatType;
			}
			else if (type.equals(Double.class))
			{
				return DoubleType;
			}
			else if (type.equals(java.nio.ByteBuffer.class))
			{
				return ByteBufferType;
			}
			else if (type.equals(Boolean.class))
			{
				return BooleanType;
			}
			else if (type.equals(String.class))
			{
				return StringType;
			}
			else if (type.equals(java.nio.file.Path.class))
			{
				return FilePathType;
			}
			else if (type.equals(java.util.Date.class))
			{
				return DateType;
			}
			else if (type.equals(com.jsoniter.any.Any.class))
			{
				return AnyType;
			}
			else if (type.equals(com.jsoniter.JsonIterator.class))
			{
				return JsonIteratorType;
			}
			else if (isOptional)
			{
				if (type.getTypeName().contains("java.lang.Long"))
				{
					return OptionalLongType;
				}
				else if (type.getTypeName().contains("java.lang.String"))
				{
					return OptionalStringType;
				}
				else if (type.getTypeName().contains("java.util.Date"))
				{
					return OptionalDateType;
				}
				else if (type.getTypeName().contains("java.lang.Boolean"))
				{
					return OptionalBooleanType;
				}
				else if (type.getTypeName().contains("java.lang.Float"))
				{
					return OptionalFloatType;
				}
				else if (type.getTypeName().contains("java.lang.Double"))
				{
					return OptionalDoubleType;
				}
				else if (type.getTypeName().contains("java.lang.Integer"))
				{
					return OptionalIntegerType;
				}
				else if (type.getTypeName().contains("java.nio.file.Path"))
				{
					return OptionalPathType;
				} 
				else
				{
					try
					{
				 
						Class<?> erasedType = extractErasedType(type); 

						//log.debug("erasedType: " + erasedType.getTypeName() + " valueOf: " + hasValueOfMethod(erasedType) + " fromString: " + hasFromStringMethod(erasedType));

						if( hasValueOfMethod(erasedType) )
						{
							return OptionalValueOfType;

						}
						else if( hasFromStringMethod(erasedType) )
						{
							return OptionalFromStringType;

						}
						
						
					} catch (Exception e)
					{
						log.error("error : " + e.getMessage(),e);
						return OptionalStringType; 
					}
 					   
					return OptionalStringType; 
				}
			}
			else if (isEnum)
			{
				return EnumType;
			}
			else if (hasValueOf)
			{
				return ValueOfType;
			}
			else if (hasFromString)
			{
				return FromStringType;
			}
			else
			{
				return ModelType;
			}
		}
	}

	@Inject
	protected RoutingModule routingModule;

	@Inject
	@Named("application.path")
	protected String applicationPath;

	protected String packageName;
	protected String className;
	protected String sourceString;

	protected Set<Class<?>> controllerClasses = null;

	// public static void main(String[] args)
	// {
	// try
	// {
	// RouteGenerator generator = new RouteGenerator("com.wurrly.controllers.handlers","RouteHandlers");
	//
	// Set<Class<?>> classes = getApiClasses("com.wurrly.controllers",null);
	//
	// generator.generateRoutes(classes);
	//
	// StringBuilder sb = new StringBuilder();
	//
	// //generator.getRestRoutes().stream().forEachOrdered( r -> sb.append(r.toString() + "\n"));
	//
	// System.out.println(sb.toString());
	//
	// System.out.println("\n" + generator.sourceString);
	//
	// } catch (Exception e)
	// {
	// log.error(e.getMessage(),e);
	// }
	//
	// }

	public HandlerGenerator(String packageName, String className, Set<Class<?>> controllerClasses)
	{
		this.packageName = packageName;
		this.className = className;
		this.controllerClasses = controllerClasses;

	}

	public Class<? extends Supplier<RoutingHandler>> compileClass()
	{
		try
		{
			this.generateRoutes();

			log.info("CLASS:\n" + this.sourceString);
			return CompilerUtils.CACHED_COMPILER.loadFromJava(packageName + "." + className, this.sourceString);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return null;
		}
	}

	protected void generateRoutes()
	{
		try
		{

			TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC).addSuperinterface(ParameterizedTypeName.get(Supplier.class, RoutingHandler.class));

			ClassName extractorClass = ClassName.get("com.wurrly.server", "Extractors");

			ClassName injectClass = ClassName.get("com.google.inject", "Inject");

			MethodSpec.Builder constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addAnnotation(injectClass);

			for (Class<?> clazz : this.controllerClasses)
			{
				String className = clazz.getSimpleName().toLowerCase();

				typeBuilder.addField(clazz, className, Modifier.PROTECTED);

				constructor.addParameter(clazz, className);

				constructor.addStatement("this.$N = $N", className, className);

				addClassMethodHandlers(typeBuilder, clazz);
			}

			typeBuilder.addMethod(constructor.build());

			JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build()).addStaticImport(extractorClass, "*").build();

			StringBuilder sb = new StringBuilder();

			javaFile.writeTo(sb);

			this.sourceString = sb.toString();

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}

	public void addClassMethodHandlers(TypeSpec.Builder typeBuilder, Class<?> clazz)
	{
		ClassName httpHandlerClass = ClassName.get("io.undertow.server", "HttpHandler");

		String controllerName = clazz.getSimpleName().toLowerCase();

		MethodSpec.Builder initBuilder = MethodSpec.methodBuilder("get").addModifiers(Modifier.PUBLIC).returns(RoutingHandler.class).addStatement("final $T router = new $T()", io.undertow.server.RoutingHandler.class, io.undertow.server.RoutingHandler.class);

		final Map<Type, String> typeLiteralsMap = Arrays.stream(clazz.getDeclaredMethods())
				.flatMap(m -> Arrays.stream(m.getParameters()).map(Parameter::getParameterizedType)
						.filter(t -> t.getTypeName().contains("<")))
				.distinct().filter(t -> {
					TypeHandler handler = TypeHandler.forType(t);
					return (handler.equals(TypeHandler.ModelType) || handler.equals(TypeHandler.OptionalModelType));
		}).collect(Collectors.toMap(java.util.function.Function.identity(), HandlerGenerator::typeLiteralNameForType));

		initBuilder.addCode("$L", "\n");

		typeLiteralsMap.forEach((t, n) -> initBuilder.addStatement("final $T<$L> $LType = new $T<$L>(){}", TypeLiteral.class, t, n, TypeLiteral.class, t));

		initBuilder.addCode("$L", "\n");

		for (Method m : clazz.getDeclaredMethods())
		{
			EndpointInfo route = new EndpointInfo();

			route.setControllerName(clazz.getSimpleName());

			String methodPath = Extractors.pathTemplateFromMethod.apply(m).replaceAll("\\/\\/", "\\/");

			methodPath = applicationPath + methodPath;

 
			HttpString httpMethod = Extractors.httpMethodFromMethod.apply(m);

			route.setMethod(httpMethod);

			route.setPathTemplate(methodPath);

			route.setControllerMethod(clazz.getSimpleName() + "." + m.getName());

			String methodName = String.format("%c%s%sHandler", Character.toLowerCase(clazz.getSimpleName().charAt(0)), clazz.getSimpleName().substring(1, clazz.getSimpleName().length()), StringUtils.capitalize(m.getName()));

			TypeSpec.Builder handlerClassBuilder = TypeSpec.anonymousClassBuilder("").addSuperinterface(httpHandlerClass);

			// handlerClassBuilder.addModifiers(Modifier.PUBLIC);

			MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("handleRequest").addModifiers(Modifier.PUBLIC).addException(ClassName.get("java.lang", "Exception")).addAnnotation(Override.class)
					.addParameter(ParameterSpec.builder(HttpServerExchange.class, "exchange", Modifier.FINAL).build());

			for (Parameter p : m.getParameters())
			{

				if (p.getParameterizedType().equals(ServerRequest.class))
				{
					continue;
				}

				try
				{
					TypeHandler t = TypeHandler.forType(p.getParameterizedType());

					if (t.isBlocking())
					{
						methodBuilder.addCode("$L", "\n");

						methodBuilder.beginControlFlow("if(exchange.isInIoThread())");
						methodBuilder.addStatement("exchange.dispatch(this)");
						methodBuilder.addStatement("return");

						methodBuilder.endControlFlow();

						break;
					}

				} catch (Exception e)
				{
					log.error(e.getMessage(), e);
				}
			}

			methodBuilder.addCode("$L", "\n");

			Arrays.stream(m.getParameters()).forEachOrdered(p -> {

				Type type = p.getParameterizedType();

				try
				{

					if (p.getType().equals(ServerRequest.class))
					{
						methodBuilder.addStatement("$T serverRequest = new $T(exchange)", ServerRequest.class, ServerRequest.class);
						methodBuilder.addCode("$L", "\n");

					}
					else
					{
						if (p.isAnnotationPresent(HeaderParam.class))
						{

 
							log.debug("header class: " + type);
 
							TypeHandler handler = TypeHandler.forType(type);
							
							log.debug("header typehandler: " + handler);

							if( handler.equals(TypeHandler.OptionalStringType) )
							{
 								handler = TypeHandler.OptionalHeaderStringType;
 								 
								TypeHandler.addStatement(methodBuilder, p,handler); 

							}
							else if( handler.equals(TypeHandler.OptionalValueOfType) )
							{
 								handler = TypeHandler.OptionalHeaderValueOfType;
 								 
 								log.debug("header typehandler: " + handler.statement);

								TypeHandler.addStatement(methodBuilder, p,handler); 

							}
							else if( handler.equals(TypeHandler.OptionalFromStringType) )
							{
 								handler = TypeHandler.OptionalHeaderFromStringType;
 								log.debug("header typehandler: " + handler.statement);

								TypeHandler.addStatement(methodBuilder, p,handler); 

							}
							else if( handler.equals(TypeHandler.StringType) )
							{
 								handler = TypeHandler.HeaderStringType;
 								log.debug("header typehandler: " + handler.statement);

								TypeHandler.addStatement(methodBuilder, p,handler); 
								
							}
							else if( handler.equals(TypeHandler.ValueOfType) )
							{
 								handler = TypeHandler.HeaderValueOfType;
 								log.debug("header typehandler: " + handler.statement);

								TypeHandler.addStatement(methodBuilder, p,handler); 
								
							}
							else if( handler.equals(TypeHandler.FromStringType) )
							{
 								handler = TypeHandler.HeaderFromStringType;
 								log.debug("header typehandler: " + handler.statement);

								TypeHandler.addStatement(methodBuilder, p,handler); 
								
							}
							 

							else
							{
								handler = TypeHandler.HeaderStringType;

								TypeHandler.addStatement(methodBuilder, p,handler); 

							}

						}
						else
						{
							TypeHandler t = TypeHandler.forType(type);

							if (t.equals(TypeHandler.OptionalModelType) || t.equals(TypeHandler.ModelType))
							{
								String interfaceType = typeLiteralsMap.get(type);

								String pType = interfaceType != null ? interfaceType + "Type" : type.getTypeName() + ".class";

								methodBuilder.addStatement(t.statement, type, p.getName(), pType);
							}
							else if (t.equals(TypeHandler.OptionalFromStringType) || t.equals(TypeHandler.OptionalValueOfType))
							{
								Class<?> erasedType = extractErasedType(p.getParameterizedType());

								TypeHandler.addStatement(methodBuilder,p);
							}
							else
							{
								TypeHandler.addStatement(methodBuilder, p);
							}
						}
					}

				} catch (Exception e)
				{
					log.error(e.getMessage(), e);
				}

			});

			methodBuilder.addCode("$L", "\n");

			CodeBlock.Builder functionBlockBuilder = CodeBlock.builder();

			String controllerMethodArgs = Arrays.stream(m.getParameters()).map(Parameter::getName).collect(Collectors.joining(","));

			if (!m.getReturnType().equals(Void.class))
			{
			//	log.debug("return : " + m.getReturnType());
				functionBlockBuilder.add("$T $L = $L.$L($L);", m.getReturnType(), "response", controllerName, m.getName(), controllerMethodArgs);

			}
	
			methodBuilder.addCode(functionBlockBuilder.build());

			methodBuilder.addCode("$L", "\n");

			String producesContentType = "*/*";

			Optional<javax.ws.rs.Produces> producesAnnotation = Optional.ofNullable(m.getAnnotation(javax.ws.rs.Produces.class));

			if (!producesAnnotation.isPresent())
			{
				producesAnnotation = Optional.ofNullable(clazz.getAnnotation(javax.ws.rs.Produces.class));

				if (producesAnnotation.isPresent())
				{
					producesContentType = producesAnnotation.get().value()[0];
				}
			}
			else
			{
				producesContentType = producesAnnotation.get().value()[0];
			}

			route.setProduces(producesContentType);

			String consumesContentType = "*/*";

			Optional<javax.ws.rs.Consumes> consumesAnnotation = Optional.ofNullable(m.getAnnotation(javax.ws.rs.Consumes.class));

			if (!consumesAnnotation.isPresent())
			{
				consumesAnnotation = Optional.ofNullable(clazz.getAnnotation(javax.ws.rs.Consumes.class));

				if (consumesAnnotation.isPresent())
				{
					consumesContentType = consumesAnnotation.get().value()[0];
				}
			}
			else
			{
				consumesContentType = consumesAnnotation.get().value()[0];
			}

			route.setConsumes(consumesContentType);

			methodBuilder.addCode("$L", "\n");
			
			if( m.getReturnType().equals(ServerResponse.class))
			{
				methodBuilder.addStatement("$L.send(this,$L)","response","exchange");

			}
			else
			{


				methodBuilder.addStatement("exchange.getResponseHeaders().put($T.CONTENT_TYPE, $S)", Headers.class, producesContentType);
	
				if (m.getReturnType().equals(String.class))
				{
					methodBuilder.addStatement("exchange.getResponseHeaders().send($L)", "response");
				}
				else
				{
					methodBuilder.addStatement("exchange.getResponseSender().send(com.jsoniter.output.JsonStream.serialize($L))", "response");
				}
	
 			
			}
			
			handlerClassBuilder.addMethod(methodBuilder.build());


			FieldSpec handlerField = FieldSpec.builder(httpHandlerClass, methodName, Modifier.FINAL).initializer("$L", handlerClassBuilder.build()).build();

			initBuilder.addCode("$L\n", handlerField.toString());

			initBuilder.addStatement("$L.add(io.undertow.util.Methods.$L,$S,$L)", "router", httpMethod, methodPath, methodName);

			initBuilder.addCode("$L", "\n");

			this.routingModule.getRegisteredEndpoints().add(route);

		}

		initBuilder.addCode("$Lreturn router;\n", "\n");

		typeBuilder.addMethod(initBuilder.build());

	}

	/**
	 * @return the packageName
	 */
	public String getPackageName()
	{
		return packageName;
	}

	/**
	 * @param packageName
	 *            the packageName to set
	 */
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}

	/**
	 * @return the className
	 */
	public String getClassName()
	{
		return className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	public static ArrayList<String> getClassNamesFromPackage(String packageName) throws Exception
	{
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		URL packageURL;
		ArrayList<String> names = new ArrayList<>();

		packageName = packageName.replace(".", "/");
		packageURL = classLoader.getResource(packageName);


		assert packageURL != null;
		URI uri = new URI(packageURL.toString());
		File folder = new File(uri.getPath());
		// won't work with path which contains blank (%20)
		// File folder = new File(packageURL.getFile());
		File[] contenuti = folder.listFiles();
		String entryName;
		assert contenuti != null;
		for (File actual : contenuti)
		{
			if (actual.isDirectory())
			{
				continue;
			}

			entryName = actual.getName();
			entryName = entryName.substring(0, entryName.lastIndexOf('.'));
			names.add(entryName);
		}

		return names;
	}

	public static Set<Class<?>> getApiClasses(String basePath, Predicate<String> pathPredicate) throws Exception
	{

		Reflections ref = new Reflections(basePath);
		Stream<Class<?>> stream = ref.getTypesAnnotatedWith(Api.class).stream();

		if (pathPredicate != null)
		{
			stream = stream.filter(clazz -> {

				Path annotation = clazz.getDeclaredAnnotation(Path.class);

				return annotation != null && pathPredicate.test(annotation.value());

			});
		}

		return stream.collect(Collectors.toSet());

	}


	
	public static Class<?> extractErasedType(Type type) throws Exception
	{
		String typeName = type.getTypeName();

		Matcher matcher = TYPE_NAME_PATTERN.matcher(typeName);

		if (matcher.find())
		{

			int matches = matcher.groupCount();

			if (matches == 2)
			{

				String erasedType = matcher.group(2);
				
				String clearDollarType = erasedType.replaceAll("\\$", ".");
				
				try
				{
					return  Class.forName(clearDollarType);
					 
				} catch (Exception e)
				{
					return Class.forName(erasedType);
				}

			 
			}

		}

		return null;
	}

	public static String typeLiteralNameForType(Type type)
	{
		String typeName = type.getTypeName();

		Matcher matcher = TYPE_NAME_PATTERN.matcher(typeName);

		if (matcher.find())
		{

			int matches = matcher.groupCount();

			if (matches == 2)
			{
				String genericInterface = matcher.group(1);
				String erasedType = matcher.group(2).replaceAll("\\$", ".");

				String[] genericParts = genericInterface.split("\\.");
				String[] erasedParts = erasedType.split("\\.");

				String genericTypeName = genericParts[genericParts.length - 1];
				String erasedTypeName;

				if (erasedParts.length > 1)
				{
					erasedTypeName = erasedParts[erasedParts.length - 2] + erasedParts[erasedParts.length - 1];
				}
				else
				{
					erasedTypeName = erasedParts[0];
				}

				return String.format("%s%s%s", Character.toLowerCase(erasedTypeName.charAt(0)), erasedTypeName.substring(1, erasedTypeName.length()), genericTypeName);
			}

		}

		return typeName;
	}

	public static void generateTypeLiteral(MethodSpec.Builder builder, Type type, String name)
	{

		builder.addCode(CodeBlock.of("\n\ncom.jsoniter.spi.TypeLiteral<$T> $L = new com.jsoniter.spi.TypeLiteral<$L>(){};\n\n", type, name, type));

	}

	public static void generateParameterReference(MethodSpec.Builder builder, Class<?> clazz)
	{

		builder.addCode(CodeBlock.of("\n\nType $LType = $T.", clazz, clazz));

	}
	
	public static boolean hasValueOfMethod(Class<?> clazz)
	{
		return Arrays.stream(clazz.getMethods()).filter( m -> m.getName().equals("valueOf")).findFirst().isPresent();
	}
	
	public static boolean hasFromStringMethod(Class<?> clazz)
	{
		return Arrays.stream(clazz.getMethods()).filter( m -> m.getName().equals("fromString")).findFirst().isPresent();
	}

}
