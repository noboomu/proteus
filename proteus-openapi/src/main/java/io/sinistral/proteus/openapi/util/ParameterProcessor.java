package io.sinistral.proteus.openapi.util;

import io.sinistral.proteus.openapi.converter.AnnotatedType;
import io.sinistral.proteus.openapi.converter.ModelConverters;
import io.sinistral.proteus.openapi.converter.ResolvedSchema;
import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.examples.Example;
import io.sinistral.proteus.openapi.models.media.Content;
import io.sinistral.proteus.openapi.models.media.Schema;
import io.sinistral.proteus.openapi.models.parameters.Parameter;
import com.fasterxml.jackson.annotation.JsonView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        for (Annotation candidate : annotations) {
            String annotationName = candidate.annotationType().getName();
            if (annotationName.equals("jakarta.ws.rs.core.Context")
                || annotationName.equals("javax.ws.rs.core.Context")) {
                return null;
            }
        }

        if (parameter == null) {
            parameter = new Parameter();
        }

        for (Annotation candidate : annotations) {
            String annotationName = candidate.annotationType().getName();
            if (annotationName.equals("jakarta.ws.rs.FormParam")
                || annotationName.equals("javax.ws.rs.FormParam")
                || annotationName.endsWith("FormDataParam")) {
                try {
                    String name = (String) candidate.annotationType().getMethod("value").invoke(candidate);
                    if (name != null && !name.isBlank()) {
                        parameter.setName(name);
                    }
                } catch (ReflectiveOperationException ignored) {
                    // Keep legacy behavior when a compatible form annotation cannot be read.
                }
                parameter.setIn("form");
            }
        }

        io.swagger.v3.oas.annotations.Parameter annotation = null;
        for (Annotation candidate : annotations) {
            if (candidate instanceof io.swagger.v3.oas.annotations.Parameter parameterAnnotation) {
                annotation = parameterAnnotation;
                break;
            }
        }

        if (annotation != null) {
            if (!annotation.ref().isBlank()) {
                parameter.set$ref(annotation.ref());
                return parameter;
            }
            if (!annotation.name().isBlank()) parameter.setName(annotation.name());
            if (annotation.in() != io.swagger.v3.oas.annotations.enums.ParameterIn.DEFAULT) {
                parameter.setIn(annotation.in().toString());
            }
            if (!annotation.description().isBlank()) parameter.setDescription(annotation.description());
            if (annotation.required()) parameter.setRequired(true);
            if (annotation.deprecated()) parameter.setDeprecated(true);
            if (annotation.allowEmptyValue()) parameter.setAllowEmptyValue(true);
            if (annotation.allowReserved()) parameter.setAllowReserved(true);
            if (annotation.style() != io.swagger.v3.oas.annotations.enums.ParameterStyle.DEFAULT) {
                parameter.setStyle(Parameter.StyleEnum.fromValue(annotation.style().toString()));
            }
            if (annotation.explode() == io.swagger.v3.oas.annotations.enums.Explode.TRUE) {
                parameter.setExplode(true);
            } else if (annotation.explode() == io.swagger.v3.oas.annotations.enums.Explode.FALSE) {
                parameter.setExplode(false);
            }
            if (!annotation.example().isBlank()) {
                parameter.setExample(OperationParser.parseAnnotationValue(annotation.example()));
            }
            if (annotation.examples().length == 1 && annotation.examples()[0].name().isBlank()) {
                parameter.setExample(OperationParser.parseExample(annotation.examples()[0]).getValue());
            } else {
                Map<String, Example> examples = new LinkedHashMap<>();
                int index = 0;
                for (io.swagger.v3.oas.annotations.media.ExampleObject example : annotation.examples()) {
                    String name = example.name().isBlank() ? "example-" + index : example.name();
                    examples.put(name, OperationParser.parseExample(example));
                    index++;
                }
                if (!examples.isEmpty()) parameter.setExamples(examples);
            }
            AnnotationsUtils.getExtensions(annotation.extensions()).forEach(parameter::addExtension);

            Content content = OperationParser.parseContent(
                annotation.content(),
                classConsumes,
                methodConsumes,
                components,
                jsonViewAnnotation
            );
            if (!content.isEmpty()) parameter.setContent(content);

            Schema explicitSchema = OperationParser.resolveArraySchema(
                annotation.array(), components, jsonViewAnnotation
            );
            if (explicitSchema == null) {
                explicitSchema = OperationParser.resolveSchemaAnnotation(
                    annotation.schema(), components, jsonViewAnnotation
                );
            }
            if (explicitSchema != null) parameter.setSchema(explicitSchema);
        }

        if (parameter.getSchema() == null && parameter.getContent() == null && type != null) {
            ResolvedSchema resolved = ModelConverters.getInstance().resolveAsResolvedSchema(
                new AnnotatedType(type)
                    .resolveAsRef(true)
                    .jsonViewAnnotation(jsonViewAnnotation)
            );
            if (resolved.schema != null) parameter.setSchema(resolved.schema);
            if (resolved.referencedSchemas != null) {
                resolved.referencedSchemas.forEach(components::addSchemas);
            }
        }

        applyDefaultValue(parameter, annotations);
        return parameter;
    }

    private static void applyDefaultValue(
        Parameter parameter,
        List<Annotation> annotations
    ) {
        String defaultValue = null;
        for (Annotation annotation : annotations) {
            String annotationName = annotation.annotationType().getName();
            if (annotationName.equals("jakarta.ws.rs.DefaultValue")
                || annotationName.equals("javax.ws.rs.DefaultValue")) {
                try {
                    defaultValue = (String) annotation.annotationType().getMethod("value").invoke(annotation);
                } catch (ReflectiveOperationException ignored) {
                    // Keep legacy behavior: an unreadable default is simply absent.
                }
                break;
            }
        }
        if (defaultValue == null) {
            return;
        }

        Schema schema = parameter.getSchema();
        if (schema == null && parameter.getContent() != null && !parameter.getContent().isEmpty()) {
            schema = parameter.getContent().values().iterator().next().getSchema();
        }
        if (schema == null) {
            return;
        }

        Object parsedDefault = OperationParser.parseAnnotationValue(defaultValue);
        if ("array".equals(schema.getType()) && schema.getItems() != null) {
            schema.getItems().setDefault(parsedDefault);
        } else {
            schema.setDefault(parsedDefault);
        }
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
        if (parameterAnnotation == null) {
            return null;
        }
        if (useSchema && parameterAnnotation.schema() != null) {
            io.swagger.v3.oas.annotations.media.Schema schema = parameterAnnotation.schema();
            if (schema.implementation() != Void.class) {
                return schema.implementation();
            }
        }
        return null;
    }
}
