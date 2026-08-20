package io.sinistral.proteus.openapi.jaxrs2;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.JsonView;
import io.sinistral.proteus.openapi.converter.AnnotatedType;
import io.sinistral.proteus.openapi.converter.ModelConverter;
import io.sinistral.proteus.openapi.converter.ModelConverterContext;
import io.sinistral.proteus.openapi.models.media.Discriminator;
import io.sinistral.proteus.openapi.models.media.ObjectSchema;
import io.sinistral.proteus.openapi.models.media.Schema;
import io.sinistral.proteus.openapi.util.AnnotationsUtils;
import io.swagger.v3.oas.annotations.media.Schema.AccessMode;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.MapperFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationConfig;
import tools.jackson.databind.introspect.AnnotatedMember;
import tools.jackson.databind.introspect.BeanPropertyDefinition;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Jackson 3-backed OpenAPI schema resolver.
 *
 * <p>Jackson owns JSON visibility, property naming, records, mix-ins and type
 * bindings. This resolver translates that serialization contract plus OpenAPI
 * and Jakarta Validation annotations into Proteus' OpenAPI model.</p>
 */
public final class JacksonModelResolver implements ModelConverter {
    private static final String COMPONENT_PREFIX = "#/components/schemas/";

    private final ObjectMapper mapper;

    public JacksonModelResolver(ObjectMapper mapper) {
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
        JavaType javaType = mapper.constructType(annotatedType.getType());
        Annotation[] annotations = mergeAnnotations(
            annotatedType.getAnnotations(),
            annotatedType.getCtxAnnotations() == null
                ? null
                : annotatedType.getCtxAnnotations().toArray(Annotation[]::new)
        );
        Class<?> activeView = activeView(annotatedType.getJsonViewAnnotation());
        return resolveType(javaType, annotations, activeView, context);
    }

    private Schema resolveType(
        JavaType type,
        Annotation[] annotations,
        Class<?> activeView,
        ModelConverterContext context
    ) {
        if (type == null || type.getRawClass() == null) {
            return null;
        }

        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation =
            findAnnotation(annotations, io.swagger.v3.oas.annotations.media.Schema.class);
        if (schemaAnnotation == null) {
            schemaAnnotation = type.getRawClass().getAnnotation(
                io.swagger.v3.oas.annotations.media.Schema.class
            );
        }
        if (schemaAnnotation != null && schemaAnnotation.hidden()) {
            return null;
        }
        if (schemaAnnotation != null && !schemaAnnotation.ref().isBlank()) {
            Schema reference = new Schema();
            reference.set$ref(schemaAnnotation.ref());
            return reference;
        }
        if (schemaAnnotation != null
            && schemaAnnotation.implementation() != Void.class
            && schemaAnnotation.implementation() != type.getRawClass()) {
            type = mapper.constructType(schemaAnnotation.implementation());
        }

        Class<?> raw = type.getRawClass();
        if (raw == Void.class || raw == void.class) {
            return null;
        }

        if (Optional.class.isAssignableFrom(raw)) {
            JavaType valueType = type.containedType(0);
            return valueType == null
                ? applySchemaAnnotation(objectSchema(), schemaAnnotation, activeView, context)
                : resolveType(valueType, annotations, activeView, context);
        }

        Schema primitive = primitiveSchema(type);
        if (primitive != null) {
            return applySchemaAnnotation(primitive, schemaAnnotation, activeView, context);
        }

        if (raw.isEnum()) {
            return applySchemaAnnotation(enumSchema(raw), schemaAnnotation, activeView, context);
        }

        if (type.isArrayType() || raw.isArray()) {
            Schema array = new Schema().type("array");
            JavaType valueType = type.getContentType();
            if (valueType != null) {
                array.setItems(resolveNested(valueType, null, activeView, context));
            }
            return applySchemaAnnotation(array, schemaAnnotation, activeView, context);
        }

        if (Collection.class.isAssignableFrom(raw) || Iterable.class.isAssignableFrom(raw)) {
            Schema array = new Schema().type("array");
            JavaType valueType = type.getContentType();
            if (valueType != null) {
                array.setItems(resolveNested(valueType, null, activeView, context));
            }
            if (Set.class.isAssignableFrom(raw)) {
                array.setUniqueItems(true);
            }
            return applySchemaAnnotation(array, schemaAnnotation, activeView, context);
        }

        if (Map.class.isAssignableFrom(raw)) {
            Schema map = objectSchema();
            JavaType valueType = type.getContentType();
            map.setAdditionalProperties(
                valueType == null
                    ? true
                    : resolveNested(valueType, null, activeView, context)
            );
            return applySchemaAnnotation(map, schemaAnnotation, activeView, context);
        }

        BeanDescription bean = beanDescription(type);
        AnnotatedMember jsonValueAccessor = bean.findJsonValueAccessor();
        if (jsonValueAccessor != null) {
            return applySchemaAnnotation(
                resolveNested(jsonValueAccessor.getType(), memberAnnotations(jsonValueAccessor), activeView, context),
                schemaAnnotation,
                activeView,
                context
            );
        }

        return resolveObject(type, bean, schemaAnnotation, activeView, context);
    }

