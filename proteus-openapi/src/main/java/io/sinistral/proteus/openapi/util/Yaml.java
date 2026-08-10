package io.sinistral.proteus.openapi.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.dataformat.yaml.YAMLMapper;

/**
 * YAML serialization for generated OpenAPI documents.
 */
public final class Yaml {
    private static final YAMLMapper MAPPER = YAMLMapper.builder()
        .changeDefaultPropertyInclusion(ignored ->
            JsonInclude.Value.construct(
                JsonInclude.Include.NON_NULL,
                JsonInclude.Include.NON_NULL
            )
        )
        .build();

    private Yaml() {}

    public static String pretty(Object value) {
        try {
            return MAPPER.writeValueAsString(Json.mapper().valueToTree(value));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to serialize OpenAPI YAML", e);
        }
    }
}
