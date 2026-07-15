package io.sinistral.proteus.openapi.converter;

import io.sinistral.proteus.openapi.models.media.Schema;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Result of schema resolution containing the main schema and any referenced schemas
 */
public class ResolvedSchema {
    public Schema schema;
    public Map<String, Schema> referencedSchemas = new LinkedHashMap<>();

    public ResolvedSchema() {
    }

    public ResolvedSchema(Schema schema) {
        this.schema = schema;
    }
}
