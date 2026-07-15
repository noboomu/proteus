package io.sinistral.proteus.openapi.converter;

import io.sinistral.proteus.openapi.models.media.Schema;

import java.util.Iterator;

/**
 * Interface for converting Java types to OpenAPI Schema objects
 */
public interface ModelConverter {

    /**
     * Resolve an annotated type into a Schema
     *
     * @param type the annotated type to resolve
     * @param context the model converter context
     * @param chain iterator for chaining converters
     * @return the resolved Schema, or null if this converter cannot handle the type
     */
    Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> chain);
}
