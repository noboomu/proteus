package io.sinistral.proteus.annotations.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method as a WebSocket error handler.
 * 
 * This method will be called when an error occurs in the WebSocket connection.
 * The method can have the following parameter types:
 * - Throwable/Exception: The error that occurred
 * - WebSocketConnection: The connection where the error occurred
 * - SecurityContext: The security context (if authentication is enabled)
 * 
 * Example:
 * <pre>
 * {@code
 * @OnError
 * public void onError(Throwable error, WebSocketConnection connection) {
 *     log.error("WebSocket error on connection {}: {}", 
 *               connection.getId(), error.getMessage(), error);
 *     // Handle error, potentially close connection
 * }
 * 
 * @OnError
 * public void onJsonError(JsonProcessingException error, WebSocketConnection connection) {
 *     log.warn("JSON parsing error: {}", error.getMessage());
 *     connection.sendText("{\"error\":\"Invalid JSON format\"}");
 * }
 * }
 * </pre>
 * 
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnError {
    
    /**
     * Whether this handler should run on a virtual thread.
     * Default is false for fast error handling.
     * 
     * @return true to run on virtual thread
     */
    boolean virtual() default false;
    
    /**
     * Specific exception types this handler should process.
     * If empty, handles all exceptions.
     * 
     * @return array of exception types to handle
     */
    Class<? extends Throwable>[] value() default {};
}
