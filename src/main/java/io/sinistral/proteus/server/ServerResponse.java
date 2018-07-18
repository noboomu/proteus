/**
 * 
 */
package io.sinistral.proteus.server;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.Inject;

import io.sinistral.proteus.server.predicates.ServerPredicates;
import io.undertow.io.IoCallback;
import io.undertow.server.DefaultResponseListener;
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
 */
public class ServerResponse<T>
{
	private static Logger log = LoggerFactory.getLogger(ServerResponse.class.getCanonicalName());

	@Inject
	protected static XmlMapper XML_MAPPER;

	@Inject
	protected static ObjectMapper OBJECT_MAPPER;

	protected ByteBuffer body;

	protected int status = StatusCodes.OK;
	protected final HeaderMap headers = new HeaderMap();
	protected final Map<String, Cookie> cookies = new HashMap<>();
	protected String contentType = null;
	protected T entity;
	protected Throwable throwable;
//	protected Class<? extends JsonContext> jsonContext;
	protected IoCallback ioCallback;
	protected boolean hasCookies = false;
	protected boolean hasHeaders = false;
	protected boolean hasIoCallback = false;
	protected boolean processXml = false;
	protected boolean processJson = false;
	protected boolean preprocessed = false;

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
	 * @param ioCallback
	 *            the ioCallback to set
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

		if (this.contentType.equals(javax.ws.rs.core.MediaType.APPLICATION_JSON))
		{
			if (!this.preprocessed)
			{
				this.processJson = true;
			}
		}
		else if (this.contentType.equals(javax.ws.rs.core.MediaType.APPLICATION_XML))
		{
			if (!this.preprocessed)
			{
				this.processXml = true;
			}
		}
	}

	public ServerResponse<T> body(ByteBuffer body)
	{
		this.body = body;
		this.preprocessed = true;
		return this;
	}

	public ServerResponse<T> body(String body)
	{
		return this.body(ByteBuffer.wrap(body.getBytes()));
	}

	public ServerResponse<T> entity(T entity)
	{
		this.entity = entity;
		this.preprocessed = false;

		return this;
	}

	public ServerResponse<T> throwable(Throwable throwable)
	{
		this.throwable = throwable;
		if (this.status == StatusCodes.ACCEPTED)
		{
			badRequest();
		}
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
		if (!this.preprocessed)
		{
			this.processJson = true;
		}
		this.contentType = javax.ws.rs.core.MediaType.APPLICATION_JSON;
		return this;
	}

	public ServerResponse<T> textHtml()
	{
		this.contentType = javax.ws.rs.core.MediaType.TEXT_HTML;
		return this;
	}

	public ServerResponse<T> applicationOctetStream()
	{
		this.contentType = javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
		return this;
	}

	public ServerResponse<T> applicationXml()
	{
		if (!this.preprocessed)
		{
			this.processXml = true;
		}
		this.contentType = javax.ws.rs.core.MediaType.APPLICATION_XML;
		return this;
	}

	public ServerResponse<T> textPlain()
	{
		this.contentType = javax.ws.rs.core.MediaType.TEXT_PLAIN;
		return this;
	}

//	public ServerResponse<T> jsonContext(Class<? extends JsonContext> context)
//	{
//		this.jsonContext = context;
//		return this;
//	}

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
	
	public ServerResponse<T> badRequest(Throwable t)
	{
		this.status = StatusCodes.BAD_REQUEST;
		this.throwable = t;
		return this;
	}

	public ServerResponse<T> internalServerError()
	{
		this.status = StatusCodes.INTERNAL_SERVER_ERROR;
		return this;
	}
	
	public ServerResponse<T> internalServerError(Throwable t)
	{
		this.status = StatusCodes.INTERNAL_SERVER_ERROR;
		this.throwable = t;
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
	
	public ServerResponse<T> notFound(Throwable t)
	{
		this.status = StatusCodes.NOT_FOUND;
		this.throwable = t;
		return this;
	}

	public ServerResponse<T> forbidden()
	{
		this.status = StatusCodes.FORBIDDEN;
		return this;
	}
	
	public ServerResponse<T> forbidden(Throwable t)
	{
		this.status = StatusCodes.FORBIDDEN;
		this.throwable = t;
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
	
	public ServerResponse<T> noContent(Throwable t)
	{
		this.status = StatusCodes.NO_CONTENT;
		this.throwable = t;
		return this;
	}

	public ServerResponse<T> withIoCallback(IoCallback ioCallback)
	{
		this.ioCallback = ioCallback;
		this.hasIoCallback = ioCallback == null;
		return this;
	}

	public void send(final HttpServerExchange exchange) throws RuntimeException
	{
		send(null, exchange);
	}

	public void send(final HttpHandler handler, final HttpServerExchange exchange) throws RuntimeException
	{
		final boolean hasBody = this.body != null;
		final boolean hasEntity = this.entity != null;
		final boolean hasError = this.throwable != null;

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
		else if (!this.processJson && !this.processXml)
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

		if (hasError)
		{
			exchange.putAttachment(DefaultResponseListener.EXCEPTION, throwable);
			
			return;
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
				if (this.processXml)
				{
					exchange.getResponseSender().send(ByteBuffer.wrap(XML_MAPPER.writeValueAsBytes(this.entity)));
				}
				else
				{

					exchange.getResponseSender().send(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(this.entity)));
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

	public static ServerResponse<ByteBuffer> response(ByteBuffer body)
	{
		return new ServerResponse<ByteBuffer>().body(body);
	}

	public static ServerResponse<ByteBuffer> response(String body)
	{
		return new ServerResponse<ByteBuffer>().body(body);
	}

	public static <T> ServerResponse<T> response(T entity)
	{
		return new ServerResponse<T>().entity(entity);
	}

	@SuppressWarnings("rawtypes")
	public static ServerResponse response()
	{
		return new ServerResponse();
	}

}
