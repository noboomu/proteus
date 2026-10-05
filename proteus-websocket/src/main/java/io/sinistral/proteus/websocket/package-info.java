/**
 * Annotation-driven Undertow WebSocket endpoints.
 *
 * <p>Classes annotated with {@link io.sinistral.proteus.websocket.WebSocket} declare
 * {@code @OnOpen}, {@code @OnMessage}, {@code @OnClose}, and {@code @OnError} methods;
 * {@link io.sinistral.proteus.websocket.WebSocketService} tracks live connections and
 * supports path-scoped broadcast.
 */
package io.sinistral.proteus.websocket;
