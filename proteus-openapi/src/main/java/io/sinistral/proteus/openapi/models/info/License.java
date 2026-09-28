package io.sinistral.proteus.openapi.models.info;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI license object naming the API license. */
public class License {

    /** Creates the object. */
    public License() {}
    /** the name. */
    private String name;
    /** the identifier. */
    private String identifier;
    /** the url. */
    private String url;
    /** the extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the name.
     *
     * @return the name, or null when unset
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the value.
     *
     * @return the value, or null when unset
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value.
     *
     * @param identifier the value
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
     * Adds an entry to the value.
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
     * Operates on the value.
    *
    * @param o the value
    * @return the result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof License)) return false;
        License license = (License) o;
        return Objects.equals(name, license.name) &&
            Objects.equals(url, license.url);
    }

    /**
     * Operates on the value.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
