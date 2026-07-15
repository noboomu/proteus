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

public class Components {
    private Map<String, Schema> schemas;
    private Map<String, ApiResponse> responses;
    private Map<String, Parameter> parameters;
    private Map<String, Example> examples;
    private Map<String, RequestBody> requestBodies;
    private Map<String, Header> headers;
    private Map<String, SecurityScheme> securitySchemes;
    private Map<String, Link> links;
    private Map<String, Callback> callbacks;
    private Map<String, PathItem> pathItems;
    private Map<String, Object> extensions;

    public Map<String, Schema> getSchemas() {
        return schemas;
    }

    public void setSchemas(Map<String, Schema> schemas) {
        this.schemas = schemas;
    }

    public void addSchemas(String key, Schema schemasItem) {
        if (this.schemas == null) {
            this.schemas = new LinkedHashMap<>();
        }
        this.schemas.put(key, schemasItem);
    }

    public Map<String, ApiResponse> getResponses() {
        return responses;
    }

    public void setResponses(Map<String, ApiResponse> responses) {
        this.responses = responses;
    }

    public void addResponses(String key, ApiResponse responsesItem) {
        if (this.responses == null) {
            this.responses = new LinkedHashMap<>();
        }
        this.responses.put(key, responsesItem);
    }

    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Parameter> parameters) {
        this.parameters = parameters;
    }

    public void addParameters(String key, Parameter parametersItem) {
        if (this.parameters == null) {
            this.parameters = new LinkedHashMap<>();
        }
        this.parameters.put(key, parametersItem);
    }

    public Map<String, Example> getExamples() {
        return examples;
    }

    public void setExamples(Map<String, Example> examples) {
        this.examples = examples;
    }

    public void addExamples(String key, Example examplesItem) {
        if (this.examples == null) {
            this.examples = new LinkedHashMap<>();
        }
        this.examples.put(key, examplesItem);
    }

    public Map<String, RequestBody> getRequestBodies() {
        return requestBodies;
    }

    public void setRequestBodies(Map<String, RequestBody> requestBodies) {
        this.requestBodies = requestBodies;
    }

    public void addRequestBodies(String key, RequestBody requestBodiesItem) {
        if (this.requestBodies == null) {
            this.requestBodies = new LinkedHashMap<>();
        }
        this.requestBodies.put(key, requestBodiesItem);
    }

    public Map<String, Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }

    public void addHeaders(String key, Header headersItem) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(key, headersItem);
    }

    public Map<String, SecurityScheme> getSecuritySchemes() {
        return securitySchemes;
    }

    public void setSecuritySchemes(Map<String, SecurityScheme> securitySchemes) {
        this.securitySchemes = securitySchemes;
    }

    public void addSecuritySchemes(String key, SecurityScheme securitySchemesItem) {
        if (this.securitySchemes == null) {
            this.securitySchemes = new LinkedHashMap<>();
        }
        this.securitySchemes.put(key, securitySchemesItem);
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    public void addLinks(String key, Link linksItem) {
        if (this.links == null) {
            this.links = new LinkedHashMap<>();
        }
        this.links.put(key, linksItem);
    }

    public Map<String, Callback> getCallbacks() {
        return callbacks;
    }

    public void setCallbacks(Map<String, Callback> callbacks) {
        this.callbacks = callbacks;
    }

    public void addCallbacks(String key, Callback callbacksItem) {
        if (this.callbacks == null) {
            this.callbacks = new LinkedHashMap<>();
        }
        this.callbacks.put(key, callbacksItem);
    }

    public Map<String, PathItem> getPathItems() {
        return pathItems;
    }

    public void setPathItems(Map<String, PathItem> pathItems) {
        this.pathItems = pathItems;
    }

    public void addPathItems(String key, PathItem pathItemsItem) {
        if (this.pathItems == null) {
            this.pathItems = new LinkedHashMap<>();
        }
        this.pathItems.put(key, pathItemsItem);
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
        if (!(o instanceof Components)) return false;
        Components that = (Components) o;
        return Objects.equals(schemas, that.schemas) &&
            Objects.equals(responses, that.responses) &&
            Objects.equals(securitySchemes, that.securitySchemes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(schemas, responses, securitySchemes);
    }
}
