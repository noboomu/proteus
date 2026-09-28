package io.sinistral.proteus.openapi.models;

import io.sinistral.proteus.openapi.models.parameters.Parameter;
import io.sinistral.proteus.openapi.models.servers.Server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** OpenAPI path item object listing operations for one path. */
public class PathItem {

    /** Creates the object. */
    public PathItem() {}
    /** the summary. */
    private String summary;
    /** the description. */
    private String description;
    /** the get. */
    private Operation get;
    /** the put. */
    private Operation put;
    /** the post. */
    private Operation post;
    /** the delete. */
    private Operation delete;
    /** the options. */
    private Operation options;
    /** the head. */
    private Operation head;
    /** the patch. */
    private Operation patch;
    /** the trace. */
    private Operation trace;
    /** the servers. */
    private List<Server> servers;
    /** the parameters. */
    private List<Parameter> parameters;
    /** the reference. */
    private String $ref;
    /** the extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the summary.
     *
     * @return the summary, or null when unset
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets the summary.
     *
     * @param summary the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
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
     * Returns the GET operation.
     *
     * @return the GET operation, or null when unset
     */
    public Operation getGet() {
        return get;
    }

    /**
     * Sets the GET operation.
     *
     * @param get the GET operation
     */
    public void setGet(Operation get) {
        this.get = get;
    }

    /**
     * Returns the GET operation.
     *
     * @return the GET operation, or null when unset
    * @param get the value
     */
    public PathItem get(Operation get) {
        this.get = get;
        return this;
    }

    /**
     * Returns the PUT operation.
     *
     * @return the PUT operation, or null when unset
     */
    public Operation getPut() {
        return put;
    }

    /**
     * Sets the PUT operation.
     *
     * @param put the PUT operation
     */
    public void setPut(Operation put) {
        this.put = put;
    }

    /**
     * Sets the PUT operation, fluent style.
     *
     * @param put the PUT operation
     * @return this instance
     */
    public PathItem put(Operation put) {
        this.put = put;
        return this;
    }

    /**
     * Returns the POST operation.
     *
     * @return the POST operation, or null when unset
     */
    public Operation getPost() {
        return post;
    }

    /**
     * Sets the POST operation.
     *
     * @param post the POST operation
     */
    public void setPost(Operation post) {
        this.post = post;
    }

    /**
     * Sets the POST operation, fluent style.
     *
     * @param post the POST operation
     * @return this instance
     */
    public PathItem post(Operation post) {
        this.post = post;
        return this;
    }

    /**
     * Returns the DELETE operation.
     *
     * @return the DELETE operation, or null when unset
     */
    public Operation getDelete() {
        return delete;
    }

    /**
     * Sets the DELETE operation.
     *
     * @param delete the DELETE operation
     */
    public void setDelete(Operation delete) {
        this.delete = delete;
    }

    /**
     * Sets the DELETE operation, fluent style.
     *
     * @param delete the DELETE operation
     * @return this instance
     */
    public PathItem delete(Operation delete) {
        this.delete = delete;
        return this;
    }

    /**
     * Returns the OPTIONS operation.
     *
     * @return the OPTIONS operation, or null when unset
     */
    public Operation getOptions() {
        return options;
    }

    /**
     * Sets the OPTIONS operation.
     *
     * @param options the OPTIONS operation
     */
    public void setOptions(Operation options) {
        this.options = options;
    }

    /**
     * Sets the OPTIONS operation, fluent style.
     *
     * @param options the OPTIONS operation
     * @return this instance
     */
    public PathItem options(Operation options) {
        this.options = options;
        return this;
    }

    /**
     * Returns the HEAD operation.
     *
     * @return the HEAD operation, or null when unset
     */
    public Operation getHead() {
        return head;
    }

    /**
     * Sets the HEAD operation.
     *
     * @param head the HEAD operation
     */
    public void setHead(Operation head) {
        this.head = head;
    }

    /**
     * Sets the HEAD operation, fluent style.
     *
     * @param head the HEAD operation
     * @return this instance
     */
    public PathItem head(Operation head) {
        this.head = head;
        return this;
    }

    /**
     * Returns the PATCH operation.
     *
     * @return the PATCH operation, or null when unset
     */
    public Operation getPatch() {
        return patch;
    }

    /**
     * Sets the PATCH operation.
     *
     * @param patch the PATCH operation
     */
    public void setPatch(Operation patch) {
        this.patch = patch;
    }

    /**
     * Sets the PATCH operation, fluent style.
     *
     * @param patch the PATCH operation
     * @return this instance
     */
    public PathItem patch(Operation patch) {
        this.patch = patch;
        return this;
    }

    /**
     * Returns the TRACE operation.
     *
     * @return the TRACE operation, or null when unset
     */
    public Operation getTrace() {
        return trace;
    }

    /**
     * Sets the TRACE operation.
     *
     * @param trace the TRACE operation
     */
    public void setTrace(Operation trace) {
        this.trace = trace;
    }

    /**
     * Sets the TRACE operation, fluent style.
     *
     * @param trace the TRACE operation
     * @return this instance
     */
    public PathItem trace(Operation trace) {
        this.trace = trace;
        return this;
    }

    /**
     * Returns the server list.
     *
     * @return the server list, or null when unset
     */
    public List<Server> getServers() {
        return servers;
    }

    /**
     * Sets the server list.
     *
     * @param servers the server list
     */
    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    /**
     * Adds an entry to the servers value.
     *
     * @param serversItem the entry
     */
    public void addServersItem(Server serversItem) {
        if (this.servers == null) {
            this.servers = new ArrayList<>();
        }
        this.servers.add(serversItem);
    }

    /**
     * Returns the parameter map.
     *
     * @return the parameter map, or null when unset
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * Sets the parameter map.
     *
     * @param parameters the parameter map
     */
    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    /**
     * Adds an entry to the parameters value.
     *
     * @param parametersItem the entry
     */
    public void addParametersItem(Parameter parametersItem) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<>();
        }
        this.parameters.add(parametersItem);
    }

    /**
     * Returns the reference value.
     *
     * @return the reference value, or null when unset
     */
    public String get$ref() {
        return $ref;
    }

    /**
     * Sets the reference value.
     *
     * @param $ref the reference value
     */
    public void set$ref(String $ref) {
        this.$ref = $ref;
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
        if (!(o instanceof PathItem)) return false;
        PathItem pathItem = (PathItem) o;
        return Objects.equals(summary, pathItem.summary) &&
            Objects.equals($ref, pathItem.$ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(summary, $ref);
    }
}
