package io.sinistral.proteus.server;

import com.google.inject.Inject;
import io.sinistral.proteus.protocol.MediaType;
import io.sinistral.proteus.server.predicates.ServerPredicates;
import io.sinistral.proteus.wrappers.JsonViewWrapper;
import io.undertow.io.IoCallback;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.util.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.StandardOpenOption;
import java.io.File;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.dataformat.xml.XmlMapper;

/**
 * Base server response. Friendlier interface to underlying exchange.
 *
 * @param <T> the response body type
 * @author jbauer
 */

public class ServerResponse<T> {

    /** The log. */
    private static Logger log = LoggerFactory.getLogger(
        ServerResponse.class.getCanonicalName()
    );

    /** The rfc1123_pattern. */
    private static final String RFC1123_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

    private static final ThreadLocal<
        DateTimeFormatter
    > RFC1123_PATTERN_FORMATTER = ThreadLocal.withInitial(() ->
        DateTimeFormatter.ofPattern(RFC1123_PATTERN)
    );

    /** The xml_mapper. */
    @Inject
    protected static XmlMapper XML_MAPPER;

    /** The object_mapper. */
    @Inject
    protected static ObjectMapper OBJECT_MAPPER;

    /** The writer_cache. */
    protected static Map<Class<?>, ObjectWriter> WRITER_CACHE =
        new ConcurrentHashMap<>();

    /** The body. */
    protected ByteBuffer body;

    /** The status. */
    protected int status = StatusCodes.OK;
    /** The headers. */
    protected final HeaderMap headers = new HeaderMap();
    /** The cookies. */
    protected final List<Cookie> cookies = new ArrayList<>();
    /** The content type. */
    protected String contentType = MediaType.APPLICATION_JSON.contentType();
    /** The entity. */
    protected T entity;
    /** The throwable. */
    protected Throwable throwable;
    //	protected Class<? extends JsonContext> jsonContext;
    /** The request method this response answers, or null. */
    protected HttpString method = null;
    /** The io callback. */
    protected IoCallback ioCallback;
    /** The has cookies. */
    protected boolean hasCookies = false;
    /** The has headers. */
    protected boolean hasHeaders = false;
    /** The has io callback. */
    protected boolean hasIoCallback = false;
    /** The process xml. */
    protected boolean processXml = false;
    /** The process json. */
    protected boolean processJson = false;
    /** The preprocessed. */
    protected boolean preprocessed = false;
    /** The location. */
    protected String location = null;

    /** Creates an empty response. */
    public ServerResponse() {}

    /**
     * Returns the body.
     *
     * @return the body, or null when unset
     */
    public ByteBuffer getBody() {
        return body;
    }

    /**
     * Returns the status.
     *
     * @return the status, or null when unset
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * Returns the cookies.
     *
     * @return the cookies, or null when unset
     */
    public List<Cookie> getCookies() {
        return this.cookies;
    }

    /**
     * Returns the headers.
     *
     * @return the headers, or null when unset
     */
    public HeaderMap getHeaders() {
        return this.headers;
    }

    /**
     * Adds an entry to the header.
     *
     * @param headerName the entry
     * @param headerValue the entry
    * @return the result
     */
    public ServerResponse<T> addHeader(
        HttpString headerName,
        String headerValue
    ) {
        this.headers.add(headerName, headerValue);
        this.hasHeaders = true;

        return this;
    }

    /**
     * Adds an entry to the header.
     *
     * @param headerString the entry
     * @param headerValue the entry
    * @return the result
     */
    public ServerResponse<T> addHeader(
        String headerString,
        String headerValue
    ) {
        HttpString headerName = HttpString.tryFromString(headerString);

        this.headers.add(headerName, headerValue);
        this.hasHeaders = true;

        return this;
    }

    /**
     * Sets the header.
     *
     * @param headerName the header
     * @param headerValue the header
    * @return the result
     */
    public ServerResponse<T> setHeader(
        HttpString headerName,
        String headerValue
    ) {
        this.headers.put(headerName, headerValue);
        this.hasHeaders = true;

        return this;
    }

    /**
     * Sets the header.
     *
     * @param headerString the header
     * @param headerValue the header
    * @return the result
     */
    public ServerResponse<T> setHeader(
        String headerString,
        String headerValue
    ) {
        HttpString headerName = HttpString.tryFromString(headerString);

        this.headers.put(headerName, headerValue);
        this.hasHeaders = true;

        return this;
    }