    private Schema resolveObject(
        JavaType type,
        BeanDescription bean,
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation,
        Class<?> activeView,
        ModelConverterContext context
    ) {
        String modelName = modelName(type, schemaAnnotation, activeView);
        if (context.getDefinedModels().containsKey(modelName)) {
            return reference(modelName);
        }

        ObjectSchema model = new ObjectSchema();
        applySchemaAnnotation(model, schemaAnnotation, activeView, context);
        context.defineModel(
            modelName,
            model,
            new AnnotatedType(type).jsonViewAnnotation(jsonView(activeView)),
            null
        );

        LinkedHashMap<String, Schema> properties = new LinkedHashMap<>();
        for (BeanPropertyDefinition property : bean.findProperties()) {
            if (!visibleInView(property, activeView)) {
                continue;
            }

            AnnotatedMember member = property.getPrimaryMember();
            Annotation[] memberAnnotations = memberAnnotations(member);
            io.swagger.v3.oas.annotations.media.Schema propertyAnnotation =
                findAnnotation(memberAnnotations, io.swagger.v3.oas.annotations.media.Schema.class);
            if (propertyAnnotation != null && propertyAnnotation.hidden()) {
                continue;
            }

            JavaType propertyType = property.getPrimaryType();
            if (propertyType == null) {
                continue;
            }
            Schema propertySchema = resolveNested(
                propertyType,
                memberAnnotations,
                activeView,
                context
            );
            if (propertySchema == null) {
                continue;
            }

            String propertyName = property.getName();
            if (propertyAnnotation != null && !propertyAnnotation.name().isBlank()) {
                propertyName = propertyAnnotation.name();
            }
            applySchemaAnnotation(propertySchema, propertyAnnotation, activeView, context);
            applyPropertyAccess(property, memberAnnotations, propertySchema);
            applyValidation(model, propertyName, propertySchema, memberAnnotations);
            applyRequired(model, propertyName, property, propertyAnnotation);

            JsonUnwrapped unwrapped = findAnnotation(memberAnnotations, JsonUnwrapped.class);
            if (unwrapped != null && unwrapped.enabled()) {
                flattenUnwrapped(properties, propertySchema, unwrapped, context);
                if (model.getRequired() != null) {
                    model.getRequired().remove(propertyName);
                }
                Schema unwrappedModel = referencedSchema(propertySchema, context);
                if (unwrappedModel != null && unwrappedModel.getRequired() != null) {
                    unwrappedModel.getRequired().forEach(required ->
                        addRequired(model, unwrapped.prefix() + required + unwrapped.suffix())
                    );
                }
            } else {
                properties.put(propertyName, propertySchema);
            }
        }
        if (!properties.isEmpty()) {
            model.setProperties(properties);
        }

        applyInheritance(type, model, activeView, context);
        applyPolymorphism(type, model, activeView, context);
        applyCompositions(model, schemaAnnotation, activeView, context);

        return reference(modelName);
    }

    private Schema resolveNested(
        JavaType type,
        Annotation[] annotations,
        Class<?> activeView,
        ModelConverterContext context
    ) {
        AnnotatedType nested = new AnnotatedType(type).resolveAsRef(true);
        if (annotations != null) {
            nested.annotations(annotations);
        }
        if (activeView != null) {
            nested.jsonViewAnnotation(jsonView(activeView));
        }
        return context.resolve(nested);
    }

