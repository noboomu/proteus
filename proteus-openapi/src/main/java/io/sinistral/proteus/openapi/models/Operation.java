package io.sinistral.proteus.openapi.models;

import io.sinistral.proteus.openapi.models.callbacks.Callback;
import io.sinistral.proteus.openapi.models.parameters.Parameter;
import io.sinistral.proteus.openapi.models.parameters.RequestBody;
import io.sinistral.proteus.openapi.models.responses.ApiResponses;
import io.sinistral.proteus.openapi.models.security.SecurityRequirement;
import io.sinistral.proteus.openapi.models.servers.Server;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Operation {
    private List<String> tags;
    private String summary;
    private String description;
    private ExternalDocumentation externalDocs;
    private String operationId;
    private List<Parameter> parameters;
    private RequestBody requestBody;
    private ApiResponses responses;
    private Map<String, Callback> callbacks;
    private Boolean deprecated;
    private List<SecurityRequirement> security;
    private List<Server> servers;
    private Map<String, Object> extensions;

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTagsItem(String tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
    }

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

    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
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

    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    public Operation requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public ApiResponses getResponses() {
        return responses;
    }

    public void setResponses(ApiResponses responses) {
        this.responses = responses;
    }

    public Operation responses(ApiResponses responses) {
        this.responses = responses;
        return this;
    }

    public Map<String, Callback> getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(Map<String, Callback> callbacks) {
        this.callbacks = callbacks;
    }

    public void addCallback(String key, Callback callbacksItem) {
        if (this.callbacks == null) {
            this.callbacks = new LinkedHashMap<>();
        }
        this.callbacks.put(key, callbacksItem);
    }

    public Boolean getDeprecated() {
        return deprecated;
    }

    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    public List<SecurityRequirement> getSecurity() {
        return security;
    }

    public void setSecurity(List<SecurityRequirement> security) {
        this.security = security;
    }

    public void addSecurityItem(SecurityRequirement securityItem) {
        if (this.security == null) {
            this.security = new ArrayList<>();
        }
        this.security.add(securityItem);
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
        if (!(o instanceof Operation)) return false;
        Operation operation = (Operation) o;
        return Objects.equals(operationId, operation.operationId) &&
            Objects.equals(summary, operation.summary);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operationId, summary);
    }

    @Override
    public String toString() {
        return "Operation{" +
            "operationId='" + operationId + '\'' +
            ", summary='" + summary + '\'' +
            '}';
    }
}