    /**
     * Returns the contentType.
     *
     * @return the contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Returns the ioCallback.
     *
     * @return the callback
     */
    public IoCallback getIoCallback() {
        return ioCallback;
    }

    /**
     * Sets the ioCallback.
     *
     * @param ioCallback the ioCallback to set
     */
    public void setIoCallback(IoCallback ioCallback) {
        this.ioCallback = ioCallback;
    }

    /**
     * Sets the body.
     *
     * @param body the body to set
     */
    public void setBody(ByteBuffer body) {
        this.body = body;
    }

    /**
     * Sets the status.
     *
     * @param status
     *            the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Sets the body and returns this instance.
     *
     * @param body the body
     * @return this instance
     */
    public ServerResponse<T> body(ByteBuffer body) {
        this.body = body;
        this.preprocessed = true;
        return this;
    }

    /**
     * Sets the body and returns this instance.
     *
     * @param body the body
     * @return this instance
     */
    public ServerResponse<T> body(byte[] body) {
        this.body = ByteBuffer.wrap(body);
        this.preprocessed = true;
        return this;
    }

    /**
     * Sets the body and returns this instance.
     *
     * @param body the body
     * @return this instance
     */
    public ServerResponse<T> body(String body) {
        return this.body(ByteBuffer.wrap(body.getBytes()));
    }

    /**
     * Sets the entity and returns this instance.
     *
     * @param entity the entity
     * @return this instance
     */
    public ServerResponse<T> entity(T entity) {
        this.entity = entity;
        this.preprocessed = false;

        return this;
    }

    /**
     * Sets the method and returns this instance.
     *
     * @param method the method
     * @return this instance
     */
    public ServerResponse<T> method(HttpString method) {
        this.method = method;
        return this;
    }

    /**
     * Sets the method and returns this instance.
     *
     * @param method the method
     * @return this instance
     */
    public ServerResponse<T> method(String method) {
        this.method = Methods.fromString(method);
        return this;
    }

    /**
     * Sets the Last-Modified header from a date.
     *
     * @param date the last modified date
     * @return this instance
     */
    public ServerResponse<T> lastModified(Date date) {
        this.headers.put(Headers.LAST_MODIFIED, date.getTime());
        this.hasHeaders = true;
        return this;
    }

    /**
     * Sets the Last-Modified header from an instant.
     *
     * @param instant the instant to set
     * @return this instance
     */
    public ServerResponse<T> lastModified(Instant instant) {
        this.headers.put(
            Headers.LAST_MODIFIED,
            RFC1123_PATTERN_FORMATTER.get().format(
                ZonedDateTime.ofInstant(instant, ZoneId.of("GMT"))
            )
        );
        this.hasHeaders = true;
        return this;
    }

    /**
     * Sets the content language and returns this instance.
     *
     * @param locale the content language
     * @return this instance
     */
    public ServerResponse<T> contentLanguage(Locale locale) {
        this.headers.put(Headers.CONTENT_LANGUAGE, locale.toLanguageTag());
        this.hasHeaders = true;
        return this;
    }

    /**
     * Sets the content language and returns this instance.
     *
     * @param language the content language
     * @return this instance
     */
    public ServerResponse<T> contentLanguage(String language) {
        this.headers.put(Headers.CONTENT_LANGUAGE, language);
        this.hasHeaders = true;
        return this;
    }

    /**
     * Sets the throwable and returns this instance.
     *
     * @param throwable the throwable
     * @return this instance
     */
    public ServerResponse<T> throwable(Throwable throwable) {
        this.throwable = throwable;

        if (this.status == StatusCodes.ACCEPTED) {
            return badRequest(throwable);
        }

        return this;
    }

    /**
     * Sets the status and returns this instance.
     *
     * @param status the status
     * @return this instance
     */
    public ServerResponse<T> status(int status) {
        this.status = status;
        return this;
    }

    /**
     * Sets the header and returns this instance.
     *
     * @param headerName the header
     * @param value the header
     * @return this instance
     */
    public ServerResponse<T> header(HttpString headerName, String value) {
        this.headers.put(headerName, value);
        this.hasHeaders = true;
        return this;
    }