    private BeanDescription beanDescription(JavaType type) {
        SerializationConfig config = mapper.serializationConfig();
        return config.classIntrospectorInstance().introspectForSerialization(
            type,
            config.classIntrospectorInstance().introspectClassAnnotations(type)
        );
    }

    private Schema primitiveSchema(JavaType type) {
        Class<?> raw = type.getRawClass();
        if (raw == String.class || raw == Character.class || raw == char.class) {
            return new Schema().type("string");
        }
        if (raw == Boolean.class || raw == boolean.class) {
            return new Schema().type("boolean");
        }
        if (raw == Byte.class || raw == byte.class || raw == Short.class || raw == short.class
            || raw == Integer.class || raw == int.class) {
            return new Schema().type("integer").format("int32");
        }
        if (raw == Long.class || raw == long.class) {
            return new Schema().type("integer").format("int64");
        }
        if (raw == BigInteger.class) {
            return new Schema().type("integer");
        }
        if (raw == Float.class || raw == float.class) {
            return new Schema().type("number").format("float");
        }
        if (raw == Double.class || raw == double.class) {
            return new Schema().type("number").format("double");
        }
        if (raw == BigDecimal.class || raw == Number.class) {
            return new Schema().type("number");
        }
        if (raw == UUID.class) {
            return new Schema().type("string").format("uuid");
        }
        if (raw == URI.class) {
            return new Schema().type("string").format("uri");
        }
        if (raw == URL.class) {
            return new Schema().type("string").format("url");
        }
        if (raw == LocalDate.class) {
            return new Schema().type("string").format("date");
        }
        if (raw == LocalTime.class) {
            return new Schema().type("string").format("partial-time");
        }
        if (raw == Date.class || raw == Calendar.class || raw == Instant.class
            || raw == OffsetDateTime.class || raw == ZonedDateTime.class
            || Temporal.class.isAssignableFrom(raw)) {
            return new Schema().type("string").format("date-time");
        }
        if (raw == File.class || raw == Path.class || raw == ByteBuffer.class) {
            return new Schema().type("string").format("binary");
        }
        if (raw == byte[].class || raw == Byte[].class) {
            return new Schema().type("string").format("byte");
        }
        if (raw == Object.class) {
            return objectSchema();
        }
        return null;
    }

    private Schema enumSchema(Class<?> enumClass) {
        Schema schema = new Schema();
        List<Object> values = new ArrayList<>();
        String valueType = null;
        for (Object constant : enumClass.getEnumConstants()) {
            JsonNode node = mapper.valueToTree(constant);
            Object value;
            String currentType;
            if (node.isTextual()) {
                value = node.asText();
                currentType = "string";
            } else if (node.isBoolean()) {
                value = node.booleanValue();
                currentType = "boolean";
            } else if (node.isIntegralNumber()) {
                value = node.longValue();
                currentType = "integer";
            } else if (node.isFloatingPointNumber()) {
                value = node.decimalValue();
                currentType = "number";
            } else {
                value = constant.toString();
                currentType = "string";
            }
            if (valueType == null) {
                valueType = currentType;
            } else if (!valueType.equals(currentType)) {
                valueType = "string";
                values.replaceAll(String::valueOf);
                value = String.valueOf(value);
            }
            values.add(value);
        }
        schema.setType(valueType == null ? "string" : valueType);
        schema.setEnum(values);
        return schema;
    }

    private String modelName(
        JavaType type,
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation,
        Class<?> activeView
    ) {
        String baseName;
        if (schemaAnnotation != null && !schemaAnnotation.name().isBlank()) {
            baseName = schemaAnnotation.name();
        } else {
            JsonTypeName jsonTypeName = type.getRawClass().getAnnotation(JsonTypeName.class);
            baseName = jsonTypeName != null && !jsonTypeName.value().isBlank()
                ? jsonTypeName.value()
                : typeToken(type);
        }
        return activeView == null
            ? baseName
            : baseName + "_" + activeView.getSimpleName();
    }

    private String typeToken(JavaType type) {
        if (type == null || type.getRawClass() == null) {
            return "Object";
        }
        if (type.isArrayType()) {
            return "Array_" + typeToken(type.getContentType());
        }
        StringBuilder token = new StringBuilder(type.getRawClass().getSimpleName());
        if (type.hasGenericTypes()) {
            for (JavaType parameter : type.getBindings().getTypeParameters()) {
                token.append(typeToken(parameter));
            }
        }
        return token.toString().replace('$', '_').replaceAll("[^A-Za-z0-9_.-]", "_");
    }

