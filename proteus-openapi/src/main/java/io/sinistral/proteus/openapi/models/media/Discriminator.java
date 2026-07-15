package io.sinistral.proteus.openapi.models.media;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Discriminator {
    private String propertyName;
    private Map<String, String> mapping;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Discriminator propertyName(String propertyName) {
        this.propertyName = propertyName;
        return this;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping(Map<String, String> mapping) {
        this.mapping = mapping;
    }

    public void addMapping(String key, String value) {
        if (this.mapping == null) {
            this.mapping = new LinkedHashMap<>();
        }
        this.mapping.put(key, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discriminator)) return false;
        Discriminator that = (Discriminator) o;
        return Objects.equals(propertyName, that.propertyName) &&
            Objects.equals(mapping, that.mapping);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyName, mapping);
    }
}
