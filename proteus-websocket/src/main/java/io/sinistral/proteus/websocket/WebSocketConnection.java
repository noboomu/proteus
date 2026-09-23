package io.sinistral.proteus.websocket;

import io.undertow.websockets.core.WebSocketChannel;

import java.util.concurrent.CompletableFuture;

/**
 * One live WebSocket connection managed by the endpoint framework.
 *
 * @author jbauer
 */
public interface WebSocketConnection {
    /** @return the UUID generated for this connection at upgrade */
    String getConnectionId();

    /** @return the endpoint path this connection upgraded on */
    String getPath();

    /** Sends a UTF-8 text frame. */
    CompletableFuture<Void> sendText(String message);

    /** Serializes with the application Jackson 3 mapper and sends a text frame. */
    CompletableFuture<Void> sendJson(Object object);

    /** Sends a raw binary frame without envelope. */
    CompletableFuture<Void> sendBinary(byte[] data);

    /** Closes with a normal status code. */
    void close();

    /** Closes with the status code and reason text from the CloseReason. */
    void close(CloseReason reason);

    /** @return true while the underlying channel is open */
    boolean isOpen();

    /** @return a connection-scoped attribute */
    <T> T getAttribute(String key);

    /** Stores a connection-scoped attribute. */
    void setAttribute(String key, Object value);

    /** @return the underlying Undertow channel */
    WebSocketChannel getChannel();
}