    private void applyPropertyAccess(
        BeanPropertyDefinition property,
        Annotation[] annotations,
        Schema propertySchema
    ) {
        JsonProperty jsonProperty = findAnnotation(annotations, JsonProperty.class);
        if (jsonProperty != null) {
            if (jsonProperty.access() == JsonProperty.Access.READ_ONLY) {
                propertySchema.setReadOnly(true);
            } else if (jsonProperty.access() == JsonProperty.Access.WRITE_ONLY) {
                propertySchema.setWriteOnly(true);
            }
        } else if (property.couldSerialize() && !property.couldDeserialize()) {
            propertySchema.setReadOnly(true);
        } else if (!property.couldSerialize() && property.couldDeserialize()) {
            propertySchema.setWriteOnly(true);
        }
    }

    private void applyRequired(
        Schema parent,
        String propertyName,
        BeanPropertyDefinition property,
        io.swagger.v3.oas.annotations.media.Schema annotation
    ) {
        boolean required = property.isRequired();
        if (annotation != null) {
            required = required || annotation.required()
                || annotation.requiredMode() == RequiredMode.REQUIRED;
            if (annotation.requiredMode() == RequiredMode.NOT_REQUIRED) {
                required = false;
            }
        }
        if (required) {
            addRequired(parent, propertyName);
        }
    }

    private void applyValidation(
        Schema parent,
        String propertyName,
        Schema property,
        Annotation[] annotations
    ) {
        if (findAnnotation(annotations, NotNull.class) != null
            || findAnnotation(annotations, NotBlank.class) != null
            || findAnnotation(annotations, NotEmpty.class) != null) {
            addRequired(parent, propertyName);
        }

        Size size = findAnnotation(annotations, Size.class);
        if (size != null) {
            if ("array".equals(property.getType())) {
                if (size.min() > 0) property.setMinItems(size.min());
                if (size.max() < Integer.MAX_VALUE) property.setMaxItems(size.max());
            } else if ("object".equals(property.getType())) {
                if (size.min() > 0) property.setMinProperties(size.min());
                if (size.max() < Integer.MAX_VALUE) property.setMaxProperties(size.max());
            } else {
                if (size.min() > 0) property.setMinLength(size.min());
                if (size.max() < Integer.MAX_VALUE) property.setMaxLength(size.max());
            }
        }

        Min min = findAnnotation(annotations, Min.class);
        Max max = findAnnotation(annotations, Max.class);
        DecimalMin decimalMin = findAnnotation(annotations, DecimalMin.class);
        DecimalMax decimalMax = findAnnotation(annotations, DecimalMax.class);
        Pattern pattern = findAnnotation(annotations, Pattern.class);
        if (min != null) property.setMinimum(BigDecimal.valueOf(min.value()));
        if (max != null) property.setMaximum(BigDecimal.valueOf(max.value()));
        if (decimalMin != null) {
            property.setMinimum(new BigDecimal(decimalMin.value()));
            property.setExclusiveMinimum(!decimalMin.inclusive());
        }
        if (decimalMax != null) {
            property.setMaximum(new BigDecimal(decimalMax.value()));
            property.setExclusiveMaximum(!decimalMax.inclusive());
        }
        if (pattern != null) property.setPattern(pattern.regexp());
    }

