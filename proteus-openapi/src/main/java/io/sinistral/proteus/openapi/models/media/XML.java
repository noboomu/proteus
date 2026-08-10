package io.sinistral.proteus.openapi.models.media;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class XML {
    private String name;
    private String namespace;
    private String prefix;
    private Boolean attribute;
    private Boolean wrapped;
    private Map<String, Object> extensions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getAttribute() {
        return attribute;
    }

    public void setAttribute(Boolean attribute) {
        this.attribute = attribute;
    }

    public Boolean getWrapped() {
        return wrapped;
    }

    public void setWrapped(Boolean wrapped) {
        this.wrapped = wrapped;
    }

    @com.fasterxml.jackson.annotation.JsonAnyGetter
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof XML)) return false;
        XML xml = (XML) o;
        return Objects.equals(name, xml.name) &&
            Objects.equals(namespace, xml.namespace) &&
            Objects.equals(prefix, xml.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, namespace, prefix);
    }
}
