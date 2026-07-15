package io.sinistral.proteus.openapi.models.parameters;

import io.sinistral.proteus.openapi.models.media.Content;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class RequestBody {
    private String description;
    private Content content;
    private Boolean required;
    private String $ref;
    private Map<String, Object> extensions;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestBody description(String description) {
        this.description = description;
        return this;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public RequestBody content(Content content) {
        this.content = content;
        return this;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public RequestBody required(Boolean required) {
        this.required = required;
        return this;
    }

    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    public RequestBody $ref(String $ref) {
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
        if (!(o instanceof RequestBody)) return false;
        RequestBody that = (RequestBody) o;
        return Objects.equals(description, that.description) &&
            Objects.equals(required, that.required) &&
            Objects.equals($ref, that.$ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, required, $ref);
    }

    @Override
    public String toString() {
        return "RequestBody{" +
            "description='" + description + '\'' +
            ", required=" + required +
            '}';
    }
}