    private Schema applySchemaAnnotation(
        Schema schema,
        io.swagger.v3.oas.annotations.media.Schema annotation,
        Class<?> activeView,
        ModelConverterContext context
    ) {
        if (schema == null || annotation == null) {
            return schema;
        }
        if (!annotation.title().isBlank()) schema.setTitle(annotation.title());
        if (!annotation.description().isBlank()) schema.setDescription(annotation.description());
        if (!annotation.type().isBlank()) schema.setType(annotation.type());
        if (!annotation.format().isBlank()) schema.setFormat(annotation.format());
        if (!annotation.ref().isBlank()) schema.set$ref(annotation.ref());
        if (!annotation.example().isBlank()) schema.setExample(parseAnnotationValue(annotation.example()));
        if (!annotation.defaultValue().isBlank()) schema.setDefault(parseAnnotationValue(annotation.defaultValue()));
        if (annotation.allowableValues().length > 0) {
            List<Object> values = Arrays.stream(annotation.allowableValues())
                .map(this::parseAnnotationValue)
                .toList();
            schema.setEnum(new ArrayList<>(values));
        }
        if (annotation.multipleOf() != 0) {
            schema.setMultipleOf(BigDecimal.valueOf(annotation.multipleOf()));
        }
        if (!annotation.minimum().isBlank()) schema.setMinimum(new BigDecimal(annotation.minimum()));
        if (!annotation.maximum().isBlank()) schema.setMaximum(new BigDecimal(annotation.maximum()));
        if (annotation.exclusiveMinimum()) schema.setExclusiveMinimum(true);
        if (annotation.exclusiveMaximum()) schema.setExclusiveMaximum(true);
        if (annotation.minLength() > 0) schema.setMinLength(annotation.minLength());
        if (annotation.maxLength() < Integer.MAX_VALUE) schema.setMaxLength(annotation.maxLength());
        if (annotation.minProperties() > 0) schema.setMinProperties(annotation.minProperties());
        if (annotation.maxProperties() > 0) schema.setMaxProperties(annotation.maxProperties());
        if (!annotation.pattern().isBlank()) schema.setPattern(annotation.pattern());
        if (annotation.nullable()) schema.setNullable(true);
        if (annotation.deprecated()) schema.setDeprecated(true);
        if (annotation.readOnly() || annotation.accessMode() == AccessMode.READ_ONLY) schema.setReadOnly(true);
        if (annotation.writeOnly() || annotation.accessMode() == AccessMode.WRITE_ONLY) schema.setWriteOnly(true);
        if (annotation.accessMode() == AccessMode.READ_WRITE) {
            schema.setReadOnly(null);
            schema.setWriteOnly(null);
        }
        if (annotation.additionalProperties()
            == io.swagger.v3.oas.annotations.media.Schema.AdditionalPropertiesValue.TRUE) {
            schema.setAdditionalProperties(true);
        } else if (annotation.additionalProperties()
            == io.swagger.v3.oas.annotations.media.Schema.AdditionalPropertiesValue.FALSE) {
            schema.setAdditionalProperties(false);
        }
        if (annotation.additionalPropertiesSchema() != Void.class) {
            schema.setAdditionalProperties(resolveNested(
                mapper.constructType(annotation.additionalPropertiesSchema()),
                null,
                activeView,
                context
            ));
        }
        for (String required : annotation.requiredProperties()) addRequired(schema, required);
        AnnotationsUtils.getExternalDocumentation(annotation.externalDocs()).ifPresent(schema::setExternalDocs);
        AnnotationsUtils.getExtensions(annotation.extensions()).forEach(schema::addExtension);
        return schema;
    }

    private Object parseAnnotationValue(String value) {
        try {
            return mapper.readValue(value, Object.class);
        } catch (Exception ignored) {
            return value;
        }
    }

    private void applyCompositions(
        Schema model,
        io.swagger.v3.oas.annotations.media.Schema annotation,
        Class<?> activeView,
        ModelConverterContext context
    ) {
        if (annotation == null) return;
        model.setAllOf(mergeSchemas(model.getAllOf(), resolveClasses(annotation.allOf(), activeView, context)));
        model.setAnyOf(mergeSchemas(model.getAnyOf(), resolveClasses(annotation.anyOf(), activeView, context)));
        model.setOneOf(mergeSchemas(model.getOneOf(), resolveClasses(annotation.oneOf(), activeView, context)));
        if (!annotation.discriminatorProperty().isBlank()) {
            if (model.getDiscriminator() == null) model.setDiscriminator(new Discriminator());
            model.getDiscriminator().setPropertyName(annotation.discriminatorProperty());
        }
        if (annotation.discriminatorMapping().length > 0) {
            if (model.getDiscriminator() == null) model.setDiscriminator(new Discriminator());
            for (io.swagger.v3.oas.annotations.media.DiscriminatorMapping mapping
                : annotation.discriminatorMapping()) {
                if (mapping.value().isBlank() || mapping.schema() == Void.class) continue;
                Schema resolved = resolveNested(
                    mapper.constructType(mapping.schema()),
                    null,
                    activeView,
                    context
                );
                if (resolved != null && resolved.get$ref() != null) {
                    model.getDiscriminator().addMapping(mapping.value(), resolved.get$ref());
                }
            }
        }
    }

