/**
 * 
 */
package com.wurrly.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;

import io.undertow.io.IoCallback;
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
 *
 */
public class ServerResponse
{
	private static Logger log = LoggerFactory.getLogger(ServerResponse.class.getCanonicalName());
	private static String APPLICATION_JSON_CONTENT_TYPE = org.apache.http.entity.ContentType.APPLICATION_JSON.getMimeType();
	private static String TEXT_PLAIN_CONTENT_TYPE = org.apache.http.entity.ContentType.TEXT_PLAIN.getMimeType();
	private static String TEXT_XML_CONTENT_TYPE = org.apache.http.entity.ContentType.TEXT_XML.getMimeType();
	private static String TEXT_HTML_CONTENT_TYPE = org.apache.http.entity.ContentType.TEXT_HTML.getMimeType();

	
	private ByteBuffer body;
	
	private int status = StatusCodes.OK;
	private final HeaderMap headers = new HeaderMap();
	private final Map<String,Cookie> cookies = new HashMap<>(); 
	private String contentType = null;
	private Object entity;
	private IoCallback ioCallback;
	private boolean hasCookies = false;
	private boolean hasHeaders = false;
	private boolean hasIoCallback = false;
  	
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
	
	public Map<String,Cookie> getCookies()
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
	 * @param callback the callback to set
	 */
	public void setIoCallback(IoCallback ioCallback)
	{
		this.ioCallback = ioCallback;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(ByteBuffer body)
	{
		this.body = body;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(int status)
	{
		this.status = status;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType)
	{
		this.contentType = contentType;
	}

	public void send( final HttpHandler handler, final HttpServerExchange exchange )
	{
		if( this.hasHeaders )
		{
			long itr = this.headers.fastIterateNonEmpty();
			
			while( itr != -1L )
			{
				final HeaderValues values = this.headers.fiCurrent(itr);
				
				exchange.getResponseHeaders().putAll(values.getHeaderName(), values);
				
				itr = this.headers.fiNextNonEmpty(itr); 
			}
		}
		
		if( this.hasCookies )
		{
			exchange.getResponseCookies().putAll(this.cookies);
		}
		
		exchange.setStatusCode( this.status );
		
		if( this.contentType != null )
		{
			exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, this.contentType);
		}
	 
		
		
		if( this.body != null)
		{
			if( !this.hasIoCallback )
			{
				exchange.getResponseSender().send(this.body); 
			}
			else
			{ 
				exchange.getResponseSender().send(this.body,this.ioCallback);

			}
		}
		else if( this.entity != null)
		{
	        if(exchange.isInIoThread()) {
	            exchange.dispatch(handler);
	            return;
	          }
	        
			
	        exchange.startBlocking();

	        final int bufferSize = exchange.getConnection().getBufferSize();
			
			final JsonStream stream = new JsonStream(exchange.getOutputStream(), bufferSize);
			
			try
			{
				stream.writeVal(this.entity);
				stream.close();
				
			} catch (IOException e)
			{
				  
				log.error(e.getMessage(),e); 
			     exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR); 
				 exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
			     exchange.getResponseSender().send(e.getMessage()); 
			}
			 
			
			exchange.endExchange();
		}
		else
		{
			exchange.endExchange();
		}
		
	}

	/**
	 * Creates builder to build {@link ServerResponse}.
	 * @return created builder
	 */ 
	public static Builder response()
	{
		return new Builder();
	}

	/**
	 * Builder to build {@link ServerResponse}.
	 */ 
	public static final class Builder
	{
		private final ServerResponse response;

		private Builder()
		{
			this.response = new ServerResponse();
		}

		public Builder body(ByteBuffer body)
		{
			this.response.body = body;
			return this;
		}
		
		public Builder entity(Object entity)
		{
			this.response.entity = entity;
			applicationJson();
			return this;
		}
		
		public Builder body(String body)
		{
			this.response.body = ByteBuffer.wrap(body.getBytes());
			return this;
		}
		
	
		public Builder status(int status)
		{
			this.response.status = status; 
			return this;
		}

		public Builder header(HttpString headerName, String value)
		{
			this.response.headers.put(headerName, value);
			this.response.hasHeaders = true;
			return this;
		}

		public Builder cookie(String cookieName, Cookie cookie)
		{
			this.response.cookies.put(cookieName, cookie);
			this.response.hasCookies = true;

			return this;
		}

		public Builder contentType(String contentType)
		{
			this.response.contentType = contentType;
			return this;
		}

		public Builder ioCallback(IoCallback ioCallback)
		{
			this.response.ioCallback = ioCallback;
			return this;
		}
		
		public Builder applicationJson()
		{
			this.response.contentType = APPLICATION_JSON_CONTENT_TYPE;
			return this;
		}
		
		public Builder textHtml()
		{
			this.response.contentType = TEXT_HTML_CONTENT_TYPE;
			return this;
		}
		
		public Builder textXml()
		{
			this.response.contentType = TEXT_XML_CONTENT_TYPE;
			return this;
		}
		
		public Builder textPlain()
		{
			this.response.contentType = TEXT_PLAIN_CONTENT_TYPE;
			return this;
		}
		
		public Builder ok()
		{
			this.response.status = StatusCodes.OK;
			return this;
		}
		
		public Builder accepted()
		{
			this.response.status = StatusCodes.ACCEPTED;
			return this;
		}
		
		public Builder badRequest()
		{
			this.response.status = StatusCodes.BAD_REQUEST;
			return this;
		}
		
		public Builder internalServerError()
		{
			this.response.status = StatusCodes.INTERNAL_SERVER_ERROR;
			return this;
		}
		
		public Builder created()
		{
			this.response.status = StatusCodes.CREATED;
			return this;
		}
		
		public Builder notFound()
		{
			this.response.status = StatusCodes.NOT_FOUND;
			return this;
		}
		
		public Builder forbidden()
		{
			this.response.status = StatusCodes.FORBIDDEN;
			return this;
		}
		
		
		public Builder found()
		{
			this.response.status = StatusCodes.FOUND;
			return this;
		}
		
		public Builder noContent()
		{
			this.response.status = StatusCodes.NO_CONTENT;
			return this;
		}
		
		public Builder withIoCallback(IoCallback ioCallback)
		{
			this.response.ioCallback = ioCallback;
			this.response.hasIoCallback = ioCallback == null;
			return this;
		}
		
		public Builder exception(Throwable t)
		{
			if(this.response.status == StatusCodes.ACCEPTED)
			{
				badRequest();
			}
			return this.entity(Any.wrap(t));
		}

		public ServerResponse build()
		{
			return this.response;
		}
	}
	
	
	
	 
}
