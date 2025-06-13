package io.sinistral.proteus.annotations.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method as a WebSocket connection open handler.
 * 
 * This method will be called when a new WebSocket connection is established.
 * The method can have the following parameter types:
 * - WebSocketConnection: The connection that was opened
 * - SecurityContext: The security context (if authentication is enabled)
 * - HttpServerExchange: The original HTTP exchange
 * 
 * Example:
 * <pre>
 * {@code
 * @OnOpen
 * public void onOpen(WebSocketConnection connection) {
 *     log.info("Connection opened: {}", connection.getId());
 *     // Add connection to a group, send welcome message, etc.
 * }
 * 
 * @OnOpen
 * public void onOpenWithAuth(WebSocketConnection connection, SecurityContext security) {
 *     String userId = security.getUserId().orElse("anonymous");
 *     log.info("User {} connected", userId);
 * }
 * }
 * </pre>
 * 
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnOpen {
    
    /**
     * Whether this handler should run on a virtual thread.
     * Default is false for fast non-blocking operations.
     * 
     * @return true to run on virtual thread
     */
    boolean virtual() default false;
}
