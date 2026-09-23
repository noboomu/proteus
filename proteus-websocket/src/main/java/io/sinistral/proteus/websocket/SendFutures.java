package io.sinistral.proteus.websocket;

import io.undertow.websockets.core.WebSocketCallback;
import io.undertow.websockets.core.WebSocketChannel;
import io.undertow.websockets.core.WebSockets;

import java.nio.ByteBuffer;

/**
 * Future adapters over the Undertow WebSocket send primitives.
 *
 * @author jbauer
 */
final class SendFutures {

    /** Utility class; no instances. */
    private SendFutures() {
    }

    /** Sends a text frame, completing the future when the send finishes.
     *
     * @param message the text payload
     * @param channel the target channel
     * @return a future completing when the send finishes
     */
    static java.util.concurrent.CompletableFuture<Void> sendText(
            String message, WebSocketChannel channel) {
        java.util.concurrent.CompletableFuture<Void> future = new java.util.concurrent.CompletableFuture<>();
        WebSockets.sendText(message, channel, callback(future));
        return future;
    }

    /** Sends a binary frame, completing the future when the send finishes.
     *
     * @param data the binary payload
     * @param channel the target channel
     * @return a future completing when the send finishes
     */
    static java.util.concurrent.CompletableFuture<Void> sendBinary(
            ByteBuffer data, WebSocketChannel channel) {
        java.util.concurrent.CompletableFuture<Void> future = new java.util.concurrent.CompletableFuture<>();
        WebSockets.sendBinary(data, channel, callback(future));
        return future;
    }

    /** Sends a close frame with a status code and reason.
     *
     * @param code the WebSocket close status code
     * @param reason the close reason text
     * @param channel the target channel
     */
    static void sendClose(int code, String reason, WebSocketChannel channel) {
        WebSockets.sendClose(code, reason, channel, new WebSocketCallback<Void>() {
            @Override
            public void complete(WebSocketChannel c, Void unused) {
                closeChannel(c);
            }

            @Override
            public void onError(WebSocketChannel c, Void unused, Throwable throwable) {
                closeChannel(c);
            }
        });
    }

    /** Closes the channel, tolerating an already-closed channel.
     *
     * @param channel the channel to close
     */
    private static void closeChannel(WebSocketChannel channel) {
        try {
            channel.close();
        } catch (java.io.IOException | RuntimeException ignored) {
            // channel already closed
        }
    }

    /** Adapts send completion into the future.
     *
     * @param future the future to complete
     * @return an Undertow callback bridging to the future
     */
    private static WebSocketCallback<Void> callback(java.util.concurrent.CompletableFuture<Void> future) {
        return new WebSocketCallback<Void>() {
            @Override
            public void complete(WebSocketChannel channel, Void unused) {
                future.complete(null);
            }

            @Override
            public void onError(WebSocketChannel channel, Void unused, Throwable throwable) {
                future.completeExceptionally(throwable);
            }
        };
    }
}
