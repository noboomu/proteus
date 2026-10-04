package io.sinistral.proteus.openapi.models.links;

import io.sinistral.proteus.openapi.models.servers.Server;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI link object connecting responses to follow-up operations. */
public class Link {

    /** Creates the object. */
    public Link() {}
    /** The operation ref. */
    private String operationRef;
    /** The operation id. */
    private String operationId;
    /** The parameters. */
    private Map<String, Object> parameters;
    /** The request body. */
    private Object requestBody;
    /** The description. */
    private String description;
    /** The server. */
    private Server server;
    /** The reference. */
    private String $ref;
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the operation reference.
     *
     * @return the operation reference, or null when unset
     */
    public String getOperationRef() {
        return operationRef;
    }

    /**
     * Sets the operation reference.
     *
     * @param operationRef the operation reference
     */
    public void setOperationRef(String operationRef) {
        this.operationRef = operationRef;
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
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * Sets the parameter map.
     *
     * @param parameters the parameter map
     */
    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns the request body.
     *
     * @return the request body, or null when unset
     */
    public Object getRequestBody() {
        return requestBody;
    }

    /**
     * Sets the request body.
     *
     * @param requestBody the request body
     */
    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
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
     * Returns the server value.
     *
     * @return the server value, or null when unset
     */
    public Server getServer() {
        return server;
    }

    /**
     * Sets the server value.
     *
     * @param server the server value
     */
    public void setServer(Server server) {
        this.server = server;
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
        if (!(o instanceof Link)) return false;
        Link link = (Link) o;
        return Objects.equals(operationRef, link.operationRef) &&
            Objects.equals(operationId, link.operationId) &&
            Objects.equals($ref, link.$ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(operationRef, operationId, $ref);
    }
}
