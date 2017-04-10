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

	private ByteBuffer body;
	
	private int status = StatusCodes.ACCEPTED;
	private final HeaderMap headers = new HeaderMap();
	private final Map<String,Cookie> cookies = new HashMap<>(); 
	private String contentType = org.apache.http.entity.ContentType.TEXT_PLAIN.getMimeType();
	private Object entity;
	private IoCallback callback;
  	
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
	public IoCallback getCallback()
	{
		return callback;
	}

	/**
	 * @param callback the callback to set
	 */
	public void setCallback(IoCallback callback)
	{
		this.callback = callback;
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
		long itr = this.headers.fastIterateNonEmpty();
		
		while( itr != -1L )
		{
			final HeaderValues values = this.headers.fiCurrent(itr);
			
			exchange.getResponseHeaders().putAll(values.getHeaderName(), values);
			
			itr = this.headers.fiNextNonEmpty(itr); 
		}
		
		exchange.getResponseCookies().putAll(this.cookies);
		
		exchange.setStatusCode( this.status );
		
		exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, this.contentType);
		
		if( this.body == null && this.entity == null )
		{ 
			exchange.endExchange();
		}
		else if( this.body != null)
		{
			if( this.callback != null )
			{
				exchange.getResponseSender().send(this.body,this.callback);
			}
			else
			{ 
				exchange.getResponseSender().send(this.body); 
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
		
	}

	/**
	 * Creates builder to build {@link ServerResponse}.
	 * @return created builder
	 */ 
	public static Builder builder()
	{
		return new Builder();
	}

	/**
	 * Builder to build {@link ServerResponse}.
	 */ 
	public static final class Builder
	{
		private ServerResponse response = new ServerResponse();

		private Builder()
		{
		}

		public Builder withBody(ByteBuffer body)
		{
			this.response.body = body;
			return this;
		}
		
		public Builder withEntity(Object entity)
		{
			this.response.entity = entity;
			jsonType();
			return this;
		}
		
		public Builder withBody(String body)
		{
			this.response.body = ByteBuffer.wrap(body.getBytes());
			return this;
		}
		
	
		public Builder withStatus(int status)
		{
			this.response.status = status; 
			return this;
		}

		public Builder withHeader(HttpString headerName, String value)
		{
			this.response.headers.put(headerName, value);
			return this;
		}

		public Builder withCookie(String cookieName, Cookie cookie)
		{
			this.response.getCookies().put(cookieName, cookie);
			return this;
		}

		public Builder withContentType(String contentType)
		{
			this.response.contentType = contentType;
			return this;
		}

		public Builder withCallback(IoCallback callback)
		{
			this.response.callback = callback;
			return this;
		}
		
		public Builder jsonType()
		{
			this.response.contentType = org.apache.http.entity.ContentType.APPLICATION_JSON.getMimeType();
			return this;
		}
		
		public Builder htmlType()
		{
			this.response.contentType = org.apache.http.entity.ContentType.TEXT_HTML.getMimeType();
			return this;
		}
		
		public Builder plainType()
		{
			this.response.contentType = org.apache.http.entity.ContentType.TEXT_PLAIN.getMimeType();
			return this;
		}
		
		public Builder ok()
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

		public ServerResponse build()
		{
			return this.response;
		}
	}
	
	
	
	 
}
