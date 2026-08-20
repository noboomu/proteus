package io.sinistral.proteus.openapi.util;

import com.fasterxml.jackson.annotation.JsonView;
import io.sinistral.proteus.openapi.converter.AnnotatedType;
import io.sinistral.proteus.openapi.converter.ModelConverters;
import io.sinistral.proteus.openapi.converter.ResolvedSchema;
import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.examples.Example;
import io.sinistral.proteus.openapi.models.headers.Header;
import io.sinistral.proteus.openapi.models.links.Link;
import io.sinistral.proteus.openapi.models.media.Content;
import io.sinistral.proteus.openapi.models.media.MediaType;
import io.sinistral.proteus.openapi.models.media.Schema;
import io.sinistral.proteus.openapi.models.parameters.RequestBody;
import io.sinistral.proteus.openapi.models.responses.ApiResponse;
import io.sinistral.proteus.openapi.models.responses.ApiResponses;

import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Parser for operation-level OpenAPI annotations.
 */
public final class OperationParser {
    private OperationParser() {}

    public static Optional<RequestBody> getRequestBody(
        io.swagger.v3.oas.annotations.parameters.RequestBody annotation,
        jakarta.ws.rs.Consumes classConsumes,
        jakarta.ws.rs.Consumes methodConsumes,
        Components components,
        JsonView jsonViewAnnotation
    ) {
        if (annotation == null) {
            return Optional.empty();
        }

        RequestBody requestBody = new RequestBody();
        boolean isEmpty = true;

        if (!annotation.ref().isBlank()) {
            requestBody.set$ref(annotation.ref());
            return Optional.of(requestBody);
        }
        if (!annotation.description().isBlank()) {
            requestBody.setDescription(annotation.description());
            isEmpty = false;
        }
        if (annotation.required()) {
            requestBody.setRequired(true);
            isEmpty = false;
        }
        if (annotation.extensions().length > 0) {
            AnnotationsUtils.getExtensions(annotation.extensions()).forEach(requestBody::addExtension);
            isEmpty = false;
        }
        if (annotation.content().length > 0) {
            isEmpty = false;
        }
        if (isEmpty) {
            return Optional.empty();
        }

        Content content = parseContent(
            annotation.content(),
            classConsumes == null ? null : classConsumes.value(),
            methodConsumes == null ? null : methodConsumes.value(),
            components,
            jsonViewAnnotation
        );
        if (!content.isEmpty()) {
            requestBody.setContent(content);
        }
        return Optional.of(requestBody);
    }

    public static Optional<ApiResponses> getApiResponses(
        io.swagger.v3.oas.annotations.responses.ApiResponse[] annotations,
        jakarta.ws.rs.Produces classProduces,
        jakarta.ws.rs.Produces methodProduces,
        Components components,
        JsonView jsonViewAnnotation
    ) {
        if (annotations == null) {
            return Optional.empty();
        }

        ApiResponses responses = new ApiResponses();
        for (io.swagger.v3.oas.annotations.responses.ApiResponse annotation : annotations) {
            ApiResponse response = new ApiResponse();
            String code = annotation.responseCode();
            String responseKey = code == null || code.isBlank() ? "default" : code;

            if (!annotation.ref().isBlank()) {
                response.set$ref(annotation.ref());
                responses.put(responseKey, response);
                continue;
            }
            if (!annotation.description().isBlank()) {
                response.setDescription(annotation.description());
            }
            AnnotationsUtils.getExtensions(annotation.extensions()).forEach(response::addExtension);
            Content content = parseContent(
                annotation.content(),
                classProduces == null ? null : classProduces.value(),
                methodProduces == null ? null : methodProduces.value(),
                components,
                jsonViewAnnotation
            );
            if (!content.isEmpty()) {
                response.setContent(content);
            }
            parseHeaders(annotation.headers(), response, components, jsonViewAnnotation);

            if (!annotation.description().isBlank()
                || response.getContent() != null
                || response.getHeaders() != null) {
                parseLinks(annotation.links(), response);
                responses.put(responseKey, response);
            }
        }
        return responses.isEmpty() ? Optional.empty() : Optional.of(responses);
    }

