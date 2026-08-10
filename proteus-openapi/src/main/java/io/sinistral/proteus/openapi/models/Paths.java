package io.sinistral.proteus.openapi.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@tools.jackson.databind.annotation.JsonSerialize(using = PathsSerializer.class)
@tools.jackson.databind.annotation.JsonDeserialize(using = PathsDeserializer.class)
public class Paths extends LinkedHashMap<String, PathItem> {
    private Map<String, Object> extensions;

    public void addPathItem(String name, PathItem item) {
        this.put(name, item);
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
        if (!(o instanceof Paths)) return false;
        if (!super.equals(o)) return false;
        Paths paths = (Paths) o;
        return Objects.equals(extensions, paths.extensions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), extensions);
    }
}
