package io.sinistral.proteus.openapi.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ExternalDocumentation {
    private String description;
    private String url;
    private Map<String, Object> extensions;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExternalDocumentation description(String description) {
        this.description = description;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ExternalDocumentation url(String url) {
        this.url = url;
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
        if (!(o instanceof ExternalDocumentation)) return false;
        ExternalDocumentation that = (ExternalDocumentation) o;
        return Objects.equals(description, that.description) &&
            Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, url);
    }
}
