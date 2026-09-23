package io.sinistral.proteus.websocket;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Registry and broadcast service for live WebSocket connections.
 *
 * @author jbauer
 */
public interface WebSocketService {
    /** Adds upgrade handlers for every registered @WebSocket path to the router.
     *
     * @param router the Undertow routing handler
     */
    void registerEndpoints(io.undertow.server.RoutingHandler router);

    /** Returns all live connections across every path.
     *
     * @return live connections
     */
    Set<WebSocketConnection> getActiveConnections();

    /** Returns live connections upgraded on the path.
     *
     * @param path the endpoint path
     * @return live connections on the path
     */
    Set<WebSocketConnection> getConnectionsForPath(String path);

    /** Returns the connection with the id.
     *
     * @param connectionId the connection UUID
     * @return the connection, or null when absent
     */
    WebSocketConnection getConnection(String connectionId);

    /** Sends a text frame to every live connection.
     *
     * @param message the text payload
     * @return a future completing when all frames are queued
     */
    CompletableFuture<Void> broadcastText(String message);

    /** Sends a text frame to every live connection on the path.
     *
     * @param path the endpoint path
     * @param message the text payload
     * @return a future completing when all frames are queued
     */
    CompletableFuture<Void> broadcastText(String path, String message);

    /** Serializes with the application mapper and broadcasts a text frame to all connections.
     *
     * @param object the payload to serialize
     * @return a future completing when all frames are queued
     */
    CompletableFuture<Void> broadcastJson(Object object);

    /** Serializes with the application mapper and broadcasts to connections on the path.
     *
     * @param path the endpoint path
     * @param object the payload to serialize
     * @return a future completing when all frames are queued
     */
    CompletableFuture<Void> broadcastJson(String path, Object object);

    /** Sends a binary frame to every live connection.
     *
     * @param data the binary payload
     * @return a future completing when all frames are queued
     */
    CompletableFuture<Void> broadcastBinary(byte[] data);

    /** Sends a binary frame to every live connection on the path.
     *
     * @param path the endpoint path
     * @param data the binary payload
     * @return a future completing when all frames are queued
     */
    CompletableFuture<Void> broadcastBinary(String path, byte[] data);
}
