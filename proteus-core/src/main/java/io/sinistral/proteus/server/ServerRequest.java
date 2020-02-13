/**
 *
 */
package io.sinistral.proteus.server;

import io.sinistral.proteus.server.predicates.ServerPredicates;
import io.undertow.io.Receiver;
import io.undertow.io.Sender;
import io.undertow.security.api.SecurityContext;
import io.undertow.server.*;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.*;
import org.xnio.XnioIoThread;
import org.xnio.channels.StreamSinkChannel;
import org.xnio.channels.StreamSourceChannel;
import org.xnio.conduits.StreamSinkConduit;
import org.xnio.conduits.StreamSourceConduit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 *
 * @author jbauer
 *
 */
public class ServerRequest
{
    public static final AttachmentKey<ByteBuffer> BYTE_BUFFER_KEY = AttachmentKey.create(ByteBuffer.class);
    protected static final Receiver.ErrorCallback ERROR_CALLBACK = (exchange, e) -> {
        exchange.putAttachment(ExceptionHandler.THROWABLE, e);
        exchange.endExchange();
    };
    protected static final String CHARSET = "UTF-8";
    protected static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    public final HttpServerExchange exchange;
    protected final String path;
    protected final String contentType;
    protected final String method;
    protected final String accept;
    protected FormData form;

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