    static Content parseContent(
        io.swagger.v3.oas.annotations.media.Content[] annotations,
        String[] classMediaTypes,
        String[] methodMediaTypes,
        Components components,
        JsonView view
    ) {
        Content result = new Content();
        if (annotations == null || annotations.length == 0) {
            return result;
        }

        for (io.swagger.v3.oas.annotations.media.Content annotation : annotations) {
            MediaType mediaType = new MediaType();
            Schema schema = resolveContentSchema(annotation, components, view);
            if (schema != null) {
                mediaType.setSchema(schema);
            }
            parseExamples(annotation.examples(), mediaType);
            AnnotationsUtils.getExtensions(annotation.extensions()).forEach(mediaType::addExtension);
            for (io.swagger.v3.oas.annotations.media.Encoding encoding : annotation.encoding()) {
                parseEncoding(encoding, mediaType, components, view);
            }

            if (!annotation.mediaType().isBlank()) {
                result.addMediaType(annotation.mediaType(), mediaType);
            } else {
                AnnotationsUtils.applyTypes(classMediaTypes, methodMediaTypes, result, mediaType);
            }
        }
        return result;
    }

    private static Schema resolveContentSchema(
        io.swagger.v3.oas.annotations.media.Content content,
        Components components,
        JsonView view
    ) {
        io.swagger.v3.oas.annotations.media.ArraySchema array = content.array();
        if (arraySchemaSpecified(array)) {
            return resolveArraySchema(array, components, view);
        }
        return resolveSchemaAnnotation(content.schema(), components, view);
    }

    static Schema resolveArraySchema(
        io.swagger.v3.oas.annotations.media.ArraySchema array,
        Components components,
        JsonView view
    ) {
        if (!arraySchemaSpecified(array)) return null;

        Schema arraySchema = new Schema();
        arraySchema.setType("array");

        io.swagger.v3.oas.annotations.media.Schema itemAnnotation = schemaSpecified(array.schema())
            ? array.schema()
            : array.items();
        Schema items = resolveSchemaAnnotation(itemAnnotation, components, view);
        if (items != null) arraySchema.setItems(items);
        if (array.minItems() != Integer.MAX_VALUE) arraySchema.setMinItems(array.minItems());
        if (array.maxItems() != Integer.MIN_VALUE) arraySchema.setMaxItems(array.maxItems());
        if (array.uniqueItems()) arraySchema.setUniqueItems(true);
        AnnotationsUtils.getExtensions(array.extensions()).forEach(arraySchema::addExtension);
        return arraySchema;
    }

    private static void applySchemaMetadata(
        Schema schema,
        io.swagger.v3.oas.annotations.media.Schema annotation
    ) {
        if (annotation == null) return;
        if (!annotation.type().isBlank()) {
            schema.setType(annotation.type());
        } else if (annotation.types().length == 1) {
            schema.setType(annotation.types()[0]);
        }
        if (!annotation.title().isBlank()) schema.setTitle(annotation.title());
        if (!annotation.description().isBlank()) schema.setDescription(annotation.description());
        if (!annotation.format().isBlank()) schema.setFormat(annotation.format());
        if (!annotation.example().isBlank()) schema.setExample(parseAnnotationValue(annotation.example()));
        if (!annotation.defaultValue().isBlank()) schema.setDefault(parseAnnotationValue(annotation.defaultValue()));
        if (annotation.allowableValues().length > 0) {
            java.util.List<Object> values = java.util.Arrays.stream(annotation.allowableValues())
                .map(OperationParser::parseAnnotationValue)
                .toList();
            schema.setEnum(new java.util.ArrayList<>(values));
        }
        if (annotation.multipleOf() != 0) {
            schema.setMultipleOf(java.math.BigDecimal.valueOf(annotation.multipleOf()));
        }
        if (!annotation.minimum().isBlank()) {
            schema.setMinimum(new java.math.BigDecimal(annotation.minimum()));
        }
        if (!annotation.maximum().isBlank()) {
            schema.setMaximum(new java.math.BigDecimal(annotation.maximum()));
        }
        if (annotation.exclusiveMinimum()) schema.setExclusiveMinimum(true);
        if (annotation.exclusiveMaximum()) schema.setExclusiveMaximum(true);
        if (annotation.minLength() > 0) schema.setMinLength(annotation.minLength());
        if (annotation.maxLength() < Integer.MAX_VALUE) schema.setMaxLength(annotation.maxLength());
        if (annotation.minProperties() > 0) schema.setMinProperties(annotation.minProperties());
        if (annotation.maxProperties() > 0) schema.setMaxProperties(annotation.maxProperties());
        if (!annotation.pattern().isBlank()) schema.setPattern(annotation.pattern());
        for (String required : annotation.requiredProperties()) schema.addRequiredItem(required);
        if (annotation.nullable()) schema.setNullable(true);
        if (annotation.deprecated()) schema.setDeprecated(true);
        if (annotation.readOnly()
            || annotation.accessMode() == io.swagger.v3.oas.annotations.media.Schema.AccessMode.READ_ONLY) {
            schema.setReadOnly(true);
        }
        if (annotation.writeOnly()
            || annotation.accessMode() == io.swagger.v3.oas.annotations.media.Schema.AccessMode.WRITE_ONLY) {
            schema.setWriteOnly(true);
        }
    }

