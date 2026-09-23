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

    private SendFutures() {
    }

    /** Sends a text frame, completing the future when the send finishes. */
    static java.util.concurrent.CompletableFuture<Void> sendText(
            String message, WebSocketChannel channel) {
        java.util.concurrent.CompletableFuture<Void> future = new java.util.concurrent.CompletableFuture<>();
        WebSockets.sendText(message, channel, callback(future));
        return future;
    }

    /** Sends a binary frame, completing the future when the send finishes. */
    static java.util.concurrent.CompletableFuture<Void> sendBinary(
            ByteBuffer data, WebSocketChannel channel) {
        java.util.concurrent.CompletableFuture<Void> future = new java.util.concurrent.CompletableFuture<>();
        WebSockets.sendBinary(data, channel, callback(future));
        return future;
    }

    /** Sends a close frame with a status code and reason. */
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

    private static void closeChannel(WebSocketChannel channel) {
        try {
            channel.close();
        } catch (java.io.IOException | RuntimeException ignored) {
            // channel already closed
        }
    }

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
