package io.sinistral.proteus.messaging;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import tools.jackson.databind.ObjectMapper;

/**
 * Default event bus codec: JSON serialization with the application Jackson 3 mapper.
 *
 * <p>Wire format is a 4-byte length prefix, a 4-byte type-name length, the type name
 * bytes, and the JSON body. {@link #transform} returns the payload unchanged so local
 * delivery passes objects by reference without encoding, per the specification.
 *
 * @author jbauer
 */
public class JsonMessageCodec implements MessageCodec<Object, Object> {

    /** Codec name registered with the Vert.x event bus. */
    public static final String CODEC_NAME = "proteus-json";

    /** Shared Jackson mapper for wire encoding. */
    private final ObjectMapper objectMapper;

    /** Creates the codec.
     *
     * @param objectMapper shared Jackson mapper
     */
    public JsonMessageCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Sets the encode to wire, fluent style.
     *
     * @param buffer the encode to wire
     * @param object the encode to wire
     */
    @Override
    public void encodeToWire(Buffer buffer, Object object) {
        byte[] json = objectMapper.writeValueAsBytes(object);
        byte[] type = object.getClass().getName().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        buffer.appendInt(type.length).appendBytes(type).appendBytes(json);
    }

    /**
     * Sets the decode from wire, fluent style.
     *
     * @param pos the decode from wire
     * @param buffer the decode from wire
     * @return this instance
     */
    @Override
    public Object decodeFromWire(int pos, Buffer buffer) {
        int typeLength = buffer.getInt(pos);
        pos += 4;
        String typeName = buffer.getString(pos, pos + typeLength, "UTF-8");
        pos += typeLength;
        try {
            Class<?> type = Class.forName(typeName);
            return objectMapper.readValue(buffer.getBytes(pos, buffer.length()), type);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unknown wire payload type: " + typeName, e);
        }
    }

    /**
     * Sets the transform, fluent style.
     *
     * @param object the transform
     * @return this instance
     */
    @Override
    public Object transform(Object object) {
        // Local delivery stays by reference; no encode/decode round trip.
        return object;
    }

    /**
     * Returns the name.
     *
     * @return the name, or null when unset
     */
    @Override
    public String name() {
        return CODEC_NAME;
    }

    /**
     * Returns the system codec id.
     *
     * @return the system codec id, or null when unset
     */
    @Override
    public byte systemCodecID() {
        return -1;
    }
}
