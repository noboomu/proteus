package io.sinistral.proteus.messaging;

import io.vertx.core.eventbus.Message;

import java.util.concurrent.CompletableFuture;

/**
 * A handler for a single event bus address.
 *
 * @param <T> the payload type the consumer accepts
 */
@FunctionalInterface
public interface EventBusConsumer<T> {
    /**
     * Processes one message. The returned future completes when processing of this message is
     * finished; a failed future increments the error metric and logs the cause at warn level.
     *
     * @param message the delivered message
     * @return a future completed when processing finishes
     */
    CompletableFuture<?> handle(Message<T> message);
}
