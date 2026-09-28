package io.sinistral.proteus.openapi.wrappers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.sinistral.proteus.server.exceptions.ServerException;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.util.AttachmentKey;
import io.undertow.util.HttpString;
import io.undertow.util.StatusCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/** Handler wrapper extracting an API key header into an exchange attachment. */
@Singleton
/** Handler wrapper extracting an API key header attachment. */
public class HeaderApiKeyWrapper implements HandlerWrapper
{
    /** the logger. */
    private static final Logger logger = LoggerFactory.getLogger(HeaderApiKeyWrapper.class.getName());

    /** throwable constant. */
    public static final AttachmentKey<Throwable> THROWABLE = AttachmentKey.create(Throwable.class);

    /** the value. */
    @Inject
    @Named("openapi.securitySchemes.ApiKeyAuth.name")
    protected static String AUTH_KEY_NAME;

    /** the value. */
    /** the  a p i_ k e y_ h e a d e r. */
    @Inject(optional = true)
    @Named("security.apiKey")
    protected static String API_KEY;

    /** the  a p i_ k e y_ h e a d e r. */
    private final HttpString API_KEY_HEADER;

    /** Creates the wrapper with the configured API key header name. */
    public HeaderApiKeyWrapper()
    {
        API_KEY_HEADER = new HttpString(AUTH_KEY_NAME);
    }

    /**
     * Operates on the value.
    *
    * @param handler the value
    * @return the result
     */
    @Override
    public HttpHandler wrap(HttpHandler handler)
    {
        return exchange -> {

            if(API_KEY == null)
            {
                handler.handleRequest(exchange);
                return;
            }

            Optional<String> keyValue = Optional.ofNullable(exchange.getRequestHeaders().getFirst(API_KEY_HEADER));

            if(keyValue.isEmpty() || !keyValue.get().equals(API_KEY))
            {
                StringBuilder sb = new StringBuilder();

                sb.append("\n");

                exchange.getRequestHeaders().forEach(h -> {

                    sb.append(h.getHeaderName()).append(": ").append(h.getFirst()).append("\n");

                });

                logger.error("Missing security credentials");
                ServerException serverException = new ServerException("Unauthorized access: " + sb, StatusCodes.UNAUTHORIZED);
                exchange.putAttachment(THROWABLE, serverException);
                throw serverException;

            }

            handler.handleRequest(exchange);



        };
    }

}