        if (this.contentType != null) {
            if (ServerPredicates.URL_ENCODED_FORM_PREDICATE.resolve(exchange)) {
                this.parseEncodedForm();
            } else if (ServerPredicates.MULTIPART_PREDICATE.resolve(exchange)) {
                this.parseMultipartForm();
            } else if (exchange.getRequestContentLength() > 0) {
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
        this.exchange.getRequestReceiver().receiveFullBytes((exchange, message) -> {
            ByteBuffer buffer = ByteBuffer.wrap(message);

            exchange.putAttachment(BYTE_BUFFER_KEY, buffer);
        }, ERROR_CALLBACK);
    }

    private void extractFormParameters(final FormData formData)
    {
        if (formData != null) {
            for (String key : formData) {
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
        if (this.form != null) {
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

        if (formDataParser != null) {
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
     * Abort current request and respond with redirect. Returns empty @ServerResponse for convenience.
     * @param location
     * @param includeParameters
     * @return serverResponse
     */
    public <T>  ServerResponse<T> redirect(String location, boolean includeParameters)
    {

        exchange.getResponseHeaders().put(Headers.LOCATION, RedirectBuilder.redirect(exchange, location, includeParameters));
        exchange.setStatusCode(302);
        exchange.endExchange();

        return new ServerResponse<>();
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

    public HttpString getProtocol() {return exchange.getProtocol();}

    public HttpServerExchange setProtocol(HttpString protocol) {return exchange.setProtocol(protocol);}

    public boolean isHttp09() {return exchange.isHttp09();}

    public boolean isHttp10() {return exchange.isHttp10();}

    public boolean isHttp11() {return exchange.isHttp11();}

    public HttpString getRequestMethod() {return exchange.getRequestMethod();}

    public HttpServerExchange setRequestMethod(HttpString requestMethod) {return exchange.setRequestMethod(requestMethod);}

    public String getRequestScheme() {return exchange.getRequestScheme();}

    public HttpServerExchange setRequestScheme(String requestScheme) {return exchange.setRequestScheme(requestScheme);}

    public String getRequestURI() {return exchange.getRequestURI();}

    public HttpServerExchange setRequestURI(String requestURI) {return exchange.setRequestURI(requestURI);}

    public HttpServerExchange setRequestURI(String requestURI, boolean containsHost) {return exchange.setRequestURI(requestURI, containsHost);}

    public boolean isHostIncludedInRequestURI() {return exchange.isHostIncludedInRequestURI();}

    public String getRequestPath() {return exchange.getRequestPath();}

    public HttpServerExchange setRequestPath(String requestPath) {return exchange.setRequestPath(requestPath);}

    public String getRelativePath() {return exchange.getRelativePath();}

    public HttpServerExchange setRelativePath(String relativePath) {return exchange.setRelativePath(relativePath);}

    public String getResolvedPath() {return exchange.getResolvedPath();}

    public HttpServerExchange setResolvedPath(String resolvedPath) {return exchange.setResolvedPath(resolvedPath);}

    public String getQueryString() {return exchange.getQueryString();}

    public HttpServerExchange setQueryString(String queryString) {return exchange.setQueryString(queryString);}

    public String getRequestURL() {return exchange.getRequestURL();}

    public String getRequestCharset() {return exchange.getRequestCharset();}

    public String getResponseCharset() {return exchange.getResponseCharset();}

    public String getHostName() {return exchange.getHostName();}

    public String getHostAndPort() {return exchange.getHostAndPort();}

    public int getHostPort() {return exchange.getHostPort();}

    public ServerConnection getConnection() {return exchange.getConnection();}

    public boolean isPersistent() {return exchange.isPersistent();}

    public boolean isInIoThread() {return exchange.isInIoThread();}

    public long getResponseBytesSent() {return exchange.getResponseBytesSent();}

    public HttpServerExchange setPersistent(boolean persistent) {return exchange.setPersistent(persistent);}

    public boolean isDispatched() {return exchange.isDispatched();}

    public HttpServerExchange unDispatch() {return exchange.unDispatch();}

    @Deprecated
    public HttpServerExchange dispatch() {return exchange.dispatch();}

    public HttpServerExchange dispatch(Runnable runnable) {return exchange.dispatch(runnable);}

    public HttpServerExchange dispatch(Executor executor, Runnable runnable) {return exchange.dispatch(executor, runnable);}

    public HttpServerExchange dispatch(HttpHandler handler) {return exchange.dispatch(handler);}

    public HttpServerExchange dispatch(Executor executor, HttpHandler handler) {return exchange.dispatch(executor, handler);}

    public HttpServerExchange setDispatchExecutor(Executor executor) {return exchange.setDispatchExecutor(executor);}

    public Executor getDispatchExecutor() {return exchange.getDispatchExecutor();}

    public HttpServerExchange upgradeChannel(HttpUpgradeListener listener) {return exchange.upgradeChannel(listener);}

    public HttpServerExchange upgradeChannel(String productName, HttpUpgradeListener listener) {return exchange.upgradeChannel(productName, listener);}

    public HttpServerExchange acceptConnectRequest(HttpUpgradeListener connectListener) {return exchange.acceptConnectRequest(connectListener);}

    public HttpServerExchange addExchangeCompleteListener(ExchangeCompletionListener listener) {return exchange.addExchangeCompleteListener(listener);}

    public HttpServerExchange addDefaultResponseListener(DefaultResponseListener listener) {return exchange.addDefaultResponseListener(listener);}

    public InetSocketAddress getSourceAddress() {return exchange.getSourceAddress();}

    public HttpServerExchange setSourceAddress(InetSocketAddress sourceAddress) {return exchange.setSourceAddress(sourceAddress);}

    public InetSocketAddress getDestinationAddress() {return exchange.getDestinationAddress();}

    public HttpServerExchange setDestinationAddress(InetSocketAddress destinationAddress) {return exchange.setDestinationAddress(destinationAddress);}

    public HeaderMap getRequestHeaders() {return exchange.getRequestHeaders();}

    public long getRequestContentLength() {return exchange.getRequestContentLength();}

    public HeaderMap getResponseHeaders() {return exchange.getResponseHeaders();}

    public long getResponseContentLength() {return exchange.getResponseContentLength();}

    public HttpServerExchange setResponseContentLength(long length) {return exchange.setResponseContentLength(length);}

    public HttpServerExchange addQueryParam(String name, String param) {return exchange.addQueryParam(name, param);}

    public HttpServerExchange addPathParam(String name, String param) {return exchange.addPathParam(name, param);}

    public Map<String, Cookie> getRequestCookies() {return exchange.getRequestCookies();}

    public HttpServerExchange setResponseCookie(Cookie cookie) {return exchange.setResponseCookie(cookie);}

    public Map<String, Cookie> getResponseCookies() {return exchange.getResponseCookies();}

    public boolean isResponseStarted() {return exchange.isResponseStarted();}

    public StreamSourceChannel getRequestChannel() {return exchange.getRequestChannel();}

    public boolean isRequestChannelAvailable() {return exchange.isRequestChannelAvailable();}

    public boolean isComplete() {return exchange.isComplete();}

    public boolean isRequestComplete() {return exchange.isRequestComplete();}

    public boolean isResponseComplete() {return exchange.isResponseComplete();}

    public StreamSinkChannel getResponseChannel() {return exchange.getResponseChannel();}

    public Sender getResponseSender() {return exchange.getResponseSender();}

    public Receiver getRequestReceiver() {return exchange.getRequestReceiver();}

    public boolean isResponseChannelAvailable() {return exchange.isResponseChannelAvailable();}

    @Deprecated
    public int getResponseCode() {return exchange.getResponseCode();}

    @Deprecated
    public HttpServerExchange setResponseCode(int statusCode) {return exchange.setResponseCode(statusCode);}

    public int getStatusCode() {return exchange.getStatusCode();}

    public HttpServerExchange setStatusCode(int statusCode) {return exchange.setStatusCode(statusCode);}

    public HttpServerExchange setReasonPhrase(String message) {return exchange.setReasonPhrase(message);}

    public String getReasonPhrase() {return exchange.getReasonPhrase();}

    public HttpServerExchange addRequestWrapper(ConduitWrapper<StreamSourceConduit> wrapper) {return exchange.addRequestWrapper(wrapper);}

    public HttpServerExchange addResponseWrapper(ConduitWrapper<StreamSinkConduit> wrapper) {return exchange.addResponseWrapper(wrapper);}

    public BlockingHttpExchange startBlocking() {return exchange.startBlocking();}

    public BlockingHttpExchange startBlocking(BlockingHttpExchange httpExchange) {return exchange.startBlocking(httpExchange);}

    public boolean isBlocking() {return exchange.isBlocking();}

    public InputStream getInputStream() {return exchange.getInputStream();}

    public OutputStream getOutputStream() {return exchange.getOutputStream();}

    public long getRequestStartTime() {return exchange.getRequestStartTime();}

    public HttpServerExchange endExchange() {return exchange.endExchange();}

    public XnioIoThread getIoThread() {return exchange.getIoThread();}

    public long getMaxEntitySize() {return exchange.getMaxEntitySize();}

    public HttpServerExchange setMaxEntitySize(long maxEntitySize) {return exchange.setMaxEntitySize(maxEntitySize);}

    public void setSecurityContext(SecurityContext securityContext) {exchange.setSecurityContext(securityContext);}

    public void addResponseCommitListener(ResponseCommitListener listener) {exchange.addResponseCommitListener(listener);}

    public <T> List<T> getAttachmentList(AttachmentKey<? extends List<T>> key) {return exchange.getAttachmentList(key);}

    public <T> T putAttachment(AttachmentKey<T> key, T value) {return exchange.putAttachment(key, value);}

    public <T> T removeAttachment(AttachmentKey<T> key) {return exchange.removeAttachment(key);}

    public <T> void addToAttachmentList(AttachmentKey<AttachmentList<T>> key, T value) {exchange.addToAttachmentList(key, value);}
}



