/**
 * 
 */
package com.wurrly.server;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Deque;
import java.util.Objects;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.spi.TypeLiteral;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData.FormValue;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.util.HttpString;
import io.undertow.util.Methods;

/**
 * @author jbauer
 */
public class Extractors
{
	private static Logger log = LoggerFactory.getLogger(Extractors.class.getCanonicalName());

	public static class Optional
	{

		public static java.util.Optional<JsonIterator> jsonIterator(final HttpServerExchange exchange)
		{
			return java.util.Optional.ofNullable( JsonIterator.parse(exchange.getAttachment(ServerRequest.JSON_DATA).array()));
		}

		public static  <T> java.util.Optional<T> typed(final HttpServerExchange exchange, final TypeLiteral<T> type )
		{
			return jsonIterator(exchange).map(i -> {
				try
				{
					return i.read(type);
				} catch (Exception e)
				{
					return null;
				}
			});
		}
		
		public static  <T> java.util.Optional<T> typed(final HttpServerExchange exchange, final Class<T> type )
		{
			return jsonIterator(exchange).map(i -> {
				try
				{
					return i.read(type);
				} catch (Exception e)
				{
					return null;
				}
			});
		}

		public static java.util.Optional<Date> date(final HttpServerExchange exchange,final String name)  {
			   
			  
			 return string(exchange, name).map( ZonedDateTime::parse ).map(ZonedDateTime::toInstant).map(Date::from);
			    
		}

		public static java.util.Optional<Any> any(final HttpServerExchange exchange )
		{
			return java.util.Optional.ofNullable(exchange.getAttachment(ServerRequest.JSON_DATA)).map(t -> JsonIterator.deserialize(t.array()));
		}

		public static  java.util.Optional<Integer> integerValue(final HttpServerExchange exchange, final String name)
		{
			return string(exchange, name).map(Integer::parseInt);
		}
		
		public static  java.util.Optional<Float> floatValue(final HttpServerExchange exchange, final String name)
		{
			return string(exchange, name).map(Float::parseFloat);
		}
		
		public static  java.util.Optional<Double> doubleValue(final HttpServerExchange exchange, final String name)
		{
			return string(exchange, name).map(Double::parseDouble);
		}
		 

		public static  java.util.Optional<Long> longValue(final HttpServerExchange exchange, final String name)
		{
			return string(exchange, name).map(Long::parseLong);
		}

		public static  java.util.Optional<Boolean> booleanValue(final HttpServerExchange exchange, final String name)
		{
			return string(exchange, name).map(Boolean::parseBoolean);
		}

		public static  <E extends Enum<E>> java.util.Optional<E> enumValue(final HttpServerExchange exchange, final Class<E> clazz, final String name)
		{
			return string(exchange, name).map(e -> Enum.valueOf(clazz, name));
		}

		public static  java.util.Optional<String> string(final HttpServerExchange exchange, final String name)
		{
			return java.util.Optional.ofNullable(exchange.getQueryParameters().get(name)).map(Deque::getFirst);
		}
 

		public static  java.util.Optional<Path> filePath(final HttpServerExchange exchange, final String name)
		{
			return java.util.Optional.ofNullable(exchange.getAttachment(FormDataParser.FORM_DATA).get(name)).map(Deque::getFirst).map(FormValue::getPath);
		}
	}
	
	public static class Header
	{
		public static  String string(final HttpServerExchange exchange, final String name)
		{
			return exchange.getRequestHeaders().get(name).getFirst();
		}
		
		public static class Optional
		{
			
			public static  java.util.Optional<String> string(final HttpServerExchange exchange, final String name)
			{
				return java.util.Optional.ofNullable(exchange.getRequestHeaders().get(name)).map(Deque::getFirst);
			}
		}
		
		
	}
	
	public static Date date(final HttpServerExchange exchange,final String name)  {
		  
		 return Date.from( ZonedDateTime.parse( string(exchange,name) ).toInstant() );
		    
	}

	public static  <T> T typed(final HttpServerExchange exchange, final TypeLiteral<T> type )  
	{
		try
		{
			return jsonIterator(exchange).read(type);
		}
		catch( Exception e )
		{
			return null;
		}
	}
	
	public static  <T> T typed(final HttpServerExchange exchange, final Class<T> type )  
	{
		try
		{
			return jsonIterator(exchange).read(type);
		}
		catch( Exception e )
		{
			return null;
		}
	}

	public static  Any any(final HttpServerExchange exchange )
	{
		try
		{
			return JsonIterator.parse( exchange.getAttachment(ServerRequest.JSON_DATA).array() ).readAny();
		} catch (IOException e)
		{
			return Any.wrapNull();
		}
	}

	public static  JsonIterator jsonIterator(final HttpServerExchange exchange )
	{
		return JsonIterator.parse(exchange.getAttachment(ServerRequest.JSON_DATA).array());
	}

	public static  Path filePath(final HttpServerExchange exchange, final String name)
	{
		return exchange.getAttachment(FormDataParser.FORM_DATA).get(name).getFirst().getPath();
	}
	
	public static  ByteBuffer fileBytes(final HttpServerExchange exchange, final String name) throws IOException
	{
		 final Path filePath = filePath(exchange,name);
		   
		 try(final FileChannel fileChannel = FileChannel.open(filePath, StandardOpenOption.READ))
		 {
			 final ByteBuffer buffer = ByteBuffer.allocate((int)fileChannel.size());
			 
			 fileChannel.read(buffer);

			 buffer.flip();
			 
			 return buffer;
		 } 
		 
	}

	public static  Float floatValue(final HttpServerExchange exchange, final String name)
	{
		return Float.parseFloat(string(exchange, name));
	}
	
	public static  Double doubleValue(final HttpServerExchange exchange, final String name)
	{
		return Double.parseDouble(string(exchange, name));
	}
	
	public static  String string(final HttpServerExchange exchange, final String name)
	{
		return exchange.getQueryParameters().get(name).getFirst();
	}

	public static  Long longValue(final HttpServerExchange exchange, final String name)
	{
		return Long.parseLong(string(exchange, name));
	}

	public static  Integer integerValue(final HttpServerExchange exchange, final String name)
	{
		return Integer.parseInt(string(exchange, name));
	}

	public static  Boolean booleanValue(final HttpServerExchange exchange, final String name)
	{
		return Boolean.parseBoolean(string(exchange, name));
	}
	


	public static  <E extends Enum<E>> E enumValue(final HttpServerExchange exchange, Class<E> clazz,final String name)
	{
		return Enum.valueOf(clazz, string(exchange, name));
	}
	
	
	
	public static  Function<Method,HttpString> httpMethodFromMethod = (m) ->
			Arrays.stream(m.getDeclaredAnnotations()).map( a -> {


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

            }).filter(Objects::nonNull).findFirst().get();
	
	 
	public static Function<Method,String> pathTemplateFromMethod = (m) ->
	{
		javax.ws.rs.Path childPath = m.getDeclaredAnnotation(javax.ws.rs.Path.class);
		
		javax.ws.rs.Path parentPath = m.getDeclaringClass().getDeclaredAnnotation(javax.ws.rs.Path.class);
		
		if(!childPath.value().equals("/"))
		{
			return (parentPath.value() + '/' + childPath.value()).replaceAll("\\/\\/", "\\/")  ;
		}
		
		return (parentPath.value() )  ;
		
	};  

}
