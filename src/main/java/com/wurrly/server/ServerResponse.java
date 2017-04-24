/**
 * 
 */
package com.wurrly.server;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonContext;
import com.jsoniter.output.JsonStream;
import com.jsoniter.spi.Encoder;
import com.wurrly.server.predicates.ServerPredicates;

import io.undertow.attribute.ExchangeAttributes;
import io.undertow.io.IoCallback;
import io.undertow.predicate.Predicate;
import io.undertow.predicate.Predicates;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.util.HeaderMap;
import io.undertow.util.HeaderValues;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;

/**
 * @author jbauer
 * @TODO ParameterizevServerResponse TypeLiteral relevant Jsoniter encoder This will remove the lookup and shave some time. Experiment with a ThreadLocal JsonStream and AsciiStream. Refer to JsonStream serialization
 */
public class ServerResponse<T>
{
	private static Logger log = LoggerFactory.getLogger(ServerResponse.class.getCanonicalName());

	protected static final XmlMapper XML_MAPPER = new XmlMapper();

	protected ByteBuffer body;

	protected int status = StatusCodes.OK;
	protected final HeaderMap headers = new HeaderMap();
	protected final Map<String, Cookie> cookies = new HashMap<>();
	protected String contentType = null;
	protected T entity;
	protected Throwable throwable;
	protected Class<? extends JsonContext> jsonContext;
	protected IoCallback ioCallback;
	protected boolean hasCookies = false;
	protected boolean hasHeaders = false;
	protected boolean hasIoCallback = false;
	protected boolean isXml = false;
	protected boolean isJson = false;

	public ServerResponse()
	{

	}

	public ByteBuffer getBody()
	{
		return body;
	}

	public int getStatus()
	{
		return this.status;
	}

	public Map<String, Cookie> getCookies()
	{
		return this.cookies;
	}

