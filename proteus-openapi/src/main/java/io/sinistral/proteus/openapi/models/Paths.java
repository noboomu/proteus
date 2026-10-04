package io.sinistral.proteus.openapi.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI paths container mapping path templates to path items. */
@tools.jackson.databind.annotation.JsonSerialize(using = PathsSerializer.class)
@tools.jackson.databind.annotation.JsonDeserialize(using = PathsDeserializer.class)
public class Paths extends LinkedHashMap<String, PathItem> {

    /** Creates the object. */
    public Paths() {}
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Adds an entry to the path value.
     *
     * @param name the entry
     * @param item the entry
     */
    public void addPathItem(String name, PathItem item) {
        this.put(name, item);
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
        if (!(o instanceof Paths)) return false;
        if (!super.equals(o)) return false;
        Paths paths = (Paths) o;
        return Objects.equals(extensions, paths.extensions);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), extensions);
    }
}
