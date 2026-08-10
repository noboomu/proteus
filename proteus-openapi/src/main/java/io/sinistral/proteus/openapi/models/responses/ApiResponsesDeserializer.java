package io.sinistral.proteus.openapi.models.responses;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

public final class ApiResponsesDeserializer extends StdDeserializer<ApiResponses> {
    public ApiResponsesDeserializer() {
        super(ApiResponses.class);
    }

    @Override
    public ApiResponses deserialize(
        JsonParser parser,
        DeserializationContext context
    ) throws JacksonException {
        JsonNode root = context.readTree(parser);
        ApiResponses responses = new ApiResponses();
        if (root == null || !root.isObject()) return responses;

        for (var entry : root.properties()) {
            String name = entry.getKey();
            JsonNode value = entry.getValue();
            if (name.startsWith("x-")) {
                responses.addExtension(name, readValue(value, Object.class, context));
            } else {
                responses.addApiResponse(name, readValue(value, ApiResponse.class, context));
            }
        }
        return responses;
    }

    private <T> T readValue(
        JsonNode node,
        Class<T> type,
        DeserializationContext context
    ) throws JacksonException {
        JsonParser parser = node.traverse(context);
        parser.nextToken();
        return context.readValue(parser, type);
    }
}
