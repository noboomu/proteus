package io.sinistral.proteus.openapi.models.servers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Server {
    private String url;
    private String description;
    private Map<String, ServerVariable> variables;
    private Map<String, Object> extensions;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Server url(String url) {
        this.url = url;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Server description(String description) {
        this.description = description;
        return this;
    }

    public Map<String, ServerVariable> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, ServerVariable> variables) {
        this.variables = variables;
    }

    public void addVariablesItem(String name, ServerVariable variablesItem) {
        if (this.variables == null) {
            this.variables = new LinkedHashMap<>();
        }
        this.variables.put(name, variablesItem);
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
        if (!(o instanceof Server)) return false;
        Server server = (Server) o;
        return Objects.equals(url, server.url) &&
            Objects.equals(description, server.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, description);
    }
}
