package io.sinistral.proteus.openapi.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * JSON utilities using Jackson 3.
 */
public final class Json {
    /** the mapper. */
    private static ObjectMapper mapper = JsonMapper.builder()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .changeDefaultPropertyInclusion(ignored ->
            JsonInclude.Value.construct(
                JsonInclude.Include.NON_NULL,
                JsonInclude.Include.NON_NULL
            )
        )
        .build();

    private Json() {}

    /**
     * Operates on the value.
    *
    * @return the result
     */
    public static ObjectMapper mapper() {
        return mapper;
    }

    /**
     * Sets the value.
     *
     * @param mapper the value
     */
    public static void setMapper(ObjectMapper mapper) {
        if (mapper == null) {
            throw new IllegalArgumentException("mapper must not be null");
        }
        Json.mapper = mapper;
    }

    /**
     * Operates on the value.
    *
    * @param value the value
    * @return the result
     */
    public static String pretty(Object value) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to serialize OpenAPI JSON", e);
        }
    }

    /**
     * Operates on the value.
    *
    * @param json the value
    * @return the result
     */
    public static String pretty(String json) {
        try {
            Object value = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to format OpenAPI JSON", e);
        }
    }
}
