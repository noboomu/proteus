package io.sinistral.proteus.openapi.wrappers;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.util.AttachmentKey;


/**
 * Handler wrapper that extracts a Bearer token from the Authorization header and attaches the
 * raw token and its validation result to the exchange.
 */
    /** Attachment key holding the raw bearer token. */
@Singleton
public class BearerTokenWrapper implements HandlerWrapper {

    /** Attachment key holding the raw bearer token. */
    public static final AttachmentKey<String> BEARER_TOKEN_KEY = AttachmentKey.create(String.class);
    /** Attachment key holding the validator result for the bearer token. */
    public static final AttachmentKey<Object> BEARER_VALIDATION_RESULT_KEY = AttachmentKey.create(Object.class);

    /** the  b e a r e r. */
    private static final String BEARER = "bearer ";
    /** the  p r e f i x_ l e n g t h. */
    private static final int PREFIX_LENGTH = BEARER.length();
    /** the  a u t h o r i z a t i o n. */
    private static final String AUTHORIZATION = "Authorization";

    /** Callback that validates a bearer token and returns an arbitrary result. */
    public interface BearerTokenValidator {
        /**
         * Validates a token.
         *
         * @param token the raw bearer token
         * @return the validation result, for example {@code Boolean.TRUE} for valid
         */
        Object validate(String token);
    }


    /** the token validator. */
    private final BearerTokenValidator tokenValidator;

    /**
     * Creates the wrapper.
     *
     * @param tokenValidator a function that takes a JWT token as input and returns a validation result (can be null or Boolean.TRUE for valid, etc.)
     *                       If null, validation is skipped and only the raw token is attached.
     */
    @Inject
    public BearerTokenWrapper(BearerTokenValidator tokenValidator) {
        this.tokenValidator = tokenValidator;
    }

    /**
     * Processes this element.
    *
    * @param handler the value
    * @return the result
     */
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
