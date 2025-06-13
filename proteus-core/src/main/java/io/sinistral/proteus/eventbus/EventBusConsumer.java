package io.sinistral.proteus.eventbus;

import io.vertx.core.eventbus.Message;
import java.util.concurrent.CompletableFuture;

/**
 * Functional interface for event bus message consumers.
 * 
 * @param <T> the message body type
 * @since 1.0
 */
@FunctionalInterface
public interface EventBusConsumer<T> {
    
    /**
     * Processes an incoming message.
     * 
     * @param message the message to process
     * @return a CompletableFuture for the response, or null for fire-and-forget consumers
     */
    CompletableFuture<?> handle(Message<T> message);
}
