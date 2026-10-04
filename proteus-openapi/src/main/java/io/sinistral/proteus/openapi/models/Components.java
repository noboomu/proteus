package io.sinistral.proteus.openapi.models;

import io.sinistral.proteus.openapi.models.callbacks.Callback;
import io.sinistral.proteus.openapi.models.examples.Example;
import io.sinistral.proteus.openapi.models.headers.Header;
import io.sinistral.proteus.openapi.models.links.Link;
import io.sinistral.proteus.openapi.models.media.Schema;
import io.sinistral.proteus.openapi.models.parameters.Parameter;
import io.sinistral.proteus.openapi.models.parameters.RequestBody;
import io.sinistral.proteus.openapi.models.responses.ApiResponse;
import io.sinistral.proteus.openapi.models.security.SecurityScheme;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/** OpenAPI components object holding reusable schemas and other artifacts. */
public class Components {

    /** Creates an empty components object. */
    public Components() {}
    /** The schemas. */
    private Map<String, Schema> schemas;
    /** The responses. */
    private Map<String, ApiResponse> responses;
    /** The parameters. */
    private Map<String, Parameter> parameters;
    /** The examples. */
    private Map<String, Example> examples;
    /** The request bodies. */
    private Map<String, RequestBody> requestBodies;
    /** The headers. */
    private Map<String, Header> headers;
    /** The security schemes. */
    private Map<String, SecurityScheme> securitySchemes;
    /** The links. */
    private Map<String, Link> links;
    /** The callbacks. */
    private Map<String, Callback> callbacks;
    /** The path items. */
    private Map<String, PathItem> pathItems;
    /** The extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the schema map.
     *
     * @return the schema map, or null when unset
     */
    public Map<String, Schema> getSchemas() {
        return schemas;
    }

    /**
     * Sets the schema map.
     * @param schemas the value to set
     *
     */
    public void setSchemas(Map<String, Schema> schemas) {
        this.schemas = schemas;
    }

    /**
     * Adds an entry to the schema map.
     * @param key the value to set
     * @param schemasItem the value to set
     *
     */
    public void addSchemas(String key, Schema schemasItem) {
        if (this.schemas == null) {
            this.schemas = new LinkedHashMap<>();
        }
        this.schemas.put(key, schemasItem);
    }

    /**
     * Returns the response map.
     *
     * @return the response map, or null when unset
     */
    public Map<String, ApiResponse> getResponses() {
        return responses;
    }

    /**
     * Sets the response map.
     * @param responses the value to set
     *
     */
    public void setResponses(Map<String, ApiResponse> responses) {
        this.responses = responses;
    }

    /**
     * Adds an entry to the response map.
     * @param key the value to set
     * @param responsesItem the value to set
     *
     */
    public void addResponses(String key, ApiResponse responsesItem) {
        if (this.responses == null) {
            this.responses = new LinkedHashMap<>();
        }
        this.responses.put(key, responsesItem);
    }

    /**
     * Returns the parameter map.
     *
     * @return the parameter map, or null when unset
     */
    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    /**
     * Sets the parameter map.
     * @param parameters the value to set
     *
     */
    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    /**
     * Adds an entry to the parameter map.
     * @param key the value to set
     * @param parametersItem the value to set
     *
     */
    public void addParameters(String key, Parameter parametersItem) {
        if (this.parameters == null) {
            this.parameters = new LinkedHashMap<>();
        }
        this.parameters.put(key, parametersItem);
    }

    /**
     * Returns the example map.
     *
     * @return the example map, or null when unset
     */
    public Map<String, Example> getExamples() {
        return examples;
    }

    /**
     * Sets the example map.
     * @param examples the value to set
     *
     */
    public void setExamples(Map<String, Example> examples) {
        this.examples = examples;
    }

    /**
     * Adds an entry to the example map.
     * @param key the value to set
     * @param examplesItem the value to set
     *
     */
    public void addExamples(String key, Example examplesItem) {
        if (this.examples == null) {
            this.examples = new LinkedHashMap<>();
        }
        this.examples.put(key, examplesItem);
    }

