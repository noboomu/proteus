package io.sinistral.proteus.websocket;

import io.undertow.websockets.core.WebSocketChannel;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default WebSocketConnection wrapping one Undertow channel.
 *
 * @author jbauer
 */
public class DefaultWebSocketConnection implements WebSocketConnection {

    /** UUID generated for this connection at upgrade. */
    private final String connectionId = UUID.randomUUID().toString();
    /** Endpoint path this connection upgraded on. */
    private final String path;
    /** Underlying Undertow channel. */
    private final WebSocketChannel channel;
    /** Shared Jackson 3 mapper for JSON sends. */
    private final ObjectMapper objectMapper;
    /** Connection-scoped attributes. */
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    /** Binds the wrapper to a live channel.
     *
     * @param path the endpoint path
     * @param channel the upgraded Undertow channel
     * @param objectMapper shared Jackson 3 mapper
     */
    public DefaultWebSocketConnection(
            String path, WebSocketChannel channel, ObjectMapper objectMapper) {
        this.path = path;
        this.channel = channel;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public CompletableFuture<Void> sendText(String message) {
        return SendFutures.sendText(message, channel);
    }

    @Override
    public CompletableFuture<Void> sendJson(Object object) {
        return sendText(objectMapper.writeValueAsString(object));
    }

    @Override
    public CompletableFuture<Void> sendBinary(byte[] data) {
        return SendFutures.sendBinary(java.nio.ByteBuffer.wrap(data), channel);
    }

    @Override
    public void close() {
        close(new CloseReason(1000, "normal closure"));
    }

    @Override
    public void close(CloseReason reason) {
        SendFutures.sendClose(reason.code(), reason.reason(), channel);
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    @Override
    public WebSocketChannel getChannel() {
        return channel;
    }
}
