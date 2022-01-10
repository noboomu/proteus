
package io.sinistral.proteus.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.inject.Inject;
import io.sinistral.proteus.protocol.HttpHeaders;
import io.sinistral.proteus.protocol.MediaType;
import io.sinistral.proteus.server.predicates.ServerPredicates;
import io.sinistral.proteus.wrappers.JsonViewWrapper;
import io.undertow.io.IoCallback;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.Cookie;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.net.URI;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jbauer
 * Base server response. Friendlier interface to underlying exchange.
 * @TODO extend javax.ws.rs.core.Response
 */

public class ServerResponse<T>
{
    private static Logger log = LoggerFactory.getLogger(ServerResponse.class.getCanonicalName());

    private final static String RFC1123_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";

    private static final ThreadLocal<DateTimeFormatter> RFC1123_PATTERN_FORMATTER =ThreadLocal.withInitial( () -> DateTimeFormatter.ofPattern(RFC1123_PATTERN));

    @Inject
    protected static XmlMapper XML_MAPPER;

    @Inject
    protected static ObjectMapper OBJECT_MAPPER;

    protected static Map<Class<?>, ObjectWriter> WRITER_CACHE = new ConcurrentHashMap<>();

    protected ByteBuffer body;

    protected int status = StatusCodes.OK;
    protected final HeaderMap headers = new HeaderMap();
    protected final Map<String, Cookie> cookies = new HashMap<>();
    protected String contentType = javax.ws.rs.core.MediaType.APPLICATION_JSON;
    protected T entity;
    protected Throwable throwable;
    //	protected Class<? extends JsonContext> jsonContext;
    protected HttpString method = null;
    protected IoCallback ioCallback;
    protected boolean hasCookies = false;
    protected boolean hasHeaders = false;
    protected boolean hasIoCallback = false;
    protected boolean processXml = false;
    protected boolean processJson = false;
    protected boolean preprocessed = false;
    protected String location = null;

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

    public Map<String, Cookie> getCookies()
    {
        return this.cookies;
    }

    public HeaderMap getHeaders()
    {
        return this.headers;
    }

    public ServerResponse<T> addHeader(HttpString headerName, String headerValue)
    {
        this.headers.add(headerName, headerValue);
        this.hasHeaders = true;

        return this;
    }

    public ServerResponse<T> addHeader(String headerString, String headerValue)
    {
        HttpString headerName = HttpString.tryFromString(headerString);

        this.headers.add(headerName, headerValue);
        this.hasHeaders = true;

        return this;
    }

    public ServerResponse<T> setHeader(HttpString headerName, String headerValue)
    {
        this.headers.put(headerName, headerValue);
        this.hasHeaders = true;

        return this;
    }

