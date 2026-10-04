package io.sinistral.proteus.openapi.models.media;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI XML metadata object for schema serialization. */
public class XML {

    /** Creates the object. */
    public XML() {}
    /** The name. */
    private String name;
    /** The namespace. */
    private String namespace;
    /** The prefix. */
    private String prefix;
    /** The attribute. */
    private Boolean attribute;
    /** The wrapped. */
    private Boolean wrapped;
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
     * Returns the namespace.
     *
     * @return the namespace, or null when unset
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * Sets the namespace.
     *
     * @param namespace the namespace
     */
    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    /**
     * Returns the prefix.
     *
     * @return the prefix, or null when unset
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the prefix.
     *
     * @param prefix the prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Returns the attribute value.
     *
     * @return the attribute value, or null when unset
     */
    public Boolean getAttribute() {
        return attribute;
    }

    /**
     * Sets the attribute value.
     *
     * @param attribute the attribute value
     */
    public void setAttribute(Boolean attribute) {
        this.attribute = attribute;
    }

    /**
     * Returns the wrapped value.
     *
     * @return the wrapped value, or null when unset
     */
    public Boolean getWrapped() {
        return wrapped;
    }

    /**
     * Sets the wrapped value.
     *
     * @param wrapped the wrapped value
     */
    public void setWrapped(Boolean wrapped) {
        this.wrapped = wrapped;
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
        if (!(o instanceof XML)) return false;
        XML xml = (XML) o;
        return Objects.equals(name, xml.name) &&
            Objects.equals(namespace, xml.namespace) &&
            Objects.equals(prefix, xml.prefix);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, namespace, prefix);
    }
}
