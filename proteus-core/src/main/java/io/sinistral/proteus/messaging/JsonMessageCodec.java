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

    private final ObjectMapper objectMapper;

    public JsonMessageCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void encodeToWire(Buffer buffer, Object object) {
        byte[] json = objectMapper.writeValueAsBytes(object);
        byte[] type = object.getClass().getName().getBytes(java.nio.charset.StandardCharsets.UTF_8);
        buffer.appendInt(type.length).appendBytes(type).appendBytes(json);
    }

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

    @Override
    public Object transform(Object object) {
        // Local delivery stays by reference; no encode/decode round trip.
        return object;
    }

    @Override
    public String name() {
        return CODEC_NAME;
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
