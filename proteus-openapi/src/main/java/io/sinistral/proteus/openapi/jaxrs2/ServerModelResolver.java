package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.converter.AnnotatedType;
import io.sinistral.proteus.openapi.converter.ModelConverter;
import io.sinistral.proteus.openapi.converter.ModelConverterContext;
import io.sinistral.proteus.openapi.models.media.Schema;
import io.sinistral.proteus.server.ServerResponse;
import io.sinistral.proteus.server.ServerRequest;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;

import java.util.Iterator;
import java.util.concurrent.CompletionStage;

/**
 * Unwraps Proteus and asynchronous response containers before delegating model
 * introspection to the next converter.
 */
public final class ServerModelResolver implements ModelConverter {
    private final ObjectMapper mapper;

    public ServerModelResolver(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Schema resolve(
        AnnotatedType annotatedType,
        ModelConverterContext context,
        Iterator<ModelConverter> next
    ) {
        if (annotatedType == null || annotatedType.getType() == null) {
            return null;
        }

        JavaType resolved = mapper.constructType(annotatedType.getType());
        if (shouldIgnoreClass(resolved)) {
            return null;
        }

        while (resolved != null && resolved.getRawClass() != null) {
            Class<?> raw = resolved.getRawClass();
            if (ServerResponse.class.isAssignableFrom(raw)
                || CompletionStage.class.isAssignableFrom(raw)) {
                JavaType contained = resolved.containedType(0);
                if (contained == null) {
                    break;
                }
                resolved = contained;
                continue;
            }
            break;
        }

        if (resolved == null || resolved.getRawClass() == null
            || resolved.getRawClass() == Void.class || resolved.getRawClass() == void.class) {
            return null;
        }

        String typeName = resolved.toCanonical();
        if (typeName.contains("java.nio.file.Path") || typeName.contains("java.nio.ByteBuffer")) {
            if (resolved.getRawClass() == java.util.Optional.class) {
                resolved = mapper.getTypeFactory().constructParametricType(java.util.Optional.class, java.io.File.class);
            } else {
                resolved = mapper.getTypeFactory().constructType(java.io.File.class);
            }
        }

        annotatedType.setType(resolved);
        return next.hasNext()
            ? next.next().resolve(annotatedType, context, next)
            : null;
    }

    private boolean shouldIgnoreClass(JavaType type) {
        if (type == null || type.getRawClass() == null) {
            return true;
        }
        Class<?> raw = type.getRawClass();
        String name = raw.getName();
        return ServerRequest.class.equals(raw)
            || Void.class.equals(raw)
            || void.class.equals(raw)
            || name.startsWith("io.undertow.")
            || name.startsWith("org.xnio.");
    }
}
