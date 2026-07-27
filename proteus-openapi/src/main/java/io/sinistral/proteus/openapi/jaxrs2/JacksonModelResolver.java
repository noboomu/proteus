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
        
        return resolveJavaType(javaType, context);
    }

    private Schema resolveJavaType(JavaType javaType, ModelConverterContext context) {
        if (javaType == null) {
            return null;
        }

        Class<?> rawClass = javaType.getRawClass();
        if (rawClass == null || rawClass.equals(Void.class) || rawClass.equals(void.class)) {
            return null;
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
            schema.setFormat("date-time");
            return schema;
        }
        if (java.nio.file.Path.class.isAssignableFrom(rawClass) || java.io.File.class.isAssignableFrom(rawClass) || java.nio.ByteBuffer.class.isAssignableFrom(rawClass)) {
             Schema schema = new Schema();
             schema.setType("string");
             schema.setFormat("binary");
             return schema;
        }

        // 2. Handle Iterables/Arrays
        if (javaType.isArrayType() || Iterable.class.isAssignableFrom(rawClass)) {
            Schema arraySchema = new Schema();
            arraySchema.setType("array");
            JavaType contentType = javaType.getContentType();
            if (contentType != null) {
                Schema itemSchema = resolveJavaType(contentType, context);
                arraySchema.setItems(itemSchema);
            }
            return arraySchema;
        }

        // 3. Handle Maps
        if (Map.class.isAssignableFrom(rawClass)) {
            Schema mapSchema = new Schema();
            mapSchema.setType("object");
            return mapSchema; 
        }
        
        // 4. Handle Objects (POJOs)
        String typeName = javaType.getRawClass().getSimpleName();
        if (javaType.hasGenericTypes()) {
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
                Schema propSchema = resolveJavaType(propType, context);
                if (propSchema != null) {
                    schemaProperties.put(propName, propSchema);
                }
            }
        }
        
        objectSchema.setProperties(schemaProperties);
        
        Schema refSchema = new Schema();
        refSchema.set$ref("#/components/schemas/" + typeName);
        return refSchema;
    }
}