    static Object parseAnnotationValue(String value) {
        try {
            return Json.mapper().readValue(value, Object.class);
        } catch (Exception ignored) {
            return value;
        }
    }

    static Schema resolveSchemaAnnotation(
        io.swagger.v3.oas.annotations.media.Schema annotation,
        Components components,
        JsonView view
    ) {
        if (!schemaSpecified(annotation)) {
            return null;
        }
        if (!annotation.ref().isBlank()) {
            Schema reference = new Schema();
            reference.set$ref(annotation.ref());
            return reference;
        }

        if (annotation.implementation() == Void.class) {
            Schema schema = new Schema();
            // Swagger 2.2.30 resolves annotation-only schemas as strings unless
            // an explicit type or composition overrides that baseline.
            schema.setType("string");
            applySchemaMetadata(schema, annotation);
            schema.setAllOf(resolveSchemaClasses(annotation.allOf(), components, view));
            schema.setAnyOf(resolveSchemaClasses(annotation.anyOf(), components, view));
            schema.setOneOf(resolveSchemaClasses(annotation.oneOf(), components, view));
            return schema;
        }

        AnnotatedType type = new AnnotatedType(annotation.implementation())
            .annotations(new Annotation[] {annotation})
            .resolveAsRef(true)
            .jsonViewAnnotation(view);
        ResolvedSchema resolved = ModelConverters.getInstance().resolveAsResolvedSchema(type);
        if (resolved.referencedSchemas != null) {
            resolved.referencedSchemas.forEach(components::addSchemas);
        }
        return resolved.schema;
    }

    private static java.util.List<Schema> resolveSchemaClasses(
        Class<?>[] classes,
        Components components,
        JsonView view
    ) {
        if (classes == null || classes.length == 0) return null;
        java.util.List<Schema> schemas = new java.util.ArrayList<>();
        for (Class<?> implementation : classes) {
            if (implementation == Void.class) continue;
            ResolvedSchema resolved = ModelConverters.getInstance().resolveAsResolvedSchema(
                new AnnotatedType(implementation)
                    .resolveAsRef(true)
                    .jsonViewAnnotation(view)
            );
            if (resolved.referencedSchemas != null) {
                resolved.referencedSchemas.forEach(components::addSchemas);
            }
            if (resolved.schema != null) schemas.add(resolved.schema);
        }
        return schemas.isEmpty() ? null : schemas;
    }