    /**
     * Sets the cookie and returns this instance.
     *
     * @param cookie the cookie
     * @return this instance
     */
    public ServerResponse<T> cookie(Cookie cookie) {
        this.cookies.add(cookie);
        this.hasCookies = true;
        return this;
    }

    /**
     * Sets the contentType.
     *
     * @param contentType
     *            the contentType to set
     */
    protected void setContentType(String contentType) {
        this.contentType = contentType;

        if (
            this.contentType.contains(MediaType.APPLICATION_JSON.contentType())
        ) {
            if (!this.preprocessed) {
                this.processJson = true;
            }
        } else if (
            this.contentType.contains(MediaType.APPLICATION_XML.contentType())
        ) {
            if (!this.preprocessed) {
                this.processXml = true;
            }
        }
    }

    /**
     * Sets the content type and returns this instance.
     *
     * @param contentType the content type
     * @return this instance
     */
    public ServerResponse<T> contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    /**
     * Sets the content type and returns this instance.
     *
     * @param mediaType the content type
     * @return this instance
     */
    public ServerResponse<T> contentType(MediaType mediaType) {
        this.setContentType(mediaType.contentType());
        return this;
    }

    /**
     * Returns the application json.
     *
     * @return the application json, or null when unset
     */
    public ServerResponse<T> applicationJson() {
        if (!this.preprocessed) {
            this.processJson = true;
        }
        this.contentType = MediaType.APPLICATION_JSON.contentType();
        return this;
    }

    /**
     * Returns the text html.
     *
     * @return the text html, or null when unset
     */
    public ServerResponse<T> textHtml() {
        this.contentType = MediaType.TEXT_HTML_UTF8.contentType();
        return this;
    }

    /**
     * Returns the application octet stream.
     *
     * @return the application octet stream, or null when unset
     */
    public ServerResponse<T> applicationOctetStream() {
        this.contentType = MediaType.APPLICATION_OCTET_STREAM.contentType();
        return this;
    }

    /**
     * Returns the application xml.
     *
     * @return the application xml, or null when unset
     */
    public ServerResponse<T> applicationXml() {
        if (!this.preprocessed) {
            this.processXml = true;
        }
        this.contentType = MediaType.APPLICATION_XML.contentType();
        return this;
    }

    /**
     * Returns the text plain.
     *
     * @return the text plain, or null when unset
     */
    public ServerResponse<T> textPlain() {
        this.contentType = MediaType.TEXT_PLAIN_UTF8.contentType();
        return this;
    }

    //	public ServerResponse<T> jsonContext(Class<? extends JsonContext> context)
    //	{
    //		this.jsonContext = context;
    //		return this;
    //	}

    /** Marks the response as 200 OK.
     *
     * @return this instance */
    public ServerResponse<T> ok() {
        this.status = StatusCodes.OK;
        return this;
    }

    /**
     * Sets the redirect and returns this instance.
     *
     * @param location the redirect
     * @return this instance
     */
    public ServerResponse<T> redirect(String location) {
        this.location = location;
        this.status = StatusCodes.FOUND;
        return this;
    }

    /**
     * Sets the redirect and returns this instance.
     *
     * @param location the redirect
     * @param status the redirect
     * @return this instance
     */
    public ServerResponse<T> redirect(String location, int status) {
        this.location = location;
        this.status = status;
        return this;
    }

    /**
     * Sets the redirect permanently and returns this instance.
     *
     * @param location the redirect permanently
     * @return this instance
     */
    public ServerResponse<T> redirectPermanently(String location) {
        this.location = location;
        this.status = StatusCodes.MOVED_PERMANENTLY;
        return this;
    }

    /**
     * Returns the found.
     *
     * @return the found, or null when unset
     */
    public ServerResponse<T> found() {
        this.status = StatusCodes.FOUND;
        return this;
    }

    /**
     * Returns the accepted.
     *
     * @return the accepted, or null when unset
     */
    public ServerResponse<T> accepted() {
        this.status = StatusCodes.ACCEPTED;
        return this;
    }

    /**
     * Returns the bad request.
     *
     * @return the bad request, or null when unset
     */
    public ServerResponse<T> badRequest() {
        this.status = StatusCodes.BAD_REQUEST;
        return this;
    }

