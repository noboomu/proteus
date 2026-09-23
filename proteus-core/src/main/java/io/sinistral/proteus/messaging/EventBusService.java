package io.sinistral.proteus.messaging;

import java.util.concurrent.CompletableFuture;

/**
 * In-process typed event bus facade over the embedded Vert.x event bus.
 *
 * @author jbauer
 */
public interface EventBusService {
    /** Fire-and-forget delivery to every consumer on the address. */
    <T> void publish(String address, T message);

    /** Point-to-point delivery to exactly one consumer on the address. */
    <T> void send(String address, T message);

    /** Request-reply with the default request timeout. */
    <T, R> CompletableFuture<R> request(String address, T message, Class<R> responseType);

    /**
     * Request-reply with an explicit timeout.
     *
     * @param timeoutMs reply wait in milliseconds
     */
    <T, R> CompletableFuture<R> request(
        String address, T message, Class<R> responseType, long timeoutMs);

    /** Registers a consumer with default options; replaces any existing registration on the address. */
    <T> EventBusRegistration registerConsumer(String address, EventBusConsumer<T> consumer);

    /** Registers a consumer with explicit options; replaces any existing registration on the address. */
    <T> EventBusRegistration registerConsumer(
        String address, EventBusConsumer<T> consumer, EventBusConsumerOptions options);

    /** Registers a named codec, overriding the default JSON codec for consumers that select it. */
    <T> void registerCodec(String codecName, EventBusCodec<T> codec);

    /** @return true when the underlying Vert.x platform is running */
    boolean isAvailable();

    /** @return a point-in-time metrics snapshot */
    EventBusMetrics getMetrics();
}
