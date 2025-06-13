package io.sinistral.proteus.annotations.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark a method as a WebSocket message handler.
 * 
 * This method will be called when a message is received from the client.
 * The method can have the following parameter types:
 * - String: Text message content
 * - byte[]: Binary message content
 * - Custom objects: Will be deserialized from JSON
 * - WebSocketConnection: The connection that sent the message
 * - SecurityContext: The security context (if authentication is enabled)
 * 
 * Example:
 * <pre>
 * {@code
 * @OnMessage
 * public void onTextMessage(String message, WebSocketConnection connection) {
 *     log.info("Received: {}", message);
 *     connection.sendText("Echo: " + message);
 * }
 * 
 * @OnMessage
 * public void onChatMessage(ChatMessage message, WebSocketConnection connection) {
 *     // Handle structured message
 *     chatService.processMessage(message);
 *     broadcast(message);
 * }
 * 
 * @OnMessage
 * public void onBinaryMessage(byte[] data, WebSocketConnection connection) {
 *     // Handle binary data
 *     fileService.processUpload(data);
 * }
 * }
 * </pre>
 * 
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnMessage {
    
    /**
     * Whether this handler should run on a virtual thread.
     * Default is true for potentially blocking operations.
     * 
     * @return true to run on virtual thread
     */
    boolean virtual() default true;
    
    /**
     * Maximum message size for this handler in bytes.
     * If not specified, uses the WebSocket endpoint's maxMessageSize.
     * 
     * @return maximum message size, or -1 to use endpoint default
     */
    long maxSize() default -1;
}
