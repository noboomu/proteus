package io.sinistral.proteus.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.sinistral.proteus.security.SecurityContext;
import io.undertow.server.RoutingHandler;
import io.undertow.websockets.WebSocketConnectionCallback;
import io.undertow.websockets.core.AbstractReceiveListener;
import io.undertow.websockets.core.BufferedTextMessage;
import io.undertow.websockets.core.BufferedBinaryMessage;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;
import io.undertow.websockets.spi.WebSocketHttpExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * Default implementation of WebSocketService.
 * 
 * This service manages WebSocket endpoints and connections using Undertow's
 * native WebSocket support.
 * 
 * @since 1.0
 */
@Singleton
public class DefaultWebSocketService implements WebSocketService {
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultWebSocketService.class);
    
    private final ObjectMapper objectMapper;
    private final Map<String, Set<WebSocketConnection>> connectionsByPath;
    private final Map<String, WebSocketConnection> connectionsById;
    private final Map<String, WebSocketEndpointHandler> endpointHandlers;
    
    @Inject
    public DefaultWebSocketService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.connectionsByPath = new ConcurrentHashMap<>();
        this.connectionsById = new ConcurrentHashMap<>();
        this.endpointHandlers = new ConcurrentHashMap<>();
    }
    
    /**
     * Registers a WebSocket endpoint handler.
     * 
     * @param path the endpoint path
     * @param handler the endpoint handler
     */
    public void registerEndpoint(String path, WebSocketEndpointHandler handler) {
        endpointHandlers.put(path, handler);
        connectionsByPath.putIfAbsent(path, new CopyOnWriteArraySet<>());
        logger.info("Registered WebSocket endpoint: {}", path);
    }
    
    @Override
    public void registerEndpoints(RoutingHandler router) {
        for (Map.Entry<String, WebSocketEndpointHandler> entry : endpointHandlers.entrySet()) {
            String path = entry.getKey();
            WebSocketEndpointHandler handler = entry.getValue();
            
            router.get(path, io.undertow.Handlers.websocket(new WebSocketConnectionCallback() {
                @Override
                public void onConnect(WebSocketHttpExchange exchange, WebSocketChannel channel) {
                    handleConnection(path, exchange, channel, handler);
                }
            }));
            
            logger.debug("WebSocket endpoint {} registered with router", path);
        }
    }
    
    private void handleConnection(String path, WebSocketHttpExchange exchange, WebSocketChannel channel, WebSocketEndpointHandler handler) {
        try {
            // Extract security context if available
            SecurityContext securityContext = extractSecurityContext(exchange);
            
            // Create connection wrapper
            WebSocketConnection connection = new WebSocketConnection(channel, exchange, securityContext, objectMapper);
            
            // Add to tracking collections
            connectionsById.put(connection.getId(), connection);
            connectionsByPath.get(path).add(connection);
            
            logger.debug("WebSocket connection established: {} on path {}", connection.getId(), path);
            
            // Set up message listener
            channel.getReceiveSetter().set(new AbstractReceiveListener() {
                @Override
                protected void onFullTextMessage(WebSocketChannel channel, BufferedTextMessage message) {
                    try {
                        handler.onMessage(connection, message.getData());
                    } catch (Exception e) {
                        logger.error("Error handling text message on connection {}", connection.getId(), e);
                        handler.onError(connection, e);
                    }
                }
                
                @Override
                @SuppressWarnings("deprecation")
                protected void onFullBinaryMessage(WebSocketChannel channel, BufferedBinaryMessage message) {
                    var pooledData = message.getData();
                    try {
                        ByteBuffer[] buffers = pooledData.getResource();
                        
                        // Convert ByteBuffer array to byte array
                        int totalLength = 0;
                        for (ByteBuffer buffer : buffers) {
                            totalLength += buffer.remaining();
                        }
                        
                        byte[] bytes = new byte[totalLength];
                        int offset = 0;
                        for (ByteBuffer buffer : buffers) {
                            int length = buffer.remaining();
                            buffer.get(bytes, offset, length);
                            offset += length;
                        }
                        
                        handler.onMessage(connection, bytes);
                    } catch (Exception e) {
                        logger.error("Error handling binary message on connection {}", connection.getId(), e);
                        handler.onError(connection, e);
                    } finally {
                        if (pooledData != null) {
                            pooledData.close();
                        }
                    }
                }
                
                @Override
                protected void onCloseMessage(io.undertow.websockets.core.CloseMessage cm, WebSocketChannel channel) {
                    CloseReason closeReason = new CloseReason(cm.getCode(), cm.getReason());
                    handleClose(connection, closeReason, handler);
                }
                
                @Override
                protected void onError(WebSocketChannel channel, Throwable error) {
                    logger.error("WebSocket error on connection {}", connection.getId(), error);
                    handler.onError(connection, error);
                }
            });
            
            channel.resumeReceives();
            
            // Add close listener
            channel.addCloseTask(ch -> {
                if (!connection.getAttribute("close_handled", Boolean.FALSE)) {
                    CloseReason closeReason = new CloseReason(CloseReason.ABNORMAL_CLOSURE, "Connection closed unexpectedly");
                    handleClose(connection, closeReason, handler);
                }
            });
            
            // Notify handler of new connection
            handler.onOpen(connection);
            
        } catch (Exception e) {
            logger.error("Error establishing WebSocket connection on path {}", path, e);
            try {
                WebSockets.sendClose(CloseReason.INTERNAL_SERVER_ERROR, "Server error", channel, null);
            } catch (Exception closeError) {
                logger.error("Error sending close message", closeError);
            }
        }
    }
    
    private void handleClose(WebSocketConnection connection, CloseReason closeReason, WebSocketEndpointHandler handler) {
        if (connection.getAttribute("close_handled", Boolean.FALSE)) {
            return; // Already handled
        }
        
        connection.setAttribute("close_handled", true);
        
        // Remove from tracking collections
        connectionsById.remove(connection.getId());
        connectionsByPath.values().forEach(connections -> connections.remove(connection));
        
        logger.debug("WebSocket connection closed: {} - {}", connection.getId(), closeReason);
        
        try {
            handler.onClose(connection, closeReason);
        } catch (Exception e) {
            logger.error("Error handling connection close for {}", connection.getId(), e);
        }
    }
    
    private SecurityContext extractSecurityContext(WebSocketHttpExchange exchange) {
        // TODO: Extract security context from exchange if JWT authentication is enabled
        // This would integrate with the JWT security framework
        return null;
    }
    
    @Override
    public Set<WebSocketConnection> getActiveConnections() {
        return connectionsById.values().stream()
                .filter(WebSocketConnection::isOpen)
                .collect(Collectors.toSet());
    }
    
    @Override
    public Set<WebSocketConnection> getConnectionsForPath(String path) {
        return connectionsByPath.getOrDefault(path, Set.of()).stream()
                .filter(WebSocketConnection::isOpen)
                .collect(Collectors.toSet());
    }
    
    @Override
    public WebSocketConnection getConnection(String connectionId) {
        WebSocketConnection connection = connectionsById.get(connectionId);
        return (connection != null && connection.isOpen()) ? connection : null;
    }
    
    @Override
    public CompletableFuture<Void> broadcastText(String message) {
        return broadcastToConnections(getActiveConnections(), conn -> conn.sendText(message));
    }
    
    @Override
    public CompletableFuture<Void> broadcastText(String path, String message) {
        return broadcastToConnections(getConnectionsForPath(path), conn -> conn.sendText(message));
    }
    
    @Override
    public CompletableFuture<Void> broadcastJson(Object object) {
        return broadcastToConnections(getActiveConnections(), conn -> conn.sendJson(object));
    }
    
    @Override
    public CompletableFuture<Void> broadcastJson(String path, Object object) {
        return broadcastToConnections(getConnectionsForPath(path), conn -> conn.sendJson(object));
    }
    
    @Override
    public CompletableFuture<Void> broadcastBinary(byte[] data) {
        return broadcastToConnections(getActiveConnections(), conn -> conn.sendBinary(data));
    }
    
    @Override
    public CompletableFuture<Void> broadcastBinary(String path, byte[] data) {
        return broadcastToConnections(getConnectionsForPath(path), conn -> conn.sendBinary(data));
    }
    
    private CompletableFuture<Void> broadcastToConnections(Set<WebSocketConnection> connections, 
                                                          java.util.function.Function<WebSocketConnection, CompletableFuture<Void>> sender) {
        if (connections.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }
        
        CompletableFuture<?>[] futures = connections.stream()
                .map(sender)
                .toArray(CompletableFuture[]::new);
        
        return CompletableFuture.allOf(futures);
    }
    
    @Override
    public int getConnectionCount() {
        return (int) connectionsById.values().stream()
                .filter(WebSocketConnection::isOpen)
                .count();
    }
    
    @Override
    public int getConnectionCount(String path) {
        return connectionsByPath.getOrDefault(path, Set.of()).size();
    }
    
    @Override
    public CompletableFuture<Void> shutdown() {
        logger.info("Shutting down WebSocket service...");
        
        // Close all active connections
        CompletableFuture<?>[] closeFutures = getActiveConnections().stream()
                .map(conn -> conn.close(CloseReason.SERVICE_RESTART, "Server shutdown"))
                .toArray(CompletableFuture[]::new);
        
        return CompletableFuture.allOf(closeFutures)
                .thenRun(() -> {
                    connectionsById.clear();
                    connectionsByPath.clear();
                    endpointHandlers.clear();
                    logger.info("WebSocket service shutdown complete");
                });
    }
}
