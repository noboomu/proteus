package io.sinistral.proteus.openapi.util;

import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.media.Schema;
import io.sinistral.proteus.openapi.models.parameters.Parameter;
import com.fasterxml.jackson.annotation.JsonView;
import tools.jackson.databind.JavaType;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Utility for processing parameter annotations
 */
public class ParameterProcessor {

    /**
     * Apply annotations to a parameter
     *
     * @param parameter the parameter to process (may be null)
     * @param type the parameter type
     * @param annotations annotations on the parameter
     * @param components OpenAPI components
     * @param classConsumes class-level consumes
     * @param methodConsumes method-level consumes
     * @param jsonViewAnnotation JsonView annotation if present
     * @return processed parameter
     */
    public static Parameter applyAnnotations(
        Parameter parameter,
        Type type,
        List<Annotation> annotations,
        Components components,
        String[] classConsumes,
        String[] methodConsumes,
        JsonView jsonViewAnnotation
    ) {
        if (parameter == null) {
            parameter = new Parameter();
        }

        // Basic type resolution - create schema from type if not present
        if (parameter.getSchema() == null && type != null) {
            Schema schema = new Schema();
            JavaType javaType = Json.mapper().getTypeFactory().constructType(type);
            String typeName = javaType.getRawClass().getSimpleName();
            schema.setType(mapJavaTypeToOpenAPIType(typeName));
            parameter.setSchema(schema);
        }

        return parameter;
    }

    /**
     * Apply annotations to a parameter (array version)
     */
    public static Parameter applyAnnotations(
        Parameter parameter,
        Type type,
        Annotation[] annotations,
        Components components,
        String[] classConsumes,
        String[] methodConsumes,
        JsonView jsonViewAnnotation
    ) {
        return applyAnnotations(parameter, type, annotations != null ? List.of(annotations) : List.of(),
            components, classConsumes, methodConsumes, jsonViewAnnotation);
    }

    /**
     * Get parameter type from annotation
     */
    public static Type getParameterType(io.swagger.v3.oas.annotations.Parameter parameterAnnotation) {
        return getParameterType(parameterAnnotation, false);
    }

    /**
     * Get parameter type from annotation with useSchema flag
     */
    public static Type getParameterType(io.swagger.v3.oas.annotations.Parameter parameterAnnotation, boolean useSchema) {
        // Extract type from Parameter annotation's schema
        if (useSchema && parameterAnnotation.schema() != null) {
            io.swagger.v3.oas.annotations.media.Schema schema = parameterAnnotation.schema();
            if (schema.implementation() != null && schema.implementation() != Void.class) {
                return schema.implementation();
            }
        }
        // Default to String.class if no type info available
        return String.class;
    }

    private static String mapJavaTypeToOpenAPIType(String javaType) {
        return switch (javaType.toLowerCase()) {
            case "string" -> "string";
            case "integer", "int", "long", "short", "byte" -> "integer";
            case "double", "float", "bigdecimal" -> "number";
            case "boolean" -> "boolean";
            case "list", "set", "collection" -> "array";
            default -> "object";
        };
    }
}