	public HeaderMap getHeaders()
	{
		return this.headers;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType()
	{
		return contentType;
	}

	/**
	 * @return the callback
	 */
	public IoCallback getIoCallback()
	{
		return ioCallback;
	}

	/**
	 * @param callback
	 *            the callback to set
	 */
	public void setIoCallback(IoCallback ioCallback)
	{
		this.ioCallback = ioCallback;
	}

	/**
	 * @param body
	 *            the body to set
	 */
	public void setBody(ByteBuffer body)
	{
		this.body = body;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}

	/**
	 * @param contentType
	 *            the contentType to set
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;

		if (this.contentType.equals(MimeTypes.APPLICATION_JSON_TYPE))
		{
			this.isJson = true;
		}
		else if (this.contentType.equals(MimeTypes.APPLICATION_XML_TYPE))
		{
			this.isXml = true;
		}
	}

	public ServerResponse<T> body(ByteBuffer body)
	{
		this.body = body;
		return this;
	}

	public ServerResponse<T> entity(T entity)
	{
		this.entity = entity;
		return this;
	}

	public ServerResponse<T> throwable(Throwable throwable)
	{
		this.throwable = throwable;
		return this;
	}

	public ServerResponse<T> body(String body)
	{
		this.body = ByteBuffer.wrap(body.getBytes());
		return this;
	}

	public ServerResponse<T> status(int status)
	{
		this.status = status;
		return this;
	}

	public ServerResponse<T> header(HttpString headerName, String value)
	{
		this.headers.put(headerName, value);
		this.hasHeaders = true;
		return this;
	}

	public ServerResponse<T> cookie(String cookieName, Cookie cookie)
	{
		this.cookies.put(cookieName, cookie);
		this.hasCookies = true;

		return this;
	}

	public ServerResponse<T> contentType(String contentType)
	{
		this.setContentType(contentType);
		return this;
	}

	public ServerResponse<T> applicationJson()
	{
		this.contentType = MimeTypes.APPLICATION_JSON_TYPE;
		this.isJson = true;

		return this;
	}

	public ServerResponse<T> textHtml()
	{
		this.contentType = MimeTypes.TEXT_HTML_TYPE;
		return this;
	}

	public ServerResponse<T> applicationXml()
	{
		this.contentType = MimeTypes.APPLICATION_XML_TYPE;
		this.isXml = true;
		return this;
	}

	public ServerResponse<T> textPlain()
	{
		this.contentType = MimeTypes.TEXT_PLAIN_TYPE;
		return this;
	}
	
	public ServerResponse<T> jsonContext(Class<? extends JsonContext> context)
	{
		this.jsonContext = context;
		return this;
	}


	public ServerResponse<T> ok()
	{
		this.status = StatusCodes.OK;
		return this;
	}

	public ServerResponse<T> accepted()
	{
		this.status = StatusCodes.ACCEPTED;
		return this;
	}

	public ServerResponse<T> badRequest()
	{
		this.status = StatusCodes.BAD_REQUEST;
		return this;
	}

	public ServerResponse<T> internalServerError()
	{
		this.status = StatusCodes.INTERNAL_SERVER_ERROR;
		return this;
	}

	public ServerResponse<T> created()
	{
		this.status = StatusCodes.CREATED;
		return this;
	}

	public ServerResponse<T> notFound()
	{
		this.status = StatusCodes.NOT_FOUND;
		return this;
	}

	public ServerResponse<T> forbidden()
	{
		this.status = StatusCodes.FORBIDDEN;
		return this;
	}

	public ServerResponse<T> found()
	{
		this.status = StatusCodes.FOUND;
		return this;
	}

	public ServerResponse<T> noContent()
	{
		this.status = StatusCodes.NO_CONTENT;
		return this;
	}

	public ServerResponse<T> withIoCallback(IoCallback ioCallback)
	{
		this.ioCallback = ioCallback;
		this.hasIoCallback = ioCallback == null;
		return this;
	}

	public ServerResponse<T> exception(Throwable t)
	{
		if (this.status == StatusCodes.ACCEPTED)
		{
			badRequest();
		}
		return this.throwable(t);
	}
	
	public void send( final HttpServerExchange exchange) throws RuntimeException
	{
		send(null,exchange);
	}

	public void send(final HttpHandler handler, final HttpServerExchange exchange) throws RuntimeException
	{
		final boolean hasBody = this.body != null;
		final boolean hasEntity = this.entity != null;

		if (this.hasHeaders)
		{
			long itr = this.headers.fastIterateNonEmpty();

			while (itr != -1L)
			{
				final HeaderValues values = this.headers.fiCurrent(itr);

				exchange.getResponseHeaders().putAll(values.getHeaderName(), values);

				itr = this.headers.fiNextNonEmpty(itr);
			}
		}

		if (this.hasCookies)
		{
			exchange.getResponseCookies().putAll(this.cookies);
		}

		exchange.setStatusCode(this.status);

		if (this.contentType != null)
		{
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, this.contentType);
		}
		else if (!this.isJson && !this.isXml)
		{
			if (ServerPredicates.ACCEPT_JSON_PREDICATE.resolve(exchange))
			{
				this.applicationJson();
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, this.contentType);
			}
			else if (ServerPredicates.ACCEPT_XML_PREDICATE.resolve(exchange))
			{
				this.applicationXml();
				exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, this.contentType);
			}
		}

		if (hasBody)
		{
			if (!this.hasIoCallback)
			{
				exchange.getResponseSender().send(this.body);
			}
			else
			{
				exchange.getResponseSender().send(this.body, this.ioCallback);
			}
		}
		else if (hasEntity)
		{
			try
			{

				if (this.isXml)
				{
					exchange.getResponseSender().send(ByteBuffer.wrap(XML_MAPPER.writeValueAsBytes(this.entity)));
				}
				else
				{
					if (exchange.isInIoThread() && handler != null)
					{
						exchange.dispatch(handler);
						return;
					}

					exchange.startBlocking();

					final int bufferSize = exchange.getConnection().getBufferSize();
					
					/**
					 * @TODO Test that this is faster than a thread local JsonStream with the TypeLiteral relevant encoder 
					 **/

					try (final JsonStream stream = new JsonStream(exchange.getOutputStream(), bufferSize))
					{
						stream.writeViewVal(this.entity,this.jsonContext);
					}

					exchange.endExchange();

				}

			} catch (Exception e)
			{
				log.error(e.getMessage() + " for entity " + this.entity, e);

				throw new IllegalArgumentException(e);
			}

		}
		else
		{
			exchange.endExchange();
		}

	}

	/**
	 * Creates builder to build {@link ServerResponse}.
	 * 
	 * @return created builder
	 */
	public static <T> ServerResponse<T> response(Class<T> clazz)
	{
		return new ServerResponse<T>();
	}
	
	public static  ServerResponse<ByteBuffer> response(ByteBuffer body)
	{
		return new ServerResponse<ByteBuffer>().body(body);
	}
	
	public static  ServerResponse<ByteBuffer> response(String body)
	{
		return new ServerResponse<ByteBuffer>().body(body);
	}
	
	public static <T> ServerResponse<T> response(T entity)
	{
		return new ServerResponse<T>().entity(entity);
	}
 
	public static ServerResponse<?> response()
	{
		return new ServerResponse();
	}
	
 

}
