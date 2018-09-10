/**
 * 
 */
package io.sinistral.proteus.server.handlers;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
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
import javax.ws.rs.BeanParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
 


import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.server.Extractors;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.endpoints.EndpointInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.RequestBufferingHandler;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import net.openhft.compiler.CompilerUtils;

/**
 * Generates code and compiles a <code>Supplier<RoutingHandler></code> class
 * from the target class's methods that are annotated with a JAX-RS method
 * annotation (i.e. <code>javax.ws.rs.GET</code>)
 * @author jbauer
 */
public class HandlerGenerator
{

	static Logger log = LoggerFactory.getLogger(HandlerGenerator.class.getCanonicalName());

	private static final Pattern TYPE_NAME_PATTERN = Pattern.compile("(java\\.util\\.[A-Za-z]+)<([^>]+)", Pattern.DOTALL | Pattern.UNIX_LINES);
	private static final Pattern CONCURRENT_TYPE_NAME_PATTERN = Pattern.compile("(java\\.util\\.concurrent\\.[A-Za-z]+)<([^>]+)", Pattern.DOTALL | Pattern.UNIX_LINES);

	public enum StatementParameterType
	{
		STRING, LITERAL, TYPE, RAW
	}

	@Inject
	@Named("application.path")
	protected String applicationPath;

	protected String packageName;
	protected String className;
	protected String sourceString;

	@Inject
	@Named("registeredEndpoints")
	protected Set<EndpointInfo> registeredEndpoints;

	protected Class<?> controllerClass;

	/**
	 * Create a new {@code HandlerGenerator} instance used to generate a
	 * {@code Supplier<RoutingHandler>} class
	 * @param packageName
	 *            generated class package name
	 * @param controllerClass
	 *            the class handlers will be generated from this class
	 */
	public HandlerGenerator(String packageName, Class<?> controllerClass)
	{
		this.packageName = packageName;
		this.controllerClass = controllerClass;
		this.className = controllerClass.getSimpleName() + "RouteSupplier";
	}

