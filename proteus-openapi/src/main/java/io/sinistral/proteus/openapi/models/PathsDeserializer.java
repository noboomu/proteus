package io.sinistral.proteus.openapi.models;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

public final class PathsDeserializer extends StdDeserializer<Paths> {
    public PathsDeserializer() {
        super(Paths.class);
    }

    @Override
    public Paths deserialize(
        JsonParser parser,
        DeserializationContext context
    ) throws JacksonException {
        JsonNode root = context.readTree(parser);
        Paths paths = new Paths();
        if (root == null || !root.isObject()) return paths;

        for (var entry : root.properties()) {
            String name = entry.getKey();
            JsonNode value = entry.getValue();
            if (name.startsWith("x-")) {
                paths.addExtension(name, readValue(value, Object.class, context));
            } else {
                paths.addPathItem(name, readValue(value, PathItem.class, context));
            }
        }
        return paths;
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
