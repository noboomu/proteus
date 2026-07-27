package io.sinistral.proteus.openapi.converter;

import io.sinistral.proteus.openapi.models.media.Schema;

/**
 * Context for model conversion
 */
public interface ModelConverterContext {

    /**
     * Resolve a type to a Schema
     *
     * @param type the annotated type to resolve
     * @return the resolved Schema
     */
    Schema resolve(AnnotatedType type);

    /**
     * Define a model with a given key
     *
     * @param name the model name/key
     * @param schema the schema to define
     * @param type the annotated type
     * @param prevName previous name if renaming
     */
    void defineModel(String name, Schema schema, AnnotatedType type, String prevName);

    /**
     * Define a model with a given key
     *
     * @param name the model name/key
     * @param schema the schema to define
     */
    void defineModel(String name, Schema schema);

    java.util.Map<String, Schema> getDefinedModels();
}
