package io.sinistral.proteus.messaging;

import java.util.concurrent.CompletableFuture;

/**
 * In-process typed event bus facade over the embedded Vert.x event bus.
 *
 * @author jbauer
 */
public interface EventBusService {
    /** Fire-and-forget delivery to every consumer on the address.
     *
     * @param address the event bus address
     * @param message the payload to deliver
     * @param <T> the payload type
     */
    <T> void publish(String address, T message);

    /** Point-to-point delivery to exactly one consumer on the address.
     *
     * @param address the event bus address
     * @param message the payload to deliver
     * @param <T> the payload type
     */
    <T> void send(String address, T message);

    /** Request-reply with the default request timeout.
     *
     * @param address the event bus address
     * @param message the request payload
     * @param responseType the expected reply type
     * @param <T> the request payload type
     * @param <R> the reply payload type
     * @return a future completed with the reply, or failed on timeout or consumer error
     */
    <T, R> CompletableFuture<R> request(String address, T message, Class<R> responseType);

    /**
     * Request-reply with an explicit timeout.
     *
     * @param address the event bus address
     * @param message the request payload
     * @param responseType the expected reply type
     * @param timeoutMs reply wait in milliseconds
     * @param <T> the request payload type
     * @param <R> the reply payload type
     * @return a future completed with the reply, or failed on timeout or consumer error
     */
    <T, R> CompletableFuture<R> request(
        String address, T message, Class<R> responseType, long timeoutMs);

    /** Registers a consumer with default options; replaces any existing registration on the address.
     *
     * @param address the event bus address
     * @param consumer the consumer callback
     * @param <T> the payload type the consumer accepts
     * @return a handle that can unregister the consumer
     */
    <T> EventBusRegistration registerConsumer(String address, EventBusConsumer<T> consumer);

    /** Registers a consumer with explicit options; replaces any existing registration on the address.
     *
     * @param address the event bus address
     * @param consumer the consumer callback
     * @param options delivery options for the registration
     * @param <T> the payload type the consumer accepts
     * @return a handle that can unregister the consumer
     */
    <T> EventBusRegistration registerConsumer(
        String address, EventBusConsumer<T> consumer, EventBusConsumerOptions options);

    /** Registers a named codec, overriding the default JSON codec for consumers that select it.
     *
     * @param codecName the name consumers select via {@link EventBusConsumerOptions#codec()}
     * @param codec the codec implementation
     * @param <T> the payload type the codec encodes
     */
    <T> void registerCodec(String codecName, EventBusCodec<T> codec);

    /** Returns true when the underlying Vert.x platform is running.
     *
     * @return true if the event bus is usable
     */
    boolean isAvailable();

    /** Returns a point-in-time metrics snapshot.
     *
     * @return immutable snapshot of counters
     */
    EventBusMetrics getMetrics();
}
