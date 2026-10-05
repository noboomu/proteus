package io.sinistral.proteus.openapi.converter;

import io.sinistral.proteus.openapi.jaxrs2.JacksonModelResolver;
import io.sinistral.proteus.openapi.jaxrs2.ServerModelResolver;
import io.sinistral.proteus.openapi.models.media.Schema;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Registry and execution context for Java type to OpenAPI schema converters.
 */
public final class ModelConverters {
    /** The  i n s t a n c e. */
    private static final ModelConverters INSTANCE = create(JsonMapper.builder().build());

    /** The converters. */
    private final CopyOnWriteArrayList<ModelConverter> converters = new CopyOnWriteArrayList<>();

    private ModelConverters(ObjectMapper mapper) {
        configure(mapper);
        ServiceLoader.load(ModelConverter.class).forEach(this::addConverter);
    }

    /**
     * Returns the instance value.
     *
     * @return the instance value, or null when unset
     */
    public static ModelConverters getInstance() {
        return INSTANCE;
    }

    /**
     * Creates an isolated converter registry backed by the supplied Jackson 3 mapper.
    *
    * @param mapper the value
    * @return the result
     */
    public static ModelConverters create(ObjectMapper mapper) {
        if (mapper == null) {
            throw new IllegalArgumentException("mapper must not be null");
        }
        return new ModelConverters(mapper);
    }

    /**
     * Replaces the built-in converters so schema introspection uses the same mapper
     * configuration as the application (mix-ins, naming strategy, visibility, etc.).
     * Custom converters remain registered ahead of the built-ins.
     *
     * @param mapper the mapper to use for schema introspection
     */
    public synchronized void configure(ObjectMapper mapper) {
        converters.removeIf(converter ->
            converter instanceof ServerModelResolver || converter instanceof JacksonModelResolver
        );
        converters.add(new ServerModelResolver(mapper));
        converters.add(new JacksonModelResolver(mapper));
    }

    /**
     * Adds an entry to the converter value.
     *
     * @param converter the entry
     */
    public void addConverter(ModelConverter converter) {
        if (converter == null) {
            return;
        }
        converters.removeIf(existing -> existing.getClass().equals(converter.getClass()));
        converters.add(0, converter);
    }

    /**
     * Returns the converters value.
     *
     * @return the converters value, never null
     */
    public List<ModelConverter> getConverters() {
        return Collections.unmodifiableList(converters);
    }

    /**
     * Processes this element.
    *
    * @param type the value
    * @return the result
     */
    public ResolvedSchema resolveAsResolvedSchema(AnnotatedType type) {
        if (type == null || type.getType() == null) {
            return new ResolvedSchema();
        }
        ModelConverterContextImpl context = new ModelConverterContextImpl(converters);
        ResolvedSchema resolved = new ResolvedSchema();
        resolved.schema = context.resolve(type);
        resolved.referencedSchemas = context.getDefinedModels();
        return resolved;
    }

    /**
     * Processes this element.
    *
    * @param type the value
    * @return the result
     */
    public Schema resolve(AnnotatedType type) {
        return new ModelConverterContextImpl(converters).resolve(type);
    }

    private static final class ModelConverterContextImpl implements ModelConverterContext {
        /** The converters. */
        private final List<ModelConverter> converters;
        /** The model by name. */
        private final Map<String, Schema> modelByName = new LinkedHashMap<>();
        /** The model by type. */
        private final Map<String, Schema> modelByType = new LinkedHashMap<>();
        /** The resolving types. */
        private final Set<String> resolvingTypes = new LinkedHashSet<>();

        private ModelConverterContextImpl(List<ModelConverter> converters) {
            this.converters = new ArrayList<>(converters);
        }

        @Override
        /**
         * Processes this element.
        *
        * @param type the value
        * @return the result
         */
        public Schema resolve(AnnotatedType type) {
            if (type == null || type.getType() == null) {
                return null;
            }
            String key = typeKey(type);
            Schema known = modelByType.get(key);
            if (known != null) {
                return known;
            }
            if (!resolvingTypes.add(key)) {
                return modelByType.get(key);
            }

            try {
                Iterator<ModelConverter> chain = converters.iterator();
                Schema resolved = chain.hasNext()
                    ? chain.next().resolve(type, this, chain)
                    : null;
                if (resolved != null) {
                    modelByType.put(key, resolved);
                }
                return resolved;
            } finally {
                resolvingTypes.remove(key);
            }
        }

        @Override
        /**
         * Processes this element.
        *
        * @param name the value
        * @param schema the value
        * @param type the value
        * @param previousName the value
         */
        public void defineModel(String name, Schema schema, AnnotatedType type, String previousName) {
            if (previousName != null && !previousName.equals(name)) {
                modelByName.remove(previousName);
            }
            defineModel(name, schema);
            if (type != null && type.getType() != null && schema != null) {
                Schema reference = new Schema();
                reference.set$ref("#/components/schemas/" + name);
                modelByType.put(typeKey(type), reference);
            }
        }

        @Override
        /**
         * Processes this element.
        *
        * @param name the value
        * @param schema the value
         */
        public void defineModel(String name, Schema schema) {
            if (name != null && !name.isBlank() && schema != null) {
                modelByName.put(name, schema);
            }
        }

        @Override
        /**
         * Returns the define model value.
         *
         * @return the define model value, never null
         */
        public Map<String, Schema> getDefinedModels() {
            return Collections.unmodifiableMap(new LinkedHashMap<>(modelByName));
        }

        private String typeKey(AnnotatedType type) {
            StringBuilder key = new StringBuilder(type.getType().getTypeName());
            if (type.getJsonViewAnnotation() != null) {
                for (Class<?> view : type.getJsonViewAnnotation().value()) {
                    key.append("|view:").append(view.getName());
                }
            }
            appendAnnotations(key, type.getAnnotations());
            if (type.getCtxAnnotations() != null) {
                appendAnnotations(key, type.getCtxAnnotations().toArray(Annotation[]::new));
            }
            return key.toString();
        }

        private void appendAnnotations(StringBuilder key, Annotation[] annotations) {
            if (annotations == null) {
                return;
            }
            for (Annotation annotation : annotations) {
                key.append('|').append(annotation.toString());
            }
        }
    }
}
