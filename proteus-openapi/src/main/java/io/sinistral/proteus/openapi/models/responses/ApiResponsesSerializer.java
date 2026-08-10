package io.sinistral.proteus.openapi.models.responses;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

import java.util.Map;

public final class ApiResponsesSerializer extends StdSerializer<ApiResponses> {
    public ApiResponsesSerializer() {
        super(ApiResponses.class);
    }

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
