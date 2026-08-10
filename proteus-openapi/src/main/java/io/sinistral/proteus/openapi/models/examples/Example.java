package io.sinistral.proteus.openapi.models.examples;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Example {
    private String summary;
    private String description;
    private Object value;
    private String externalValue;
    private String $ref;
    private Map<String, Object> extensions;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getExternalValue() {
        return externalValue;
    }

    public void setExternalValue(String externalValue) {
        this.externalValue = externalValue;
    }

    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
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
        if (!(o instanceof Example)) return false;
        Example example = (Example) o;
        return Objects.equals(summary, example.summary) &&
            Objects.equals(value, example.value) &&
            Objects.equals($ref, example.$ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(summary, value, $ref);
    }
}
