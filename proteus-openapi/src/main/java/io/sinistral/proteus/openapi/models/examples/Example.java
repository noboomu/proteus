package io.sinistral.proteus.openapi.models.examples;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI example object holding a sample value for a media type. */
public class Example {

    /** Creates an empty example object. */
    public Example() {}
    /** the summary. */
    private String summary;
    /** the description. */
    private String description;
    /** the value. */
    private Object value;
    /** the external value. */
    private String externalValue;
    /** the reference. */
    private String $ref;
    /** the extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the summary.
     *
     * @return the summary, or null when unset
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the summary.
     * @param summary the value to set
     *
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

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
     * @param description the value to set
     *
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the value.
     *
     * @return the value, or null when unset
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value.
     * @param value the value to set
     *
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Returns the external value.
     *
     * @return the external value, or null when unset
     */
    public String getExternalValue() {
        return externalValue;
    }

    /**
     * Sets the external value.
     * @param externalValue the value to set
     *
     */
    public void setExternalValue(String externalValue) {
        this.externalValue = externalValue;
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
     * @param extensions the value to set
     *
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
        if (!(o instanceof Example)) return false;
        Example example = (Example) o;
        return Objects.equals(summary, example.summary) &&
            Objects.equals(value, example.value) &&
            Objects.equals($ref, example.$ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(summary, value, $ref);
    }
}
