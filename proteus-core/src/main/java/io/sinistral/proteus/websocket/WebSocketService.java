package io.sinistral.proteus.websocket;

import io.undertow.server.RoutingHandler;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Service interface for WebSocket functionality.
 * 
 * This service manages WebSocket endpoints, connections, and provides
 * broadcasting capabilities.
 * 
 * @since 1.0
 */
public interface WebSocketService {
    
    /**
     * Registers WebSocket handlers with the routing handler.
     * 
     * @param router the routing handler to register with
     */
    void registerEndpoints(RoutingHandler router);
    
    /**
     * Gets all active WebSocket connections.
     * 
     * @return set of active connections
     */
    Set<WebSocketConnection> getActiveConnections();
    
    /**
     * Gets active connections for a specific endpoint path.
     * 
     * @param path the endpoint path
     * @return set of active connections for the path
     */
    Set<WebSocketConnection> getConnectionsForPath(String path);
    
    /**
     * Gets a connection by its ID.
     * 
     * @param connectionId the connection ID
     * @return the connection, or null if not found
     */
    WebSocketConnection getConnection(String connectionId);
    
    /**
     * Broadcasts a text message to all active connections.
     * 
     * @param message the message to broadcast
     * @return CompletableFuture that completes when broadcast is done
     */
    CompletableFuture<Void> broadcastText(String message);
    
    /**
     * Broadcasts a text message to connections on a specific path.
     * 
     * @param path the endpoint path
     * @param message the message to broadcast
     * @return CompletableFuture that completes when broadcast is done
     */
    CompletableFuture<Void> broadcastText(String path, String message);
    
    /**
     * Broadcasts an object as JSON to all active connections.
     * 
     * @param object the object to broadcast
     * @return CompletableFuture that completes when broadcast is done
     */
    CompletableFuture<Void> broadcastJson(Object object);
    
    /**
     * Broadcasts an object as JSON to connections on a specific path.
     * 
     * @param path the endpoint path
     * @param object the object to broadcast
     * @return CompletableFuture that completes when broadcast is done
     */
    CompletableFuture<Void> broadcastJson(String path, Object object);
    
    /**
     * Broadcasts a binary message to all active connections.
     * 
     * @param data the binary data to broadcast
     * @return CompletableFuture that completes when broadcast is done
     */
    CompletableFuture<Void> broadcastBinary(byte[] data);
    
    /**
     * Broadcasts a binary message to connections on a specific path.
     * 
     * @param path the endpoint path
     * @param data the binary data to broadcast
     * @return CompletableFuture that completes when broadcast is done
     */
    CompletableFuture<Void> broadcastBinary(String path, byte[] data);
    
    /**
     * Gets the total number of active connections.
     * 
     * @return number of active connections
     */
    int getConnectionCount();
    
    /**
     * Gets the number of active connections for a specific path.
     * 
     * @param path the endpoint path
     * @return number of active connections for the path
     */
    int getConnectionCount(String path);
    
    /**
     * Closes all connections and shuts down the service.
     * 
     * @return CompletableFuture that completes when shutdown is done
     */
    CompletableFuture<Void> shutdown();
}
