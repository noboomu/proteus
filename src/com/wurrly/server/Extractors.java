/**
 * 
 */
package com.wurrly.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Deque;
import java.util.Optional;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.spi.TypeLiteral;

import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormData.FormValue;

/**
 * @author jbauer
 */
public class Extractors
{
	public static class Optional
	{

		public static final java.util.Optional<JsonIterator> jsonIterator(final HttpServerExchange exchange)
		{
			return java.util.Optional.ofNullable( JsonIterator.parse(exchange.getAttachment(ServerRequest.JSON_DATA).array()));
		}

		public static final <T> java.util.Optional<T> typed(final HttpServerExchange exchange, final TypeLiteral<T> type )
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

		public static final java.util.Optional<Any> any(final HttpServerExchange exchange, final String name)
		{
			return java.util.Optional.ofNullable(exchange.getAttachment(ServerRequest.JSON_DATA)).map(t -> {
				 
					return JsonIterator.deserialize(t.array());
				 
			});
		}

		public static final java.util.Optional<Integer> integerValue(final HttpServerExchange exchange, final String name)
		{
			return string(exchange, name).map(Integer::parseInt);
		}

		public static final java.util.Optional<Long> longValue(final HttpServerExchange exchange, final String name)
		{
			return string(exchange, name).map(Long::parseLong);
		}

		public static final java.util.Optional<Boolean> booleanValue(final HttpServerExchange exchange, final String name)
		{
			return string(exchange, name).map(Boolean::parseBoolean);
		}

		public static final <E extends Enum<E>> java.util.Optional<E> enumValue(final HttpServerExchange exchange, final Class<E> clazz, final String name)
		{
			return string(exchange, name).map(e -> Enum.valueOf(clazz, name));
		}

		public static final java.util.Optional<String> string(final HttpServerExchange exchange, final String name)
		{
			return java.util.Optional.ofNullable(exchange.getQueryParameters().get(name)).map(Deque::getFirst);
		}

		public static final java.util.Optional<Path> filePath(final HttpServerExchange exchange, final String name)
		{
			return java.util.Optional.ofNullable(exchange.getAttachment(FormDataParser.FORM_DATA).get(name)).map(Deque::getFirst).map(FormValue::getPath);
		}
	}

	public static final <T> T typed(final HttpServerExchange exchange, final TypeLiteral<T> type ) throws Exception
	{
		return jsonIterator(exchange).read(type);
	}

	public static final Any any(final HttpServerExchange exchange, final String name)
	{
		try
		{
			return JsonIterator.parse( exchange.getAttachment(ServerRequest.JSON_DATA).array() ).readAny();
		} catch (IOException e)
		{
			return Any.wrapNull();
		}
	}

	public static final JsonIterator jsonIterator(final HttpServerExchange exchange )
	{
		return JsonIterator.parse(exchange.getAttachment(ServerRequest.JSON_DATA).array());
	}

	public static final Path filePath(final HttpServerExchange exchange, final String name)
	{
		return exchange.getAttachment(FormDataParser.FORM_DATA).get(name).getFirst().getPath();
	}
	
	public static final ByteBuffer fileBytes(final HttpServerExchange exchange, final String name) throws IOException
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

	public static final String string(final HttpServerExchange exchange, final String name)
	{
		return exchange.getQueryParameters().get(name).getFirst();
	}

	public static final Long longValue(final HttpServerExchange exchange, final String name)
	{
		return Long.parseLong(string(exchange, name));
	}

	public static final Integer integerValue(final HttpServerExchange exchange, final String name)
	{
		return Integer.parseInt(string(exchange, name));
	}

	public static final Boolean booleanValue(final HttpServerExchange exchange, final String name)
	{
		return Boolean.parseBoolean(string(exchange, name));
	}

	public static final <E extends Enum<E>> E enumValue(final HttpServerExchange exchange, final String name, Class<E> clazz)
	{
		return Enum.valueOf(clazz, string(exchange, name));
	}

}