    private List<Schema> resolveClasses(
        Class<?>[] classes,
        Class<?> activeView,
        ModelConverterContext context
    ) {
        if (classes == null || classes.length == 0) return null;
        List<Schema> schemas = new ArrayList<>();
        for (Class<?> type : classes) {
            if (type != Void.class) {
                Schema schema = resolveNested(mapper.constructType(type), type.getAnnotations(), activeView, context);
                if (schema != null) schemas.add(schema);
            }
        }
        return schemas.isEmpty() ? null : schemas;
    }

    private List<Schema> mergeSchemas(List<Schema> existing, List<Schema> additions) {
        if (additions == null || additions.isEmpty()) {
            return existing;
        }
        List<Schema> merged = existing == null
            ? new ArrayList<>()
            : new ArrayList<>(existing);
        for (Schema addition : additions) {
            boolean duplicate = merged.stream().anyMatch(current ->
                current == addition
                    || (current != null && addition != null
                        && current.get$ref() != null
                        && current.get$ref().equals(addition.get$ref()))
            );
            if (!duplicate) merged.add(addition);
        }
        return merged;
    }

    private void applyPolymorphism(
        JavaType type,
        Schema model,
        Class<?> activeView,
        ModelConverterContext context
    ) {
        JsonTypeInfo typeInfo = type.getRawClass().getAnnotation(JsonTypeInfo.class);
        JsonSubTypes subTypes = type.getRawClass().getAnnotation(JsonSubTypes.class);
        io.swagger.v3.oas.annotations.media.Schema openApi = type.getRawClass().getAnnotation(
            io.swagger.v3.oas.annotations.media.Schema.class
        );

        LinkedHashMap<String, Class<?>> subtypeClasses = new LinkedHashMap<>();
        if (subTypes != null) {
            for (JsonSubTypes.Type subtype : subTypes.value()) {
                String name = subtype.name().isBlank()
                    ? subtype.value().getSimpleName()
                    : subtype.name();
                subtypeClasses.put(name, subtype.value());
            }
        }
        if (openApi != null) {
            for (Class<?> subtype : openApi.subTypes()) {
                subtypeClasses.putIfAbsent(subtype.getSimpleName(), subtype);
            }
        }
        if (subtypeClasses.isEmpty()) return;

        String discriminatorProperty = null;
        if (typeInfo != null && !typeInfo.property().isBlank()) {
            discriminatorProperty = typeInfo.property();
        } else if (openApi != null && !openApi.discriminatorProperty().isBlank()) {
            discriminatorProperty = openApi.discriminatorProperty();
        }
        if (discriminatorProperty != null) {
            model.setDiscriminator(new Discriminator().propertyName(discriminatorProperty));
            if (model.getProperties() == null) {
                model.setProperties(new LinkedHashMap<>());
            }
            model.getProperties().putIfAbsent(discriminatorProperty, new Schema().type("string"));
            addRequired(model, discriminatorProperty);
        }

        // Swagger 2.2.30 registers subtypes but does not add oneOf or discriminator mappings
        // for this JsonTypeInfo shape.
        for (Class<?> subtype : subtypeClasses.values()) {
            resolveNested(
                mapper.constructType(subtype),
                subtype.getAnnotations(),
                activeView,
                context
            );
        }
    }

    private void applyInheritance(
        JavaType type,
        Schema model,
        Class<?> activeView,
        ModelConverterContext context
    ) {
        Class<?> parent = type.getRawClass().getSuperclass();
        if (parent == null || parent == Object.class || parent.getName().startsWith("java.")) return;
        if (parent.getAnnotation(JsonSubTypes.class) == null
            && parent.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class) == null) return;

