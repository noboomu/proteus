package io.sinistral.proteus.websocket;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.undertow.server.RoutingHandler;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.WebSocketProtocolHandshakeHandler;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Registry and broadcast service wiring annotated WebSocket endpoints into the Undertow
 * router.
 *
 * <p>Handler exceptions close the connection with code 1011. Broadcast futures complete
 * when all target sends have been initiated; a failed send fails the future without
 * aborting the remaining sends.
 *
 * @author jbauer
 */
@Singleton
public class DefaultWebSocketService implements WebSocketService {

    private static final Logger log = LoggerFactory.getLogger(DefaultWebSocketService.class);

    private final ObjectMapper objectMapper;
    private final int maxFrameSizeBytes;
    private final int idleTimeoutMs;
    private final int maxConnectionsPerPath;

    // endpoint path -> endpoint singleton instance
    private final Map<String, Object> endpointsByPath = new ConcurrentHashMap<>();
    // channel -> live connection wrapper
    private final Map<WebSocketChannel, DefaultWebSocketConnection> connections = new ConcurrentHashMap<>();
    // connection id -> live connection wrapper
    private final Map<String, DefaultWebSocketConnection> connectionsById = new ConcurrentHashMap<>();

    @Inject
    public DefaultWebSocketService(ObjectMapper objectMapper, com.typesafe.config.Config config) {
        this.objectMapper = objectMapper;
        this.maxFrameSizeBytes = config.hasPath("proteus.websocket.maxFrameSizeBytes")
                ? config.getInt("proteus.websocket.maxFrameSizeBytes")
                : 1_048_576;
        this.idleTimeoutMs = config.hasPath("proteus.websocket.idleTimeoutMs")
                ? config.getInt("proteus.websocket.idleTimeoutMs")
                : 300_000;
        this.maxConnectionsPerPath = config.hasPath("proteus.websocket.maxConnectionsPerPath")
                ? config.getInt("proteus.websocket.maxConnectionsPerPath")
                : 0;
    }

    /** Registers one endpoint bean; invoked by the scanner at application startup. */
    public void registerEndpoint(String path, Object endpoint) {
        endpointsByPath.put(path, endpoint);
    }

    /** @return the connection wrapper for a live Undertow channel, or null */
    public DefaultWebSocketConnection connectionFor(WebSocketChannel channel) {
        return connections.get(channel);
    }

    /** Removes a closed channel from the registries. */
    public void remove(WebSocketChannel channel) {
        DefaultWebSocketConnection connection = connections.remove(channel);
        if (connection != null) {
            connectionsById.remove(connection.getConnectionId());
        }
    }

    @Override
    public void registerEndpoints(RoutingHandler router) {
        for (Map.Entry<String, Object> entry : endpointsByPath.entrySet()) {
            String path = entry.getKey();
            Object endpoint = entry.getValue();
            EndpointReceiveListener listener = listenerFor(endpoint);
            WebSocketProtocolHandshakeHandler handshakeHandler = new WebSocketProtocolHandshakeHandler(
                    (WebSocketConnectionCallback) (exchange, channel) -> {
                        DefaultWebSocketConnection connection =
                                new DefaultWebSocketConnection(path, channel, objectMapper);
                        if (maxConnectionsPerPath > 0
                                && getConnectionsForPath(path).size() >= maxConnectionsPerPath) {
                            // Upgrade already completed; reject with close code 1013 (try again later).
                            WebSockets.sendClose(1013, "try again later", channel, null);
                            try {
                                channel.close();
                            } catch (java.io.IOException e) {
                                log.warn("Failed closing rejected connection on {}", path, e);
                            }
                            return;
                        }
                        if (idleTimeoutMs > 0) {
                            channel.setIdleTimeout(idleTimeoutMs);
                        }
                        // Registry cleanup must run even for abrupt (close-frame-less) disconnects.
                        channel.addCloseTask(c -> remove(c));
                        connections.put(channel, connection);
                        connectionsById.put(connection.getConnectionId(), connection);
                        channel.getReceiveSetter().set(listener);
                        channel.resumeReceives();
                        listener.fireOpen(connection);
                    });
            router.add(io.undertow.util.Methods.GET, path, handshakeHandler);
            log.info("Registered WebSocket endpoint at {}", path);
        }
    }

