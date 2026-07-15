package io.sinistral.proteus.openapi.util;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.json.JsonMapper;

/**
 * JSON utilities using Jackson 3.0
 */
public class Json {
    private static ObjectMapper mapper;

    static {
        mapper = JsonMapper.builder()
            .enable(SerializationFeature.INDENT_OUTPUT)
            .build();
    }

    public static ObjectMapper mapper() {
        return mapper;
    }

    public static void setMapper(ObjectMapper mapper) {
        Json.mapper = mapper;
    }

    public static String pretty(Object value) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            return value.toString();
        }
    }

    public static String pretty(String json) {
        try {
            Object obj = mapper.readValue(json, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            return json;
        }
    }
}
