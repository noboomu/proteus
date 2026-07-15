package io.sinistral.proteus.openapi.converter;

import io.sinistral.proteus.openapi.jaxrs2.ServerModelResolver;
import io.sinistral.proteus.openapi.models.Components;
import io.sinistral.proteus.openapi.models.media.Schema;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.util.*;

/**
 * Registry for model converters
 */
public class ModelConverters {
    private static ModelConverters instance;
    private final List<ModelConverter> converters = new ArrayList<>();
    private final ObjectMapper mapper;

    private ModelConverters() {
        this.mapper = JsonMapper.builder().build();
        // Register default ServerModelResolver
        converters.add(new ServerModelResolver(mapper));
    }

    public static ModelConverters getInstance() {
        if (instance == null) {
            instance = new ModelConverters();
        }
        return instance;
    }

    public void addConverter(ModelConverter converter) {
        converters.add(0, converter); // Add at beginning for priority
    }

    public ResolvedSchema resolveAsResolvedSchema(AnnotatedType annotatedType) {
        ResolvedSchema resolvedSchema = new ResolvedSchema();

        ModelConverterContextImpl context = new ModelConverterContextImpl(converters);
        Schema schema = context.resolve(annotatedType);

        resolvedSchema.schema = schema;
        resolvedSchema.referencedSchemas = context.getDefinedModels();

        return resolvedSchema;
    }

    public Schema resolve(AnnotatedType annotatedType) {
        ModelConverterContextImpl context = new ModelConverterContextImpl(converters);
        return context.resolve(annotatedType);
    }

    private static class ModelConverterContextImpl implements ModelConverterContext {
        private final List<ModelConverter> converters;
        private final Map<String, Schema> modelByName = new LinkedHashMap<>();

        public ModelConverterContextImpl(List<ModelConverter> converters) {
            this.converters = new ArrayList<>(converters);
        }

        @Override
        public Schema resolve(AnnotatedType type) {
            Iterator<ModelConverter> chain = converters.iterator();
            if (chain.hasNext()) {
                return chain.next().resolve(type, this, chain);
            }
            return null;
        }

        @Override
        public void defineModel(String name, Schema schema, AnnotatedType type, String prevName) {
            defineModel(name, schema);
        }

        @Override
        public void defineModel(String name, Schema schema) {
            if (name != null && schema != null) {
                modelByName.put(name, schema);
            }
        }

        public Map<String, Schema> getDefinedModels() {
            return new LinkedHashMap<>(modelByName);
        }
    }
}
