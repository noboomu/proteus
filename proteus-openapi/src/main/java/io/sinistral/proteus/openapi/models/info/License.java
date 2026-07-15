package io.sinistral.proteus.openapi.models.info;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class License {
    private String name;
    private String identifier;
    private String url;
    private Map<String, Object> extensions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
        if (!(o instanceof License)) return false;
        License license = (License) o;
        return Objects.equals(name, license.name) &&
            Objects.equals(url, license.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
