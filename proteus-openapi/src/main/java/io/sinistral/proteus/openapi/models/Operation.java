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

/** OpenAPI operation object describing one API operation. */
public class Operation {

    /** Creates the object. */
    public Operation() {}
    /** The tags. */
    private List<String> tags;
    /** The summary. */
    private String summary;
    /** The description. */
    private String description;
    /** The external docs. */
    private ExternalDocumentation externalDocs;
    /** The operation id. */
    private String operationId;
    /** The parameters. */
    private List<Parameter> parameters;
    /** The request body. */
    private RequestBody requestBody;
    /** The responses. */
    private ApiResponses responses;
    /** The callbacks. */
    private Map<String, Callback> callbacks;
    /** The deprecated. */
    private Boolean deprecated;
    /** The security. */
    private List<SecurityRequirement> security;
    /** The servers. */
    private List<Server> servers;
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the tag names.
     *
     * @return the tag names, or null when unset
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * Sets the tag names.
     *
     * @param tags the tag names
     */
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    /**
     * Adds an entry to the tags value.
     *
     * @param tagsItem the entry
     */
    public void addTagsItem(String tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
    }

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
     * Returns the external documentation.
     *
     * @return the external documentation, or null when unset
     */
    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    /**
     * Sets the external documentation.
     *
     * @param externalDocs the external documentation
     */
    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    /**
     * Returns the operation id.
     *
     * @return the operation id, or null when unset
     */
    public String getOperationId() {
        return operationId;
    }

    /**
     * Sets the operation id.
     *
     * @param operationId the operation id
     */
    public void setOperationId(String operationId) {
        this.operationId = operationId;
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
     * Returns the request body.
     *
     * @return the request body, or null when unset
     */
    public RequestBody getRequestBody() {
        return requestBody;
    }

    /**
     * Sets the request body.
     *
     * @param requestBody the request body
     */
    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }

    /**
     * Sets the request body and returns this instance.
     *
     * @param requestBody the request body
     * @return this instance
     */
    public Operation requestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    /**
     * Returns the response map.
     *
     * @return the response map, or null when unset
     */
    public ApiResponses getResponses() {
        return responses;
    }

    /**
     * Sets the response map.
     *
     * @param responses the response map
     */
    public void setResponses(ApiResponses responses) {
        this.responses = responses;
    }

    /**
     * Sets the responses and returns this instance.
     *
     * @param responses the response map
     * @return this instance
     */
    public Operation responses(ApiResponses responses) {
        this.responses = responses;
        return this;
    }

    /**
     * Returns the callback map.
     *
     * @return the callback map, or null when unset
     */
    public Map<String, Callback> getCallbacks() {
        return callbacks;
    }

    /**
     * Sets the callback map.
     *
     * @param callbacks the callback map
     */
    public void setCallbacks(Map<String, Callback> callbacks) {
        this.callbacks = callbacks;
    }

    /**
     * Adds an entry to the callback value.
     *
     * @param key the entry
     * @param callbacksItem the entry
     */
    public void addCallback(String key, Callback callbacksItem) {
        if (this.callbacks == null) {
            this.callbacks = new LinkedHashMap<>();
        }
        this.callbacks.put(key, callbacksItem);
    }

    /**
     * Returns the deprecated flag.
     *
     * @return the deprecated flag, or null when unset
     */
    public Boolean getDeprecated() {
        return deprecated;
    }

    /**
     * Sets the deprecated flag.
     *
     * @param deprecated the deprecated flag
     */
    public void setDeprecated(Boolean deprecated) {
        this.deprecated = deprecated;
    }

    /**
     * Returns the security requirements.
     *
     * @return the security requirements, or null when unset
     */
    public List<SecurityRequirement> getSecurity() {
        return security;
    }

    /**
     * Sets the security requirements.
     *
     * @param security the security requirements
     */
    public void setSecurity(List<SecurityRequirement> security) {
        this.security = security;
    }

    /**
     * Adds an entry to the security value.
     *
     * @param securityItem the entry
     */
    public void addSecurityItem(SecurityRequirement securityItem) {
        if (this.security == null) {
            this.security = new ArrayList<>();
        }
        this.security.add(securityItem);
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
        if (!(o instanceof Operation)) return false;
        Operation operation = (Operation) o;
        return Objects.equals(operationId, operation.operationId) &&
            Objects.equals(summary, operation.summary);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(operationId, summary);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public String toString() {
        return "Operation{" +
            "operationId='" + operationId + '\'' +
            ", summary='" + summary + '\'' +
            '}';
    }
}
