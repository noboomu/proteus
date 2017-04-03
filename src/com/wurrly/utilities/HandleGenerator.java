/**
 * 
 */
package com.wurrly.utilities;

import static java.lang.invoke.MethodHandles.lookup;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.TypeLiteral;
import com.wurrly.controllers.Users;
import com.wurrly.models.User;
import com.wurrly.server.Extractors;
import com.wurrly.server.ServerRequest;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData.FormValue;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

/**
 * @author jbauer
 *
 */
public class HandleGenerator
{
	private static Logger Logger = LoggerFactory.getLogger(HandleGenerator.class.getCanonicalName());

	public static void main(String[] args)
	{

		try
		{
			String prefix = "com.wurrly.controllers";
			List<String> classNames = getClassNamesFromPackage(prefix);
			
			for( String s : classNames)
			{
				String className = prefix + "." + s;
				
				Class<?> clazz = Class.forName(className);
				
				System.out.println(clazz);
				
				Users users = new Users();
				
				for( Method m : clazz.getDeclaredMethods() )
				{
					System.out.println("method: " + m);
					HttpHandler handler = generateHandler(users, m,false);
					
					System.out.println("handler: " + handler);
					 
				}
			}
			
			System.out.println(classNames);

		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
	
	
	public final static Function<Method,HttpString> extractHttpMethod = (m) ->
	{
		return Arrays.stream(m.getDeclaredAnnotations()).map( a -> {
			
			System.out.println("annotation: " + a);
			
			if( a instanceof javax.ws.rs.POST)
			{
				return Methods.POST;
			}
			else if( a instanceof javax.ws.rs.GET)
			{
				return Methods.GET;
			}
			else if( a instanceof javax.ws.rs.PUT)
			{
				return Methods.PUT;
			}
			else if( a instanceof javax.ws.rs.DELETE)
			{
				return Methods.DELETE;
			}
			else if( a instanceof javax.ws.rs.OPTIONS)
			{
				return Methods.OPTIONS;
			}
		
			else
			{
				return null;
			}
			
		}).filter( hm -> hm != null ).findFirst().get();
		
	};
	
	 
	public final static Function<Method,String> extractPathTemplate = (m) ->
	{
		javax.ws.rs.Path childPath = m.getDeclaredAnnotation(javax.ws.rs.Path.class);
		
		javax.ws.rs.Path parentPath = m.getDeclaringClass().getDeclaredAnnotation(javax.ws.rs.Path.class);
		
		if(!childPath.value().equals("/"))
		{
			return (parentPath.value() + '/' + childPath.value()).replaceAll("\\/\\/", "\\/")  ;
		}
		
		return (parentPath.value() )  ;
		
	};
			
			
			
	public static HttpHandler  generateHandler(final Users target, final Method targetMethod, final boolean blocking)
	{
		try
		{
		    final MethodType factoryMethodType = MethodType.methodType(targetMethod.getDeclaringClass());

		    final Class<?> methodReturn = targetMethod.getReturnType();
		    final Class<?>[] methodParams = targetMethod.getParameterTypes();

		    final MethodType functionMethodType = MethodType.methodType(methodReturn, methodParams);
		    
		    MethodHandles.Lookup lookup = lookup();
		    targetMethod.setAccessible(true);
		    final MethodHandles.Lookup caller = lookup.in(targetMethod.getDeclaringClass());
		    final MethodHandle implementationMethod;

		    try {
		        implementationMethod = caller.unreflect(targetMethod);
		    } catch (IllegalAccessException e) {
		       throw Throwables.propagate(e);
		    }
		    
		    final String[] parameterNames = new String[targetMethod.getParameterCount()];
		    final Type[] types = new Type[targetMethod.getParameterCount()];
//		    final BiFunction<ServerRequest,String,?>[] biFunctions = new BiFunction[targetMethod.getParameterCount()];
//		    
//		    
//
		    for( int i = 1; i < targetMethod.getParameterCount(); i++ )
		    {
		    	final Parameter p = targetMethod.getParameters()[i];
		    	parameterNames[i] = p.getName();
		    	types[i] = p.getParameterizedType();
		    	
//		    	Logger.debug("Type: " + types[i]);
//		    	
//		    	if( types[i].equals(Long.class) )
//		    	{
//		    		Logger.debug("Long type");
//
//		    		//biFunctions[i] = extractLong;
//		    	}
//		    	else if( types[i].equals(String.class) )
//		    	{
//		    		Logger.debug("String type");
//
//		    		//biFunctions[i] = extractString;
//		    	}
//		    	else if( types[i].equals(java.nio.file.Path.class) )
//		    	{
//		    		Logger.debug("Path type");
//		    		//biFunctions[i] = extractFilePath;
//		    	}
//		    	else if( types[i].equals(Any.class) )
//		    	{
//		    		Logger.debug("Any type");
//		    		biFunctions[i] = extractAny;
//		    	}
//		    	else if( types[i].getTypeName().startsWith("java.util.Optional") )
//		    	{
//		    		Type rawType = ((ParameterizedType) types[i] );
//		    		
//		    		Logger.debug("Raw type: " + rawType);
//		    		
//		    		if( types[i].getTypeName().contains("java.lang.String") )
//		    		{
//		    			biFunctions[i] = extractOptionalString;
//		    		}
//		    		
//		    	}
		    	
		    } 
		    
//		    final Object[] args = new Object[targetMethod.getParameterCount()];
		    
		    final User tmpUser = new User();
		    
		    final HttpHandler mapper = new HttpHandler()
		    {
		    	@Override
				public void handleRequest(final HttpServerExchange exchange) throws Exception
				{
			    	try
					{
//		    	 
//		    		if(exchange.isInIoThread())
//					{
//		    		//	Logger.debug("in io thread");
//						exchange.dispatch(this);
//						return;
//					}
//		    		else
//		    		{
//		    		//	Logger.debug("not in io thread");
//		    		}
		    		
	    			//Logger.debug("is dispatched: " + exchange.isDispatched());
	    			//Logger.debug("exchange.getConnection().getWorker(): " + exchange.getConnection().getWorker());

	    			 
		    		final ServerRequest request = new ServerRequest(exchange);
		    		 
		    		final Long id =  extractLong(exchange,"userId");

		    		java.util.List<com.wurrly.models.User> t;
		    		
		    		
		    		final Optional<String> context = extractOptional(exchange,"context");
		    		//final User.UserType userType = extractEnum(exchange,"type",User.UserType.class);

		    	//	final User user = extractJsonIterator(exchange,"user").read(User.class);

		    		//final Any json = target.userForm(request, id, context,  userType, Extractors.fileBytes(exchange, "testFile"));
		    		
		    		//TypeLiteral<List<User>> typeLiteral = TypeLiteral.create(types[2]);
		    		
		    		final Any json = target.user(request,  id, context );

//		    		json.whenComplete( ( u, e ) -> {
//		    			
//		    			if(e != null)
//		    			{
//		    				e.printStackTrace(); 
//		    				exchange.setStatusCode(500);
//		    				exchange.getResponseSender().send(e.getMessage());
//		    			}
//		    			else
//		    			{
		    			 exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json"); 
				    	 exchange.getResponseSender().send(JsonStream.serialize(json));
				    	  
				    	 
//		    			}
//
//		    			
//		    		});
			    	  
 					
					} catch (Throwable e)
					{
						e.printStackTrace(); 
					}
				}
		    };
		    
		    return mapper;
		    
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
 
//	
//	public final static BiFunction<ServerRequest,String,Any> extractAny = (request,name) -> {
//		
//		try
//		{
//			return  request.exchange.getAttachment(ServerRequest.JSON_DATA).readAny();
//		} catch (IOException e)
//		{
//			// TODO Auto-generated catch block
//			return Any.wrap(false);
//		}
//		
//	};
//	
//	public static JsonIterator extractJsonIterator(final HttpServerExchange exchange, final String name)
//	{
//		return exchange.getAttachment(ServerRequest.JSON_DATA);
//	}
//	
//	public static Any extractAny(final HttpServerExchange exchange, final String name)
//	{
//		try
//		{
//			return  exchange.getAttachment(ServerRequest.JSON_DATA).readAny();
//		} catch (IOException e)
//		{ 
//			return Any.wrap(false);
//		}
//	}
	
	public static String extractString(final HttpServerExchange exchange, final String name)
	{
		return exchange.getQueryParameters().get(name).getFirst();
	}
	
	public static Path extractFilePath(final HttpServerExchange exchange, final String name)
	{
		return exchange.getAttachment(FormDataParser.FORM_DATA).get(name).getFirst().getPath();
	}
	
	public static Long extractLong(final HttpServerExchange exchange, final String name)
	{
		return Long.parseLong(extractString(exchange,name));
	}
	
	public static Integer extractInteger(final HttpServerExchange exchange, final String name)
	{
		return Integer.parseInt(extractString(exchange,name));
	}
	
	public static Boolean extractBoolean(final HttpServerExchange exchange, final String name)
	{
		return Boolean.parseBoolean(extractString(exchange,name));
	}
	
	public static <E extends Enum<E>> E extractEnum(final HttpServerExchange exchange, final String name, Class<E> clazz)
	{
		return Enum.valueOf(clazz, extractString(exchange,name));
	}
 
	public static Optional<String> extractOptional(final HttpServerExchange exchange, final String name)
	{
		return Optional.ofNullable(exchange.getQueryParameters().get(name)).map(Deque::getFirst);
	}
	
	public static Optional<Path> extractOptionalFilePath(final HttpServerExchange exchange, final String name)
	{
		return Optional.ofNullable(exchange.getAttachment(FormDataParser.FORM_DATA).get(name)).map(Deque::getFirst).map(FormValue::getPath);
	}
	
	public static Optional<Integer> extractOptionalInteger(final HttpServerExchange exchange, final String name)
	{
		return extractOptional(exchange,name).map(Integer::parseInt);
	}
	
	public static Optional<Long> extractOptionalLong(final HttpServerExchange exchange, final String name)
	{
		return extractOptional(exchange,name).map(Long::parseLong);
	}
	
	public static Optional<Boolean> extractOptionalBoolean(final HttpServerExchange exchange, final String name)
	{
		return extractOptional(exchange,name).map(Boolean::parseBoolean);
	}
	
	public static <E extends Enum<E>> Optional<E> extractOptionalEnum(final HttpServerExchange exchange, final String name, Class<E> clazz)
	{
		return extractOptional(exchange,name).map( e -> Enum.valueOf(clazz, name));
	}
	 
	
//	private static interface RequestMapper<T> extends Function<HttpServerExchange,T>
//	{
//		T apply(HttpServerExchange exchange,String name);
//	}
	
//	public static BiFunction<HttpServerExchange,String, String> stringParameterMapper = (HttpServerExchange exchange, String name) ->
//	{  
//		return baseParameter(exchange,name).get(); 
//	};
//	
//	public static BiFunction<HttpServerExchange,String, Optional<Long>> longParameterMapper = (HttpServerExchange exchange, String name) ->
//	{  
//		return baseParameter(exchange,name).map(Long::parseLong); 
//	};
//	
//	public static BiFunction<HttpServerExchange, String, Optional<String>> optionalStringParameterMapper = (HttpServerExchange exchange, String name) ->
//	{  
//			return baseParameter(exchange,name); 
//	};
	
//	public static RequestMapper<Long> generateLongParameterMapper(String name)
//	{ 
//		return (HttpServerExchange exchange) -> { 
//			return baseParameter(exchange,name).map(Long::parseLong).get();
//		};  
//	}
//	
//	public static RequestMapper<Integer> generateIntegerParameterMapper(String name)
//	{ 
//		return (HttpServerExchange exchange) -> { 
//			return baseParameter(exchange,name).map(Integer::parseInt).get();
//		};  
//	}
	
	public static ArrayList<String> getClassNamesFromPackage(String packageName) throws Exception{
	    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	    URL packageURL;
	    ArrayList<String> names = new ArrayList<String>();;

	    packageName = packageName.replace(".", "/");
	    packageURL = classLoader.getResource(packageName);

    	System.out.println(packageURL);

	  
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
	        	System.out.println(actual);
	            entryName = actual.getName();
	            entryName = entryName.substring(0, entryName.lastIndexOf('.'));
	            names.add(entryName);
	        }
	  
	    return names;
	}

}
