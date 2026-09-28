package io.sinistral.proteus.openapi.models.callbacks;

import io.sinistral.proteus.openapi.models.PathItem;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

/** Deserializes a {@link Callback}, routing {@code x-} keys to extensions. */
public final class CallbackDeserializer extends StdDeserializer<Callback> {
    /** Creates the deserializer for the {@link Callback} type. */
    public CallbackDeserializer() {
        super(Callback.class);
    }

    /**
     * Reads callback entries into the map and {@code x-} keys into extensions.
     *
     * @param parser the source parser
     * @param context the deserialization context
     * @return the populated callback
     * @throws JacksonException when reading fails
     */
    @Override
    public Callback deserialize(
        JsonParser parser,
        DeserializationContext context
    ) throws JacksonException {
        JsonNode root = context.readTree(parser);
        Callback callback = new Callback();
        if (root == null || !root.isObject()) return callback;

        for (var entry : root.properties()) {
            String name = entry.getKey();
            JsonNode value = entry.getValue();
            if ("$ref".equals(name)) {
                callback.set$ref(value.asString());
            } else if (name.startsWith("x-")) {
                callback.addExtension(name, readValue(value, Object.class, context));
            } else {
                callback.addPathItem(name, readValue(value, PathItem.class, context));
            }
        }
        return callback;
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
