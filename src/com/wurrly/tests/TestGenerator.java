/**
 * 
 */
package com.wurrly.tests;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;
import com.wurrly.controllers.Users;
import com.wurrly.utilities.HandleGenerator;
import com.wurrly.utilities.ServerRequest;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.HttpString;
import io.undertow.util.MimeMappings;

/**
 * @author jbauer
 *
 */
public class TestGenerator
{
	private static Logger log = LoggerFactory.getLogger(TestGenerator.class.getCanonicalName());

	public static enum StatementParameterType
	{
		STRING,LITERAL,TYPE
	}
	
	public static enum TypeHandler
	{
 		
		LongType("Long $L = Long.parseInt(request.exchange.getQueryParameters().get($S).getFirst())",false,StatementParameterType.LITERAL,StatementParameterType.STRING),
		IntegerType("Integer $L = Integer.parseInt(request.exchange.getQueryParameters().get($S).getFirst())",false,StatementParameterType.LITERAL,StatementParameterType.STRING),
		StringType("String $L = exchange.getQueryParameters().get($S).getFirst()",false,StatementParameterType.LITERAL,StatementParameterType.STRING),
		BooleanType("Boolean $L = Boolean.parseBoolean(exchange.getQueryParameters().get($S).getFirst())",false,StatementParameterType.LITERAL,StatementParameterType.STRING),
		FilePathType("Path $L = request.files($S).getFirst().getPath()",true,StatementParameterType.LITERAL,StatementParameterType.STRING),
		AnyType("Any $L = exchange.getAttachment(ServerRequest.REQUEST_JSON_BODY).readAny()",true,StatementParameterType.LITERAL),
		JsonIteratorType("JsonIterator $L = exchange.getAttachment(ServerRequest.REQUEST_JSON_BODY)",true,StatementParameterType.LITERAL),
		ModelType("$T $L = exchange.getAttachment(ServerRequest.REQUEST_JSON_BODY).read($T.class)",true,StatementParameterType.TYPE,StatementParameterType.LITERAL,StatementParameterType.TYPE),

		OptionalJsonIteratorType("Optional<JsonIterator> $L = Optional.ofNullable(exchange.getAttachment(ServerRequest.REQUEST_JSON_BODY))",true,StatementParameterType.LITERAL),
		OptionalAnyType("Optional<Any> $L = Optional.ofNullable(exchange.getAttachment(ServerRequest.REQUEST_JSON_BODY)).map(JsonIterator::readAny())",true,StatementParameterType.LITERAL),
		OptionalStringType("Optional<String> $L = Optional.ofNullable(exchange.getQueryParameters().get($S)).map(Deque::getFirst)",false,StatementParameterType.LITERAL,StatementParameterType.STRING),
		OptionalLongType("Optional<Long> $L = Optional.ofNullable(exchange.getQueryParameters().get($S)).map(Deque::getFirst).map(Long::parseLong)",false,StatementParameterType.LITERAL,StatementParameterType.STRING),
		OptionalIntegerType("Optional<Integer> $L = Optional.ofNullable(exchange.getQueryParameters().get($S)).map(Deque::getFirst).map(Integer::parseInt)",false,StatementParameterType.LITERAL,StatementParameterType.STRING),
		OptionalBooleanType("Optional<Boolean> $L = Optional.ofNullable(exchange.getQueryParameters().get($S)).map(Deque::getFirst).map(Boolean::parseBoolean)",false,StatementParameterType.LITERAL,StatementParameterType.STRING),
		OptionalPathType("Optional<Path> $L = Optional.ofNullable(serverRequest.files($S)).map(Deque::getFirst).map(io.undertow.server.form.FormData.FormValue::getPath)",true,StatementParameterType.LITERAL,StatementParameterType.STRING),
 
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
		final private StatementParameterType[] parameterTypes;
 
		
		private TypeHandler(String statement, boolean isBlocking, StatementParameterType ... types)
		{
			this.statement = statement;
			this.isBlocking = isBlocking;
			this.parameterTypes = types;
		}
		
