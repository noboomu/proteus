package io.sinistral.proteus.openapi.models.parameters;

import io.sinistral.proteus.openapi.models.media.Content;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI request body object describing operation input. */
public class RequestBody {

    /** Creates the object. */
    public RequestBody() {}
    /** the description. */
    private String description;
    /** the content. */
    private Content content;
    /** the required. */
    private Boolean required;
    /** the reference. */
    private String $ref;
    /** the extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the description.
     *
     * @return the description, or null when unset
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description, fluent style.
     *
     * @param description the description
     * @return this instance
     */
    public RequestBody description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the content.
     *
     * @return the content, or null when unset
     */
    public Content getContent() {
        return content;
    }

    /**
     * Sets the content.
     *
     * @param content the content
     */
    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * Sets the content, fluent style.
     *
     * @param content the content
     * @return this instance
     */
    public RequestBody content(Content content) {
        this.content = content;
        return this;
    }

    /**
     * Returns the required flag.
     *
     * @return the required flag, or null when unset
     */
    public Boolean getRequired() {
        return required;
    }

    /**
     * Sets the required flag.
     *
     * @param required the required flag
     */
    public void setRequired(Boolean required) {
        this.required = required;
    }

    /**
     * Sets the required flag, fluent style.
     *
     * @param required the required flag
     * @return this instance
     */
    public RequestBody required(Boolean required) {
        this.required = required;
        return this;
    }

    /**
     * Returns the reference value.
     *
     * @return the reference value, or null when unset
     */
    public String get$ref() {
        return $ref;
    }

    /**
     * Sets the reference value.
     *
     * @param $ref the reference value
     */
    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    /**
     * Processes this element.
    *
    * @param $ref the value
    * @return the result
     */
    public RequestBody $ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    /**
     * Returns the extension map.
     *
     * @return the extension map, or null when unset
     */
    @com.fasterxml.jackson.annotation.JsonAnyGetter
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Sets the extension map.
     *
     * @param extensions the extension map
     */
    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    /**
     * Adds an entry to the extension value.
     *
     * @param name the entry
     * @param value the entry
     */
    @com.fasterxml.jackson.annotation.JsonAnySetter
    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) {
            return;
        }
        if (this.extensions == null) {
            this.extensions = new LinkedHashMap<>();
        }
        this.extensions.put(name, value);
    }

    /**
     * Processes this element.
    *
    * @param o the value
    * @return the result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RequestBody)) return false;
        RequestBody that = (RequestBody) o;
        return Objects.equals(description, that.description) &&
            Objects.equals(required, that.required) &&
            Objects.equals($ref, that.$ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, required, $ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public String toString() {
        return "RequestBody{" +
            "description='" + description + '\'' +
            ", required=" + required +
            '}';
    }
}
