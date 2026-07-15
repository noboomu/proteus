package io.sinistral.proteus.openapi.models;

import io.sinistral.proteus.openapi.models.parameters.Parameter;
import io.sinistral.proteus.openapi.models.servers.Server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PathItem {
    private String summary;
    private String description;
    private Operation get;
    private Operation put;
    private Operation post;
    private Operation delete;
    private Operation options;
    private Operation head;
    private Operation patch;
    private Operation trace;
    private List<Server> servers;
    private List<Parameter> parameters;
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

    public Operation getGet() {
        return get;
    }

    public void setGet(Operation get) {
        this.get = get;
    }

    public PathItem get(Operation get) {
        this.get = get;
        return this;
    }

    public Operation getPut() {
        return put;
    }

    public void setPut(Operation put) {
        this.put = put;
    }

    public PathItem put(Operation put) {
        this.put = put;
        return this;
    }

    public Operation getPost() {
        return post;
    }

    public void setPost(Operation post) {
        this.post = post;
    }

    public PathItem post(Operation post) {
        this.post = post;
        return this;
    }

    public Operation getDelete() {
        return delete;
    }

    public void setDelete(Operation delete) {
        this.delete = delete;
    }

    public PathItem delete(Operation delete) {
        this.delete = delete;
        return this;
    }

    public Operation getOptions() {
        return options;
    }

    public void setOptions(Operation options) {
        this.options = options;
    }

    public PathItem options(Operation options) {
        this.options = options;
        return this;
    }

    public Operation getHead() {
        return head;
    }

    public void setHead(Operation head) {
        this.head = head;
    }

    public PathItem head(Operation head) {
        this.head = head;
        return this;
    }

    public Operation getPatch() {
        return patch;
    }

    public void setPatch(Operation patch) {
        this.patch = patch;
    }

    public PathItem patch(Operation patch) {
        this.patch = patch;
        return this;
    }

    public Operation getTrace() {
        return trace;
    }

    public void setTrace(Operation trace) {
        this.trace = trace;
    }

    public PathItem trace(Operation trace) {
        this.trace = trace;
        return this;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public void addServersItem(Server serversItem) {
        if (this.servers == null) {
            this.servers = new ArrayList<>();
        }
        this.servers.add(serversItem);
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public void addParametersItem(Parameter parametersItem) {
        if (this.parameters == null) {
            this.parameters = new ArrayList<>();
        }
        this.parameters.add(parametersItem);
    }

    public String get$ref() {
        return $ref;
    }

    public void set$ref(String $ref) {
        this.$ref = $ref;
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
        if (!(o instanceof PathItem)) return false;
        PathItem pathItem = (PathItem) o;
        return Objects.equals(summary, pathItem.summary) &&
            Objects.equals($ref, pathItem.$ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(summary, $ref);
    }
}
