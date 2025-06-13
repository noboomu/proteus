package io.sinistral.proteus.eventbus;

import io.vertx.core.eventbus.Message;
import java.util.concurrent.CompletableFuture;

/**
 * Event bus service for reactive messaging in Proteus applications.
 * 
 * This service provides a simplified interface over Vert.x Event Bus,
 * integrating with Proteus's virtual thread support and security context.
 * 
 * <p>The event bus supports several messaging patterns:
 * <ul>
 *   <li><strong>Publish/Subscribe</strong> - One-to-many messaging</li>
 *   <li><strong>Point-to-Point</strong> - One-to-one messaging</li>
 *   <li><strong>Request/Reply</strong> - Synchronous messaging with response</li>
 * </ul>
 * 
 * @since 1.0
 */
public interface EventBusService {
    
    /**
     * Publishes a message to all consumers registered for the given address.
     * This is a fire-and-forget operation (publish/subscribe pattern).
     * 
     * @param address the event bus address
     * @param message the message body
     * @param <T> the message type
     */
    <T> void publish(String address, T message);
    
    /**
     * Sends a message to exactly one consumer registered for the given address.
     * This is a point-to-point messaging pattern.
     * 
     * @param address the event bus address
     * @param message the message body
     * @param <T> the message type
     */
    <T> void send(String address, T message);
    
    /**
     * Sends a message and waits for a reply asynchronously.
     * This implements the request/reply messaging pattern.
     * 
     * @param address the event bus address
     * @param message the message body
     * @param responseType the expected response type
     * @param <T> the message type
     * @param <R> the response type
     * @return a CompletableFuture containing the response
     */
    <T, R> CompletableFuture<R> request(String address, T message, Class<R> responseType);
    
    /**
     * Sends a message and waits for a reply with a timeout.
     * 
     * @param address the event bus address
     * @param message the message body
     * @param responseType the expected response type
     * @param timeoutMs timeout in milliseconds
     * @param <T> the message type
     * @param <R> the response type
     * @return a CompletableFuture containing the response
     */
    <T, R> CompletableFuture<R> request(String address, T message, Class<R> responseType, long timeoutMs);
    
    /**
     * Registers a consumer for the given address.
     * This is typically called automatically for methods annotated with @ConsumeEvent.
     * 
     * @param address the event bus address
     * @param consumer the message consumer
     * @param <T> the message type
     * @return a registration that can be used to unregister the consumer
     */
    <T> EventBusRegistration registerConsumer(String address, EventBusConsumer<T> consumer);
    
    /**
     * Registers a consumer with specific options.
     * 
     * @param address the event bus address
     * @param consumer the message consumer
     * @param options consumer options (blocking, ordered, etc.)
     * @param <T> the message type
     * @return a registration that can be used to unregister the consumer
     */
    <T> EventBusRegistration registerConsumer(String address, EventBusConsumer<T> consumer, EventBusConsumerOptions options);
    
    /**
     * Registers a codec for custom message serialization.
     * 
     * @param codecName the name of the codec
     * @param codec the codec implementation
     * @param <T> the message type handled by the codec
     */
    <T> void registerCodec(String codecName, EventBusCodec<T> codec);
    
    /**
     * Checks if the event bus is currently available.
     * 
     * @return true if the event bus is available, false otherwise
     */
    boolean isAvailable();
    
    /**
     * Gets metrics about the event bus performance.
     * 
     * @return event bus metrics
     */
    EventBusMetrics getMetrics();
}