    /** Builds the frame dispatcher for one endpoint bean. */
    private EndpointReceiveListener listenerFor(Object endpoint) {
        Method onOpen = null;
        Method onTextMessage = null;
        Method onBinaryMessage = null;
        Method onClose = null;
        Method onError = null;
        for (Class<?> type = endpoint.getClass(); type != null; type = type.getSuperclass()) {
            for (Method method : type.getDeclaredMethods()) {
                if (method.isAnnotationPresent(OnOpen.class) && onOpen == null) {
                    onOpen = accessible(method);
                } else if (method.isAnnotationPresent(OnMessage.class)) {
                    // First String-parameter method handles text; first byte[] method binary.
                    boolean textCapable =
                            java.util.Arrays.stream(method.getParameterTypes()).anyMatch(p -> p == String.class);
                    boolean binaryCapable =
                            java.util.Arrays.stream(method.getParameterTypes()).anyMatch(p -> p == byte[].class);
                    if (textCapable && onTextMessage == null) {
                        onTextMessage = accessible(method);
                    } else if (binaryCapable && onBinaryMessage == null) {
                        onBinaryMessage = accessible(method);
                    }
                } else if (method.isAnnotationPresent(OnClose.class) && onClose == null) {
                    onClose = accessible(method);
                } else if (method.isAnnotationPresent(OnError.class) && onError == null) {
                    onError = accessible(method);
                }
            }
        }
        return new EndpointReceiveListener(
                endpoint, onOpen, onTextMessage, onBinaryMessage, onClose, onError, this);
    }

    private static Method accessible(Method method) {
        method.setAccessible(true);
        return method;
    }

    @Override
    public Set<WebSocketConnection> getActiveConnections() {
        return Set.copyOf(connections.values());
    }

    @Override
    public Set<WebSocketConnection> getConnectionsForPath(String path) {
        return connections.values().stream()
                .filter(connection -> connection.getPath().equals(path))
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public WebSocketConnection getConnection(String connectionId) {
        return connectionsById.get(connectionId);
    }

    @Override
    public CompletableFuture<Void> broadcastText(String message) {
        return broadcastHelper(getActiveConnections(), connection -> connection.sendText(message));
    }

    @Override
    public CompletableFuture<Void> broadcastText(String path, String message) {
        return broadcastHelper(getConnectionsForPath(path), connection -> connection.sendText(message));
    }

    @Override
    public CompletableFuture<Void> broadcastJson(Object object) {
        String json = objectMapper.writeValueAsString(object);
        return broadcastHelper(getActiveConnections(), connection -> connection.sendText(json));
    }

    @Override
    public CompletableFuture<Void> broadcastJson(String path, Object object) {
        String json = objectMapper.writeValueAsString(object);
        return broadcastHelper(getConnectionsForPath(path), connection -> connection.sendText(json));
    }

    @Override
    public CompletableFuture<Void> broadcastBinary(byte[] data) {
        return broadcastHelper(getActiveConnections(), connection -> connection.sendBinary(data));
    }

    @Override
    public CompletableFuture<Void> broadcastBinary(String path, byte[] data) {
        return broadcastHelper(getConnectionsForPath(path), connection -> connection.sendBinary(data));
    }

    /** Sends to every open target; a failed send fails the future but does not abort the rest. */
    private CompletableFuture<Void> broadcastHelper(
            Set<WebSocketConnection> targets, java.util.function.Function<WebSocketConnection, CompletableFuture<Void>> sender) {
        List<WebSocketConnection> open = targets.stream()
                .filter(connection -> connection.isOpen())
                .collect(Collectors.toList());
        if (open.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        CompletableFuture<?>[] sends = open.stream()
                .map(connection -> {
                    try {
                        return sender.apply(connection);
                    } catch (RuntimeException e) {
                        return CompletableFuture.failedFuture(e);
                    }
                })
                .toArray(CompletableFuture[]::new);
        return CompletableFuture.allOf(sends);
    }
}
