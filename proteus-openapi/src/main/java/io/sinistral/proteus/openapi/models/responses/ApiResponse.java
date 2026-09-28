package io.sinistral.proteus.openapi.models.responses;

import io.sinistral.proteus.openapi.models.headers.Header;
import io.sinistral.proteus.openapi.models.links.Link;
import io.sinistral.proteus.openapi.models.media.Content;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * OpenAPI response object describing one possible operation response.
 */
public class ApiResponse {

    /** Creates the object. */
    public ApiResponse() {}
    /** the description. */
    private String description;
    /** the headers. */
    private Map<String, Header> headers;
    /** the content. */
    private Content content;
    /** the links. */
    private Map<String, Link> links;
    /** the reference. */
    private String $ref;
    /** the extensions. */
    private Map<String, Object> extensions;

    /**
     * Returns the response description.
     *
     * @return the description, or null when unset
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the response description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the description, fluent style.
     *
     * @param description the description
     * @return this instance
     */
    public ApiResponse description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Returns the response headers.
     *
     * @return the header map, or null when unset
     */
    public Map<String, Header> getHeaders() {
        return headers;
    }

    /**
     * Sets the response headers.
     *
     * @param headers the header map
     */
    public void setHeaders(Map<String, Header> headers) {
        this.headers = headers;
    }

    /**
     * Adds one header, creating the map when needed.
     *
     * @param name the header name
     * @param header the header object
     */
    public void addHeaderObject(String name, Header header) {
        if (this.headers == null) {
            this.headers = new LinkedHashMap<>();
        }
        this.headers.put(name, header);
    }

    /**
     * Returns the response content.
     *
     * @return the content, or null when unset
     */
    public Content getContent() {
        return content;
    }

    /**
     * Sets the response content.
     *
     * @param content the content
     */
    public void setContent(Content content) {
        this.content = content;
    }

    /**
     * Sets the content, fluent style.
     *
     * @param content the content
     * @return this instance
     */
    public ApiResponse content(Content content) {
        this.content = content;
        return this;
    }

    /**
     * Returns the operation links.
     *
     * @return the link map, or null when unset
     */
    public Map<String, Link> getLinks() {
        return links;
    }

    /**
     * Sets the operation links.
     *
     * @param links the link map
     */
    public void setLinks(Map<String, Link> links) {
        this.links = links;
    }

    /**
     * Adds one link, creating the map when needed.
     *
     * @param name the link name
     * @param link the link object
     */
    public void addLink(String name, Link link) {
        if (this.links == null) {
            this.links = new LinkedHashMap<>();
        }
        this.links.put(name, link);
    }

    /**
     * Returns the reference target.
     *
     * @return the reference, or null when unset
     */
    public String get$ref() {
        return $ref;
    }

    /**
     * Sets the reference target.
     *
     * @param $ref the reference
     */
    public void set$ref(String $ref) {
        this.$ref = $ref;
    }

    /**
     * Returns the extension properties.
     *
     * @return the extension map, or null when unset
     */
    @com.fasterxml.jackson.annotation.JsonAnyGetter
    public Map<String, Object> getExtensions() {
        return extensions;
    }

    /**
     * Sets the extension properties.
     *
     * @param extensions the extension map
     */
    public void setExtensions(Map<String, Object> extensions) {
        this.extensions = extensions;
    }

    /**
     * Adds one extension property; names not starting with {@code x-} are ignored.
     *
     * @param name the extension name
     * @param value the extension value
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
        if (!(o instanceof ApiResponse)) return false;
        ApiResponse that = (ApiResponse) o;
        return Objects.equals(description, that.description) &&
            Objects.equals($ref, that.$ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(description, $ref);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public String toString() {
        return "ApiResponse{" +
            "description='" + description + '\'' +
            '}';
    }
}