    /**
     * Sets the bad request and returns this instance.
     *
     * @param t the bad request
     * @return this instance
     */
    public ServerResponse<T> badRequest(Throwable t) {
        this.throwable = t;
        return this.badRequest();
    }

    /**
     * Sets the bad request and returns this instance.
     *
     * @param message the bad request
     * @return this instance
     */
    public ServerResponse<T> badRequest(String message) {
        return this.errorMessage(message).badRequest();
    }

    /**
     * Returns the internal server error.
     *
     * @return the internal server error, or null when unset
     */
    public ServerResponse<T> internalServerError() {
        this.status = StatusCodes.INTERNAL_SERVER_ERROR;
        return this;
    }

    /**
     * Sets the internal server error and returns this instance.
     *
     * @param t the internal server error
     * @return this instance
     */
    public ServerResponse<T> internalServerError(Throwable t) {
        this.throwable = t;
        return this.internalServerError();
    }

    /**
     * Sets the internal server error and returns this instance.
     *
     * @param message the internal server error
     * @return this instance
     */
    public ServerResponse<T> internalServerError(String message) {
        return this.errorMessage(message).internalServerError();
    }

    /**
     * Returns the created.
     *
     * @return the created, or null when unset
     */
    public ServerResponse<T> created() {
        this.status = StatusCodes.CREATED;
        return this;
    }

    /**
     * Sets the created and returns this instance.
     *
     * @param location the created
     * @return this instance
     */
    public ServerResponse<T> created(String location) {
        this.status = StatusCodes.CREATED;
        this.location = location;
        return this;
    }

    /**
     * Sets the created and returns this instance.
     *
     * @param uri the created
     * @return this instance
     */
    public ServerResponse<T> created(URI uri) {
        this.status = StatusCodes.CREATED;
        this.location = uri.toString();
        return this;
    }

    /**
     * Returns the not modified.
     *
     * @return the not modified, or null when unset
     */
    public ServerResponse<T> notModified() {
        this.status = StatusCodes.NOT_MODIFIED;
        return this;
    }

    /**
     * Returns the not found.
     *
     * @return the not found, or null when unset
     */
    public ServerResponse<T> notFound() {
        this.status = StatusCodes.NOT_FOUND;
        return this;
    }

    /**
     * Sets the not found and returns this instance.
     *
     * @param t the not found
     * @return this instance
     */
    public ServerResponse<T> notFound(Throwable t) {
        this.throwable = t;
        return this.notFound();
    }

    /**
     * Sets the not found and returns this instance.
     *
     * @param message the not found
     * @return this instance
     */
    public ServerResponse<T> notFound(String message) {
        return this.errorMessage(message).notFound();
    }

    /**
     * Returns the forbidden.
     *
     * @return the forbidden, or null when unset
     */
    public ServerResponse<T> forbidden() {
        this.status = StatusCodes.FORBIDDEN;
        return this;
    }

    /**
     * Sets the forbidden and returns this instance.
     *
     * @param t the forbidden
     * @return this instance
     */
    public ServerResponse<T> forbidden(Throwable t) {
        this.throwable = t;
        return this.forbidden();
    }

    /**
     * Sets the forbidden and returns this instance.
     *
     * @param message the forbidden
     * @return this instance
     */
    public ServerResponse<T> forbidden(String message) {
        return this.errorMessage(message).forbidden();
    }

    /**
     * Returns the no content.
     *
     * @return the no content, or null when unset
     */
    public ServerResponse<T> noContent() {
        this.status = StatusCodes.NO_CONTENT;
        return this;
    }

    /**
     * Sets the no content and returns this instance.
     *
     * @param t the no content
     * @return this instance
     */
    public ServerResponse<T> noContent(Throwable t) {
        this.throwable = t;
        return this.noContent();
    }

    /**
     * Sets the no content and returns this instance.
     *
     * @param message the no content
     * @return this instance
     */
    public ServerResponse<T> noContent(String message) {
        return this.errorMessage(message).noContent();
    }

    /**
     * Returns the service unavailable.
     *
     * @return the service unavailable, or null when unset
     */
    public ServerResponse<T> serviceUnavailable() {
        this.status = StatusCodes.SERVICE_UNAVAILABLE;
        return this;
    }

    /**
     * Sets the service unavailable and returns this instance.
     *
     * @param t the service unavailable
     * @return this instance
     */
    public ServerResponse<T> serviceUnavailable(Throwable t) {
        this.throwable = t;
        return this.serviceUnavailable();
    }

