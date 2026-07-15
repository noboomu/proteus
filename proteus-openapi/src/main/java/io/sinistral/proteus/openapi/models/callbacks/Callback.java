package io.sinistral.proteus.openapi.models.callbacks;

import io.sinistral.proteus.openapi.models.PathItem;

import java.util.LinkedHashMap;
import java.util.Objects;

public class Callback extends LinkedHashMap<String, PathItem> {
    private String $ref;

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
