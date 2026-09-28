package io.sinistral.proteus.openapi.models.responses;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * OpenAPI responses container mapping status codes and {@code default} to response objects.
 */
@tools.jackson.databind.annotation.JsonSerialize(using = ApiResponsesSerializer.class)
@tools.jackson.databind.annotation.JsonDeserialize(using = ApiResponsesDeserializer.class)
/** OpenAPI responses container mapping status codes to responses. */
public class ApiResponses extends LinkedHashMap<String, ApiResponse> {

    /** Creates the object. */
    public ApiResponses() {}
    /** the  d e f a u l t. */
    private static final String DEFAULT = "default";
    /** the extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the default response.
     *
     * @return the default response, or null when unset
     */
    public ApiResponse getDefault() {
        return get(DEFAULT);
    }

    /**
     * Sets the default response; a null value removes it.
     *
     * @param defaultResponse the default response
     */
    public void setDefault(ApiResponse defaultResponse) {
        if (defaultResponse == null) {
            remove(DEFAULT);
        } else {
            put(DEFAULT, defaultResponse);
        }
    }

    /**
     * Sets the default response, fluent style.
     *
     * @param defaultResponse the default response
     * @return this instance
     */
    public ApiResponses _default(ApiResponse defaultResponse) {
        setDefault(defaultResponse);
        return this;
    }

    /**
     * Adds one response under the given status code name.
     *
     * @param name the status code or {@code default}
     * @param item the response object
     * @return this instance
     */
    public ApiResponses addApiResponse(String name, ApiResponse item) {
        this.put(name, item);
        return this;
    }

    /**
     * Returns the extension properties.
     *
     * @return the extension map, or null when unset
     */
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Sets the extension properties.
     *
     * @param extensions the extension map
     */
    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    /**
     * Adds one extension property; names not starting with {@code x-} are ignored.
     *
     * @param name the extension name
     * @param value the extension value
     */
    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) return;
        if (extensions == null) extensions = new LinkedHashMap<>();
        extensions.put(name, value);
    }

    /**
     * Processes this element.
    *
    * @param o the value
    * @return the result
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof ApiResponses && super.equals(o);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
