package io.sinistral.proteus.openapi.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.dataformat.yaml.YAMLMapper;

/**
 * YAML serialization for generated OpenAPI documents.
 */
public final class Yaml {
    /** the  m a p p e r. */
    private static final YAMLMapper MAPPER = YAMLMapper.builder()
        .changeDefaultPropertyInclusion(ignored ->
            JsonInclude.Value.construct(
                JsonInclude.Include.NON_NULL,
                JsonInclude.Include.NON_NULL
            )
        )
        .build();

    private Yaml() {}

    /**
     * Processes this element.
    *
    * @param value the value
    * @return the result
     */
    public static String pretty(Object value) {
        try {
            return MAPPER.writeValueAsString(Json.mapper().valueToTree(value));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to serialize OpenAPI YAML", e);
        }
    }
}
