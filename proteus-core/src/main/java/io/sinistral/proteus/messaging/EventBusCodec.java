package io.sinistral.proteus.messaging;

import io.vertx.core.buffer.Buffer;

/**
 * Payload codec for event bus messages.
 *
 * @param <T> the payload type
 */
public interface EventBusCodec<T> {
    /** Encodes a payload for the wire.
     *
     * @param object the payload instance to encode
     * @return the encoded buffer
     */
    Buffer encode(T object);

    /** Decodes a payload from the wire.
     *
     * @param buffer the wire representation produced by {@link #encode(Object)}
     * @return the decoded payload instance
     */
    T decode(Buffer buffer);
}
