package io.sinistral.proteus.security.handlers;

import io.sinistral.proteus.security.SecurityContext;
import io.undertow.util.AttachmentKey;

/**
 * Attachment key for storing a validated {@link SecurityContext} on an Undertow exchange.
 * This is the authoritative request-scoped carrier between security and generated endpoint
 * handlers; it is deliberately not a thread-local carrier.
 *
 * @since 0.9.5
 */
public class SecurityContextAttachment {

    /**
     * Key for storing and retrieving the context from an {@code HttpServerExchange}.
     */
    public static final AttachmentKey<SecurityContext> KEY = AttachmentKey.create(SecurityContext.class);

    private SecurityContextAttachment() {
        // Utility class
    }
}
