package io.sinistral.proteus.openapi.models.security;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class SecurityRequirement extends LinkedHashMap<String, List<String>> {

    public SecurityRequirement addList(String name, List<String> item) {
        this.put(name, item);
        return this;
    }

    public SecurityRequirement addList(String name) {
        this.put(name, new ArrayList<>());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SecurityRequirement)) return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
