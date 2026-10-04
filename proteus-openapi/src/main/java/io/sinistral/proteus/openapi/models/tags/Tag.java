package io.sinistral.proteus.openapi.models.tags;

import io.sinistral.proteus.openapi.models.ExternalDocumentation;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI tag object naming and describing a route group. */
public class Tag {

    /** Creates the object. */
    public Tag() {}
    /** The name. */
    private String name;
    /** The description. */
    private String description;
    /** The external docs. */
    private ExternalDocumentation externalDocs;
    /** The extensions. */
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
     * Sets the name and returns this instance.
     *
     * @param name the name
     * @return this instance
     */
    public Tag name(String name) {
        this.name = name;
        return this;
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
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description and returns this instance.
     *
     * @param description the description
     * @return this instance
     */
    public Tag description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the external documentation.
     *
     * @return the external documentation, or null when unset
     */
    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    /**
     * Sets the external documentation.
     *
     * @param externalDocs the external documentation
     */
    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    /**
     * Sets the external docs and returns this instance.
     *
     * @param externalDocs the external documentation
     * @return this instance
     */
    public Tag externalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
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
        if (!(o instanceof Tag)) return false;
        Tag tag = (Tag) o;
        return Objects.equals(name, tag.name);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public String toString() {
        return "Tag{name='" + name + "'}";
    }
}
