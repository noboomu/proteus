package io.sinistral.proteus.websocket;

/**
 * Interface for handling WebSocket endpoint events.
 * 
 * This interface defines the contract for WebSocket endpoint handlers
 * that process connection lifecycle events and messages.
 * 
 * @since 1.0
 */
public interface WebSocketEndpointHandler {
    
    /**
     * Called when a new WebSocket connection is established.
     * 
     * @param connection the new connection
     */
    void onOpen(WebSocketConnection connection);
    
    /**
     * Called when a text message is received.
     * 
     * @param connection the connection that sent the message
     * @param message the text message
     */
    default void onMessage(WebSocketConnection connection, String message) {
        // Default implementation does nothing
    }
    
    /**
     * Called when a binary message is received.
     * 
     * @param connection the connection that sent the message
     * @param data the binary data
     */
    default void onMessage(WebSocketConnection connection, byte[] data) {
        // Default implementation does nothing
    }
    
    /**
     * Called when a WebSocket connection is closed.
     * 
     * @param connection the connection that was closed
     * @param closeReason the reason for closure
     */
    void onClose(WebSocketConnection connection, CloseReason closeReason);
    
    /**
     * Called when an error occurs on a WebSocket connection.
     * 
     * @param connection the connection where the error occurred
     * @param error the error that occurred
     */
    void onError(WebSocketConnection connection, Throwable error);
}
