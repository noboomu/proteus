
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

import io.sinistral.proteus.server.predicates.ServerPredicates;

import io.undertow.io.Receiver;
import io.undertow.security.api.SecurityContext;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.AttachmentKey;
import io.undertow.util.FastConcurrentDirectDeque;
import io.undertow.util.Headers;

/**
 *
 * @author jbauer
 *
 */
public class ServerRequest
{
    protected static final Receiver.ErrorCallback ERROR_CALLBACK = new Receiver.ErrorCallback()
    {
        @Override
        public void error(HttpServerExchange exchange, IOException e)
        {
            exchange.putAttachment(DefaultResponseListener.EXCEPTION, e);
            exchange.endExchange();
        }
    };
    
    public static final AttachmentKey<ByteBuffer> BYTE_BUFFER_KEY = AttachmentKey.create(ByteBuffer.class);
    
    protected static final String CHARSET = "UTF-8";
    protected static final String TMP_DIR = System.getProperty("java.io.tmpdir");
    
    public final HttpServerExchange exchange;
    protected final String path;
    protected FormData form;
    protected final String contentType;
    protected final String method;
    protected final String accept;

    public ServerRequest()
    {
        this.method = null;
        this.path = null;
        this.exchange = null;
        this.contentType = null;
        this.accept = null;
    }

    public ServerRequest(HttpServerExchange exchange) throws IOException
    {
        this.method = exchange.getRequestMethod().toString();
        this.path = exchange.getRequestPath();
        this.exchange = exchange;
        this.contentType = exchange.getRequestHeaders().getFirst(Headers.CONTENT_TYPE);
        this.accept = exchange.getRequestHeaders().getFirst(Headers.ACCEPT);

        if (this.contentType != null)
        {
            if (ServerPredicates.URL_ENCODED_FORM_PREDICATE.resolve(exchange))
            {
                this.parseEncodedForm();
            }
            else if (ServerPredicates.MULTIPART_PREDICATE.resolve(exchange))
            {
                this.parseMultipartForm();
            }
            else if (exchange.getRequestContentLength() > 0)
            {
                this.extractBytes();
            }
        }
    }

    public String accept()
    {
        return this.accept;
    }

    public String contentType()
    {
        return this.contentType;
    }

    public HttpServerExchange exchange()
    {
        return exchange;
    }

    private void extractBytes() throws IOException
    {
        this.exchange.getRequestReceiver().receiveFullBytes(new Receiver.FullBytesCallback()
                                       {
                                           @Override
                                           public void handle(HttpServerExchange exchange, byte[] message)
                                           {
                                               ByteBuffer buffer = ByteBuffer.wrap(message);

                                               exchange.putAttachment(BYTE_BUFFER_KEY, buffer);
                                           }
                                       },ERROR_CALLBACK);
    }

    private void extractFormParameters(final FormData formData)
    {
        if (formData != null)
        {
            for (String key : formData)
            {
                final Deque<FormData.FormValue> formValues = formData.get(key);
                final Deque<String> values = formValues.stream()
                                                       .filter(fv -> !fv.isFileItem())
                                                       .map(FormData.FormValue::getValue)
                                                       .collect(java.util.stream.Collectors.toCollection(FastConcurrentDirectDeque::new));

                exchange.getQueryParameters().put(key, values);
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

    public String method()
    {
        return this.method;
    }

    private void parseEncodedForm() throws IOException
    {
        this.exchange.startBlocking();

        final FormData formData = new FormEncodedDataDefinition().setDefaultEncoding(this.exchange.getRequestCharset()).create(exchange).parseBlocking();

        this.exchange.putAttachment(FormDataParser.FORM_DATA, formData);
        
        extractFormParameters(formData);
    }

    private void parseMultipartForm() throws IOException
    {
        this.exchange.startBlocking();

        final FormDataParser formDataParser = new MultiPartParserDefinition().setTempFileLocation(new File(TMP_DIR).toPath()).setDefaultEncoding(CHARSET).create(this.exchange);

        if (formDataParser != null)
        {
            final FormData formData = formDataParser.parseBlocking();

            this.exchange.putAttachment(FormDataParser.FORM_DATA, formData);
            
            extractFormParameters(formData);
        }
    }

    public String path()
    {
        return path;
    }

    public String queryString()
    {
        return exchange.getQueryString();
    }

    public String rawPath()
    {
        return exchange.getRequestURI();
    }

    public void startAsync(final Executor executor, final Runnable runnable)
    {
        exchange.dispatch(executor, runnable);
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
     * @return the path parameters
     * @see io.undertow.server.HttpServerExchange#getPathParameters()
     */
    public Map<String, Deque<String>> getPathParameters()
    {
        return exchange.getPathParameters();
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
     * @return the security context
     * @see io.undertow.server.HttpServerExchange#getSecurityContext()
     */
    public SecurityContext getSecurityContext()
    {
        return exchange.getSecurityContext();
    }

	/**
	 * @return the exchange
	 */
	public HttpServerExchange getExchange()
	{
		return exchange;
	}

	/**
	 * @return the path
	 */
	public String getPath()
	{
		return path;
	}

	/**
	 * @return the contentType
	 */
	public String getContentType()
	{
		return contentType;
	}

	/**
	 * @return the method
	 */
	public String getMethod()
	{
		return method;
	}

	/**
	 * @return the accept
	 */
	public String getAccept()
	{
		return accept;
	}
}



