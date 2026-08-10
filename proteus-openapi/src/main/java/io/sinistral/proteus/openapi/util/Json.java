package io.sinistral.proteus.openapi.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * JSON utilities using Jackson 3.
 */
public final class Json {
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

    public static ObjectMapper mapper() {
        return mapper;
    }

    public static void setMapper(ObjectMapper mapper) {
        if (mapper == null) {
            throw new IllegalArgumentException("mapper must not be null");
        }
        Json.mapper = mapper;
    }

    public static String pretty(Object value) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to serialize OpenAPI JSON", e);
        }
    }

    public static String pretty(String json) {
        try {
            Object value = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to format OpenAPI JSON", e);
        }
    }
}