		public static void addStatement(MethodSpec.Builder builder, Parameter parameter)  throws Exception
		{
			TypeHandler handler = forType(parameter.getParameterizedType());
			
			Object[] args = new Object[handler.parameterTypes.length];
			
			for( int i = 0; i < handler.parameterTypes.length; i++ )
			{
				switch(handler.parameterTypes[i])
				{
				case LITERAL:
					args[i] =  parameter.getName();
					break;
				case STRING:
					args[i] =  parameter.getName();
					break;
				case TYPE:
					args[i] =  parameter.getParameterizedType();
					break;
				default:
					break;
				
				}
			}
			
			builder.addStatement( handler.statement, args);
		}
		
		public static TypeHandler  forType( Type type ) throws Exception
		{
			log.debug(type.getTypeName() + " " + type.toString());

			if( type.equals( Long.class ) )
			{
				return LongType;
			}
			else if( type.equals( Integer.class ) )
			{
				return IntegerType;
			}
			else if( type.equals( Boolean.class ) )
			{
				return BooleanType;
			}
			else if( type.equals( String.class ) )
			{
				return StringType;
			}
			else if( type.equals( java.nio.file.Path.class ) )
			{
				return FilePathType;
			}
			else if( type.equals( com.jsoniter.any.Any.class ) )
			{
				return AnyType;
			}
			else if( type.equals( com.jsoniter.JsonIterator.class ) )
			{
				return JsonIteratorType;
			}
			else if( type.getTypeName().contains("java.util.Optional") )
			{
 				if( type.getTypeName().contains("java.lang.Long"))
				{
					return OptionalLongType;
				}
				else if( type.getTypeName().contains("java.lang.String"))
				{
					return OptionalStringType;
				}
				else if( type.getTypeName().contains("java.lang.Boolean"))
				{
					return OptionalBooleanType;
				}
				else if( type.getTypeName().contains("java.lang.Integer"))
				{
					return OptionalIntegerType;
				}
				else if( type.getTypeName().contains("java.nio.file.Path"))
				{
					return OptionalPathType;
				}
				else
				{
					throw new Exception("No type handler found!");
				}
			}
			else
			{
				return ModelType;
			}
		}
	}
	
