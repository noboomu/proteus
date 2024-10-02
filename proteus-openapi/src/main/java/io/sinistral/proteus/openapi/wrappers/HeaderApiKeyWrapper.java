package io.sinistral.proteus.openapi.wrappers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.sinistral.proteus.server.exceptions.ServerException;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.stream.Stream;

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

    public static class InvalidAPIKeyException extends  ServerException
    {
        @Serial
        private static final long serialVersionUID = 7557112473280469649L;

        private InetAddress address;

        public InvalidAPIKeyException(String message, InetAddress address)
        {
            super(message,StatusCodes.UNAUTHORIZED);
            this.address = address;
        }

        public InetAddress getAddress() {
            return address;
        }

        public void setAddress(InetAddress address) {
            this.address = address;
        }
    }

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

            InetAddress address;

            HeaderMap headers = exchange.getRequestHeaders();

            Optional<HeaderValues> values = Optional.ofNullable(headers.get(Headers.X_FORWARDED_FOR));

            if(values.isPresent())
            {
                String xForwardedFor = values.get().getFirst();

                xForwardedFor = Stream.of(xForwardedFor.split(",")).map(String::trim).findFirst().orElse("127.0.0.1");

                address = InetAddress.getByName(xForwardedFor);
            }
            else
            {
                InetSocketAddress socketAddress = exchange.getSourceAddress();

                address = socketAddress.getAddress();
            }

            Optional<String> keyValue = Optional.ofNullable(exchange.getRequestHeaders().getFirst(API_KEY_HEADER));

            if(keyValue.isEmpty() || !keyValue.get().equals(API_KEY))
            {
                StringBuilder sb = new StringBuilder();

                sb.append("\n");

                exchange.getRequestHeaders().forEach(h -> {

                    sb.append(h.getHeaderName()).append(": ").append(h.getFirst()).append("\n");

                });

                exchange.getQueryParameters().forEach((k,v) -> {

                    sb.append("query: ").append(k).append(": ").append(v.getFirst()).append("\n");

                });

                exchange.getPathParameters().forEach((k,v) -> {

                    sb.append("path: ").append(k).append(": ").append(v.getFirst()).append("\n");

                });

                logger.error("Missing security credentials: {}", sb);
                exchange.putAttachment(THROWABLE, new InvalidAPIKeyException("Unauthorized access: " + sb,address));
                throw new InvalidAPIKeyException("Unauthorized access: " + sb, address);

            }

            handler.handleRequest(exchange);



        };
    }

}