	/**
	 * Compiles the generated source into a new {@link Class}
	 * @return a new {@code Supplier<RoutingHandler>} class
	 */
	public Class<? extends Supplier<RoutingHandler>> compileClass()
	{
		try
		{
			this.generateRoutes();

			log.debug("\n\nGenerated Class Source:\n\n" + this.sourceString);

			return CompilerUtils.CACHED_COMPILER.loadFromJava(packageName + "." + className, this.sourceString);

		} catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Generates the routing Java source code
	 */
	protected void generateRoutes()
	{
		try
		{

			TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC)
					.addSuperinterface(ParameterizedTypeName.get(Supplier.class, RoutingHandler.class));

			ClassName extractorClass = ClassName.get("io.sinistral.proteus.server", "Extractors");

			ClassName injectClass = ClassName.get("com.google.inject", "Inject");

			MethodSpec.Builder constructor = MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).addAnnotation(injectClass);

			String className = this.controllerClass.getSimpleName().toLowerCase() + "Controller";

			typeBuilder.addField(this.controllerClass, className, Modifier.PROTECTED, Modifier.FINAL);

			ClassName wrapperClass = ClassName.get("io.undertow.server", "HandlerWrapper");
			ClassName stringClass = ClassName.get("java.lang", "String");
			ClassName mapClass = ClassName.get("java.util", "Map");

			TypeName mapOfWrappers = ParameterizedTypeName.get(mapClass, stringClass, wrapperClass);

			TypeName annotatedMapOfWrappers = mapOfWrappers
					.annotated(AnnotationSpec.builder(com.google.inject.name.Named.class).addMember("value", "$S", "registeredHandlerWrappers").build());

			typeBuilder.addField(mapOfWrappers, "registeredHandlerWrappers", Modifier.PROTECTED, Modifier.FINAL);

			constructor.addParameter(this.controllerClass, className);
			constructor.addParameter(annotatedMapOfWrappers, "registeredHandlerWrappers");
			constructor.addStatement("this.$N = $N", className, className);
			constructor.addStatement("this.$N = $N", "registeredHandlerWrappers", "registeredHandlerWrappers");

			addClassMethodHandlers(typeBuilder, this.controllerClass);

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

	protected void addClassMethodHandlers(TypeSpec.Builder typeBuilder, Class<?> clazz) throws Exception
	{
		ClassName httpHandlerClass = ClassName.get("io.undertow.server", "HttpHandler");

		String controllerName = clazz.getSimpleName().toLowerCase() + "Controller";
		
		Integer handlerWrapperIndex = 1;

		HashSet<String> handlerNameSet = new HashSet<>();

		MethodSpec.Builder initBuilder = MethodSpec.methodBuilder("get").addModifiers(Modifier.PUBLIC).returns(RoutingHandler.class)
				.addStatement("final $T router = new $T()", io.undertow.server.RoutingHandler.class, io.undertow.server.RoutingHandler.class);

		final Map<Type, String> parameterizedLiteralsNameMap = Arrays.stream(clazz.getDeclaredMethods())
				.filter(m -> m.getAnnotation(ApiOperation.class) != null)
				.flatMap(
							m -> Arrays.stream(m.getParameters()).map(Parameter::getParameterizedType)
									.filter(t -> t.getTypeName().contains("<") && !t.getTypeName().contains("concurrent")))
				.distinct().filter(t ->
				{

					TypeHandler handler = TypeHandler.forType(t);
					return (handler.equals(TypeHandler.ModelType) || handler.equals(TypeHandler.OptionalModelType));

				}).collect(Collectors.toMap(java.util.function.Function.identity(), HandlerGenerator::typeReferenceNameForParameterizedType));

		Arrays.stream(clazz.getDeclaredMethods())
			.filter(m -> m.getAnnotation(ApiOperation.class) != null)
			.flatMap(m -> Arrays.stream(m.getParameters()))
			.forEach(p ->
			{
	
				BeanParam beanParam = p.getAnnotation(BeanParam.class);
	
				boolean isBeanParameter = beanParam != null;
	
				if (isBeanParameter)
				{
					TypeHandler handler = TypeHandler.forType(p.getParameterizedType(), true);
	
					if (handler.equals(TypeHandler.BeanListValueOfType) || handler.equals(TypeHandler.BeanListFromStringType) || handler.equals(TypeHandler.OptionalBeanListValueOfType)
							|| handler.equals(TypeHandler.OptionalBeanListFromStringType))
					{
						parameterizedLiteralsNameMap.put(p.getParameterizedType(), HandlerGenerator.typeReferenceNameForParameterizedType(p.getParameterizedType()));
					}
				}
	
			});

		final Map<Type, String> literalsNameMap = Arrays.stream(clazz.getDeclaredMethods())
				.filter(m -> m.getAnnotation(ApiOperation.class) != null)
				.flatMap(m -> Arrays.stream(
																																												m.getParameters())
				.map(Parameter::getParameterizedType)).filter(t ->
				{

					if (t.getTypeName().contains("java.util"))
					{
						return false;
					}

					try
					{
						Class<?> optionalType = (Class<?>) extractErasedType(t);

						if (optionalType != null)
						{
							t = optionalType;
						}

					} catch (Exception e)
					{

					}
					
					/*
					 * 	if (t.getTypeName().matches("java\\.lang|java\\.nio|java\\.io|java\\.util"))
					{
						return false;
					}
					 */

					if (t.getTypeName().contains("java.lang"))
					{
						return false;
					}
					else if (t.getTypeName().contains("java.nio"))
					{
						return false;
					}
					else if (t.getTypeName().contains("java.io"))
					{
						return false;
					}
					else if (t.getTypeName().contains("java.util"))
					{
						return false;
					}
					else if (t.equals(HttpServerExchange.class) || t.equals(ServerRequest.class))
					{
						return false;
					}

					if (t instanceof Class)
					{
						Class<?> pClazz = (Class<?>) t;
						if (pClazz.isPrimitive())
						{
							return false;
						}
						if (pClazz.isEnum())
						{
							return false;
						}

					}

					return true;

				})
				.distinct()
				.collect(Collectors.toMap(java.util.function.Function.identity(), HandlerGenerator::typeReferenceNameForType));

		parameterizedLiteralsNameMap
				.forEach((t, n) -> initBuilder.addStatement("final $T<$L> $LTypeReference = new $T<$L>(){}", TypeReference.class, t, n, TypeReference.class, t));

		literalsNameMap.forEach((t, n) -> initBuilder.addStatement("final $T<$T> $LTypeReference = new $T<$T>(){}", TypeReference.class, t, n, TypeReference.class, t));

		Optional<io.sinistral.proteus.annotations.Chain> typeLevelWrapAnnotation = Optional.ofNullable(clazz.getAnnotation(io.sinistral.proteus.annotations.Chain.class));
		
		Map<Class<? extends HandlerWrapper>, String> typeLevelHandlerWrapperMap = new LinkedHashMap<Class<? extends HandlerWrapper>, String>();

		if (typeLevelWrapAnnotation.isPresent())
		{
			io.sinistral.proteus.annotations.Chain w = typeLevelWrapAnnotation.get();

			Class<? extends HandlerWrapper> wrapperClasses[] = w.value();

			for (int i = 0; i < wrapperClasses.length; i++)
			{
				Class<? extends HandlerWrapper> wrapperClass = wrapperClasses[i];

				String wrapperName = generateFieldName(wrapperClass.getCanonicalName());

				initBuilder.addStatement("final $T $L = new $T()", wrapperClass, wrapperName, wrapperClass);

				typeLevelHandlerWrapperMap.put(wrapperClass, wrapperName);
			}
		}

		initBuilder.addStatement("$T currentHandler = $L", HttpHandler.class, "null");

		initBuilder.addCode("$L", "\n");

		List<String> consumesContentTypes = new ArrayList<>();
		List<String> producesContentTypes = new ArrayList<>();

		/*
		 * Controller Level Authorization
		 */

		List<String> typeLevelSecurityDefinitions = new ArrayList<>();

		if (Optional.ofNullable(clazz.getAnnotation(io.swagger.annotations.Api.class)).isPresent())
		{
			io.swagger.annotations.Api apiAnnotation = clazz.getAnnotation(io.swagger.annotations.Api.class);

			io.swagger.annotations.Authorization[] authorizationAnnotations = apiAnnotation.authorizations();

			if (authorizationAnnotations.length > 0)
			{
				for (io.swagger.annotations.Authorization authorizationAnnotation : authorizationAnnotations)
				{
					if (authorizationAnnotation.value().length() > 0)
					{
						typeLevelSecurityDefinitions.add(authorizationAnnotation.value());
					}
				}
			}
		}

		log.debug("Scanning methods for class " + clazz.getName());

		int nameIndex = 1;

		for (Method m : clazz.getDeclaredMethods())
		{

			if (!Optional.ofNullable(m.getAnnotation(javax.ws.rs.Path.class)).isPresent())
			{
				continue;
			}

			log.debug("Scanning method " + m.getName() + "\n");

			EndpointInfo endpointInfo = new EndpointInfo();

			String producesContentType = "*/*";
			String consumesContentType = "*/*";

			Boolean isBlocking = false;

			Optional<Blocking> blockingAnnotation = Optional.ofNullable(m.getAnnotation(Blocking.class));

			if (blockingAnnotation.isPresent())
			{
				isBlocking = blockingAnnotation.get().value();
			}

			Optional<javax.ws.rs.Produces> producesAnnotation = Optional.ofNullable(m.getAnnotation(javax.ws.rs.Produces.class));

			if (!producesAnnotation.isPresent())
			{
				producesAnnotation = Optional.ofNullable(clazz.getAnnotation(javax.ws.rs.Produces.class));

				if (producesAnnotation.isPresent())
				{

					producesContentTypes = Arrays.stream(producesAnnotation.get().value()).flatMap(v -> Arrays.stream((v.split(",")))).collect(Collectors.toList());

					producesContentType = producesContentTypes.stream().collect(Collectors.joining(","));
				}

			}
			else
			{
				producesContentTypes = Arrays.stream(producesAnnotation.get().value()).flatMap(v -> Arrays.stream((v.split(",")))).collect(Collectors.toList());

				producesContentType = producesContentTypes.stream().collect(Collectors.joining(","));
			}

			endpointInfo.setProduces(producesContentType);

			Optional<javax.ws.rs.Consumes> consumesAnnotation = Optional.ofNullable(m.getAnnotation(javax.ws.rs.Consumes.class));

			if (!consumesAnnotation.isPresent())
			{
				consumesAnnotation = Optional.ofNullable(clazz.getAnnotation(javax.ws.rs.Consumes.class));

				if (consumesAnnotation.isPresent())
				{
					consumesContentTypes = Arrays.stream(consumesAnnotation.get().value()).flatMap(v -> Arrays.stream((v.split(",")))).collect(Collectors.toList());

					consumesContentType = consumesContentTypes.stream().collect(Collectors.joining(","));
				}
			}
			else
			{
				consumesContentTypes = Arrays.stream(consumesAnnotation.get().value()).flatMap(v -> Arrays.stream((v.split(",")))).collect(Collectors.toList());

				consumesContentType = consumesContentTypes.stream().collect(Collectors.joining(","));
			}

			endpointInfo.setControllerName(clazz.getSimpleName());

			String methodPath = null;

			try
			{
				methodPath = Extractors.pathTemplateFromMethod.apply(m).replaceAll("\\/\\/", "\\/");
			} catch (Exception e)
			{
				log.error(e.getMessage() + " for " + m, e);
				continue;
			}

			methodPath = applicationPath + methodPath;

			HttpString httpMethod = Extractors.httpMethodFromMethod.apply(m);

			endpointInfo.setMethod(httpMethod);

			endpointInfo.setConsumes(consumesContentType);

			endpointInfo.setPathTemplate(methodPath);

			endpointInfo.setControllerMethod(m.getName());

			String handlerName = String.format("%c%s%sHandler_%s", Character.toLowerCase(clazz.getSimpleName().charAt(0)), clazz.getSimpleName()
					.substring(1, clazz.getSimpleName().length()), StringUtils.capitalize(m.getName()), String.valueOf(nameIndex++));

			handlerNameSet.add(handlerName);

			TypeSpec.Builder handlerClassBuilder = TypeSpec.anonymousClassBuilder("").addSuperinterface(httpHandlerClass);

			MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("handleRequest").addModifiers(Modifier.PUBLIC).addException(ClassName.get("java.lang", "Exception"))
					.addAnnotation(Override.class)
					.addParameter(ParameterSpec.builder(HttpServerExchange.class, "exchange", Modifier.FINAL).build());

			for (Parameter p : m.getParameters())
			{

				if (p.getParameterizedType().equals(ServerRequest.class) || p.getParameterizedType().equals(HttpServerExchange.class)
						|| p.getParameterizedType().equals(HttpHandler.class))
				{
					continue;
				}

				try
				{
					BeanParam beanParam = p.getAnnotation(BeanParam.class);

					boolean isBeanParameter = beanParam != null;

					TypeHandler t = TypeHandler.forType(p.getParameterizedType(), isBeanParameter);

					if (t.isBlocking())
					{
						isBlocking = true;
						break;
					}

				} catch (Exception e)
				{
					log.error(e.getMessage(), e);
				}
			}

			log.debug("parameterizedLiteralsNameMap: " + parameterizedLiteralsNameMap);
			
			
			Arrays.stream(m.getParameters()).forEachOrdered(p ->
			{

				Type type = p.getParameterizedType();

				try
				{

					log.debug("Parameter " + p.getName() + " of type " + type);

					if (p.getType().equals(ServerRequest.class))
					{
						methodBuilder.addStatement("$T $L = new $T(exchange)", ServerRequest.class, p.getName(), ServerRequest.class);

					}
					else if (p.getType().equals(HttpServerExchange.class))
					{
						// methodBuilder.addCode("$L", "\n");
					}
					else if (p.getType().equals(HttpHandler.class))
					{
						methodBuilder.addStatement("$T $L = this", HttpHandler.class, p.getName());
						// methodBuilder.addCode("$L", "\n");
					}
					else
					{
						if (p.isAnnotationPresent(HeaderParam.class))
						{

							TypeHandler handler = TypeHandler.forType(type);

							if (handler.equals(TypeHandler.OptionalStringType))
							{
								handler = TypeHandler.OptionalHeaderStringType;

								TypeHandler.addStatement(methodBuilder, p, handler);

							}
							else if (handler.equals(TypeHandler.OptionalValueOfType))
							{
								handler = TypeHandler.OptionalHeaderValueOfType;

								TypeHandler.addStatement(methodBuilder, p, handler);

							}
							else if (handler.equals(TypeHandler.OptionalFromStringType))
							{
								handler = TypeHandler.OptionalHeaderFromStringType;
								TypeHandler.addStatement(methodBuilder, p, handler);

							}
							else if (handler.equals(TypeHandler.StringType))
							{
								handler = TypeHandler.HeaderStringType;
								TypeHandler.addStatement(methodBuilder, p, handler);

							}
							else if (handler.equals(TypeHandler.ValueOfType))
							{
								handler = TypeHandler.HeaderValueOfType;
								TypeHandler.addStatement(methodBuilder, p, handler);

							}
							else if (handler.equals(TypeHandler.FromStringType))
							{
								handler = TypeHandler.HeaderFromStringType;
								TypeHandler.addStatement(methodBuilder, p, handler);

							}

							else
							{
								handler = TypeHandler.HeaderStringType;

								TypeHandler.addStatement(methodBuilder, p, handler);

							}

						}
						else
						{
							BeanParam beanParam = p.getAnnotation(BeanParam.class);

							boolean isBeanParameter = beanParam != null;
							

							TypeHandler t = TypeHandler.forType(type, isBeanParameter);

							log.debug("beanParam handler: " + t);

							if (t.equals(TypeHandler.OptionalModelType) || t.equals(TypeHandler.ModelType))
							{
								String interfaceType = parameterizedLiteralsNameMap.get(type);

								String typeName = type.getTypeName();

								if (typeName.indexOf("$") > -1)
								{
									typeName = typeName.replace("$", ".");
								}

								String pType = interfaceType != null ? interfaceType + "TypeReference" : typeName + ".class";

								methodBuilder.addStatement(t.statement, type, p.getName(), pType);

							}
							else if (t.equals(TypeHandler.BeanListFromStringType) || t.equals(TypeHandler.BeanListValueOfType))
							{
								String interfaceType = parameterizedLiteralsNameMap.get(type);

								String typeName = type.getTypeName();

								if (typeName.indexOf("$") > -1)
								{
									typeName = typeName.replace("$", ".");
								}

								String pType = interfaceType != null ? interfaceType + "TypeReference" : typeName + ".class";

								methodBuilder.addStatement(t.statement, type, p.getName(), pType);

							}
							else if (t.equals(TypeHandler.OptionalFromStringType) || t.equals(TypeHandler.OptionalValueOfType))
							{

								TypeHandler.addStatement(methodBuilder, p);
							}
							else if (t.equals(TypeHandler.QueryOptionalListFromStringType) || t.equals(TypeHandler.QueryOptionalListValueOfType)
									|| t.equals(TypeHandler.QueryOptionalSetValueOfType) || t.equals(TypeHandler.QueryOptionalSetValueOfType))
							{
								ParameterizedType pType = (ParameterizedType) type;

								if (type instanceof ParameterizedType)
								{
									pType = (ParameterizedType) type;
									type = pType.getActualTypeArguments()[0];
								}

								Class<?> erasedType = (Class<?>) extractErasedType(type);

								methodBuilder.addStatement(t.statement, pType, p.getName(), p.getName(), erasedType);

							}
							else if (t.equals(TypeHandler.OptionalBeanListFromStringType) || t.equals(TypeHandler.OptionalBeanListValueOfType))
							{
								ParameterizedType pType = (ParameterizedType) type;

								if (type instanceof ParameterizedType)
								{
									pType = (ParameterizedType) type;
									type = pType.getActualTypeArguments()[0];
								}

								Class<?> erasedType = (Class<?>) extractErasedType(type);

								try
								{

									methodBuilder.addStatement(t.statement, pType, p.getName(), p.getName(), erasedType);

								} catch (Exception e)
								{
									log.error("method builder: \nstatement: " + t.statement + "\npType: " + pType + "\np.name(): " + p.getName() + "\nerasedType: " + erasedType);
								}

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

			if (!m.getReturnType().toString().equals("void"))
			{
				if (m.getReturnType().getTypeName().contains("java.util.concurrent.CompletionStage")
						|| m.getReturnType().getTypeName().contains("java.util.concurrent.CompletableFuture"))
				{
					Type futureType = m.getGenericReturnType();

					functionBlockBuilder.add("$T $L = $L.$L($L);", futureType, "response", controllerName, m.getName(), controllerMethodArgs);

				}
				else
				{
					functionBlockBuilder.add("$T $L = $L.$L($L);", m.getGenericReturnType(), "response", controllerName, m.getName(), controllerMethodArgs);
				}

				methodBuilder.addCode(functionBlockBuilder.build());

				methodBuilder.addCode("$L", "\n");

				if (m.getReturnType().equals(ServerResponse.class))
				{
					methodBuilder.addStatement("$L.send(this,$L)", "response", "exchange");

				}
				else if (m.getReturnType().getTypeName().contains("java.util.concurrent.CompletionStage")
						|| m.getReturnType().getTypeName().contains("java.util.concurrent.CompletableFuture"))
				{
					String postProcess = ".";

					if (!producesContentType.contains(","))
					{
						if (producesContentType.contains(MediaType.APPLICATION_JSON))
						{
							postProcess = ".applicationJson().";
						}
						else if (producesContentType.contains(MediaType.APPLICATION_XML))
						{
							postProcess = ".applicationXml().";
						}
						else if (producesContentType.contains(MediaType.TEXT_HTML))
						{
							postProcess = ".textHtml().";
						}
					}

					methodBuilder.addCode(
											"$L.thenAcceptAsync( r ->  r" + postProcess + "send(this,$L), io.undertow.util.SameThreadExecutor.INSTANCE )\n\t.exceptionally( ex -> ",
											"response", "exchange");
					methodBuilder.beginControlFlow("", "");
					methodBuilder.addCode("\t\tthrow new java.util.concurrent.CompletionException(ex);\n\t");
					methodBuilder.endControlFlow(")", "");
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

			}
			else
			{

				functionBlockBuilder.add("$L.$L($L);", controllerName, m.getName(), controllerMethodArgs);

				methodBuilder.addCode(functionBlockBuilder.build());

				methodBuilder.addCode("$L", "\n");

				handlerClassBuilder.addMethod(methodBuilder.build());

			}

			FieldSpec handlerField = FieldSpec.builder(httpHandlerClass, handlerName, Modifier.FINAL).initializer("$L", handlerClassBuilder.build()).build();

			initBuilder.addCode("$L\n", handlerField.toString());

			Optional<io.sinistral.proteus.annotations.Chain> wrapAnnotation = Optional.ofNullable(m.getAnnotation(io.sinistral.proteus.annotations.Chain.class));

			/*
			 * Authorization
			 */

			List<String> securityDefinitions = new ArrayList<>();

			/*
			 * @TODO wrap blocking in BlockingHandler
			 */

			if (Optional.ofNullable(m.getAnnotation(io.swagger.annotations.ApiOperation.class)).isPresent())
			{
				io.swagger.annotations.ApiOperation apiOperationAnnotation = m.getAnnotation(io.swagger.annotations.ApiOperation.class);

				io.swagger.annotations.Authorization[] authorizationAnnotations = apiOperationAnnotation.authorizations();
				if (authorizationAnnotations.length > 0)
				{
					for (io.swagger.annotations.Authorization authorizationAnnotation : authorizationAnnotations)
					{
						if (authorizationAnnotation.value().length() > 0)
						{
							securityDefinitions.add(authorizationAnnotation.value());
						}
					}
				}
			}

			if (securityDefinitions.isEmpty())
			{
				securityDefinitions.addAll(typeLevelSecurityDefinitions);
			}

			if (isBlocking)
			{
				handlerName = "new io.undertow.server.handlers.BlockingHandler(new io.undertow.server.handlers.RequestBufferingHandler.Wrapper(1).wrap(" + handlerName + "))";
			}

			if (wrapAnnotation.isPresent() || typeLevelHandlerWrapperMap.size() > 0 || securityDefinitions.size() > 0)
			{
				initBuilder.addStatement("currentHandler = $L", handlerName);

				if (wrapAnnotation.isPresent())
				{
					io.sinistral.proteus.annotations.Chain w = wrapAnnotation.get();

					Class<? extends HandlerWrapper> wrapperClasses[] = w.value();

					for (int i = 0; i < wrapperClasses.length; i++)
					{
						Class<? extends HandlerWrapper> wrapperClass = wrapperClasses[i];

						String wrapperName = typeLevelHandlerWrapperMap.get(wrapperClass);

						if (wrapperName == null)
						{
							wrapperName = String.format("%s_%d",generateFieldName(wrapperClass.getCanonicalName()),handlerWrapperIndex++) ;

							initBuilder.addStatement("final $T $L = new $T()", wrapperClass, wrapperName, wrapperClass);
						}

						initBuilder.addStatement("currentHandler = $L.wrap($L)", wrapperName, "currentHandler");
					}
				}

				for (Class<? extends HandlerWrapper> wrapperClass : typeLevelHandlerWrapperMap.keySet())
				{
					String wrapperName = typeLevelHandlerWrapperMap.get(wrapperClass);
					initBuilder.addStatement("currentHandler = $L.wrap($L)", wrapperName, "currentHandler");
				}

				for (String securityDefinitionName : securityDefinitions)
				{
					initBuilder.addStatement("currentHandler = registeredHandlerWrappers.get($S).wrap($L)", securityDefinitionName, "currentHandler");
				}

				initBuilder.addStatement("$L.add(io.undertow.util.Methods.$L,$S,$L)", "router", httpMethod, methodPath, "currentHandler");
			}
			else
			{
				initBuilder.addStatement("$L.add(io.undertow.util.Methods.$L,$S,$L)", "router", httpMethod, methodPath, handlerName);
			}

			initBuilder.addCode("$L", "\n");

			registeredEndpoints.add(endpointInfo);

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

	protected static ArrayList<String> getClassNamesFromPackage(String packageName) throws Exception
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

	protected static Set<Class<?>> getApiClasses(String basePath, Predicate<String> pathPredicate) throws Exception
	{

		Reflections ref = new Reflections(basePath);
		Stream<Class<?>> stream = ref.getTypesAnnotatedWith(Api.class).stream();

		if (pathPredicate != null)
		{
			stream = stream.filter(clazz ->
			{

				Path annotation = clazz.getDeclaredAnnotation(Path.class);

				return annotation != null && pathPredicate.test(annotation.value());

			});
		}

		return stream.collect(Collectors.toSet());

	}

	protected static Type extractErasedType(Type type) throws Exception
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
					Class<?> clazz = Class.forName(clearDollarType);
					return clazz;

				} catch (Exception e1)
				{
					try
					{
						Class<?> clazz = Class.forName(erasedType);

						return clazz;

					} catch (Exception e2)
					{
						return type;
					}
				}

			}
			else if (matches > 2)
			{

				String erasedType = matcher.group(3);

				String clearDollarType = erasedType.replaceAll("\\$", ".");

				try
				{
					Class<?> clazz = Class.forName(clearDollarType);
					return clazz;

				} catch (Exception e1)
				{
					try
					{
						Class<?> clazz = Class.forName(erasedType);

						return clazz;

					} catch (Exception e2)
					{
						return type;
					}
				}

			}

		}
		else
		{
			// log.warn("No type found for " + typeName);
		}

		return null;
	}

	protected static String typeReferenceNameForParameterizedType(Type type)
	{

		String typeName = type.getTypeName();

		if (typeName.contains("Optional"))
		{
			log.warn("For an optional named: " + typeName);
		}

		Matcher matcher = TYPE_NAME_PATTERN.matcher(typeName);

		if (matcher.find())
		{

			int matches = matcher.groupCount();

			if (matches == 2)
			{
				String genericInterface = matcher.group(1);
				String erasedType = matcher.group(2).replaceAll("\\$", ".");

				// log.debug("genericInterface: " + genericInterface);
				// log.debug("erasedType: " + erasedType);

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

				typeName = String.format("%s%s%s", Character.toLowerCase(erasedTypeName.charAt(0)), erasedTypeName.substring(1, erasedTypeName.length()), genericTypeName);

				return typeName;
			}

		}

		matcher = CONCURRENT_TYPE_NAME_PATTERN.matcher(typeName);

		if (matcher.find())
		{

			int matches = matcher.groupCount();

			if (matches == 2)
			{
				String genericInterface = matcher.group(1);
				String erasedType = matcher.group(2).replaceAll("\\$", ".");

				// log.debug("genericInterface: " + genericInterface);
				// log.debug("erasedType: " + erasedType);

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

				typeName = String.format("%s%s%s", Character.toLowerCase(erasedTypeName.charAt(0)), erasedTypeName.substring(1, erasedTypeName.length()), genericTypeName);
				return typeName;
			}

		}

		return typeName;
	}

	protected static String typeReferenceNameForType(Type type)
	{
		String typeName = type.getTypeName();

		String[] erasedParts = typeName.split("\\.");

		String erasedTypeName;

		if (erasedParts.length > 1)
		{
			erasedTypeName = erasedParts[erasedParts.length - 2] + erasedParts[erasedParts.length - 1];
		}
		else
		{
			erasedTypeName = erasedParts[0];
		}

		typeName = generateFieldName(erasedTypeName);

		return typeName;
	}

	protected static String generateFieldName(String name)
	{
		String[] parts = name.split("\\.");

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < parts.length; i++)
		{
			String part = parts[i];

			if (i == 0)
			{
				sb.append(String.format("%s%s", Character.toLowerCase(part.charAt(0)), part.substring(1, part.length())));
			}
			else
			{
				sb.append(String.format("%s%s", Character.toUpperCase(part.charAt(0)), part.substring(1, part.length())));
			}
		}

		return sb.toString();
	}

	protected static void generateTypeReference(MethodSpec.Builder builder, Type type, String name)
	{

		builder.addCode(
						CodeBlock
								.of("\n\ncom.fasterxml.jackson.core.type.TypeReference<$T> $L = new com.fasterxml.jackson.core.type.TypeReference<$L>(){};\n\n", type, name, type));

	}

	protected static void generateParameterReference(MethodSpec.Builder builder, Class<?> clazz)
	{

		builder.addCode(CodeBlock.of("\n\nType $LType = $T.", clazz, clazz));

	}

	protected static boolean hasValueOfMethod(Class<?> clazz)
	{
		return Arrays.stream(clazz.getMethods()).filter(m -> m.getName().equals("valueOf")).findFirst().isPresent();
	}

	protected static boolean hasFromStringMethod(Class<?> clazz)
	{
		return Arrays.stream(clazz.getMethods()).filter(m -> m.getName().equals("fromString")).findFirst().isPresent();
	}

}
