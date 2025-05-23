package io.sinistral.proteus.openapi.wrappers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.util.AttachmentKey;


@Singleton
public class BearerTokenWrapper implements HandlerWrapper {

    public static final AttachmentKey<String> BEARER_TOKEN_KEY = AttachmentKey.create(String.class);
    public static final AttachmentKey<Object> BEARER_VALIDATION_RESULT_KEY = AttachmentKey.create(Object.class);

    private static final String BEARER = "bearer ";
    private static final int PREFIX_LENGTH = BEARER.length();
    private static final String AUTHORIZATION = "Authorization";

    public interface BearerTokenValidator {
        Object validate(String token);
    }


    private final BearerTokenValidator tokenValidator;

    /**
     * @param tokenValidator a function that takes a JWT token as input and returns a validation result (can be null or Boolean.TRUE for valid, etc.)
     *                       If null, validation is skipped and only the raw token is attached.
     */
    @Inject
    public BearerTokenWrapper(BearerTokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    @Override
    public HttpHandler wrap(final HttpHandler handler) {
        return exchange -> {
            String authHeader = exchange.getRequestHeaders().getFirst(AUTHORIZATION);
            if (authHeader != null && authHeader.toLowerCase().startsWith(BEARER)) {
                String token = authHeader.substring(PREFIX_LENGTH);
                if (tokenValidator != null) {
                    Object validationResult = tokenValidator.validate(token);
                    exchange.putAttachment(BEARER_TOKEN_KEY, token);
                    exchange.putAttachment(BEARER_VALIDATION_RESULT_KEY, validationResult);
                } else {

                    exchange.putAttachment(BEARER_TOKEN_KEY, token);
                }
            }
            handler.handleRequest(exchange);
        };
    }
}
