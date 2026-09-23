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

    /** Returns a copy with the blocking flag set.
     *
     * @param blocking run the consumer on a blocking executor
     * @return a copy with the flag applied
     */
    public EventBusConsumerOptions withBlocking(boolean blocking) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeout);
    }

    /** Returns a copy with the ordered flag set.
     *
     * @param ordered dispatch messages serially in arrival order
     * @return a copy with the flag applied
     */
    public EventBusConsumerOptions withOrdered(boolean ordered) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeout);
    }

    /** Returns a copy with the local flag set.
     *
     * @param local deliver only messages published within this process
     * @return a copy with the flag applied
     */
    public EventBusConsumerOptions withLocal(boolean local) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeout);
    }

    /** Returns a copy with the codec name set.
     *
     * @param codec empty for the default JSON codec, otherwise a registered codec name
     * @return a copy with the codec applied
     */
    public EventBusConsumerOptions withCodec(String codec) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeout);
    }

    /** Returns a copy with the request timeout set.
     *
     * @param timeoutMs request-reply timeout in milliseconds
     * @return a copy with the timeout applied
     */
    public EventBusConsumerOptions withTimeout(long timeoutMs) {
        return new EventBusConsumerOptions(blocking, ordered, local, codec, timeoutMs);
    }
}
