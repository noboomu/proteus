package io.sinistral.proteus.annotations.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a class as a WebSocket endpoint.
 * 
 * This annotation is used to register WebSocket endpoints with Undertow.
 * The annotated class will have its methods scanned for WebSocket event handlers.
 * 
 * Example:
 * <pre>
 * {@code
 * @WebSocket("/chat")
 * @Singleton
 * public class ChatWebSocket {
 *     
 *     @OnOpen
 *     public void onOpen(WebSocketConnection connection) {
 *         // Handle connection open
 *     }
 *     
 *     @OnMessage
 *     public void onMessage(String message, WebSocketConnection connection) {
 *         // Handle incoming message
 *     }
 *     
 *     @OnClose
 *     public void onClose(WebSocketConnection connection) {
 *         // Handle connection close
 *     }
 * }
 * }
 * </pre>
 * 
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocket {
    
    /**
     * The path pattern for this WebSocket endpoint.
     * 
     * @return the path pattern (e.g., "/chat", "/api/ws/{userId}")
     */
    String value();
    
    /**
     * Optional sub-protocols that this WebSocket endpoint supports.
     * 
     * @return array of supported sub-protocols
     */
    String[] subprotocols() default {};
    
    /**
     * Optional extensions that this WebSocket endpoint supports.
     * 
     * @return array of supported extensions
     */
    String[] extensions() default {};
    
    /**
     * Whether this endpoint requires authentication.
     * When true, JWT token validation will be performed.
     * 
     * @return true if authentication is required
     */
    boolean requiresAuth() default false;
    
    /**
     * Required roles for this WebSocket endpoint when authentication is enabled.
     * 
     * @return array of required roles
     */
    String[] roles() default {};
    
    /**
     * Maximum message size in bytes. Default is 64KB.
     * 
     * @return maximum message size
     */
    long maxMessageSize() default 65536;
    
    /**
     * Connection timeout in milliseconds. Default is 5 minutes.
     * 
     * @return connection timeout
     */
    long connectionTimeout() default 300000;
}
