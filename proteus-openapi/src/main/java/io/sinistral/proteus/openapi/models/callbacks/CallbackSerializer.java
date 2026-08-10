package io.sinistral.proteus.openapi.models.callbacks;

import io.sinistral.proteus.openapi.models.PathItem;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ser.std.StdSerializer;

import java.util.Map;

public final class CallbackSerializer extends StdSerializer<Callback> {
    public CallbackSerializer() {
        super(Callback.class);
    }

    @Override
    public void serialize(
        Callback callback,
        JsonGenerator generator,
        SerializationContext context
    ) throws JacksonException {
        generator.writeStartObject(callback);
        if (callback.get$ref() != null) {
            generator.writeStringProperty("$ref", callback.get$ref());
        }
        for (Map.Entry<String, PathItem> entry : callback.entrySet()) {
            context.defaultSerializeProperty(entry.getKey(), entry.getValue(), generator);
        }
        if (callback.getExtensions() != null) {
            for (Map.Entry<String, Object> extension : callback.getExtensions().entrySet()) {
                context.defaultSerializeProperty(extension.getKey(), extension.getValue(), generator);
            }
        }
        generator.writeEndObject();
    }
}
