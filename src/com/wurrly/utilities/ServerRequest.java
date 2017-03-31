/**
 * 
 */
package com.wurrly.utilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.IoUtils;
import org.xnio.channels.StreamSourceChannel;

import com.jsoniter.JsonIterator;

import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.AttachmentKey;
import io.undertow.util.Headers;
import io.undertow.websockets.core.UTF8Output;

public class ServerRequest
{
	private static Logger Logger = LoggerFactory.getLogger(ServerRequest.class.getCanonicalName());

    public static final AttachmentKey<JsonIterator> REQUEST_JSON_BODY = AttachmentKey.create(JsonIterator.class);

	private static final String URL_ENCODED_FORM_TYPE = "x-www-form-urlencoded";
	private static final String FORM_DATA_TYPE = "form-data";
	private static final String OCTET_STREAM_TYPE = "octet-stream";
	private static final String JSON_TYPE = "application/json"; 
	private static final String CHARSET = "UTF-8";
	
	public final HttpServerExchange exchange;
	
	private final String path;
	private FormData form;
	private JsonIterator json;
	private final String contentType;
	private final String method;

	private static final String TMP_DIR = System.getProperty("java.io.tmpdir");
 	
	public ServerRequest()
	{
		this.method = null;
		this.path = null;
		this.exchange = null;
		this.contentType = null;

	}
	
	public ServerRequest(HttpServerExchange exchange) throws IOException
	{
		this.method = exchange.getRequestMethod().toString();
		this.path = URLDecoder.decode(exchange.getRequestPath(), CHARSET);
		this.exchange = exchange;
		this.contentType = exchange.getRequestHeaders().getFirst("Content-Type");

		//Logger.debug("content tyoe: " + contentType);
		if (this.contentType != null)
		{
			if (this.contentType.contains(URL_ENCODED_FORM_TYPE) || this.contentType.contains(OCTET_STREAM_TYPE))
			{
 				this.parseEncodedForm();
			}
			else if (this.contentType.contains(FORM_DATA_TYPE))
			{
				this.parseMultipartForm();
			}
			else if (this.contentType.contains(JSON_TYPE))
			{
				this.parseJson();
			}
		}
		
		this.exchange.getResponseHeaders().put(Headers.SERVER, "Bowser"); 


	}

	public Deque<FormData.FormValue> files(final String name)
	{

 		
		if (this.form != null)
		{
			return form.get(name);
		}

		return null;
	}

	/**
	 * @param key
	 * @return
	 * @see io.undertow.util.AbstractAttachable#getAttachment(io.undertow.util.AttachmentKey)
	 */
	public <T> T getAttachment(AttachmentKey<T> key)
	{
		return exchange.getAttachment(key);
	}

	/**
	 * @return
	 * @see io.undertow.server.HttpServerExchange#getDestinationAddress()
	 */
	public InetSocketAddress getDestinationAddress()
	{
		return exchange.getDestinationAddress();
	}
	
	// ChainedHandlerWrapper

	/**
	 * @return
	 * @see io.undertow.server.HttpServerExchange#getQueryParameters()
	 */
	public Map<String, Deque<String>> getQueryParameters()
	{
		return exchange.getQueryParameters();
	}

	/**
	 * @return
	 * @see io.undertow.server.HttpServerExchange#getPathParameters()
	 */
	public Map<String, Deque<String>> getPathParameters()
	{
		return exchange.getPathParameters();
	}

	private void parseEncodedForm() throws IOException
	{
		this.exchange.startBlocking();
		this.form = new FormEncodedDataDefinition().setDefaultEncoding(CHARSET).create(exchange).parseBlocking();
		
		Logger.debug("Form " + form);
	}
	
	private void parseJson() throws IOException
	{
	//	Logger.debug("parsing json");

		this.exchange.startBlocking();
		
		if(this.exchange.getRequestContentLength() != -1)
		{
 
//			try(PooledByteBuffer resource = this.exchange.getConnection().getByteBufferPool().allocate())
//			{
//			 final ByteBuffer buffer = resource.getBuffer();
ByteBuffer buffer = ByteBuffer.allocate((int) this.exchange.getRequestContentLength());
//			 final UTF8Output string = new UTF8Output();
//
//			 final StreamSourceChannel channel = this.exchange.getRequestChannel();
// 			 
//			 try {
//		            int r = 0;
//		            do {
//		                r = channel.read(buffer);
//		                if (r == 0) {
//		                    //channel.getReadSetter().set(this);
//		                    channel.resumeReads();
//		                } else if (r == -1) {
// 		                	JsonIterator iterator = JsonIterator.parse(string.extract());
//		                    this.exchange.putAttachment(REQUEST_JSON_BODY, iterator);
//		                    IoUtils.safeClose(channel);
//		                } else {
//		                    buffer.flip();
//		                    string.write(buffer);
//		                }
//		            } while (r > 0);
		    
			 
		 	this.exchange.getRequestChannel().read(buffer);
			
			JsonIterator iterator = JsonIterator.parse(buffer.array());
            this.exchange.putAttachment(REQUEST_JSON_BODY, iterator);

//		     } catch (IOException e) {
//		           throw e;
//		        }  
//			}
//			Logger.debug("iterator " + iterator);

 		}
		else
		{
            InputStream is = exchange.getInputStream();
            if (is != null) {
            	
            	try
				{
            		 if (is.available() != -1) {
            			 
            			 try(Scanner scanner = new Scanner(is, "UTF-8"))
            			 {
            				 String s = scanner.useDelimiter("\\A").next();
                             s = s.trim();
                 			 JsonIterator iterator = JsonIterator.parse(s); 
                 			 Logger.debug("iterator " + iterator);

                             this.exchange.putAttachment(REQUEST_JSON_BODY, iterator);
            			 } 
                         
                     }
            		 else
            		 {
             			//Logger.debug("inputstream is not available ");

            		 }
            		 
				 } catch (IOException e) {
					 Logger.error("IOException: ", e);
	             }
            	
            }
            else
            {
    			//Logger.debug("inputstream is null ");

            }
		}
		
	}

	private void parseMultipartForm() throws IOException
	{
		
		this.exchange.startBlocking();
		this.form = new MultiPartParserDefinition().setTempFileLocation(new File(TMP_DIR).toPath()).setDefaultEncoding(CHARSET).create(exchange).parseBlocking();
		
		Logger.debug("Form " + form);

	 
	}

	public String queryString()
	{
		return exchange.getQueryString();
	}

	public String method()
	{
		return this.method;
	}

	public String path()
	{
		return path;
	}

	public String rawPath()
	{
		return exchange.getRequestURI();
	}

	public void startAsync(final Executor executor, final Runnable runnable)
	{
		exchange.dispatch(executor, runnable);
	}
}