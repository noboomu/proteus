package io.sinistral.proteus.openapi.models.responses;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

import java.util.Map;

/**
 * Serializes an {@link ApiResponses} container including extension properties.
 */
public final class ApiResponsesSerializer extends StdSerializer<ApiResponses> {
    /** Creates the serializer for the {@link ApiResponses} type. */
    public ApiResponsesSerializer() {
        super(ApiResponses.class);
    }

    /**
     * Writes the responses map followed by extension properties.
     *
     * @param responses the container to serialize
     * @param generator the target generator
     * @param context the serialization context
     * @throws JacksonException when writing fails
     */
    @Override
    public void serialize(
        ApiResponses responses,
        JsonGenerator generator,
        SerializationContext context
    ) throws JacksonException {
        generator.writeStartObject(responses);
        for (Map.Entry<String, ApiResponse> entry : responses.entrySet()) {
            context.defaultSerializeProperty(entry.getKey(), entry.getValue(), generator);
        }
        if (responses.getExtensions() != null) {
            for (Map.Entry<String, Object> extension : responses.getExtensions().entrySet()) {
                context.defaultSerializeProperty(extension.getKey(), extension.getValue(), generator);
            }
        }
        generator.writeEndObject();
    }
}
