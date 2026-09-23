package io.sinistral.proteus.websocket;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Registry and broadcast service for live WebSocket connections.
 *
 * @author jbauer
 */
public interface WebSocketService {
    /** Adds upgrade handlers for every registered @WebSocket path to the router. */
    void registerEndpoints(io.undertow.server.RoutingHandler router);

    /** @return all live connections across every path */
    Set<WebSocketConnection> getActiveConnections();

    /** @return live connections upgraded on the path */
    Set<WebSocketConnection> getConnectionsForPath(String path);

    /** @return the connection with the id, or null */
    WebSocketConnection getConnection(String connectionId);

    /** Sends a text frame to every live connection. */
    CompletableFuture<Void> broadcastText(String message);

    /** Sends a text frame to every live connection on the path. */
    CompletableFuture<Void> broadcastText(String path, String message);

    /** Serializes with the application mapper and broadcasts a text frame to all connections. */
    CompletableFuture<Void> broadcastJson(Object object);

    /** Serializes with the application mapper and broadcasts to connections on the path. */
    CompletableFuture<Void> broadcastJson(String path, Object object);

    /** Sends a binary frame to every live connection. */
    CompletableFuture<Void> broadcastBinary(byte[] data);

    /** Sends a binary frame to every live connection on the path. */
    CompletableFuture<Void> broadcastBinary(String path, byte[] data);
}