    /**
     * Sets the service unavailable and returns this instance.
     *
     * @param message the service unavailable
     * @return this instance
     */
    public ServerResponse<T> serviceUnavailable(String message) {
        return this.errorMessage(message).serviceUnavailable();
    }

    /**
     * Returns the unauthorized.
     *
     * @return the unauthorized, or null when unset
     */
    public ServerResponse<T> unauthorized() {
        this.status = StatusCodes.UNAUTHORIZED;
        return this;
    }

    /**
     * Sets the unauthorized and returns this instance.
     *
     * @param t the unauthorized
     * @return this instance
     */
    public ServerResponse<T> unauthorized(Throwable t) {
        this.throwable = t;
        return this.unauthorized();
    }

    /**
     * Sets the unauthorized and returns this instance.
     *
     * @param message the unauthorized
     * @return this instance
     */
    public ServerResponse<T> unauthorized(String message) {
        return this.errorMessage(message).unauthorized();
    }

    /**
     * Sets the error message and returns this instance.
     *
     * @param message the error message
     * @return this instance
     */
    public ServerResponse<T> errorMessage(String message) {
        this.throwable = new Throwable(message);
        return this;
    }

    /**
     * Returns a value with the io callback applied.
     *
     * @param ioCallback the io callback
     * @return this instance
     */
    public ServerResponse<T> withIoCallback(IoCallback ioCallback) {
        this.ioCallback = ioCallback;
        this.hasIoCallback = ioCallback != null;
        return this;
    }

    /**
     * Sets the send and returns this instance.
     *
     * @param exchange the send
    * @throws RuntimeException when the operation fails
     */
    public void send(final HttpServerExchange exchange)
        throws RuntimeException {
        send(null, exchange);
    }