    public ServerResponse<T> setHeader(String headerString, String headerValue)
    {
        HttpString headerName = HttpString.tryFromString(headerString);

        this.headers.put(headerName, headerValue);
        this.hasHeaders = true;

        return this;
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
     * @param ioCallback
     *            the ioCallback to set
     */
    public void setIoCallback(IoCallback ioCallback)
    {
        this.ioCallback = ioCallback;
    }

    /**
     * @param body
     *            the body to set
     */
    public void setBody(ByteBuffer body)
    {
        this.body = body;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
    }

    /**
     * @param status
     *            the status to set
     */
    public void setStatus(Response.Status status)
    {
        this.status = status.getStatusCode();
    }

    public ServerResponse<T> body(ByteBuffer body)
    {
        this.body = body;
        this.preprocessed = true;
        return this;
    }

    public ServerResponse<T> body(byte[] body)
    {
        this.body = ByteBuffer.wrap(body);
        this.preprocessed = true;
        return this;
    }

    public ServerResponse<T> body(String body)
    {
        return this.body(ByteBuffer.wrap(body.getBytes()));
    }

    public ServerResponse<T> entity(T entity)
    {
        this.entity = entity;
        this.preprocessed = false;

        return this;
    }

    public ServerResponse<T> method(HttpString method)
    {
        this.method = method;
        return this;
    }

    public ServerResponse<T> method(String method)
    {
        this.method = Methods.fromString(method);
        return this;
    }

    public ServerResponse<T> lastModified(Date date)
    {
        this.headers.put(Headers.LAST_MODIFIED, date.getTime());
        this.hasHeaders = true;
        return this;
    }

    /**
     * @param instant
     *            the instant to set
     */
    public ServerResponse<T> lastModified(Instant instant)
    {
       this.headers.put(Headers.LAST_MODIFIED,RFC1123_PATTERN_FORMATTER.get().format(ZonedDateTime.ofInstant(instant, ZoneId.of("GMT"))));
       this.hasHeaders = true;
       return this;
    }

    public ServerResponse<T> contentLanguage(Locale locale)
    {
        this.headers.put(Headers.CONTENT_LANGUAGE, locale.toLanguageTag());
        this.hasHeaders = true;
        return this;
    }

    public ServerResponse<T> contentLanguage(String language)
    {
        this.headers.put(Headers.CONTENT_LANGUAGE, language);
        this.hasHeaders = true;
        return this;
    }

    public ServerResponse<T> throwable(Throwable throwable)
    {
        this.throwable = throwable;

        if (this.status == StatusCodes.ACCEPTED) {
            return badRequest(throwable);
        }

        return this;
    }

    public ServerResponse<T> status(Response.Status status)
    {
        this.status = status.getStatusCode();
        return this;
    }

    public ServerResponse<T> status(int status)
    {
        this.status = status;
        return this;
    }

    public ServerResponse<T> header(HttpString headerName, String value)
    {
        this.headers.put(headerName, value);
        this.hasHeaders = true;
        return this;
    }

    public ServerResponse<T> cookie(String cookieName, Cookie cookie)
    {
        this.cookies.put(cookieName, cookie);
        this.hasCookies = true;
        return this;
    }


    /**
     * @param contentType
     *            the contentType to set
     */
    protected void setContentType(String contentType)
    {
        this.contentType = contentType;

        if (this.contentType.contains(javax.ws.rs.core.MediaType.APPLICATION_JSON)) {
            if (!this.preprocessed) {
                this.processJson = true;
            }
        } else if (this.contentType.contains(javax.ws.rs.core.MediaType.APPLICATION_XML)) {
            if (!this.preprocessed) {
                this.processXml = true;
            }
        }
    }

    public ServerResponse<T> contentType(String contentType)
    {
        this.setContentType(contentType);
        return this;
    }


    public ServerResponse<T> contentType(javax.ws.rs.core.MediaType mediaType)
    {
        this.setContentType(mediaType.toString());
        return this;
    }

    public ServerResponse<T> contentType(MediaType mediaType)
    {
        this.setContentType(mediaType.contentType());
        return this;
    }

    public ServerResponse<T> applicationJson()
    {
        if (!this.preprocessed) {
            this.processJson = true;
        }
        this.contentType = javax.ws.rs.core.MediaType.APPLICATION_JSON;
        return this;
    }

    public ServerResponse<T> textHtml()
    {
        this.contentType = javax.ws.rs.core.MediaType.TEXT_HTML;
        return this;
    }

    public ServerResponse<T> applicationOctetStream()
    {
        this.contentType = javax.ws.rs.core.MediaType.APPLICATION_OCTET_STREAM;
        return this;
    }

    public ServerResponse<T> applicationXml()
    {
        if (!this.preprocessed) {
            this.processXml = true;
        }
        this.contentType = javax.ws.rs.core.MediaType.APPLICATION_XML;
        return this;
    }

    public ServerResponse<T> textPlain()
    {
        this.contentType = javax.ws.rs.core.MediaType.TEXT_PLAIN;
        return this;
    }

//	public ServerResponse<T> jsonContext(Class<? extends JsonContext> context)
//	{
//		this.jsonContext = context;
//		return this;
//	}

    public ServerResponse<T> ok()
    {
        this.status = StatusCodes.OK;
        return this;
    }

    public ServerResponse<T> redirect(String location)
    {
        this.location = location;
        this.status = StatusCodes.FOUND;
        return this;
    }

    public ServerResponse<T> redirect(String location, int status)
    {
        this.location = location;
        this.status = status;
        return this;
    }

    public ServerResponse<T> redirectPermanently(String location)
    {
        this.location = location;
        this.status = StatusCodes.MOVED_PERMANENTLY;
        return this;
    }

    public ServerResponse<T> found()
    {
        this.status = StatusCodes.FOUND;
        return this;
    }


    public ServerResponse<T> accepted()
    {
        this.status = StatusCodes.ACCEPTED;
        return this;
    }

    public ServerResponse<T> badRequest()
    {
        this.status = StatusCodes.BAD_REQUEST;
        return this;
    }

    public ServerResponse<T> badRequest(Throwable t)
    {
        this.throwable = t;
        return this.badRequest();
    }

    public ServerResponse<T> badRequest(String message)
    {
        return this.errorMessage(message).badRequest();
    }

    public ServerResponse<T> internalServerError()
    {
        this.status = StatusCodes.INTERNAL_SERVER_ERROR;
        return this;
    }

    public ServerResponse<T> internalServerError(Throwable t)
    {
        this.throwable = t;
        return this.internalServerError();
    }

    public ServerResponse<T> internalServerError(String message)
    {
        return this.errorMessage(message).internalServerError();
    }

    public ServerResponse<T> created()
    {
        this.status = StatusCodes.CREATED;
        return this;
    }

    public ServerResponse<T> created(String location)
    {
        this.status = StatusCodes.CREATED;
        this.location = location;
        return this;
    }

    public ServerResponse<T> created(URI uri)
    {
        this.status = StatusCodes.CREATED;
        this.location = uri.toString();
        return this;
    }

    public ServerResponse<T> notModified()
    {
        this.status = StatusCodes.NOT_MODIFIED;
        return this;
    }


    public ServerResponse<T> notFound()
    {
        this.status = StatusCodes.NOT_FOUND;
        return this;
    }

    public ServerResponse<T> notFound(Throwable t)
    {
        this.throwable = t;
        return this.notFound();
    }

    public ServerResponse<T> notFound(String message)
    {
        return this.errorMessage(message).notFound();
    }


    public ServerResponse<T> forbidden()
    {
        this.status = StatusCodes.FORBIDDEN;
        return this;
    }

    public ServerResponse<T> forbidden(Throwable t)
    {
        this.throwable = t;
        return this.forbidden();
    }

    public ServerResponse<T> forbidden(String message)
    {
        return this.errorMessage(message).forbidden();
    }

    public ServerResponse<T> noContent()
    {
        this.status = StatusCodes.NO_CONTENT;
        return this;
    }

    public ServerResponse<T> noContent(Throwable t)
    {
        this.throwable = t;
        return this.noContent();
    }


    public ServerResponse<T> noContent(String message)
    {
        return this.errorMessage(message).noContent();
    }

    public ServerResponse<T> serviceUnavailable()
    {
        this.status = StatusCodes.SERVICE_UNAVAILABLE;
        return this;
    }

    public ServerResponse<T> serviceUnavailable(Throwable t)
    {
        this.throwable = t;
        return this.serviceUnavailable();
    }


    public ServerResponse<T> serviceUnavailable(String message)
    {
        return this.errorMessage(message).serviceUnavailable();
    }

    public ServerResponse<T> unauthorized()
    {
        this.status = StatusCodes.UNAUTHORIZED;
        return this;
    }

    public ServerResponse<T> unauthorized(Throwable t)
    {
        this.throwable = t;
        return this.unauthorized();
    }


    public ServerResponse<T> unauthorized(String message)
    {
        return this.errorMessage(message).unauthorized();
    }

    public ServerResponse<T> errorMessage(String message)
    {
        this.throwable = new Throwable(message);
        return this;
    }

    public ServerResponse<T> withIoCallback(IoCallback ioCallback)
    {
        this.ioCallback = ioCallback;
        this.hasIoCallback = ioCallback == null;
        return this;
    }


    public void send(final HttpServerExchange exchange) throws RuntimeException
    {
        send(null, exchange);
    }

    public void send(final HttpHandler handler, final HttpServerExchange exchange) throws RuntimeException
    {

        final boolean hasBody = this.body != null;
        final boolean hasEntity = this.entity != null;
        final boolean hasError = this.throwable != null;

        if(exchange.isResponseStarted())
        {
            return;
        }

        exchange.setStatusCode(this.status);


        if (hasError) {
            if(this.status == StatusCodes.OK)
            {
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

        if (this.status == StatusCodes.FOUND || this.status == StatusCodes.MOVED_PERMANENTLY || this.status == StatusCodes.TEMPORARY_REDIRECT || this.status == StatusCodes.SEE_OTHER || this.status == StatusCodes.PERMANENT_REDIRECT) {
            if ((this.status == StatusCodes.FOUND || this.status == StatusCodes.MOVED_PERMANENTLY) && (this.method != null)) {
                exchange.setRequestMethod(this.method);
            }

            exchange.endExchange();
            return;
        }

        if (this.contentType != null) {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, this.contentType);
        }


        if (this.hasHeaders) {
            long itr = this.headers.fastIterateNonEmpty();

            while (itr != -1L) {
                final HeaderValues values = this.headers.fiCurrent(itr);

                exchange.getResponseHeaders().putAll(values.getHeaderName(), values);

                itr = this.headers.fiNextNonEmpty(itr);
            }
        }

        if (this.hasCookies) {
            exchange.getResponseCookies().putAll(this.cookies);
        } else if (!this.processJson && !this.processXml) {
            if (ServerPredicates.ACCEPT_JSON_PREDICATE.resolve(exchange)) {
                this.applicationJson();
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, this.contentType);
            } else if (ServerPredicates.ACCEPT_XML_PREDICATE.resolve(exchange)) {
                this.applicationXml();
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, this.contentType);
            } else if (ServerPredicates.ACCEPT_TEXT_PREDICATE.resolve(exchange)) {
                this.textPlain();
                exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, this.contentType);
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
                if (this.processXml) {
                    exchange.getResponseSender().send(ByteBuffer.wrap(XML_MAPPER.writeValueAsBytes(this.entity)));
                } else {

                    final Class jsonViewClass = exchange.getAttachment(JsonViewWrapper.JSON_VIEW_KEY);

                    if(jsonViewClass != null)
                    {
                        ObjectWriter writer = WRITER_CACHE.computeIfAbsent(jsonViewClass, (view) -> OBJECT_MAPPER.writerWithView(view));
                        exchange.getResponseSender().send(ByteBuffer.wrap(writer.writeValueAsBytes(this.entity)));
                    }
                    else
                    {
                        exchange.getResponseSender().send(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(this.entity)));
                    }

                 }

            } catch (Exception e) {

                log.error(e.getMessage() + " for entity " + this.entity, e);

                throw new IllegalArgumentException(e);
            }

        } else {

            if(handler != null)
            {
                try {
                    handler.handleRequest(exchange);
                } catch (Exception e) {
                    log.error("Error handling request",e);
                    exchange.endExchange();
                }
            }
            else {
                exchange.endExchange();
            }
        }

    }

    /**
     * Creates builder to build {@link ServerResponse}.
     *
     * @return created builder
     */
    public static <T> ServerResponse<T> response(Class<T> clazz)
    {
        return new ServerResponse<T>();
    }


    public static ServerResponse<ByteBuffer> response(ByteBuffer body)
    {
        return new ServerResponse<ByteBuffer>().body(body);
    }

    public static ServerResponse<ByteBuffer> response(String body)
    {
        return new ServerResponse<ByteBuffer>().body(body);
    }

    public static <T> ServerResponse<T> response(T entity)
    {
        return new ServerResponse<T>().entity(entity);
    }

    @SuppressWarnings("rawtypes")
    public static ServerResponse response()
    {
        return new ServerResponse();
    }

}
