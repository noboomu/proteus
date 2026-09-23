package io.sinistral.proteus.messaging.nats;

import java.time.Instant;

import tools.jackson.databind.ObjectMapper;

/**
 * Wire envelope for every bridged NATS message.
 *
 * @param address originating event bus address
 * @param contentType always application/json for the default codec
 * @param sentAt epoch milliseconds at the sender
 * @param payload the JSON-encoded message body
 */
public record NatsEnvelope(String address, String contentType, long sentAt, Object payload) {

    /** Content type carried by every default-codec envelope. */
    public static final String CONTENT_TYPE_JSON = "application/json";

    /** Creates an envelope stamped with the current epoch milliseconds.
     *
     * @param address the event bus address
     * @param payload the payload to carry
     * @return the stamped envelope
     */
    public static NatsEnvelope of(String address, Object payload) {
        return new NatsEnvelope(
            address, CONTENT_TYPE_JSON, Instant.now().toEpochMilli(), payload);
    }

    /** Serializes this envelope to a JSON string.
     *
     * @param objectMapper shared Jackson mapper
     * @return the JSON representation
     */
    public String toJson(ObjectMapper objectMapper) {
        return objectMapper.writeValueAsString(this);
    }

    /** Parses an envelope from its JSON representation.
     *
     * @param json the JSON produced by {@link #toJson(ObjectMapper)}
     * @param objectMapper shared Jackson mapper
     * @return the decoded envelope
     */
    public static NatsEnvelope fromJson(String json, ObjectMapper objectMapper) {
        return objectMapper.readValue(json, NatsEnvelope.class);
    }
}
