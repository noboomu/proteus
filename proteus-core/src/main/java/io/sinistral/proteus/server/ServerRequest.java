/**
 *
 */
package io.sinistral.proteus.server;

import io.sinistral.proteus.server.predicates.ServerPredicates;
import io.undertow.UndertowOptions;
import io.undertow.io.Receiver;
import io.undertow.io.Sender;
import io.undertow.security.api.SecurityContext;
import io.undertow.server.BlockingHttpExchange;
import io.undertow.server.ConduitWrapper;
import io.undertow.server.DefaultResponseListener;
import io.undertow.server.ExchangeCompletionListener;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.HttpUpgradeListener;
import io.undertow.server.ResponseCommitListener;
import io.undertow.server.ServerConnection;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.form.FormData;
import io.undertow.server.handlers.form.FormDataParser;
import io.undertow.server.handlers.form.FormEncodedDataDefinition;
import io.undertow.server.handlers.form.MultiPartParserDefinition;
import io.undertow.util.AttachmentKey;
import io.undertow.util.AttachmentList;
import io.undertow.util.FastConcurrentDirectDeque;
import io.undertow.util.HeaderMap;
import io.undertow.util.Headers;
import io.undertow.util.HttpString;
import io.undertow.util.RedirectBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnio.XnioIoThread;
import org.xnio.XnioWorker;
import org.xnio.channels.StreamSinkChannel;
import org.xnio.channels.StreamSourceChannel;
import org.xnio.conduits.StreamSinkConduit;
import org.xnio.conduits.StreamSourceConduit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static io.undertow.server.handlers.form.FormDataParser.FORM_DATA;

/**
 * Request abstraction attached to an exchange, exposing headers, parameters, and the buffered body.
 *
 * @author jbauer
 */
public class ServerRequest {

    /** Shared logger for request processing. */
    private static final Logger logger = LoggerFactory.getLogger(ServerRequest.class.getName());

    /** Attachment key for the raw request byte buffer. */
    public static final AttachmentKey<ByteBuffer> BYTE_BUFFER_KEY = AttachmentKey.create(ByteBuffer.class);

    /** Attachment key for the server request instance. */
    public static final AttachmentKey<ServerRequest> SERVER_REQUEST_ATTACHMENT_KEY = AttachmentKey.create(ServerRequest.class);

    /** the error_callback. */
    protected static final Receiver.ErrorCallback ERROR_CALLBACK = (exchange, e) -> {
        exchange.putAttachment(ExceptionHandler.THROWABLE, e);
        exchange.endExchange();
    };

    /** the tmp_dir. */
    protected static final String TMP_DIR = System.getProperty("java.io.tmpdir");

    /** the exchange. */
    public final HttpServerExchange exchange;

    /** the path. */
    protected final String path;

    /** the content type. */
    protected final String contentType;

    /** the method. */
    protected final String method;

    /** the accept. */
    protected final String accept;

    /** Creates an empty request with no exchange. */
    public ServerRequest()
    {

        this.method = null;
        this.path = null;
        this.exchange = null;
        this.contentType = null;
        this.accept = null;
    }

