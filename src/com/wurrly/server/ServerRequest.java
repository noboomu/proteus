/**
 * 
 */
package com.wurrly.server;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.channels.StreamSourceChannel;

import com.jsoniter.JsonIterator;

import io.undertow.UndertowMessages;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.AttachmentKey;
import io.undertow.util.FastConcurrentDirectDeque;
import io.undertow.util.Headers;
import io.undertow.util.MalformedMessageException;

import java.util.stream.Collectors;

public class ServerRequest
{
	private static Logger log = LoggerFactory.getLogger(ServerRequest.class.getCanonicalName());

    public static final AttachmentKey<ByteBuffer> JSON_DATA = AttachmentKey.create(ByteBuffer.class);
 
 
	private static final String OCTET_STREAM_TYPE = org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM.getMimeType();
	private static final String APPLICATION_JSON = org.apache.http.entity.ContentType.APPLICATION_JSON.getMimeType();
	private static final String CHARSET = "UTF-8";
	
	public final HttpServerExchange exchange;
	
	private final String path;
	private FormData form; 
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

		
		if (this.contentType != null )
		{
			if (this.contentType.contains(FormEncodedDataDefinition.APPLICATION_X_WWW_FORM_URLENCODED) )
			{
 				this.parseEncodedForm();
			}
			else if (this.contentType.contains(MultiPartParserDefinition.MULTIPART_FORM_DATA) || this.contentType.contains(OCTET_STREAM_TYPE))
			{
				this.parseMultipartForm();
			}
			else if (this.contentType.contains(APPLICATION_JSON)  && this.exchange.getRequestContentLength() != -1)
			{
				this.parseJson();
			}
		}
		


	}
	
	public void respond(ServerResponse response)
	{
		
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

 
	
	private void parseJson() throws IOException
	{ 
		
		if(this.exchange.getRequestContentLength() != -1)
		{
			this.exchange.startBlocking();

//			ByteBuffer buffer = ByteBuffer.allocate((int) this.exchange.getRequestContentLength());
//			this.exchange.getRequestChannel().read(buffer); 
//			JsonIterator iterator = JsonIterator.parse(buffer.array());
//            this.exchange.putAttachment(JSON_DATA, iterator);
			
			 try (PooledByteBuffer pooled = exchange.getConnection().getByteBufferPool().getArrayBackedPool().allocate()){
	                ByteBuffer buf = pooled.getBuffer();
	                 
                    final StreamSourceChannel channel = this.exchange.getRequestChannel(); 

	                while (true) {
	                	
	                    buf.clear();
	                    
 	                    int c = channel.read(buf); 
 	                     
 	                    
 
	                    if (c == -1) {
	                     
//	                    	JsonIterator iterator = JsonIterator.parse(buf.array());
 
	                    	int pos = buf.limit();
	                    	
	 	                    ByteBuffer buffer = ByteBuffer.allocate(pos);

	                    	System.arraycopy(buf.array(), 0, buffer.array(), 0, pos);
	                    	
	    	                exchange.putAttachment(JSON_DATA, buffer);
	    	                break;
	    	                
	                    } else if (c != 0) {
	                        buf.limit(c);
	                    }
	                }
 	            } catch (MalformedMessageException e) {
	                throw new IOException(e);
	            }
//			try(PooledByteBuffer resource = this.exchange.getConnection().getByteBufferPool().allocate())
//			{
//			 final ByteBuffer buffer = resource.getBuffer();
			 
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
		    
			 
		 

//		     } catch (IOException e) {
//		           throw e;
//		        }  
//			}
//			Logger.debug("iterator " + iterator);

 		}
		else
		{
			this.exchange.startBlocking();

            InputStream is = exchange.getInputStream();
            if (is != null) {
            	
            	try
				{
            		 if (is.available() != -1) {
            			 
            			 try(Scanner scanner = new Scanner(is, "UTF-8"))
            			 {
            				 String s = scanner.useDelimiter("\\A").next();
                             s = s.trim();
                 			// JsonIterator iterator = JsonIterator.parse(s); 
                 			// log.debug("iterator " + iterator);

                             this.exchange.putAttachment(JSON_DATA, ByteBuffer.wrap(s.getBytes()));
            			 } 
                         
                     }
            		 else
            		 {
             			//Logger.debug("inputstream is not available ");

            		 }
            		 
				 } catch (IOException e) {
					 log.error("IOException: ", e);
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
		final FormDataParser formDataParser = new MultiPartParserDefinition()
				.setTempFileLocation(new File(TMP_DIR).toPath())
				.setDefaultEncoding(CHARSET)
				.create(this.exchange);

		log.debug(this.exchange+"\nmime: " + this.contentType);
		
		log.debug("boundary: " +    Headers.extractQuotedValueFromHeader(this.contentType, "boundary"));
		
		
		if(formDataParser != null)
		{ 
			final FormData formData = formDataParser.parseBlocking();  
			
			log.debug("formData: " +    formData);

			this.exchange.putAttachment(FormDataParser.FORM_DATA, formData);    
			extractFormParameters(formData);
		} 
	}
	
	private void parseEncodedForm() throws IOException
	{
		
		this.exchange.startBlocking();
	  
		final FormData formData = new FormEncodedDataDefinition().setDefaultEncoding(this.exchange.getRequestCharset()).create(exchange).parseBlocking();
		  
		this.exchange.putAttachment(FormDataParser.FORM_DATA, formData); 
		  
		extractFormParameters(formData);
	}
	
	private void extractFormParameters(final FormData formData)
	{
		  if (formData != null) {
		        for (String key : formData) 
		        {
		          Deque<FormData.FormValue> formValues = formData.get(key);
		          Deque<String> values = formValues.stream().filter(fv -> !fv.isFile()).map(FormData.FormValue::getValue).collect(java.util.stream.Collectors.toCollection(FastConcurrentDirectDeque::new));
		          exchange.getQueryParameters().put(key, values);
		      }
		  }
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