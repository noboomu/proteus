package io.sinistral.proteus.openapi.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI external documentation object with a URL reference. */
public class ExternalDocumentation {

    /** Creates the object. */
    public ExternalDocumentation() {}
    /** The description. */
    private String description;
    /** The url. */
    private String url;
    /** The extensions. */
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
     * Operates on the description.
    *
    * @param description the value
    * @return the result
     */
    public ExternalDocumentation description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the URL.
     *
     * @return the URL, or null when unset
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL.
     *
     * @param url the URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Operates on the URL.
    *
    * @param url the value
    * @return the result
     */
    public ExternalDocumentation url(String url) {
        this.url = url;
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
     * Adds a vendor extension entry, ignoring names that are not {@code x-} prefixed.
     *
     * @param name the extension name
     * @param value the extension value
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
     * Operates on the value.
    *
    * @param o the value
    * @return the result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExternalDocumentation)) return false;
        ExternalDocumentation that = (ExternalDocumentation) o;
        return Objects.equals(description, that.description) &&
            Objects.equals(url, that.url);
    }

    /**
     * Operates on the value.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, url);
    }
}
