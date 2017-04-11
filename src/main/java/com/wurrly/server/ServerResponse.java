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
	
	private static final String APPLICATION_JSON_CONTENT_TYPE = org.apache.http.entity.ContentType.APPLICATION_JSON.getMimeType();
	private static final String TEXT_PLAIN_CONTENT_TYPE = org.apache.http.entity.ContentType.TEXT_PLAIN.getMimeType();
	private static final String TEXT_XML_CONTENT_TYPE = org.apache.http.entity.ContentType.TEXT_XML.getMimeType();
	private static final String TEXT_HTML_CONTENT_TYPE = org.apache.http.entity.ContentType.TEXT_HTML.getMimeType();
 
	protected ByteBuffer body;
	
	protected int status = StatusCodes.OK;
	protected final HeaderMap headers = new HeaderMap();
	protected final Map<String,Cookie> cookies = new HashMap<>(); 
	protected String contentType = null;
	protected Object entity;
	protected IoCallback ioCallback;
	protected boolean hasCookies = false;
	protected boolean hasHeaders = false;
	protected boolean hasIoCallback = false;
  	
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
	
	public ServerResponse body(ByteBuffer body)
	{
		this.body = body;
		return this;
	}
	
	public ServerResponse entity(Object entity)
	{
		this.entity = entity;
		applicationJson();
		return this;
	}
	
	public ServerResponse body(String body)
	{
		this.body = ByteBuffer.wrap(body.getBytes());
		return this;
	}
	

	public ServerResponse status(int status)
	{
		this.status = status; 
		return this;
	}

	public ServerResponse header(HttpString headerName, String value)
	{
		this.headers.put(headerName, value);
		this.hasHeaders = true;
		return this;
	}

	public ServerResponse cookie(String cookieName, Cookie cookie)
	{
		this.cookies.put(cookieName, cookie);
		this.hasCookies = true;

		return this;
	}

	public ServerResponse contentType(String contentType)
	{
		this.contentType = contentType;
		return this;
	}
 
	
	public ServerResponse applicationJson()
	{
		this.contentType = APPLICATION_JSON_CONTENT_TYPE;
		return this;
	}
	
	public ServerResponse textHtml()
	{
		this.contentType = TEXT_HTML_CONTENT_TYPE;
		return this;
	}
	
	public ServerResponse textXml()
	{
		this.contentType = TEXT_XML_CONTENT_TYPE;
		return this;
	}
	
	public ServerResponse textPlain()
	{
		this.contentType = TEXT_PLAIN_CONTENT_TYPE;
		return this;
	}
	
	public ServerResponse ok()
	{
		this.status = StatusCodes.OK;
		return this;
	}
	
	public ServerResponse accepted()
	{
		this.status = StatusCodes.ACCEPTED;
		return this;
	}
	
	public ServerResponse badRequest()
	{
		this.status = StatusCodes.BAD_REQUEST;
		return this;
	}
	
	public ServerResponse internalServerError()
	{
		this.status = StatusCodes.INTERNAL_SERVER_ERROR;
		return this;
	}
	
	public ServerResponse created()
	{
		this.status = StatusCodes.CREATED;
		return this;
	}
	
	public ServerResponse notFound()
	{
		this.status = StatusCodes.NOT_FOUND;
		return this;
	}
	
	public ServerResponse forbidden()
	{
		this.status = StatusCodes.FORBIDDEN;
		return this;
	}
	
	
	public ServerResponse found()
	{
		this.status = StatusCodes.FOUND;
		return this;
	}
	
	public ServerResponse noContent()
	{
		this.status = StatusCodes.NO_CONTENT;
		return this;
	}
	
	public ServerResponse withIoCallback(IoCallback ioCallback)
	{
		this.ioCallback = ioCallback;
		this.hasIoCallback = ioCallback == null;
		return this;
	}
	
	public ServerResponse exception(Throwable t)
	{
		if(this.status == StatusCodes.ACCEPTED)
		{
			badRequest();
		}
		return this.entity(Any.wrap(t));
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
	public static ServerResponse response()
	{
		return new ServerResponse();
	}

	 
	
	
	 
}
