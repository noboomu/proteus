package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.converter.AnnotatedType;
import io.sinistral.proteus.openapi.converter.ModelConverter;
import io.sinistral.proteus.openapi.converter.ModelConverterContext;
import io.sinistral.proteus.openapi.models.media.ObjectSchema;
import io.sinistral.proteus.openapi.models.media.Schema;
import tools.jackson.databind.BeanDescription;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.introspect.BeanPropertyDefinition;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

/**
 * Minimal Jackson 3 Model Resolver to generate OpenAPI Schema objects.
 */
public class JacksonModelResolver implements ModelConverter {

    protected final ObjectMapper mapper;

    public JacksonModelResolver(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> next) {
        JavaType javaType = mapper.getTypeFactory().constructType(annotatedType.getType());
        
        return resolveJavaType(javaType, context, annotatedType.getAnnotations());
    }

    private Schema resolveJavaType(JavaType javaType, ModelConverterContext context, Annotation[] annotations) {
        if (javaType == null) {
            return null;
        }

        Class<?> rawClass = javaType.getRawClass();
        if (rawClass == null || rawClass.equals(Void.class) || rawClass.equals(void.class)) {
            return null;
        }

        // 0. Handle Optionals by unwrapping
        if (java.util.Optional.class.isAssignableFrom(rawClass)) {
            JavaType innerType = javaType.containedType(0);
            if (innerType != null) {
                return resolveJavaType(innerType, context, annotations);
            }
        }

        // 1. Handle Primitives and standard types
        if (String.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("string");
            return schema;
        }
        if (boolean.class.isAssignableFrom(rawClass) || Boolean.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("boolean");
            return schema;
        }
        if (int.class.isAssignableFrom(rawClass) || Integer.class.isAssignableFrom(rawClass) ||
            short.class.isAssignableFrom(rawClass) || Short.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("integer");
            schema.setFormat("int32");
            return schema;
        }
        if (long.class.isAssignableFrom(rawClass) || Long.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("integer");
            schema.setFormat("int64");
            return schema;
        }
        if (float.class.isAssignableFrom(rawClass) || Float.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("number");
            schema.setFormat("float");
            return schema;
        }
        if (double.class.isAssignableFrom(rawClass) || Double.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("number");
            schema.setFormat("double");
            return schema;
        }
        if (java.math.BigDecimal.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("number");
            return schema;
        }
        if (java.util.Date.class.isAssignableFrom(rawClass) || java.time.temporal.Temporal.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("string");
            
            if (java.time.LocalDate.class.isAssignableFrom(rawClass)) {
                schema.setFormat("date");
            } else if (java.time.LocalTime.class.isAssignableFrom(rawClass)) {
                schema.setFormat("partial-time");
            } else {
                schema.setFormat("date-time");
            }
            return schema;
        }
        if (java.nio.file.Path.class.isAssignableFrom(rawClass) || java.io.File.class.isAssignableFrom(rawClass) || java.nio.ByteBuffer.class.isAssignableFrom(rawClass)) {
             Schema schema = new Schema();
             schema.setType("string");
             schema.setFormat("binary");
             return schema;
        }
        if (java.util.UUID.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("string");
            schema.setFormat("uuid");
            return schema;
        }
        if (java.net.URI.class.isAssignableFrom(rawClass) || java.net.URL.class.isAssignableFrom(rawClass)) {
            Schema schema = new Schema();
            schema.setType("string");
            schema.setFormat("uri");
            return schema;
        }

        // 1.5 Handle Enums
        if (rawClass.isEnum()) {
            Schema schema = new Schema();
            schema.setType("string");
            List<String> enumValues = new java.util.ArrayList<>();
            
            // Check for @JsonValue
            java.lang.reflect.Method jsonValueMethod = null;
            for (java.lang.reflect.Method m : rawClass.getDeclaredMethods()) {
                if (m.isAnnotationPresent(com.fasterxml.jackson.annotation.JsonValue.class)) {
                    jsonValueMethod = m;
                    break;
                }
            }

            for (Object enumConstant : rawClass.getEnumConstants()) {
                if (jsonValueMethod != null) {
                    try {
                        enumValues.add(String.valueOf(jsonValueMethod.invoke(enumConstant)));
                    } catch (Exception e) {
                        enumValues.add(enumConstant.toString());
                    }
                } else {
                    enumValues.add(enumConstant.toString());
                }
            }
            schema.setEnum(enumValues);
            return schema;
        }

        // 2. Handle Iterables/Arrays
        if (javaType.isArrayType() || Iterable.class.isAssignableFrom(rawClass)) {
            Schema arraySchema = new Schema();
            arraySchema.setType("array");
            JavaType contentType = javaType.getContentType();
            if (contentType != null) {
                Schema itemSchema = resolveJavaType(contentType, context, null);
                arraySchema.setItems(itemSchema);
            }
            return arraySchema;
        }

        // 3. Handle Maps
        if (Map.class.isAssignableFrom(rawClass)) {
            Schema mapSchema = new Schema();
            mapSchema.setType("object");
            JavaType contentType = javaType.getContentType();
            if (contentType != null) {
                Schema additionalPropSchema = resolveJavaType(contentType, context, null);
                if (additionalPropSchema != null) {
                    mapSchema.setAdditionalProperties(additionalPropSchema);
                }
            }
            return mapSchema; 
        }
        
        // 4. Handle Objects (POJOs)
        String typeName = javaType.getRawClass().getSimpleName();
        io.swagger.v3.oas.annotations.media.Schema schemaAnnotation = null;

        // Extract class-level @Schema annotation if present
        if (annotations != null) {
            for (Annotation a : annotations) {
                if (a instanceof io.swagger.v3.oas.annotations.media.Schema) {
                    schemaAnnotation = (io.swagger.v3.oas.annotations.media.Schema) a;
                    break;
                }
            }
        }
        if (schemaAnnotation == null) {
            schemaAnnotation = javaType.getRawClass().getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
        }

        if (schemaAnnotation != null && schemaAnnotation.name() != null && !schemaAnnotation.name().isEmpty()) {
            typeName = schemaAnnotation.name();
        } else if (javaType.hasGenericTypes()) {
            StringBuilder nameBuilder = new StringBuilder(typeName);
            for (JavaType param : javaType.getBindings().getTypeParameters()) {
                nameBuilder.append("_").append(param.getRawClass().getSimpleName());
            }
            typeName = nameBuilder.toString();
        }

        if (context.getDefinedModels().containsKey(typeName)) {
            Schema refSchema = new Schema();
            refSchema.set$ref("#/components/schemas/" + typeName);
            return refSchema;
        }
        
        ObjectSchema objectSchema = new ObjectSchema();
        if (schemaAnnotation != null) {
            if (schemaAnnotation.description() != null && !schemaAnnotation.description().isEmpty()) {
                objectSchema.setDescription(schemaAnnotation.description());
            }
            if (schemaAnnotation.title() != null && !schemaAnnotation.title().isEmpty()) {
                objectSchema.setTitle(schemaAnnotation.title());
            }
            if (schemaAnnotation.hidden()) {
                // If it's explicitly hidden, we shouldn't map its properties at all or expose it
                return null;
            }
        }
        
        // Handle Jackson Polymorphism (@JsonTypeInfo / @JsonSubTypes)
        com.fasterxml.jackson.annotation.JsonTypeInfo typeInfo = javaType.getRawClass().getAnnotation(com.fasterxml.jackson.annotation.JsonTypeInfo.class);
        com.fasterxml.jackson.annotation.JsonSubTypes subTypes = javaType.getRawClass().getAnnotation(com.fasterxml.jackson.annotation.JsonSubTypes.class);
        
        if (typeInfo != null && subTypes != null) {
            io.sinistral.proteus.openapi.models.media.Discriminator discriminator = new io.sinistral.proteus.openapi.models.media.Discriminator();
            discriminator.setPropertyName(typeInfo.property());
            
            Map<String, String> mapping = new LinkedHashMap<>();
            List<Schema> anyOfList = new java.util.ArrayList<>();
            
            for (com.fasterxml.jackson.annotation.JsonSubTypes.Type subType : subTypes.value()) {
                String subTypeName = subType.name();
                Class<?> subTypeClass = subType.value();
                
                if (subTypeName.isEmpty()) {
                    subTypeName = subTypeClass.getSimpleName();
                }
                
                // Recursively map the subtype
                JavaType subJavaType = mapper.constructType(subTypeClass);
                Schema subSchemaRef = resolveJavaType(subJavaType, context, subTypeClass.getAnnotations());
                
                if (subSchemaRef != null) {
                    mapping.put(subTypeName, subSchemaRef.get$ref());
                    anyOfList.add(subSchemaRef);
                }
            }
            
            discriminator.setMapping(mapping);
            objectSchema.setDiscriminator(discriminator);
            objectSchema.setAnyOf(anyOfList);
        }

        context.defineModel(typeName, objectSchema);

        tools.jackson.databind.SerializationConfig config = mapper.serializationConfig();
        tools.jackson.databind.introspect.ClassIntrospector introspector = config.classIntrospectorInstance();
        BeanDescription beanDesc = introspector.introspectForSerialization(javaType, introspector.introspectClassAnnotations(javaType));
        List<BeanPropertyDefinition> properties = beanDesc.findProperties();
        
        Map<String, Schema> schemaProperties = new LinkedHashMap<>();
        for (BeanPropertyDefinition prop : properties) {
            String propName = prop.getName();
            JavaType propType = prop.getPrimaryType();
            if (propType != null) {
                // Determine annotations from the accessor (getter, setter, or field)
                Annotation[] propAnnotations = new Annotation[0];
                io.swagger.v3.oas.annotations.media.Schema propSchemaAnnotation = null;

                if (prop.getPrimaryMember() != null) {
                    propAnnotations = prop.getPrimaryMember().annotations().stream()
                        .toArray(Annotation[]::new);
                    
                    for (Annotation a : propAnnotations) {
                        if (a instanceof io.swagger.v3.oas.annotations.media.Schema) {
                            propSchemaAnnotation = (io.swagger.v3.oas.annotations.media.Schema) a;
                            break;
                        }
                    }
                }

                // Check if property is explicitly ignored
                boolean isIgnored = false;
                if (prop.getPrimaryMember() != null) {
                    if (prop.getPrimaryMember().hasAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class)) {
                        com.fasterxml.jackson.annotation.JsonIgnore ignore = prop.getPrimaryMember().getAnnotation(com.fasterxml.jackson.annotation.JsonIgnore.class);
                        if (ignore.value()) {
                            isIgnored = true;
                        }
                    }
                }
                if (isIgnored) {
                    continue;
                }

                if (propSchemaAnnotation != null && propSchemaAnnotation.hidden()) {
                    continue; // Skip hidden properties
                }

                if (propSchemaAnnotation != null && propSchemaAnnotation.name() != null && !propSchemaAnnotation.name().isEmpty()) {
                    propName = propSchemaAnnotation.name();
                } else if (prop.getPrimaryMember() != null && prop.getPrimaryMember().hasAnnotation(com.fasterxml.jackson.annotation.JsonProperty.class)) {
                    com.fasterxml.jackson.annotation.JsonProperty jsonProp = prop.getPrimaryMember().getAnnotation(com.fasterxml.jackson.annotation.JsonProperty.class);
                    if (jsonProp.value() != null && !jsonProp.value().isEmpty() && !jsonProp.value().equals(com.fasterxml.jackson.annotation.JsonProperty.USE_DEFAULT_NAME)) {
                        propName = jsonProp.value();
                    }
                    if (jsonProp.required()) {
                        objectSchema.addRequiredItem(propName);
                    }
                }

                Schema propSchema = resolveJavaType(propType, context, propAnnotations);
                if (propSchema != null) {
                    if (propSchemaAnnotation != null) {
                        if (propSchemaAnnotation.description() != null && !propSchemaAnnotation.description().isEmpty()) {
                            propSchema.setDescription(propSchemaAnnotation.description());
                        }
                        if (propSchemaAnnotation.example() != null && !propSchemaAnnotation.example().isEmpty()) {
                            propSchema.setExample(propSchemaAnnotation.example());
                        }
                        if (propSchemaAnnotation.required()) {
                            if (objectSchema.getRequired() == null || !objectSchema.getRequired().contains(propName)) {
                                objectSchema.addRequiredItem(propName);
                            }
                        }
                        if (propSchemaAnnotation.readOnly()) {
                            propSchema.setReadOnly(true);
                        }
                        if (propSchemaAnnotation.writeOnly()) {
                            propSchema.setWriteOnly(true);
                        }
                        if (propSchemaAnnotation.minimum() != null && !propSchemaAnnotation.minimum().isEmpty()) {
                            try { propSchema.setMinimum(new BigDecimal(propSchemaAnnotation.minimum())); } catch (Exception e) {}
                        }
                        if (propSchemaAnnotation.maximum() != null && !propSchemaAnnotation.maximum().isEmpty()) {
                            try { propSchema.setMaximum(new BigDecimal(propSchemaAnnotation.maximum())); } catch (Exception e) {}
                        }
                        if (propSchemaAnnotation.minLength() > 0) {
                            propSchema.setMinLength(propSchemaAnnotation.minLength());
                        }
                        if (propSchemaAnnotation.maxLength() > 0 && propSchemaAnnotation.maxLength() != Integer.MAX_VALUE) {
                            propSchema.setMaxLength(propSchemaAnnotation.maxLength());
                        }
                        if (propSchemaAnnotation.pattern() != null && !propSchemaAnnotation.pattern().isEmpty()) {
                            propSchema.setPattern(propSchemaAnnotation.pattern());
                        }
                    }

                    // Apply Jakarta validation annotations if present
                    for (Annotation a : propAnnotations) {
                        if (a.annotationType().getSimpleName().equals("NotNull") || 
                            a.annotationType().getSimpleName().equals("NotEmpty") || 
                            a.annotationType().getSimpleName().equals("NotBlank")) {
                            if (objectSchema.getRequired() == null || !objectSchema.getRequired().contains(propName)) {
                                objectSchema.addRequiredItem(propName);
                            }
                        } else if (a.annotationType().getSimpleName().equals("Size")) {
                            try {
                                Object min = a.annotationType().getMethod("min").invoke(a);
                                Object max = a.annotationType().getMethod("max").invoke(a);
                                if (min instanceof Integer && (Integer) min > 0) {
                                    propSchema.setMinLength((Integer) min);
                                }
                                if (max instanceof Integer && (Integer) max < Integer.MAX_VALUE) {
                                    propSchema.setMaxLength((Integer) max);
                                }
                            } catch (Exception e) {}
                        } else if (a.annotationType().getSimpleName().equals("Min")) {
                            try {
                                Object value = a.annotationType().getMethod("value").invoke(a);
                                if (value instanceof Long) {
                                    propSchema.setMinimum(new BigDecimal((Long) value));
                                } else if (value instanceof Integer) {
                                    propSchema.setMinimum(new BigDecimal((Integer) value));
                                }
                            } catch (Exception e) {}
                        } else if (a.annotationType().getSimpleName().equals("Max")) {
                            try {
                                Object value = a.annotationType().getMethod("value").invoke(a);
                                if (value instanceof Long) {
                                    propSchema.setMaximum(new BigDecimal((Long) value));
                                } else if (value instanceof Integer) {
                                    propSchema.setMaximum(new BigDecimal((Integer) value));
                                }
                            } catch (Exception e) {}
                        }
                    }
                    
                    schemaProperties.put(propName, propSchema);
                }
            }
        }
        
        if (!schemaProperties.isEmpty()) {
            objectSchema.setProperties(schemaProperties);
        }
        
        Schema refSchema = new Schema();
        refSchema.set$ref("#/components/schemas/" + typeName);
        return refSchema;
    }
}
