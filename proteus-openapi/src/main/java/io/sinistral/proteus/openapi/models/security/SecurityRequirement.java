package io.sinistral.proteus.openapi.models.security;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/** OpenAPI security requirement naming required schemes. */
public class SecurityRequirement extends LinkedHashMap<String, List<String>> {

    /** Creates the object. */
    public SecurityRequirement() {}

    /**
     * Adds an entry to the list value.
     *
     * @param name the entry
     * @param item the entry
    * @return the result
     */
    public SecurityRequirement addList(String name, List<String> item) {
        this.put(name, item);
        return this;
    }

    /**
     * Adds an entry to the list value.
     *
     * @param name the entry
    * @return the result
     */
    public SecurityRequirement addList(String name) {
        this.put(name, new ArrayList<>());
        return this;
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
        if (!(o instanceof SecurityRequirement)) return false;
        return super.equals(o);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}
