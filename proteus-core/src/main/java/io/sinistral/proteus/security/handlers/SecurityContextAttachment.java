package io.sinistral.proteus.security.handlers;

import io.sinistral.proteus.security.SecurityContext;
import io.undertow.util.AttachmentKey;

/**
 * Attachment key for storing SecurityContext in Undertow exchanges.
 * This allows security context to be passed between handlers in the request processing chain.
 * 
 * @since 1.0
 */
public class SecurityContextAttachment {
    
    /**
     * Attachment key for storing and retrieving SecurityContext from HttpServerExchange.
     */
    public static final AttachmentKey<SecurityContext> KEY = AttachmentKey.create(SecurityContext.class);
    
    private SecurityContextAttachment() {
        // Utility class
    }
}