    /**
     * Creates a request bound to an exchange.
     *
     * @param exchange the underlying server exchange
     * @throws IOException when request metadata cannot be read
     */
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
            else if (ServerPredicates.MULTIPART_FORM_PREDICATE.resolve(exchange))
            {
                this.parseMultipartForm();
            }
            else if (exchange.getRequestContentLength() > 0)
            {
                this.exchange.getRequestReceiver().receiveFullBytes((ex, message) -> {
                    ByteBuffer buffer = ByteBuffer.wrap(message);
                    ex.putAttachment(BYTE_BUFFER_KEY, buffer);
                }, ERROR_CALLBACK);
            }
        }
    }

    /**
     * Returns the accept.
     *
     * @return the accept, or null when unset
     */
    public String accept()
    {

        return this.accept;
    }

    /**
     * Returns the content type.
     *
     * @return the content type, or null when unset
     */
    public String contentType()
    {

        return this.contentType;
    }

    /**
     * Returns the exchange.
     *
     * @return the exchange, or null when unset
     */
    public HttpServerExchange exchange()
    {

        return exchange;
    }

    /**
     * Sets the files, fluent style.
     *
     * @param name the files
     * @return this instance
     */
    public Deque<FormData.FormValue> files(final String name)
    {

        FormData formData = this.exchange.getAttachment(FORM_DATA);

        if (formData != null)
        {
            return formData.get(name);
        }

        return null;
    }

    /**
     * Returns the method.
     *
     * @return the method, or null when unset
     */
    public String method()
    {

        return this.method;
    }

    /**
     * Returns the path.
     *
     * @return the path, or null when unset
     */
    public String path()
    {

        return path;
    }

    /**
     * Returns the query string.
     *
     * @return the query string, or null when unset
     */
    public String queryString()
    {

        return exchange.getQueryString();
    }

    /**
     * Returns the raw path.
     *
     * @return the raw path, or null when unset
     */
    public String rawPath()
    {

        return exchange.getRequestURI();
    }

    /**
     * Sets the start async, fluent style.
     *
     * @param executor the start async
     * @param runnable the start async
     */
    public void startAsync(final Executor executor, final Runnable runnable)
    {

        exchange.dispatch(executor, runnable);
    }

    /**
     * Aborts the current request and responds with a 302 redirect.
     *
     * @param location the redirect target
     * @param includeParameters whether to carry query parameters over
     * @param <T> the response entity type
     * @return an empty server response for convenience
     */
    public <T> ServerResponse<T> redirect(String location, boolean includeParameters)
    {

        exchange.setRelativePath("/");
        exchange.setStatusCode(302);
        exchange.getResponseHeaders().put(Headers.LOCATION, RedirectBuilder.redirect(exchange, location, includeParameters));
        exchange.endExchange();

        return new ServerResponse<>();
    }

    /**
     * Aborts the current request and responds with a 301 redirect.
     *
     * @param location the redirect target
     * @param includeParameters whether to carry query parameters over
     * @param <T> the response entity type
     * @return an empty server response for convenience
     */
    public <T> ServerResponse<T> redirectPermanently(String location, boolean includeParameters)
    {

        exchange.setRelativePath("/");
        exchange.setStatusCode(301);
        exchange.getResponseHeaders().put(Headers.LOCATION, RedirectBuilder.redirect(exchange, location, includeParameters));
        exchange.endExchange();

        return new ServerResponse<>();
    }

    /**
     * Returns the attachment stored under the key.
     *
     * @param key the attachment key
     * @param <T> the attachment type
     * @return the attachment, or null when absent
     * @see io.undertow.util.AbstractAttachable#getAttachment(io.undertow.util.AttachmentKey)
     */
    public <T> T getAttachment(AttachmentKey<T> key)
    {

        return exchange.getAttachment(key);
    }

    /**
     * Returns the path parameters.
     *
     * @return the path parameters
     * @see io.undertow.server.HttpServerExchange#getPathParameters()
     */
    public Map<String, Deque<String>> getPathParameters()
    {

        return exchange.getPathParameters();
    }

    /**
     * Returns the query parameters.
     *
     * @return the query parameters
     * @see io.undertow.server.HttpServerExchange#getQueryParameters()
     */
    public Map<String, Deque<String>> getQueryParameters()
    {

        return exchange.getQueryParameters();
    }

    /**
     * Returns the exchange security context.
     *
     * @return the security context
     * @see io.undertow.server.HttpServerExchange#getSecurityContext()
     */
    public SecurityContext getSecurityContext()
    {

        return exchange.getSecurityContext();
    }

    /**
     * Returns the underlying server exchange.
     *
     * @return the exchange
     */
    public HttpServerExchange getExchange()
    {

        return exchange;
    }

    /**
     * Returns the worker bound to this connection, or null when closed.
     *
     * @return the worker
     */
    public XnioWorker getWorker()
    {

        return Optional.ofNullable(exchange.getConnection()).filter(ServerConnection::isOpen).map(ServerConnection::getWorker).orElse(null);
    }

    /**
     * Returns the request path.
     *
     * @return the path
     */
    public String getPath()
    {

        return path;
    }

    /**
     * Returns the request content type.
     *
     * @return the contentType
     */
    public String getContentType()
    {

        return contentType;
    }

    /**
     * Returns the request method name.
     *
     * @return the method
     */
    public String getMethod()
    {

        return method;
    }

    /**
     * Returns the accept.
     *
     * @return the accept
     */
    public String getAccept()
    {

        return accept;
    }

    /**
     * Returns the protocol.
     *
     * @return the protocol, or null when unset
     */
    public HttpString getProtocol() {

        return exchange.getProtocol();
    }

    /**
     * Sets the protocol.
     *
     * @param protocol the protocol
    * @return the result
     */
    public HttpServerExchange setProtocol(HttpString protocol) {

        return exchange.setProtocol(protocol);
    }

    /**
     * Returns the http09.
     *
     * @return the http09, or null when unset
     */
    public boolean isHttp09() {

        return exchange.isHttp09();
    }

    /**
     * Returns the http10.
     *
     * @return the http10, or null when unset
     */
    public boolean isHttp10() {

        return exchange.isHttp10();
    }

    /**
     * Returns the http11.
     *
     * @return the http11, or null when unset
     */
    public boolean isHttp11() {

        return exchange.isHttp11();
    }

    /**
     * Returns the request method.
     *
     * @return the request method, or null when unset
     */
    public HttpString getRequestMethod() {

        return exchange.getRequestMethod();
    }

    /**
     * Sets the request method.
     *
     * @param requestMethod the request method
    * @return the result
     */
    public HttpServerExchange setRequestMethod(HttpString requestMethod) {

        return exchange.setRequestMethod(requestMethod);
    }

    /**
     * Returns the request scheme.
     *
     * @return the request scheme, or null when unset
     */
    public String getRequestScheme() {

        return exchange.getRequestScheme();
    }

    /**
     * Sets the request scheme.
     *
     * @param requestScheme the request scheme
    * @return the result
     */
    public HttpServerExchange setRequestScheme(String requestScheme) {

        return exchange.setRequestScheme(requestScheme);
    }

    /**
     * Returns the request uri.
     *
     * @return the request uri, or null when unset
     */
    public String getRequestURI() {

        return exchange.getRequestURI();
    }

    /**
     * Sets the request uri.
     *
     * @param requestURI the request uri
    * @return the result
     */
    public HttpServerExchange setRequestURI(String requestURI) {

        return exchange.setRequestURI(requestURI);
    }

    /**
     * Sets the request uri.
     *
     * @param requestURI the request uri
     * @param containsHost the request uri
    * @return the result
     */
    public HttpServerExchange setRequestURI(String requestURI, boolean containsHost) {

        return exchange.setRequestURI(requestURI, containsHost);
    }

    /**
     * Returns the host included in request uri.
     *
     * @return the host included in request uri, or null when unset
     */
    public boolean isHostIncludedInRequestURI() {

        return exchange.isHostIncludedInRequestURI();
    }

    /**
     * Returns the request path.
     *
     * @return the request path, or null when unset
     */
    public String getRequestPath() {

        return exchange.getRequestPath();
    }

    /**
     * Sets the request path.
     *
     * @param requestPath the request path
    * @return the result
     */
    public HttpServerExchange setRequestPath(String requestPath) {

        return exchange.setRequestPath(requestPath);
    }

    /**
     * Returns the relative path.
     *
     * @return the relative path, or null when unset
     */
    public String getRelativePath() {

        return exchange.getRelativePath();
    }

    /**
     * Sets the relative path.
     *
     * @param relativePath the relative path
    * @return the result
     */
    public HttpServerExchange setRelativePath(String relativePath) {

        return exchange.setRelativePath(relativePath);
    }

    /**
     * Returns the resolved path.
     *
     * @return the resolved path, or null when unset
     */
    public String getResolvedPath() {

        return exchange.getResolvedPath();
    }

    /**
     * Sets the resolved path.
     *
     * @param resolvedPath the resolved path
    * @return the result
     */
    public HttpServerExchange setResolvedPath(String resolvedPath) {

        return exchange.setResolvedPath(resolvedPath);
    }

    /**
     * Returns the query string.
     *
     * @return the query string, or null when unset
     */
    public String getQueryString() {

        return exchange.getQueryString();
    }

    /**
     * Sets the query string.
     *
     * @param queryString the query string
    * @return the result
     */
    public HttpServerExchange setQueryString(String queryString) {

        return exchange.setQueryString(queryString);
    }

    /**
     * Returns the request url.
     *
     * @return the request url, or null when unset
     */
    public String getRequestURL() {

        return exchange.getRequestURL();
    }

    /**
     * Returns the request charset.
     *
     * @return the request charset, or null when unset
     */
    public String getRequestCharset() {

        return exchange.getRequestCharset();
    }

    /**
     * Returns the response charset.
     *
     * @return the response charset, or null when unset
     */
    public String getResponseCharset() {

        return exchange.getResponseCharset();
    }

    /**
     * Returns the host name.
     *
     * @return the host name, or null when unset
     */
    public String getHostName() {

        return exchange.getHostName();
    }

    /**
     * Returns the host and port.
     *
     * @return the host and port, or null when unset
     */
    public String getHostAndPort() {

        return exchange.getHostAndPort();
    }

    /**
     * Returns the host port.
     *
     * @return the host port, or null when unset
     */
    public int getHostPort() {

        return exchange.getHostPort();
    }

    /**
     * Returns the connection.
     *
     * @return the connection, or null when unset
     */
    public ServerConnection getConnection() {

        return exchange.getConnection();
    }

    /**
     * Returns the persistent.
     *
     * @return the persistent, or null when unset
     */
    public boolean isPersistent() {

        return exchange.isPersistent();
    }

    /**
     * Returns the in io thread.
     *
     * @return the in io thread, or null when unset
     */
    public boolean isInIoThread() {

        return exchange.isInIoThread();
    }

    /**
     * Returns the response bytes sent.
     *
     * @return the response bytes sent, or null when unset
     */
    public long getResponseBytesSent() {

        return exchange.getResponseBytesSent();
    }

    /**
     * Sets the persistent.
     *
     * @param persistent the persistent
    * @return the result
     */
    public HttpServerExchange setPersistent(boolean persistent) {

        return exchange.setPersistent(persistent);
    }

    /**
     * Returns the dispatched.
     *
     * @return the dispatched, or null when unset
     */
    public boolean isDispatched() {

        return exchange.isDispatched();
    }

    /**
     * Returns the un dispatch.
     *
     * @return the un dispatch, or null when unset
     */
    public HttpServerExchange unDispatch() {

        return exchange.unDispatch();
    }

    /**
     * Returns the dispatch.
     *
     * @return the dispatch, or null when unset
     */
    @Deprecated
    public HttpServerExchange dispatch() {

        return exchange.dispatch();
    }

    /**
     * Sets the dispatch, fluent style.
     *
     * @param runnable the dispatch
     * @return this instance
     */
    public HttpServerExchange dispatch(Runnable runnable) {

        return exchange.dispatch(runnable);
    }

    /**
     * Sets the dispatch, fluent style.
     *
     * @param executor the dispatch
     * @param runnable the dispatch
     * @return this instance
     */
    public HttpServerExchange dispatch(Executor executor, Runnable runnable) {

        return exchange.dispatch(executor, runnable);
    }

    /**
     * Sets the dispatch, fluent style.
     *
     * @param handler the dispatch
     * @return this instance
     */
    public HttpServerExchange dispatch(HttpHandler handler) {

        return exchange.dispatch(handler);
    }

    /**
     * Sets the dispatch, fluent style.
     *
     * @param executor the dispatch
     * @param handler the dispatch
     * @return this instance
     */
    public HttpServerExchange dispatch(Executor executor, HttpHandler handler) {

        return exchange.dispatch(executor, handler);
    }

    /**
     * Sets the dispatch executor.
     *
     * @param executor the dispatch executor
    * @return the result
     */
    public HttpServerExchange setDispatchExecutor(Executor executor) {

        return exchange.setDispatchExecutor(executor);
    }

    /**
     * Returns the dispatch executor.
     *
     * @return the dispatch executor, or null when unset
     */
    public Executor getDispatchExecutor() {

        return exchange.getDispatchExecutor();
    }

    /**
     * Sets the upgrade channel, fluent style.
     *
     * @param listener the upgrade channel
     * @return this instance
     */
    public HttpServerExchange upgradeChannel(HttpUpgradeListener listener) {

        return exchange.upgradeChannel(listener);
    }

    /**
     * Sets the upgrade channel, fluent style.
     *
     * @param productName the upgrade channel
     * @param listener the upgrade channel
     * @return this instance
     */
    public HttpServerExchange upgradeChannel(String productName, HttpUpgradeListener listener) {

        return exchange.upgradeChannel(productName, listener);
    }

    /**
     * Sets the accept connect request, fluent style.
     *
     * @param connectListener the accept connect request
     * @return this instance
     */
    public HttpServerExchange acceptConnectRequest(HttpUpgradeListener connectListener) {

        return exchange.acceptConnectRequest(connectListener);
    }

    /**
     * Adds an entry to the exchange complete listener.
     *
     * @param listener the entry
    * @return the result
     */
    public HttpServerExchange addExchangeCompleteListener(ExchangeCompletionListener listener) {

        return exchange.addExchangeCompleteListener(listener);
    }

    /**
     * Adds an entry to the default response listener.
     *
     * @param listener the entry
    * @return the result
     */
    public HttpServerExchange addDefaultResponseListener(DefaultResponseListener listener) {

        return exchange.addDefaultResponseListener(listener);
    }

    /**
     * Returns the source address.
     *
     * @return the source address, or null when unset
     */
    public InetSocketAddress getSourceAddress() {

        return exchange.getSourceAddress();
    }

    /**
     * Sets the source address.
     *
     * @param sourceAddress the source address
    * @return the result
     */
    public HttpServerExchange setSourceAddress(InetSocketAddress sourceAddress) {

        return exchange.setSourceAddress(sourceAddress);
    }

    /**
     * Returns the destination address.
     *
     * @return the destination address, or null when unset
     */
    public InetSocketAddress getDestinationAddress() {

        return exchange.getDestinationAddress();
    }

    /**
     * Sets the destination address.
     *
     * @param destinationAddress the destination address
    * @return the result
     */
    public HttpServerExchange setDestinationAddress(InetSocketAddress destinationAddress) {

        return exchange.setDestinationAddress(destinationAddress);
    }

    /**
     * Returns the request headers.
     *
     * @return the request headers, or null when unset
     */
    public HeaderMap getRequestHeaders() {

        return exchange.getRequestHeaders();
    }

    /**
     * Returns the request content length.
     *
     * @return the request content length, or null when unset
     */
    public long getRequestContentLength() {

        return exchange.getRequestContentLength();
    }

    /**
     * Returns the response headers.
     *
     * @return the response headers, or null when unset
     */
    public HeaderMap getResponseHeaders() {

        return exchange.getResponseHeaders();
    }

    /**
     * Returns the response content length.
     *
     * @return the response content length, or null when unset
     */
    public long getResponseContentLength() {

        return exchange.getResponseContentLength();
    }

    /**
     * Sets the response content length.
     *
     * @param length the response content length
    * @return the result
     */
    public HttpServerExchange setResponseContentLength(long length) {

        return exchange.setResponseContentLength(length);
    }

    /**
     * Adds an entry to the query param.
     *
     * @param name the entry
     * @param param the entry
    * @return the result
     */
    public HttpServerExchange addQueryParam(String name, String param) {

        return exchange.addQueryParam(name, param);
    }

    /**
     * Adds an entry to the path param.
     *
     * @param name the entry
     * @param param the entry
    * @return the result
     */
    public HttpServerExchange addPathParam(String name, String param) {

        return exchange.addPathParam(name, param);
    }

    /**
     * Returns the request cookies.
     *
     * @return the request cookies, or null when unset
     */
    public Iterable<Cookie> getRequestCookies() {

        return exchange.requestCookies();
    }

    /**
     * Sets the response cookie.
     *
     * @param cookie the response cookie
    * @return the result
     */
    public HttpServerExchange setResponseCookie(Cookie cookie) {

        return exchange.setResponseCookie(cookie);
    }

    /**
     * Returns the response cookies.
     *
     * @return the response cookies, or null when unset
     */
    public Iterable<Cookie> getResponseCookies() {

        return exchange.responseCookies();
    }

    /**
     * Returns the response started.
     *
     * @return the response started, or null when unset
     */
    public boolean isResponseStarted() {

        return exchange.isResponseStarted();
    }

    /**
     * Returns the request channel.
     *
     * @return the request channel, or null when unset
     */
    public StreamSourceChannel getRequestChannel() {

        return exchange.getRequestChannel();
    }

    /**
     * Returns the request channel available.
     *
     * @return the request channel available, or null when unset
     */
    public boolean isRequestChannelAvailable() {

        return exchange.isRequestChannelAvailable();
    }

    /**
     * Returns the complete.
     *
     * @return the complete, or null when unset
     */
    public boolean isComplete() {

        return exchange.isComplete();
    }

    /**
     * Returns the request complete.
     *
     * @return the request complete, or null when unset
     */
    public boolean isRequestComplete() {

        return exchange.isRequestComplete();
    }

    /**
     * Returns the response complete.
     *
     * @return the response complete, or null when unset
     */
    public boolean isResponseComplete() {

        return exchange.isResponseComplete();
    }

    /**
     * Returns the response channel.
     *
     * @return the response channel, or null when unset
     */
    public StreamSinkChannel getResponseChannel() {

        return exchange.getResponseChannel();
    }

    /**
     * Returns the response sender.
     *
     * @return the response sender, or null when unset
     */
    public Sender getResponseSender() {

        return exchange.getResponseSender();
    }

    /**
     * Returns the request receiver.
     *
     * @return the request receiver, or null when unset
     */
    public Receiver getRequestReceiver() {

        return exchange.getRequestReceiver();
    }

    /**
     * Returns the response channel available.
     *
     * @return the response channel available, or null when unset
     */
    public boolean isResponseChannelAvailable() {

        return exchange.isResponseChannelAvailable();
    }

    /**
     * Returns the response code.
     *
     * @return the response code, or null when unset
     */
    @Deprecated
    public int getResponseCode() {

        return exchange.getResponseCode();
    }

    /**
     * Sets the response code.
     *
     * @param statusCode the response code
    * @return the result
     */
    @Deprecated
    public HttpServerExchange setResponseCode(int statusCode) {

        return exchange.setResponseCode(statusCode);
    }

    /**
     * Returns the status code.
     *
     * @return the status code, or null when unset
     */
    public int getStatusCode() {

        return exchange.getStatusCode();
    }

    /**
     * Sets the status code.
     *
     * @param statusCode the status code
    * @return the result
     */
    public HttpServerExchange setStatusCode(int statusCode) {

        return exchange.setStatusCode(statusCode);
    }

    /**
     * Sets the reason phrase.
     *
     * @param message the reason phrase
    * @return the result
     */
    public HttpServerExchange setReasonPhrase(String message) {

        return exchange.setReasonPhrase(message);
    }

    /**
     * Returns the reason phrase.
     *
     * @return the reason phrase, or null when unset
     */
    public String getReasonPhrase() {

        return exchange.getReasonPhrase();
    }

    /**
     * Adds an entry to the request wrapper.
     *
     * @param wrapper the entry
    * @return the result
     */
    public HttpServerExchange addRequestWrapper(ConduitWrapper<StreamSourceConduit> wrapper) {

        return exchange.addRequestWrapper(wrapper);
    }

    /**
     * Adds an entry to the response wrapper.
     *
     * @param wrapper the entry
    * @return the result
     */
    public HttpServerExchange addResponseWrapper(ConduitWrapper<StreamSinkConduit> wrapper) {

        return exchange.addResponseWrapper(wrapper);
    }

    /**
     * Returns the start blocking.
     *
     * @return the start blocking, or null when unset
     */
    public BlockingHttpExchange startBlocking() {

        return exchange.startBlocking();
    }

    /**
     * Sets the start blocking, fluent style.
     *
     * @param httpExchange the start blocking
     * @return this instance
     */
    public BlockingHttpExchange startBlocking(BlockingHttpExchange httpExchange) {

        return exchange.startBlocking(httpExchange);
    }

    /**
     * Returns the blocking.
     *
     * @return the blocking, or null when unset
     */
    public boolean isBlocking() {

        return exchange.isBlocking();
    }

    /**
     * Returns the input stream.
     *
     * @return the input stream, or null when unset
     */
    public InputStream getInputStream() {

        return exchange.getInputStream();
    }

    /**
     * Returns the output stream.
     *
     * @return the output stream, or null when unset
     */
    public OutputStream getOutputStream() {

        return exchange.getOutputStream();
    }

    /**
     * Returns the request start time.
     *
     * @return the request start time, or null when unset
     */
    public long getRequestStartTime() {

        return exchange.getRequestStartTime();
    }

    /**
     * Returns the end exchange.
     *
     * @return the end exchange, or null when unset
     */
    public HttpServerExchange endExchange() {

        return exchange.endExchange();
    }

    /**
     * Returns the io thread.
     *
     * @return the io thread, or null when unset
     */
    public XnioIoThread getIoThread() {

        return exchange.getIoThread();
    }

    /**
     * Returns the max entity size.
     *
     * @return the max entity size, or null when unset
     */
    public long getMaxEntitySize() {

        return exchange.getMaxEntitySize();
    }

    /**
     * Sets the max entity size.
     *
     * @param maxEntitySize the max entity size
    * @return the result
     */
    public HttpServerExchange setMaxEntitySize(long maxEntitySize) {

        return exchange.setMaxEntitySize(maxEntitySize);
    }

    /**
     * Sets the security context.
     *
     * @param securityContext the security context
     */
    public void setSecurityContext(SecurityContext securityContext) {

        exchange.setSecurityContext(securityContext);
    }

    /**
     * Adds an entry to the response commit listener.
     *
     * @param listener the entry
     */
    public void addResponseCommitListener(ResponseCommitListener listener) {

        exchange.addResponseCommitListener(listener);
    }

    /**
     * Returns the attachment list stored under the key.
     *
     * @param key the attachment list key
     * @param <T> the element type
     * @return the attachment list, or an empty list when unset
     */
    public <T> List<T> getAttachmentList(AttachmentKey<? extends List<T>> key) {

        return exchange.getAttachmentList(key);
    }

    /**
     * Stores an attachment under the key.
     *
     * @param key the attachment key
     * @param value the attachment value
     * @param <T> the attachment type
     * @return the previous attachment, or null
     */
    public <T> T putAttachment(AttachmentKey<T> key, T value) {

        return exchange.putAttachment(key, value);
    }

    /**
     * Removes the attachment stored under the key.
     *
     * @param key the attachment key
     * @param <T> the attachment type
     * @return the removed attachment, or null
     */
    public <T> T removeAttachment(AttachmentKey<T> key) {

        return exchange.removeAttachment(key);
    }

    /**
     * Adds a value to an attachment list key.
     *
     * @param key the attachment list key
     * @param value the value to append
     * @param <T> the attachment type
     */
    public <T> void addToAttachmentList(AttachmentKey<AttachmentList<T>> key, T value) {

        exchange.addToAttachmentList(key, value);
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
                                                       .collect(Collectors.toCollection(FastConcurrentDirectDeque::new));

                if (values.size() > 0)
                {
                    exchange.getQueryParameters().put(key, values);
                }

            }
        }
    }

    private void parseEncodedForm() throws IOException
    {

        try (BlockingHttpExchange blockingHttpExchange = this.exchange.startBlocking())
        {
            try (FormDataParser formDataParser = new FormEncodedDataDefinition().setDefaultEncoding(this.exchange.getRequestCharset()).create(exchange))
            {
                if (formDataParser != null)
                {
                    final FormData formData = formDataParser.parseBlocking();
                    extractFormParameters(formData);
                }
            }
        }
    }

    private void parseMultipartForm() throws IOException
    {

        final String charset = exchange.getRequestCharset();

       this.exchange.startBlocking();


        final MultiPartParserDefinition multiPartParserDefinition = new MultiPartParserDefinition()
                .setTempFileLocation(Path.of(TMP_DIR)).setDefaultEncoding(charset);

        final long thresholdSize = exchange.getConnection().getUndertowOptions().get(UndertowOptions.MAX_BUFFERED_REQUEST_SIZE, 0);

        multiPartParserDefinition.setFileSizeThreshold(thresholdSize);

        final FormDataParser formDataParser = multiPartParserDefinition.create(this.exchange);

        if(formDataParser != null)
        {
            final FormData formData = formDataParser.parseBlocking();
            extractFormParameters(formData);

        }



    }

}



