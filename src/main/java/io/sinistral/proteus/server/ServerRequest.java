/**
 * 
 */
package io.sinistral.proteus.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.Executor;

import org.xnio.Pool;
import org.xnio.Pooled;
import org.xnio.channels.StreamSourceChannel;

import io.sinistral.proteus.server.predicates.ServerPredicates;
import io.undertow.security.api.SecurityContext;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.AttachmentKey;
import io.undertow.util.FastConcurrentDirectDeque;
import io.undertow.util.Headers;
import io.undertow.util.MalformedMessageException;

/**
 * 
 * @author jbauer
 *
 */
public class ServerRequest
{    
    public static final AttachmentKey<ByteBuffer> BYTE_BUFFER_KEY = AttachmentKey.create(ByteBuffer.class);
 
	protected static final String CHARSET = "UTF-8";
	protected static final String TMP_DIR = System.getProperty("java.io.tmpdir");

	public final HttpServerExchange exchange;
	
	protected final String path;
	protected FormData form; 
	protected final String contentType;
	protected final String method;  
	

  	
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
		this.path = exchange.getRequestPath();
		this.exchange = exchange;
		this.contentType = exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE);
 
		if (this.contentType != null )
		{
			if ( ServerPredicates.URL_ENCODED_FORM_PREDICATE.resolve(exchange) )
			{
 				this.parseEncodedForm();
			}
			else if ( ServerPredicates.MULTIPART_PREDICATE.resolve(exchange) )
			{
				this.parseMultipartForm();
			}
			else if ( ServerPredicates.STRING_BODY_PREDICATE.resolve(exchange) )
			{ 
				this.extractBytes();
			}   
		} 
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
	 * @return the attachment
	 * @see io.undertow.util.AbstractAttachable#getAttachment(io.undertow.util.AttachmentKey)
	 */
	public <T> T getAttachment(AttachmentKey<T> key)
	{
		return exchange.getAttachment(key);
	}

	/**
	 * @return the inetSocketAddress
	 * @see io.undertow.server.HttpServerExchange#getDestinationAddress()
	 */
	public InetSocketAddress getDestinationAddress()
	{
		return exchange.getDestinationAddress();
	}
 
	/**
	 * @return the query parameters
	 * @see io.undertow.server.HttpServerExchange#getQueryParameters()
	 */
	public Map<String, Deque<String>> getQueryParameters()
	{
		return exchange.getQueryParameters();
	}

	/**
	 * @return the path parameters
	 * @see io.undertow.server.HttpServerExchange#getPathParameters()
	 */
	public Map<String, Deque<String>> getPathParameters()
	{
		return exchange.getPathParameters();
	}

	/**
	 * @return the security context
	 * @see io.undertow.server.security.api#getSecurityContext()
	 */
	public SecurityContext getSecurityContext()
	{
		return exchange.getSecurityContext();
	}
 
	
	private void extractBytes() throws IOException
	{  
		 
			this.exchange.startBlocking();
						 
			
			 try (Pooled<ByteBuffer> pooled = exchange.getConnection().getBufferPool().allocate()){
	                ByteBuffer buf = pooled.getResource();
	                 
                    final StreamSourceChannel channel = this.exchange.getRequestChannel(); 

	                while (true) {
	                	
	                    buf.clear();
	                    
 	                    int c = channel.read(buf); 
 	                      
	                    if (c == -1) {
	                      
	                    	int pos = buf.limit();
	                    	
	 	                    ByteBuffer buffer = ByteBuffer.allocate(pos);
	 	                    
	 	                   int nTransfer = Math.min(buffer.remaining(), buf.remaining());
		 	                  if (nTransfer > 0)
		 	                  {
		 	                	 buffer.put(buf.array(), 
		 	                	           buf.arrayOffset()+buf.position(), 
		 	                                  nTransfer);
		 	                	buf.position(buf.position()+nTransfer);
		 	                  }

	                    //	System.arraycopy(buf.array(), 0, buffer.array(), 0, pos);
	                    	
	    	                exchange.putAttachment(BYTE_BUFFER_KEY, buffer);
	    	                
	    	                break;
	    	                
	                    } else if (c != 0) {
	                        buf.limit(c);
	                    }
	                }
 	            } catch (MalformedMessageException e) {
	                throw new IOException(e);
	            } 
 		
	}

	private void parseMultipartForm() throws IOException
	{
		
		this.exchange.startBlocking();
		final FormDataParser formDataParser = new MultiPartParserDefinition()
				.setTempFileLocation(new File(TMP_DIR).toPath())
				.setDefaultEncoding(CHARSET)
				.create(this.exchange);

		 
		if(formDataParser != null)
		{ 
			final FormData formData = formDataParser.parseBlocking();   
			this.exchange.putAttachment(FormDataParser.FORM_DATA, formData);    
			extractFormParameters(formData);
		} 
	}
	
	private void parseEncodedForm() throws IOException
	{ 
		this.exchange.startBlocking();
	  
		final FormData formData = new FormEncodedDataDefinition()
				.setDefaultEncoding(this.exchange.getRequestCharset())
				.create(exchange).parseBlocking();
		  
		this.exchange.putAttachment(FormDataParser.FORM_DATA, formData); 
		  
		extractFormParameters(formData);
	}
	
	private void extractFormParameters(final FormData formData)
	{
		  if (formData != null) {
		        for (String key : formData) 
		        {
		          final Deque<FormData.FormValue> formValues = formData.get(key);
		          final Deque<String> values = formValues.stream()
		        		  .filter(fv -> !fv.isFile())
		        		  .map(FormData.FormValue::getValue)
		        		  .collect(java.util.stream.Collectors.toCollection(FastConcurrentDirectDeque::new));
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

	public HttpServerExchange exchange()
	{
		return exchange;
	}

	public void startAsync(final Executor executor, final Runnable runnable)
	{
		exchange.dispatch(executor, runnable);
	}
}