package io.sinistral.proteus.openapi.wrappers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.sinistral.proteus.server.exceptions.ServerException;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.util.AttachmentKey;
import io.undertow.util.HttpString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.util.Optional;

@Singleton
public class HeaderApiKeyWrapper implements HandlerWrapper
{
    private static final Logger logger = LoggerFactory.getLogger(HeaderApiKeyWrapper.class.getName());

    public static final AttachmentKey<Throwable> THROWABLE = AttachmentKey.create(Throwable.class);

    @Inject
    @Named("openapi.securitySchemes.ApiKeyAuth.name")
    protected static String AUTH_KEY_NAME;

    @Inject(optional = true)
    @Named("security.apiKey")
    protected static String API_KEY;

    private final HttpString API_KEY_HEADER;

    public HeaderApiKeyWrapper()
    {
        API_KEY_HEADER = new HttpString(AUTH_KEY_NAME);
    }

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
                exchange.putAttachment(THROWABLE, new ServerException("Unauthorized access: " + sb, Response.Status.UNAUTHORIZED));
                throw new ServerException("Unauthorized access: " + sb, Response.Status.UNAUTHORIZED);

            }

            handler.handleRequest(exchange);



        };
    }

}
