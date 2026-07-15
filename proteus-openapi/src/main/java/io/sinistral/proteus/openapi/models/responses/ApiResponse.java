package io.sinistral.proteus.openapi.models.responses;

import io.sinistral.proteus.openapi.models.headers.Header;
import io.sinistral.proteus.openapi.models.links.Link;
import io.sinistral.proteus.openapi.models.media.Content;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class ApiResponse {
    private String description;
    private Map<String, Header> headers;
    private Content content;
    private Map<String, Link> links;
    private String $ref;
    private Map<String, Object> extensions;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ApiResponse description(String description) {
        this.description = description;
        return this;
    }

    public Map<String, Header> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }

    public void addHeaderObject(String name, Header header) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(name, header);
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public ApiResponse content(Content content) {
        this.content = content;
        return this;
    }

    public Map<String, Link> getLinks() {
        return links;
    }

    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    public void addLink(String name, Link link) {
        if (this.links == null) {
            this.links = new LinkedHashMap<>();
        }
        this.links.put(name, link);
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
        if (!(o instanceof ApiResponse)) return false;
        ApiResponse that = (ApiResponse) o;
        return Objects.equals(description, that.description) &&
            Objects.equals($ref, that.$ref);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, $ref);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
            "description='" + description + '\'' +
            '}';
    }
}
