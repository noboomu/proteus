package io.sinistral.proteus.websocket;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedBinaryMessage;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.CloseMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * Receive listener dispatching Undertow frames to annotated endpoint methods.
 *
 * <p>Handler exceptions close the connection with code 1011 and log the cause at warn
 * level. Argument order for multi-parameter handlers is flexible: the dispatcher matches
 * on parameter type, not position.
 *
 * @author jbauer
 */
class EndpointReceiveListener extends AbstractReceiveListener {

    /** Class logger. */
    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(EndpointReceiveListener.class);

    /** Endpoint singleton receiving dispatched frames. */
    private final Object endpoint;
    /** Handler for {@code @OnOpen}, or null. */
    private final Method onOpen;
    /** Handler for {@code @OnMessage} text frames, or null. */
    private final Method onTextMessage;
    /** Handler for {@code @OnMessage} binary frames, or null. */
    private final Method onBinaryMessage;
    /** Handler for {@code @OnClose}, or null. */
    private final Method onClose;
    /** Handler for {@code @OnError}, or null. */
    private final Method onError;
    /** Owning service used for connection lookup and cleanup. */
    private final DefaultWebSocketService service;
    /** Inbound frame limit; oversized frames close with 1009. */
    private final long maxFrameSizeBytes;

    /** Binds the dispatcher to its handlers.
     *
     * @param endpoint endpoint singleton receiving dispatched frames
     * @param onOpen handler for {@code @OnOpen}, or null
     * @param onTextMessage handler for {@code @OnMessage} text frames, or null
     * @param onBinaryMessage handler for {@code @OnMessage} binary frames, or null
     * @param onClose handler for {@code @OnClose}, or null
     * @param onError handler for {@code @OnError}, or null
     * @param service owning service for connection bookkeeping
     * @param maxFrameSizeBytes inbound frame limit in bytes
     */
    EndpointReceiveListener(
            Object endpoint,
            Method onOpen,
            Method onTextMessage,
            Method onBinaryMessage,
            Method onClose,
            Method onError,
            DefaultWebSocketService service,
            long maxFrameSizeBytes) {
        this.endpoint = endpoint;
        this.onOpen = onOpen;
        this.onTextMessage = onTextMessage;
        this.onBinaryMessage = onBinaryMessage;
        this.onClose = onClose;
        this.onError = onError;
        this.service = service;
        this.maxFrameSizeBytes = maxFrameSizeBytes;
    }

    /** Invoked by the service after registration; fires the open side effect.
     *
     * @param connection the newly registered connection wrapper
     */
    void fireOpen(DefaultWebSocketConnection connection) {
        invokeHandler(onOpen, connection, null, null);
    }

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message)
            throws IOException {
        // Undertow 2.3.17's async read path never applies BufferedTextMessage's
        // maxMessageSize check, so enforce the cap here on the assembled message.
        // NB: getData() consumes the underlying UTF8Output, so extract exactly once.
        String payload = message.getData();
        if (maxFrameSizeBytes > 0 && payload.length() > maxFrameSizeBytes) {
            rejectOversized(channel);
            return;
        }
        DefaultWebSocketConnection connection = service.connectionFor(channel);
        invokeHandler(onTextMessage, connection, payload, null);
    }

    @Override
    protected void onFullBinaryMessage(WebSocketChannel channel, BufferedBinaryMessage message)
            throws IOException {
        DefaultWebSocketConnection connection = service.connectionFor(channel);
        ByteBuffer[] payload = message.getData().getResource();
        if (maxFrameSizeBytes > 0 && remaining(payload) > maxFrameSizeBytes) {
            message.getData().free();
            rejectOversized(channel);
            return;
        }
        byte[] bytes = new byte[remaining(payload)];
        int offset = 0;
        for (ByteBuffer buffer : payload) {
            int length = buffer.remaining();
            buffer.get(bytes, offset, length);
            offset += length;
        }
        message.getData().free();
        invokeHandler(onBinaryMessage, connection, bytes, null);
    }

    /** Closes the channel with 1009 (message too big) per RFC 6455.
     *
     * @param channel the channel carrying the oversized frame
     */
    private void rejectOversized(WebSocketChannel channel) {
        WebSockets.sendClose(new CloseMessage(1009, "message too big"), channel, null);
        try {
            channel.close();
        } catch (IOException e) {
            log.warn("Failed closing oversized-frame channel", e);
        }
    }

    /** Sums remaining bytes across a fragmented buffer chain.
     *
     * @param buffers the fragmented payload buffers
     * @return total remaining bytes
     */
    private static int remaining(ByteBuffer[] buffers) {
        int total = 0;
        for (ByteBuffer buffer : buffers) {
            total += buffer.remaining();
        }
        return total;
    }

    @Override
    protected void onCloseMessage(CloseMessage cm, WebSocketChannel channel) {
        DefaultWebSocketConnection connection = service.connectionFor(channel);
        try {
            invokeHandler(onClose, connection, new CloseReason(cm.getCode(), cm.getReason()), null);
        } finally {
            service.remove(channel);
        }
    }

    @Override
    protected void onError(WebSocketChannel channel, Throwable error) {
        DefaultWebSocketConnection connection = service.connectionFor(channel);
        invokeHandler(onError, connection, null, error);
    }

    /**
     * Invokes one annotated handler with flexible argument order.
     *
     * @param handler the annotated method to invoke
     * @param connection the live connection wrapper
     * @param textOrBytes the text payload, binary payload, or null
     * @param error the dispatched error, or null
     */
    private void invokeHandler(
            Method handler,
            DefaultWebSocketConnection connection,
            Object textOrBytes,
            Throwable error) {
        if (handler == null) {
            return;
        }
        Object[] arguments = new Object[handler.getParameterCount()];
        int filled = 0;
        for (Class<?> type : handler.getParameterTypes()) {
            if (WebSocketConnection.class.isAssignableFrom(type)) {
                arguments[filled++] = connection;
            } else if (textOrBytes instanceof String text && type == String.class) {
                arguments[filled++] = text;
            } else if (textOrBytes instanceof byte[] bytes && type == byte[].class) {
                arguments[filled++] = bytes;
            } else if (type == CloseReason.class && textOrBytes instanceof CloseReason reason) {
                arguments[filled++] = reason;
            } else if (type == Throwable.class && error != null) {
                arguments[filled++] = error;
            } else {
                arguments[filled++] = null;
            }
        }
        try {
            handler.invoke(endpoint, arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Throwable cause = e instanceof InvocationTargetException ite ? ite.getCause() : e;
            log.warn("WebSocket handler {} failed", handler.getName(), cause);
            if (connection != null && connection.isOpen()) {
                connection.close(new CloseReason(1011, "server error"));
            }
        }
    }
}
