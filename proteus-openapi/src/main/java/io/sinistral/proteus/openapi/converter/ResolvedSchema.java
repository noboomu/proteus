package io.sinistral.proteus.openapi.converter;

import io.sinistral.proteus.openapi.models.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Result of schema resolution containing the main schema and any referenced schemas
 */
public class ResolvedSchema {
    /** The schema. */
    public Schema schema;
    /** The value. */
    public Map<String, Schema> referencedSchemas = new LinkedHashMap<>();

    /** Creates an empty resolved schema. */
    public ResolvedSchema() {
    }

    /** Creates a resolved schema wrapping the given schema.
     *
     * @param schema the resolved schema
     */
    public ResolvedSchema(Schema schema) {
        this.schema = schema;
    }
}