    private static boolean schemaSpecified(io.swagger.v3.oas.annotations.media.Schema schema) {
        return schema != null && (
            schema.implementation() != Void.class
                || !schema.ref().isBlank()
                || !schema.type().isBlank()
                || schema.types().length > 0
                || !schema.format().isBlank()
                || !schema.name().isBlank()
                || !schema.title().isBlank()
                || !schema.description().isBlank()
                || !schema.example().isBlank()
                || !schema.defaultValue().isBlank()
                || schema.multipleOf() != 0
                || !schema.minimum().isBlank()
                || !schema.maximum().isBlank()
                || schema.exclusiveMinimum()
                || schema.exclusiveMaximum()
                || schema.minLength() > 0
                || schema.maxLength() < Integer.MAX_VALUE
                || schema.minProperties() > 0
                || schema.maxProperties() > 0
                || !schema.pattern().isBlank()
                || schema.requiredProperties().length > 0
                || schema.nullable()
                || schema.deprecated()
                || schema.readOnly()
                || schema.writeOnly()
                || schema.accessMode() != io.swagger.v3.oas.annotations.media.Schema.AccessMode.AUTO
                || schema.oneOf().length > 0
                || schema.anyOf().length > 0
                || schema.allOf().length > 0
                || schema.allowableValues().length > 0
        );
    }

    private static boolean arraySchemaSpecified(io.swagger.v3.oas.annotations.media.ArraySchema array) {
        return array != null && (
            schemaSpecified(array.items())
                || schemaSpecified(array.schema())
                || schemaSpecified(array.arraySchema())
                || array.minItems() != Integer.MAX_VALUE
                || array.maxItems() != Integer.MIN_VALUE
                || array.uniqueItems()
        );
    }

    private static void parseEncoding(
        io.swagger.v3.oas.annotations.media.Encoding annotation,
        MediaType mediaType,
        Components components,
        JsonView view
    ) {
        if (annotation == null || annotation.name().isBlank()) return;

        io.sinistral.proteus.openapi.models.media.Encoding encoding =
            new io.sinistral.proteus.openapi.models.media.Encoding();
        if (!annotation.contentType().isBlank()) {
            encoding.setContentType(annotation.contentType());
        }
        if (!annotation.style().isBlank()) {
            for (io.sinistral.proteus.openapi.models.media.Encoding.StyleEnum style
                : io.sinistral.proteus.openapi.models.media.Encoding.StyleEnum.values()) {
                if (style.toString().equals(annotation.style())) {
                    encoding.setStyle(style);
                    break;
                }
            }
        }
        if (annotation.explode()) encoding.setExplode(true);
        if (annotation.allowReserved()) encoding.setAllowReserved(true);

        ApiResponse headerHolder = new ApiResponse();
        parseHeaders(annotation.headers(), headerHolder, components, view);
        if (headerHolder.getHeaders() != null) {
            encoding.setHeaders(headerHolder.getHeaders());
        }
        AnnotationsUtils.getExtensions(annotation.extensions()).forEach(encoding::addExtension);
        mediaType.addEncoding(annotation.name(), encoding);
    }

    private static void parseExamples(
        io.swagger.v3.oas.annotations.media.ExampleObject[] annotations,
        MediaType mediaType
    ) {
        if (annotations == null || annotations.length == 0) return;
        if (annotations.length == 1 && annotations[0].name().isBlank()) {
            mediaType.setExample(parseExample(annotations[0]).getValue());
            return;
        }
        int index = 0;
        for (io.swagger.v3.oas.annotations.media.ExampleObject annotation : annotations) {
            Example example = parseExample(annotation);
            String name = annotation.name().isBlank() ? "example-" + index : annotation.name();
            mediaType.addExamples(name, example);
            index++;
        }
    }

    static Example parseExample(
        io.swagger.v3.oas.annotations.media.ExampleObject annotation
    ) {
        Example example = new Example();
        if (!annotation.name().isBlank()) {
            example.setDescription(annotation.name());
        }
        if (!annotation.summary().isBlank()) {
            example.setSummary(annotation.summary());
        }
        if (!annotation.description().isBlank()) {
            example.setDescription(annotation.description());
        }
        if (!annotation.ref().isBlank()) example.set$ref(annotation.ref());
        if (!annotation.externalValue().isBlank()) example.setExternalValue(annotation.externalValue());
        if (!annotation.value().isBlank()) {
            try {
                example.setValue(Json.mapper().readTree(annotation.value()));
            } catch (Exception ignored) {
                example.setValue(annotation.value());
            }
        }
        AnnotationsUtils.getExtensions(annotation.extensions()).forEach(example::addExtension);
        return example;
    }

