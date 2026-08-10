package io.sinistral.proteus.openapi.models.responses;

import java.util.LinkedHashMap;
import java.util.Map;

@tools.jackson.databind.annotation.JsonSerialize(using = ApiResponsesSerializer.class)
@tools.jackson.databind.annotation.JsonDeserialize(using = ApiResponsesDeserializer.class)
public class ApiResponses extends LinkedHashMap<String, ApiResponse> {
    private static final String DEFAULT = "default";
    private Map<String, Object> extensions;

    public ApiResponse getDefault() {
        return get(DEFAULT);
    }

    public void setDefault(ApiResponse defaultResponse) {
        if (defaultResponse == null) {
            remove(DEFAULT);
        } else {
            put(DEFAULT, defaultResponse);
        }
    }

    public ApiResponses _default(ApiResponse defaultResponse) {
        setDefault(defaultResponse);
        return this;
    }

    public ApiResponses addApiResponse(String name, ApiResponse item) {
        this.put(name, item);
        return this;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) return;
        if (extensions == null) extensions = new LinkedHashMap<>();
        extensions.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof ApiResponses && super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
