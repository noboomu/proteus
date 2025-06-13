package io.sinistral.proteus.eventbus;

import io.vertx.core.buffer.Buffer;

/**
 * Custom codec for event bus message serialization/deserialization.
 * 
 * @param <T> the message type handled by this codec
 * @since 1.0
 */
public interface EventBusCodec<T> {
    
    /**
     * Encodes a message object to a buffer.
     * 
     * @param object the object to encode
     * @return the encoded buffer
     */
    Buffer encode(T object);
    
    /**
     * Decodes a buffer to a message object.
     * 
     * @param buffer the buffer to decode
     * @return the decoded object
     */
    T decode(Buffer buffer);
    
    /**
     * Gets the name of this codec.
     * 
     * @return the codec name
     */
    String name();
    
    /**
     * Gets the system codec ID for this codec.
     * This should be unique across all codecs in the system.
     * 
     * @return the system codec ID
     */
    byte systemCodecID();
    
    /**
     * Gets the type handled by this codec.
     * 
     * @return the message type class
     */
    Class<T> getType();
}
