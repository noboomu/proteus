package io.sinistral.proteus.annotations.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method as a WebSocket connection close handler.
 * 
 * This method will be called when a WebSocket connection is closed.
 * The method can have the following parameter types:
 * - WebSocketConnection: The connection that was closed
 * - SecurityContext: The security context (if authentication is enabled)
 * - CloseReason: Information about why the connection was closed
 * 
 * Example:
 * <pre>
 * {@code
 * @OnClose
 * public void onClose(WebSocketConnection connection) {
 *     log.info("Connection closed: {}", connection.getId());
 *     // Remove from groups, cleanup resources, etc.
 * }
 * 
 * @OnClose
 * public void onCloseWithReason(WebSocketConnection connection, CloseReason reason) {
 *     log.info("Connection {} closed: {} - {}", 
 *              connection.getId(), reason.getCode(), reason.getReason());
 * }
 * }
 * </pre>
 * 
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnClose {
    
    /**
     * Whether this handler should run on a virtual thread.
     * Default is false for fast cleanup operations.
     * 
     * @return true to run on virtual thread
     */
    boolean virtual() default false;
}
