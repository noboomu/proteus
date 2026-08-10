package io.sinistral.proteus.openapi.models.callbacks;

import io.sinistral.proteus.openapi.models.PathItem;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@tools.jackson.databind.annotation.JsonSerialize(using = CallbackSerializer.class)
@tools.jackson.databind.annotation.JsonDeserialize(using = CallbackDeserializer.class)
public class Callback extends LinkedHashMap<String, PathItem> {
    private String $ref;
    private Map<String, Object> extensions;

    public Callback addPathItem(String name, PathItem item) {
        this.put(name, item);
        return this;
    }

    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    public Callback $ref(String $ref) {
        this.$ref = $ref;
        return this;
    }

    public Map<String, Object> getExtensions() {
        return extensions;
    }

    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    public void addExtension(String name, Object value) {
        if (name == null || name.isEmpty() || !name.startsWith("x-")) return;
        if (extensions == null) extensions = new LinkedHashMap<>();
        extensions.put(name, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Callback)) return false;
        if (!super.equals(o)) return false;
        Callback callback = (Callback) o;
        return Objects.equals($ref, callback.$ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), $ref);
    }
}