    /**
     * Sets the send and returns this instance.
     *
     * @param handler the send
     * @param exchange the send
    * @throws RuntimeException when the operation fails
     */
    public void send(
        final HttpHandler handler,
        final HttpServerExchange exchange
    ) throws RuntimeException {
        final boolean hasBody = this.body != null;
        final boolean hasEntity = this.entity != null;
        final boolean hasError = this.throwable != null;

        if (exchange.isResponseStarted()) {
            return;
        }

        exchange.setStatusCode(this.status);

        if (hasError) {
            if (this.status == StatusCodes.OK) {
                exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
            }
            exchange.putAttachment(ExceptionHandler.THROWABLE, throwable);
            exchange.endExchange();
            return;
        }

        //        if(location != null && (status == 301 || status == 302))
        //        {
        //            exchange.setRelativePath("/");
        //            exchange.setStatusCode(status);
        //            exchange.getResponseHeaders().put(Headers.LOCATION, RedirectBuilder.redirect(exchange, location, true));
        //            exchange.endExchange();
        //        }

        if (this.location != null) {
            exchange.getResponseHeaders().put(Headers.LOCATION, this.location);
        }

        if (
            this.status == StatusCodes.FOUND ||
            this.status == StatusCodes.MOVED_PERMANENTLY ||
            this.status == StatusCodes.TEMPORARY_REDIRECT ||
            this.status == StatusCodes.SEE_OTHER ||
            this.status == StatusCodes.PERMANENT_REDIRECT
        ) {
            if (
                (this.status == StatusCodes.FOUND ||
                    this.status == StatusCodes.MOVED_PERMANENTLY) &&
                (this.method != null)
            ) {
                exchange.setRequestMethod(this.method);
            }

            exchange.endExchange();
            return;
        }

        if (this.contentType != null) {
            exchange
                .getResponseHeaders()
                .put(Headers.CONTENT_TYPE, this.contentType);
        }

        if (this.hasHeaders) {
            long itr = this.headers.fastIterateNonEmpty();

            while (itr != -1L) {
                final HeaderValues values = this.headers.fiCurrent(itr);

                exchange
                    .getResponseHeaders()
                    .putAll(values.getHeaderName(), values);

                itr = this.headers.fiNextNonEmpty(itr);
            }
        }

        if (this.hasCookies) {
            for (Cookie cookie : this.cookies) {
                exchange.setResponseCookie(cookie);
            }
        }

        if (!this.processJson && !this.processXml) {
            if (ServerPredicates.ACCEPT_JSON_PREDICATE.resolve(exchange)) {
                this.applicationJson();
                exchange
                    .getResponseHeaders()
                    .put(Headers.CONTENT_TYPE, this.contentType);
            } else if (
                ServerPredicates.ACCEPT_XML_PREDICATE.resolve(exchange)
            ) {
                this.applicationXml();
                exchange
                    .getResponseHeaders()
                    .put(Headers.CONTENT_TYPE, this.contentType);
            } else if (
                ServerPredicates.ACCEPT_TEXT_PREDICATE.resolve(exchange)
            ) {
                this.textPlain();
                exchange
                    .getResponseHeaders()
                    .put(Headers.CONTENT_TYPE, this.contentType);
            }
        }

        if (hasBody) {
            if (!this.hasIoCallback) {
                exchange.getResponseSender().send(this.body);
            } else {
                exchange.getResponseSender().send(this.body, this.ioCallback);
            }
        } else if (hasEntity) {
            try {
                if (this.entity instanceof ByteBuffer) {
                    if (!this.hasIoCallback) {
                        exchange.getResponseSender().send((ByteBuffer) this.entity);
                    } else {
                        exchange.getResponseSender().send((ByteBuffer) this.entity, this.ioCallback);
                    }
                } else if (this.entity instanceof File) {
                    File file = (File) this.entity;
                    exchange.getResponseHeaders().put(Headers.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"");
                    exchange.getResponseHeaders().put(Headers.CONTENT_LENGTH, file.length());
                    exchange.getResponseSender().transferFrom(FileChannel.open(file.toPath(), StandardOpenOption.READ), this.ioCallback != null ? this.ioCallback : IoCallback.END_EXCHANGE);
                } else if (this.processXml) {
                    exchange
                        .getResponseSender()
                        .send(
                            ByteBuffer.wrap(
                                XML_MAPPER.writeValueAsBytes(this.entity)
                            )
                        );
                } else {
                    final Class<?> jsonViewClass = exchange.getAttachment(
                        JsonViewWrapper.JSON_VIEW_KEY
                    );

                    if (jsonViewClass != null) {
                        ObjectWriter writer = WRITER_CACHE.computeIfAbsent(
                            jsonViewClass,
                            view -> OBJECT_MAPPER.writerWithView(view)
                        );
                        exchange
                            .getResponseSender()
                            .send(
                                ByteBuffer.wrap(
                                    writer.writeValueAsBytes(this.entity)
                                )
                            );
                    } else {
                        exchange
                            .getResponseSender()
                            .send(
                                ByteBuffer.wrap(
                                    OBJECT_MAPPER.writeValueAsBytes(this.entity)
                                )
                            );
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage() + " for entity " + this.entity, e);

                throw new IllegalArgumentException(e);
            }
        } else {
            if (handler != null) {
                try {
                    handler.handleRequest(exchange);
                } catch (Exception e) {
                    log.error("Error handling request", e);
                    exchange.endExchange();
                }
            } else {
                exchange.endExchange();
            }
        }
    }

    /**
     * Creates an empty response for the given type.
     *
     * @param clazz the entity class
     * @param <T> the entity type
     * @return created response
     */
    public static <T> ServerResponse<T> response(Class<T> clazz) {
        return new ServerResponse<T>();
    }

    /**
     * Creates a response wrapping a byte buffer body.
     *
     * @param body the body buffer
     * @return the response
     */
    public static ServerResponse<ByteBuffer> response(ByteBuffer body) {
        return new ServerResponse<ByteBuffer>().body(body);
    }

    /**
     * Creates a response wrapping a string body.
     *
     * @param body the body string
     * @return the response
     */
    public static ServerResponse<ByteBuffer> response(String body) {
        return new ServerResponse<ByteBuffer>().body(body);
    }

    /**
     * Creates a response wrapping an entity.
     *
     * @param entity the response entity
     * @param <T> the entity type
     * @return the response
     */
    public static <T> ServerResponse<T> response(T entity) {
        return new ServerResponse<T>().entity(entity);
    }

    /**
     * Returns the response.
     *
     * @return the response, or null when unset
     */
    @SuppressWarnings("rawtypes")
    public static ServerResponse response() {
        return new ServerResponse();
    }
}
