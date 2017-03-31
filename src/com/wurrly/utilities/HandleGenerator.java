/**
 * 
 */
package com.wurrly.utilities;

import static java.lang.invoke.MethodHandles.lookup;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import com.wurrly.controllers.Users;
import com.wurrly.models.User;

import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
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
		    final BiFunction<ServerRequest,String,?>[] biFunctions = new BiFunction[targetMethod.getParameterCount()];
		    
		    

		    for( int i = 1; i < targetMethod.getParameterCount(); i++ )
		    {
		    	final Parameter p = targetMethod.getParameters()[i];
		    	parameterNames[i] = p.getName();
		    	types[i] = p.getParameterizedType();
		    	
		    	Logger.debug("Type: " + types[i]);
		    	
		    	if( types[i].equals(Long.class) )
		    	{
		    		Logger.debug("Long type");

		    		biFunctions[i] = extractLong;
		    	}
		    	else if( types[i].equals(String.class) )
		    	{
		    		Logger.debug("String type");

		    		biFunctions[i] = extractString;
		    	}
		    	else if( types[i].equals(java.nio.file.Path.class) )
		    	{
		    		Logger.debug("Path type");
		    		biFunctions[i] = extractFilePath;
		    	}
		    	else if( types[i].equals(Any.class) )
		    	{
		    		Logger.debug("Any type");
		    		biFunctions[i] = extractAny;
		    	}
		    	else if( types[i].getTypeName().startsWith("java.util.Optional") )
		    	{
		    		Type rawType = ((ParameterizedType) types[i] );
		    		
		    		Logger.debug("Raw type: " + rawType);
		    		
		    		if( types[i].getTypeName().contains("java.lang.String") )
		    		{
		    			biFunctions[i] = extractOptionalString;
		    		}
		    		
		    	}
		    	
		    } 
		    
//		    final Object[] args = new Object[targetMethod.getParameterCount()];
		    
		    final HttpHandler mapper = new HttpHandler()
		    {
		    	@Override
				public void handleRequest(final HttpServerExchange exchange) throws Exception
				{
			    	try
					{
		    	 
		    		if(exchange.isInIoThread())
					{
		    		//	Logger.debug("in io thread");
						exchange.dispatch(this);
						return;
					}
		    		else
		    		{
		    		//	Logger.debug("not in io thread");
		    		}
		    		
	    			//Logger.debug("is dispatched: " + exchange.isDispatched());
	    			//Logger.debug("exchange.getConnection().getWorker(): " + exchange.getConnection().getWorker());

	    			 
		    		final ServerRequest request = new ServerRequest(exchange);
		    		 
//		    		final Long any =  Long.parseLong(request.exchange.getQueryParameters().get("userId").getFirst());

		    		final Optional<String> context = Optional.ofNullable(request.exchange.getQueryParameters().get("context")).map(Deque::getFirst);

		    		final User json = target.createUser(request,context,request.exchange.getAttachment(ServerRequest.REQUEST_JSON_BODY).read(User.class));
			    	 
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
	
	public final static BiFunction<ServerRequest,String,Long> extractLong = (request,name) -> {
		
		return Long.parseLong(request.exchange.getQueryParameters().get(name).getFirst());
		
	};
	
	public final static BiFunction<ServerRequest,String,Any> extractAny = (request,name) -> {
		
		try
		{
			return  request.exchange.getAttachment(ServerRequest.REQUEST_JSON_BODY).readAny();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			return Any.wrap(false);
		}
		
	};
	
	public final static BiFunction<ServerRequest,String,Path> extractFilePath = (request,name) -> {
		
		return request.files(name).getFirst().getPath();
		
	};
	
	public final static BiFunction<ServerRequest,String,Optional<String>> extractOptionalString = (request,name) -> {
		
		return Optional.ofNullable(request.exchange.getQueryParameters().get(name)).map(Deque::getFirst);
		
	};
	
	public final static BiFunction<ServerRequest,String,String> extractString = (request,name) -> {
		
		return request.exchange.getQueryParameters().get(name).getFirst();
		
	};
	
	   static Optional<Long> pathParamAsLong(HttpServerExchange exchange, String name) {
	        return baseParameter(exchange, name).map(Long::parseLong);
	    }

	    static Optional<Integer> pathParamAsInteger(HttpServerExchange exchange, String name) {
	        return baseParameter(exchange, name).map(Integer::parseInt);
	    } 
	    
	    
	static Optional<String> baseParameter(HttpServerExchange exchange, String name) {
        
        return Optional.ofNullable(exchange.getQueryParameters().get(name))
                       .map(Deque::getFirst);
    }
	
	private static interface RequestMapper<T> extends Function<HttpServerExchange,T>
	{
		T apply(HttpServerExchange exchange,String name);
	}
	
	public static BiFunction<HttpServerExchange,String, String> stringParameterMapper = (HttpServerExchange exchange, String name) ->
	{  
		return baseParameter(exchange,name).get(); 
	};
	
	public static BiFunction<HttpServerExchange,String, Optional<Long>> longParameterMapper = (HttpServerExchange exchange, String name) ->
	{  
		return baseParameter(exchange,name).map(Long::parseLong); 
	};
	
	public static BiFunction<HttpServerExchange, String, Optional<String>> optionalStringParameterMapper = (HttpServerExchange exchange, String name) ->
	{  
			return baseParameter(exchange,name); 
	};
	
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
