package io.sinistral.proteus.openapi.models.responses;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

/**
 * Deserializes an {@link ApiResponses} container, routing {@code x-} keys to extensions.
 */
public final class ApiResponsesDeserializer extends StdDeserializer<ApiResponses> {
    /** Creates the deserializer for the {@link ApiResponses} type. */
    public ApiResponsesDeserializer() {
        super(ApiResponses.class);
    }

    /**
     * Reads status code entries into responses and {@code x-} keys into extensions.
     *
     * @param parser the source parser
     * @param context the deserialization context
     * @return the populated container
     * @throws JacksonException when reading fails
     */
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