    /**
     * Returns the request body map.
     *
     * @return the request body map, or null when unset
     */
    public Map<String, RequestBody> getRequestBodies() {
        return requestBodies;
    }

    /**
     * Sets the request body map.
     * @param requestBodies the value to set
     *
     */
    public void setRequestBodies(Map<String, RequestBody> requestBodies) {
        this.requestBodies = requestBodies;
    }

    /**
     * Adds an entry to the request body map.
     * @param key the value to set
     * @param requestBodiesItem the value to set
     *
     */
    public void addRequestBodies(String key, RequestBody requestBodiesItem) {
        if (this.requestBodies == null) {
            this.requestBodies = new LinkedHashMap<>();
        }
        this.requestBodies.put(key, requestBodiesItem);
    }

    /**
     * Returns the header map.
     *
     * @return the header map, or null when unset
     */
    public Map<String, Header> getHeaders() {
        return headers;
    }

    /**
     * Sets the header map.
     * @param headers the value to set
     *
     */
    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }

    /**
     * Adds an entry to the header map.
     * @param key the value to set
     * @param headersItem the value to set
     *
     */
    public void addHeaders(String key, Header headersItem) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(key, headersItem);
    }

    /**
     * Returns the security scheme map.
     *
     * @return the security scheme map, or null when unset
     */
    public Map<String, SecurityScheme> getSecuritySchemes() {
        return securitySchemes;
    }

    /**
     * Sets the security scheme map.
     * @param securitySchemes the value to set
     *
     */
    public void setSecuritySchemes(Map<String, SecurityScheme> securitySchemes) {
        this.securitySchemes = securitySchemes;
    }

    /**
     * Adds an entry to the security scheme map.
     * @param key the value to set
     * @param securitySchemesItem the value to set
     *
     */
    public void addSecuritySchemes(String key, SecurityScheme securitySchemesItem) {
        if (this.securitySchemes == null) {
            this.securitySchemes = new LinkedHashMap<>();
        }
        this.securitySchemes.put(key, securitySchemesItem);
    }

    /**
     * Returns the link map.
     *
     * @return the link map, or null when unset
     */
    public Map<String, Link> getLinks() {
        return links;
    }

    /**
     * Sets the link map.
     * @param links the value to set
     *
     */
    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    /**
     * Adds an entry to the link map.
     * @param key the value to set
     * @param linksItem the value to set
     *
     */
    public void addLinks(String key, Link linksItem) {
        if (this.links == null) {
            this.links = new LinkedHashMap<>();
        }
        this.links.put(key, linksItem);
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
     * @param callbacks the value to set
     *
     */
    public void setCallbacks(Map<String, Callback> callbacks) {
        this.callbacks = callbacks;
    }

    /**
     * Adds an entry to the callback map.
     * @param key the value to set
     * @param callbacksItem the value to set
     *
     */
    public void addCallbacks(String key, Callback callbacksItem) {
        if (this.callbacks == null) {
            this.callbacks = new LinkedHashMap<>();
        }
        this.callbacks.put(key, callbacksItem);
    }

    /**
     * Returns the path item map.
     *
     * @return the path item map, or null when unset
     */
    public Map<String, PathItem> getPathItems() {
        return pathItems;
    }

    /**
     * Sets the path item map.
     * @param pathItems the value to set
     *
     */
    public void setPathItems(Map<String, PathItem> pathItems) {
        this.pathItems = pathItems;
    }

    /**
     * Adds an entry to the path item map.
     * @param key the value to set
     * @param pathItemsItem the value to set
     *
     */
    public void addPathItems(String key, PathItem pathItemsItem) {
        if (this.pathItems == null) {
            this.pathItems = new LinkedHashMap<>();
        }
        this.pathItems.put(key, pathItemsItem);
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
     * @param extensions the value to set
     *
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
        if (!(o instanceof Components)) return false;
        Components that = (Components) o;
        return Objects.equals(schemas, that.schemas) &&
            Objects.equals(responses, that.responses) &&
            Objects.equals(securitySchemes, that.securitySchemes);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(schemas, responses, securitySchemes);
    }
}
