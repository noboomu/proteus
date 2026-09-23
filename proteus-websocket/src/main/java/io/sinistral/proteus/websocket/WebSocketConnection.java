package io.sinistral.proteus.websocket;

import io.undertow.websockets.core.WebSocketChannel;

import java.util.concurrent.CompletableFuture;

/**
 * One live WebSocket connection managed by the endpoint framework.
 *
 * @author jbauer
 */
public interface WebSocketConnection {
    /** Returns the UUID generated for this connection at upgrade.
     *
     * @return the connection id
     */
    String getConnectionId();

    /** Returns the endpoint path this connection upgraded on.
     *
     * @return the endpoint path
     */
    String getPath();

    /** Sends a UTF-8 text frame.
     *
     * @param message the text payload
     * @return a future completing when the frame is queued
     */
    CompletableFuture<Void> sendText(String message);

    /** Serializes with the application Jackson 3 mapper and sends a text frame.
     *
     * @param object the payload to serialize
     * @return a future completing when the frame is queued
     */
    CompletableFuture<Void> sendJson(Object object);

    /** Sends a raw binary frame without envelope.
     *
     * @param data the binary payload
     * @return a future completing when the frame is queued
     */
    CompletableFuture<Void> sendBinary(byte[] data);

    /** Closes with a normal status code. */
    void close();

    /** Closes with the status code and reason text from the CloseReason.
     *
     * @param reason the close status and reason text
     */
    void close(CloseReason reason);

    /** Returns true while the underlying channel is open.
     *
     * @return true while open
     */
    boolean isOpen();

    /** Returns a connection-scoped attribute.
     *
     * @param <T> attribute value type
     * @param key the attribute key
     * @return the stored value, or null
     */
    <T> T getAttribute(String key);

    /** Stores a connection-scoped attribute.
     *
     * @param key the attribute key
     * @param value the value to store
     */
    void setAttribute(String key, Object value);

    /** Returns the underlying Undertow channel.
     *
     * @return the live Undertow channel
     */
    WebSocketChannel getChannel();
}