	public static void main(String[] args)
	{

		try
		{
			TypeSpec.Builder typeBuilder  = TypeSpec.classBuilder("RouteHandlers");
			typeBuilder.addModifiers(Modifier.PUBLIC,Modifier.STATIC);
			
			 ClassName controllersClass = ClassName.get("com.wurrly.controllers", "Users");

			 ClassName serverRequestClass = ClassName.get("com.wurrly.utilities", "ServerRequest");
			 ClassName handleGeneratorClass = ClassName.get("com.wurrly.utilities", "HandleGenerator");
			 ClassName filePathClass = ClassName.get("java.nio.file","Path");
			 ClassName dequeClass = ClassName.get("java.util","Deque");
			 ClassName optionalClass = ClassName.get("java.util","Optional");
			 ClassName jsonIteratorClass = ClassName.get(com.jsoniter.JsonIterator.class);
			 ClassName jsonAnyClass = ClassName.get(com.jsoniter.any.Any.class);
			 ClassName jsonStreamClass = ClassName.get(com.jsoniter.output.JsonStream.class);

			 ClassName formDataDefinitionClass = ClassName.get("io.undertow.server.form", "FormData" );
			 ClassName formDataClass = ClassName.get("io.undertow.server.form","FormEncodedDataDefinition"  );
			 ClassName formParserDefinitionClass = ClassName.get("io.undertow.server.form", "MultiPartParserDefinition" );

			 ClassName.get("io.undertow","Undertow");
			 ClassName.get("io.undertow","UndertowOptions");
 			 ClassName exchangeClass = ClassName.get("io.undertow.server","HttpServerExchange");
			 ClassName.get("io.undertow.server","RoutingHandler");
			 ClassName.get("io.undertow.util","Headers");
			 
			 ClassName injectClass = ClassName.get("com.google.inject","Inject");
			 ClassName namedClass = ClassName.get("com.google.inject.name","Named");
 
			 String prefix = "com.wurrly.controllers";
			 List<String> classNames = getClassNamesFromPackage(prefix);
				
			  
					
			 for( String classNameSuffix : classNames)
			 {
				 String className = prefix + "." + classNameSuffix;
					
					Class<?> clazz = Class.forName(className);
					
					addClassMethodHandlers(typeBuilder, clazz);
			 }
			  

			 JavaFile javaFile = JavaFile.builder("com.wurrly.controllers.handlers", typeBuilder.build())
		    .addStaticImport(handleGeneratorClass, "*")
		    .build();
			
			javaFile.writeTo(System.out);

			
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	public static void addClassMethodHandlers( TypeSpec.Builder typeBuilder, Class<?> clazz )
	{
		 ClassName httpHandlerClass = ClassName.get("io.undertow.server","HttpHandler");
		 ClassName exchangeClass = ClassName.get("io.undertow.server","HttpServerExchange");
		 
		 
		 

		 String controllerName = clazz.getSimpleName().toLowerCase() + "Controller";
		 
		 FieldSpec.Builder fieldBuilder = FieldSpec.builder(clazz, controllerName, Modifier.PROTECTED);
		 
		 ClassName injectClass = ClassName.get("com.google.inject","Inject");

		 fieldBuilder.addAnnotation(AnnotationSpec.builder(injectClass).build());
		 
		 typeBuilder.addField(fieldBuilder.build());
		 
		 
		 MethodSpec.Builder initBuilder = MethodSpec.methodBuilder("addRouteHandlers").addModifiers(Modifier.PUBLIC)
				 .addParameter(ParameterSpec.builder( io.undertow.server.RoutingHandler.class, "router", Modifier.FINAL).build());

		for( Method m : Users.class.getDeclaredMethods() )
		{
			String methodPath = HandleGenerator.extractPathTemplate.apply(m);
			
			log.debug("method path: " + methodPath);
			
			HttpString httpMethod = HandleGenerator.extractHttpMethod.apply(m);
			
			String methodName =    m.getName()  + StringUtils.capitalize(httpMethod.toString().toLowerCase()) + "Handler";
			
			 
			TypeSpec.Builder handlerClassBuilder  = TypeSpec.anonymousClassBuilder("")
				    .addSuperinterface(httpHandlerClass);
				   
				  
 				               

			//handlerClassBuilder.addModifiers(Modifier.PUBLIC);
 
		 	MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("handleRequest").addModifiers(Modifier.PUBLIC)
		 			.addException(ClassName.get("java.lang","Exception"))
		 			.addAnnotation(Override.class)
		 			.addParameter(ParameterSpec.builder(HttpServerExchange.class, "exchange", Modifier.FINAL).build());

			for( Parameter p : m.getParameters() )
			{
						if(p.getParameterizedType().equals(ServerRequest.class))
						{
							continue;
						}
						
						try
						{
							TypeHandler t = TypeHandler.forType(p.getParameterizedType());
							
							log.debug("t handler: " + t.name());
							if(t.isBlocking())
							{
								methodBuilder.beginControlFlow("if(exchange.isInIoThread())");
								methodBuilder.addStatement("exchange.dispatch(this)");
								methodBuilder.endControlFlow();

								break;
							}
							
						} catch (Exception e)
						{
							log.error(e.getMessage(),e);
						}
			};
			
		 
		 	
			Arrays.stream(m.getParameters()).forEachOrdered( p -> {
				
				try
				{
					
			 
				 
				
				if(p.getType().equals(ServerRequest.class))
				{
					methodBuilder.addCode(CodeBlock.of("ServerRequest serverRequest = new ServerRequest(exchange);\n"));
				}
				else
				{
					TypeHandler.addStatement(methodBuilder,p);
				}
//				else if(p.getType().equals(Long.class))
//				{
//					methodBuilder.addStatement("\nLong $L = extractLong.apply(exchange, $S)", p.getName(), p.getName());
//					}
//				else if(p.getType().equals(File.class))
//				{
//					methodBuilder.addStatement("\nFile $L = extractFile.apply(exchange, $S)", p.getName(), p.getName());
//					}
//				else if(p.getParameterizedType().equals(Optional.class))
//				{
//				 
//					methodBuilder.addStatement("\n$T $L = extractOptionalString.apply(exchange, $S)",p.getParameterizedType(),  p.getName(), p.getName()); 
//					}
				
				} catch (Exception e)
				{
					log.error(e.getMessage(),e);
				}
					
			});
			
			CodeBlock.Builder functionBlockBuilder = CodeBlock.builder();
			 
			String controllerMethodArgs  = Arrays.stream(m.getParameters()).map( p -> p.getName() ).collect(Collectors.joining(","));
			
			if( !m.getReturnType().equals(Void.class))
			{
				log.debug("return : " + m.getReturnType());
				functionBlockBuilder.add("$T $L = $L.$L($L);\n", m.getReturnType(), "response", controllerName, m.getName(), controllerMethodArgs );

			}
   
			methodBuilder.addCode(functionBlockBuilder.build());
			
			
			String returnContentType = MimeMappings.DEFAULT.getMimeType("json");
			
			Optional<javax.ws.rs.Produces> producesAnnotation =  Optional.ofNullable(m.getAnnotation(javax.ws.rs.Produces.class));
			
			if( !producesAnnotation.isPresent() )
			{
				producesAnnotation = Optional.ofNullable(clazz.getAnnotation(javax.ws.rs.Produces.class));
				
				if( producesAnnotation.isPresent() )
				{
					returnContentType = producesAnnotation.get().value()[0]; 
				}
 			}
			else
			{
				returnContentType = producesAnnotation.get().value()[0]; 
			}
			
			methodBuilder.addStatement("exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, $S)",returnContentType); 

			if( m.getReturnType().equals(String.class))
			{
				methodBuilder.addStatement("exchange.getResponseHeaders().send($L)","response");  
			}
			else 
			{
				methodBuilder.addStatement("exchange.getResponseSender().send(com.jsoniter.output.JsonStream.serialize($L))","response");  
			}
			
			
			handlerClassBuilder.addMethod(methodBuilder.build());

			
			FieldSpec handlerField = FieldSpec.builder(httpHandlerClass, methodName, Modifier.FINAL).initializer("$L",handlerClassBuilder.build()  ).build();
			 
 			
 			
			
			
 
			
			initBuilder.addCode("$L\n",handlerField.toString());
			
			initBuilder.addStatement("$L.add(io.undertow.util.Methods.$L,$S,$L)", "router", httpMethod,methodPath, methodName);
		}
		
		typeBuilder.addMethod(initBuilder.build());
			
		 
	}
	
	public static ArrayList<String> getClassNamesFromPackage(String packageName) throws Exception{
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    URL packageURL;
	    ArrayList<String> names = new ArrayList<String>();;

	    packageName = packageName.replace(".", "/");
	    packageURL = classLoader.getResource(packageName);

    	log.debug(packageURL+"");

	  
	    URI uri = new URI(packageURL.toString());
	    File folder = new File(uri.getPath());
	        // won't work with path which contains blank (%20)
	        // File folder = new File(packageURL.getFile()); 
	        File[] contenuti = folder.listFiles();
	        String entryName;
	        for(File actual: contenuti){
	        	if(actual.isDirectory())
	        	{
	        		continue;
	        	}
	        	log.debug(actual+"");
	            entryName = actual.getName();
	            entryName = entryName.substring(0, entryName.lastIndexOf('.'));
	            names.add(entryName);
	        }
	  
	    return names;
	}

}
