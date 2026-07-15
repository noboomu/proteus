/**
 *
 */
package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.converter.AnnotatedType;
import io.sinistral.proteus.openapi.converter.ModelConverter;
import io.sinistral.proteus.openapi.converter.ModelConverterContext;
import io.sinistral.proteus.openapi.models.media.Schema;
import io.sinistral.proteus.server.ServerResponse;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.introspect.Annotated;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author jbauer
 * Custom model resolver for Proteus server types
 */
public class ServerModelResolver implements ModelConverter {

    protected final ObjectMapper mapper;

    private static org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(
        ServerModelResolver.class.getCanonicalName()
    );

    public ServerModelResolver(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /*
     * (non-Javadoc)
     * @see io.swagger.v3.core.jackson.ModelResolver#resolve(io.swagger.v3.core.
     * converter.AnnotatedType,
     * io.swagger.v3.core.converter.ModelConverterContext, java.util.Iterator)
     */
    @Override
    public Schema resolve(
        AnnotatedType annotatedType,
        ModelConverterContext context,
        Iterator<ModelConverter> next
    ) {
        JavaType classType = mapper.getTypeFactory().constructType(
            annotatedType.getType()
        );
        Class<?> rawClass = classType.getRawClass();
        JavaType resolvedType = classType;

        if ((rawClass != null) && !resolvedType.isPrimitive()) {
            if (rawClass.isAssignableFrom(ServerResponse.class)) {
                resolvedType = classType.containedType(0);
            } else if (rawClass.isAssignableFrom(CompletableFuture.class)) {
                Class<?> futureCls = classType.containedType(0).getRawClass();

                if (futureCls.isAssignableFrom(ServerResponse.class)) {
                    final JavaType futureType =
                        mapper.getTypeFactory().constructType(
                            classType.containedType(0)
                        );

                    resolvedType = futureType.containedType(0);
                } else {
                    resolvedType = classType.containedType(0);
                }
            }

            if (resolvedType != null) {
                if (resolvedType.getTypeName().contains("java.lang.Void")) {
                    resolvedType =
                        mapper.getTypeFactory().constructFromCanonical(
                            Void.class.getName()
                        );
                } else if (resolvedType.getTypeName().contains("Optional")) {
                    if (
                        resolvedType
                            .getTypeName()
                            .contains("java.nio.file.Path")
                    ) {
                        resolvedType =
                            mapper.getTypeFactory().constructParametricType(
                                Optional.class,
                                File.class
                            );
                    }

                    if (resolvedType.getTypeName().contains("ByteBuffer")) {
                        resolvedType =
                            mapper.getTypeFactory().constructParametricType(
                                Optional.class,
                                File.class
                            );
                    }
                } else {
                    if (
                        resolvedType
                            .getTypeName()
                            .contains("java.nio.file.Path")
                    ) {
                        resolvedType =
                            mapper.getTypeFactory().constructFromCanonical(
                                File.class.getName()
                            );
                    }

                    if (resolvedType.getTypeName().contains("ByteBuffer")) {
                        resolvedType =
                            mapper.getTypeFactory().constructFromCanonical(
                                File.class.getName()
                            );
                    }
                }

                annotatedType.setType(resolvedType);
            }
        }

        try {
            // Delegate to the next converter in the chain
            if (next.hasNext()) {
                return next.next().resolve(annotatedType, context, next);
            }
            return null;
        } catch (Exception e) {
            log.error(
                "Error processing " +
                    annotatedType +
                    " " +
                    classType +
                    " " +
                    annotatedType.getName(),
                e
            );

            return null;
        }
    }

    /**
     * Check if a type should be ignored during resolution
     */
    protected boolean shouldIgnoreClass(Type type) {
        JavaType classType = mapper.getTypeFactory().constructType(type);
        String canonicalName = classType.toCanonical();

        if (
            canonicalName.startsWith("io.undertow") ||
            canonicalName.startsWith("org.xnio") ||
            canonicalName.equals("io.sinistral.proteus.server.ServerRequest") ||
            canonicalName.contains(Void.class.getName())
        ) {
            return true;
        }

        return false;
    }
}
