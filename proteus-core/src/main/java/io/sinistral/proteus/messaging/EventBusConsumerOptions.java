package io.sinistral.proteus.messaging;

/**
 * Immutable delivery options for an event bus consumer registration.
 *
 * @param blocking run the consumer on a dedicated blocking executor rather than the event loop
 * @param ordered dispatch messages serially in arrival order
 * @param local deliver only messages published within this process
 * @param codec empty selects the default JSON codec; non-empty selects a registered named codec
 * @param timeout timeout in milliseconds applied to request-reply initiated by the consumer
 */
public record EventBusConsumerOptions(
    boolean blocking, boolean ordered, boolean local, String codec, long timeout
) {
    /** Defaults: non-blocking, unordered, local, default codec, 30 second timeout. */
    public EventBusConsumerOptions() {
        this(false, false, true, "", 30_000L);
    }

    /** @return a copy with the blocking flag set */
    public EventBusConsumerOptions withBlocking(boolean blocking) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeout);
    }

    /** @return a copy with the ordered flag set */
    public EventBusConsumerOptions withOrdered(boolean ordered) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeout);
    }

    /** @return a copy with the local flag set */
    public EventBusConsumerOptions withLocal(boolean local) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeout);
    }

    /** @return a copy with the codec name set */
    public EventBusConsumerOptions withCodec(String codec) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeout);
    }

    /** @return a copy with the request timeout set */
    public EventBusConsumerOptions withTimeout(long timeoutMs) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeoutMs);
    }
}
