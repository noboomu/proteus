package io.sinistral.proteus.openapi.models.servers;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI server object with base URL and variables. */
public class Server {

    /** Creates the object. */
    public Server() {}
    /** The url. */
    private String url;
    /** The description. */
    private String description;
    /** The variables. */
    private Map<String, ServerVariable> variables;
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the URL.
     *
     * @return the URL, or null when unset
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL.
     *
     * @param url the URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Sets the url and returns this instance.
     *
     * @param url the URL
     * @return this instance
     */
    public Server url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Returns the description.
     *
     * @return the description, or null when unset
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description and returns this instance.
     *
     * @param description the description
     * @return this instance
     */
    public Server description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the server variable map.
     *
     * @return the server variable map, or null when unset
     */
    public Map<String, ServerVariable> getVariables() {
        return variables;
    }

    /**
     * Sets the server variable map.
     *
     * @param variables the server variable map
     */
    public void setVariables(Map<String, ServerVariable> variables) {
        this.variables = variables;
    }

    /**
     * Adds an entry to the variables value.
     *
     * @param name the entry
     * @param variablesItem the entry
     */
    public void addVariablesItem(String name, ServerVariable variablesItem) {
        if (this.variables == null) {
            this.variables = new LinkedHashMap<>();
        }
        this.variables.put(name, variablesItem);
    }

    /**
     * Returns the extension map.
     *
     * @return the extension map, or null when unset
     */
    @com.fasterxml.jackson.annotation.JsonAnyGetter
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Sets the extension map.
     *
     * @param extensions the extension map
     */
    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    /**
     * Adds an entry to the extension value.
     *
     * @param name the entry
     * @param value the entry
     */
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

    /**
     * Processes this element.
    *
    * @param o the value
    * @return the result
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Server)) return false;
        Server server = (Server) o;
        return Objects.equals(url, server.url) &&
            Objects.equals(description, server.description);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(url, description);
    }
}
