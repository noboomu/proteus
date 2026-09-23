package io.sinistral.proteus.websocket;

import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedBinaryMessage;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.CloseMessage;
import io.undertow.websockets.core.WebSocketChannel;

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

    private static final org.slf4j.Logger log =
            org.slf4j.LoggerFactory.getLogger(EndpointReceiveListener.class);

    private final Object endpoint;
    private final Method onOpen;
    private final Method onTextMessage;
    private final Method onBinaryMessage;
    private final Method onClose;
    private final Method onError;
    private final DefaultWebSocketService service;

    EndpointReceiveListener(
            Object endpoint,
            Method onOpen,
            Method onTextMessage,
            Method onBinaryMessage,
            Method onClose,
            Method onError,
            DefaultWebSocketService service) {
        this.endpoint = endpoint;
        this.onOpen = onOpen;
        this.onTextMessage = onTextMessage;
        this.onBinaryMessage = onBinaryMessage;
        this.onClose = onClose;
        this.onError = onError;
        this.service = service;
    }

    /** Invoked by the service after registration; fires the open side effect. */
    void fireOpen(DefaultWebSocketConnection connection) {
        invokeHandler(onOpen, connection, null, null);
    }

    @Override
    protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message)
            throws IOException {
        DefaultWebSocketConnection connection = service.connectionFor(channel);
        invokeHandler(onTextMessage, connection, message.getData(), null);
    }

    @Override
    protected void onFullBinaryMessage(WebSocketChannel channel, BufferedBinaryMessage message)
            throws IOException {
        DefaultWebSocketConnection connection = service.connectionFor(channel);
        ByteBuffer[] payload = message.getData().getResource();
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
     * @param textOrBytes the text payload, binary payload, or null
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
