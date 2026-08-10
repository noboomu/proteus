package io.sinistral.proteus.openapi.models;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

import java.util.Map;

public final class PathsSerializer extends StdSerializer<Paths> {
    public PathsSerializer() {
        super(Paths.class);
    }

    @Override
    public void serialize(
        Paths paths,
        JsonGenerator generator,
        SerializationContext context
    ) throws JacksonException {
        generator.writeStartObject(paths);
        for (Map.Entry<String, PathItem> entry : paths.entrySet()) {
            context.defaultSerializeProperty(entry.getKey(), entry.getValue(), generator);
        }
        if (paths.getExtensions() != null) {
            for (Map.Entry<String, Object> extension : paths.getExtensions().entrySet()) {
                context.defaultSerializeProperty(extension.getKey(), extension.getValue(), generator);
            }
        }
        generator.writeEndObject();
    }
}