        Schema parentReference = resolveNested(
            mapper.constructType(parent),
            parent.getAnnotations(),
            activeView,
            context
        );
        if (parentReference != null) {
            removeInheritedProperties(model, parentReference, context);

            ObjectSchema childProperties = new ObjectSchema();
            if (model.getProperties() != null) {
                childProperties.setProperties(new LinkedHashMap<>(model.getProperties()));
            }
            if (model.getRequired() != null) {
                childProperties.setRequired(new ArrayList<>(model.getRequired()));
            }
            model.setProperties(null);
            model.setRequired(null);

            List<Schema> allOf = new ArrayList<>();
            allOf.add(parentReference);
            allOf.add(childProperties);
            if (model.getAllOf() != null) {
                allOf.addAll(model.getAllOf());
            }
            model.setAllOf(allOf);
        }
    }

    private void removeInheritedProperties(Schema model, Schema parentReference, ModelConverterContext context) {
        if (parentReference.get$ref() == null || model.getProperties() == null) {
            return;
        }

        String parentName = parentReference.get$ref().substring(parentReference.get$ref().lastIndexOf('/') + 1);
        Schema parentModel = context.getDefinedModels().get(parentName);
        if (parentModel == null || parentModel.getProperties() == null) {
            return;
        }

        parentModel.getProperties().keySet().forEach(model.getProperties()::remove);
        if (model.getProperties().isEmpty()) {
            model.setProperties(null);
        }

        if (model.getRequired() != null && parentModel.getRequired() != null) {
            model.getRequired().removeAll(parentModel.getRequired());
            if (model.getRequired().isEmpty()) {
                model.setRequired(null);
            }
        }
    }

    private void flattenUnwrapped(
        Map<String, Schema> target,
        Schema propertySchema,
        JsonUnwrapped annotation,
        ModelConverterContext context
    ) {
        Schema source = referencedSchema(propertySchema, context);
        if (source == null || source.getProperties() == null) {
            return;
        }
        source.getProperties().forEach((name, schema) ->
            target.put(annotation.prefix() + name + annotation.suffix(), (Schema) schema)
        );
    }

    private Schema referencedSchema(Schema schema, ModelConverterContext context) {
        if (schema != null && schema.get$ref() != null && schema.get$ref().startsWith(COMPONENT_PREFIX)) {
            return context.getDefinedModels().get(
                schema.get$ref().substring(COMPONENT_PREFIX.length())
            );
        }
        return schema;
    }

    private boolean visibleInView(BeanPropertyDefinition property, Class<?> activeView) {
        if (activeView == null) return true;
        Class<?>[] views = property.findViews();
        if (views == null || views.length == 0) {
            return mapper.serializationConfig().isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
        }
        for (Class<?> declaredView : views) {
            if (declaredView.isAssignableFrom(activeView)) return true;
        }
        return false;
    }

    private Annotation[] memberAnnotations(AnnotatedMember member) {
        if (member == null) return new Annotation[0];
        if (member._annotationMap() != null) {
            return member._annotationMap().values().toArray(Annotation[]::new);
        }
        Member reflected = member.getMember();
        return reflected instanceof AnnotatedElement
            ? ((AnnotatedElement) reflected).getAnnotations()
            : new Annotation[0];
    }

    private Class<?> activeView(JsonView view) {
        return view == null || view.value().length == 0 ? null : view.value()[0];
    }

    private JsonView jsonView(Class<?> view) {
        if (view == null) return null;
        return new JsonView() {
            @Override
            public Class<?>[] value() {
                return new Class<?>[] {view};
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return JsonView.class;
            }
        };
    }

    private Schema objectSchema() {
        return new ObjectSchema();
    }

    private Schema reference(String modelName) {
        Schema reference = new Schema();
        reference.set$ref(COMPONENT_PREFIX + modelName);
        return reference;
    }

    private void addRequired(Schema schema, String name) {
        if (name == null || name.isBlank()) return;
        if (schema.getRequired() == null || !schema.getRequired().contains(name)) {
            schema.addRequiredItem(name);
        }
    }

    private Annotation[] mergeAnnotations(Annotation[] first, Annotation[] second) {
        LinkedHashMap<Class<? extends Annotation>, Annotation> merged = new LinkedHashMap<>();
        if (first != null) {
            for (Annotation annotation : first) merged.put(annotation.annotationType(), annotation);
        }
        if (second != null) {
            for (Annotation annotation : second) merged.put(annotation.annotationType(), annotation);
        }
        return merged.values().toArray(Annotation[]::new);
    }

    private <A extends Annotation> A findAnnotation(Annotation[] annotations, Class<A> type) {
        if (annotations == null) return null;
        for (Annotation annotation : annotations) {
            if (type.isInstance(annotation)) return type.cast(annotation);
        }
        return null;
    }
}
