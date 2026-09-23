package io.sinistral.proteus.messaging;

import io.vertx.core.buffer.Buffer;

/**
 * Payload codec for event bus messages.
 *
 * @param <T> the payload type
 */
public interface EventBusCodec<T> {
    /** Encodes a payload for the wire. */
    Buffer encode(T object);

    /** Decodes a payload from the wire. */
    T decode(Buffer buffer);
}