    private static void parseHeaders(
        io.swagger.v3.oas.annotations.headers.Header[] annotations,
        ApiResponse response,
        Components components,
        JsonView view
    ) {
        if (annotations == null) return;
        for (io.swagger.v3.oas.annotations.headers.Header annotation : annotations) {
            if (annotation.hidden() || annotation.name().isBlank()) continue;
            Header header = new Header();
            if (!annotation.description().isBlank()) {
                header.setDescription(annotation.description());
            }
            if (annotation.required()) {
                header.setRequired(true);
            }
            if (annotation.deprecated()) {
                header.setDeprecated(true);
            }
            header.setStyle(Header.StyleEnum.SIMPLE);
            if (!annotation.ref().isBlank()) {
                header.set$ref(annotation.ref());
            }
            Schema schema = arraySchemaSpecified(annotation.array())
                ? resolveArraySchema(annotation.array(), components, view)
                : resolveSchemaAnnotation(annotation.schema(), components, view);
            if (schema != null) {
                header.setSchema(schema);
            }
            if (!annotation.example().isBlank()) {
                try {
                    header.setExample(Json.mapper().readTree(annotation.example()));
                } catch (Exception ignored) {
                    header.setExample(annotation.example());
                }
            }
            if (annotation.examples().length == 1 && annotation.examples()[0].name().isBlank()) {
                header.setExample(parseExample(annotation.examples()[0]));
            } else if (annotation.examples().length > 0) {
                Map<String, Example> examples = new LinkedHashMap<>();
                for (io.swagger.v3.oas.annotations.media.ExampleObject example : annotation.examples()) {
                    if (!example.name().isBlank()) {
                        examples.put(example.name(), parseExample(example));
                    }
                }
                if (!examples.isEmpty()) {
                    header.setExamples(examples);
                }
            }
            if (annotation.explode() == io.swagger.v3.oas.annotations.enums.Explode.TRUE) {
                header.setExplode(true);
            }
            response.addHeaderObject(annotation.name(), header);
        }
    }

    private static void parseLinks(
        io.swagger.v3.oas.annotations.links.Link[] annotations,
        ApiResponse response
    ) {
        if (annotations == null) return;
        for (io.swagger.v3.oas.annotations.links.Link annotation : annotations) {
            Link link = new Link();
            boolean isEmpty = true;
            if (!annotation.description().isBlank()) {
                link.setDescription(annotation.description());
                isEmpty = false;
            }
            if (!annotation.operationId().isBlank()) {
                link.setOperationId(annotation.operationId());
                isEmpty = false;
            } else if (!annotation.operationRef().isBlank()) {
                link.setOperationRef(annotation.operationRef());
                isEmpty = false;
            }
            if (!annotation.ref().isBlank()) {
                link.set$ref(annotation.ref());
                isEmpty = false;
            }
            if (annotation.extensions().length > 0) {
                Map<String, Object> extensions = AnnotationsUtils.getExtensions(annotation.extensions());
                if (extensions != null) {
                    extensions.forEach(link::addExtension);
                    isEmpty = false;
                }
            }
            if (isEmpty) continue;

            if (!annotation.requestBody().isBlank()) {
                try {
                    link.setRequestBody(Json.mapper().readTree(annotation.requestBody()));
                } catch (Exception ignored) {
                    link.setRequestBody(annotation.requestBody());
                }
            }
            Map<String, Object> parameters = new LinkedHashMap<>();
            for (io.swagger.v3.oas.annotations.links.LinkParameter parameter : annotation.parameters()) {
                if (!parameter.name().isBlank()) {
                    parameters.put(parameter.name(), parameter.expression());
                }
            }
            if (!parameters.isEmpty()) link.setParameters(parameters);
            response.addLink(annotation.name(), link);
        }
    }
}
