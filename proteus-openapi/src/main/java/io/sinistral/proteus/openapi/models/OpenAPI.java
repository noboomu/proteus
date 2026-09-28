package io.sinistral.proteus.openapi.models;

import io.sinistral.proteus.openapi.models.info.Info;
import io.sinistral.proteus.openapi.models.security.SecurityRequirement;
import io.sinistral.proteus.openapi.models.servers.Server;
import io.sinistral.proteus.openapi.models.tags.Tag;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Root object for OpenAPI 3.1 specification
 */
public class OpenAPI {
    /** the openapi. */
    private String openapi = "3.1.0";
    /** the info. */
    private Info info;
    /** the json schema dialect. */
    private String jsonSchemaDialect;
    /** the servers. */
    private List<Server> servers;
    /** the paths. */
    private Paths paths;
    /** the webhooks. */
    private Map<String, PathItem> webhooks;
    /** the components. */
    private Components components;
    /** the security. */
    private List<SecurityRequirement> security;
    /** the tags. */
    private List<Tag> tags;
    /** the external docs. */
    private ExternalDocumentation externalDocs;
    /** the extensions. */
    private Map<String, Object> extensions;

    /** Creates an empty document with the default spec version. */
    public OpenAPI() {
    }

    /** Creates a document with the given spec version.
     *
     * @param specVersion the target specification version
     */
    public OpenAPI(SpecVersion specVersion) {
        if (specVersion != null) {
            this.openapi = specVersion.getVersion();
        }
    }

    /** OpenAPI specification version selector. */
    public enum SpecVersion {
        /** The 3.0.0 version. */
        V30("3.0.0"),
        /** The 3.1.0 version. */
        V31("3.1.0");

        /** the version. */
        private final String version;

        SpecVersion(String version) {
            this.version = version;
        }

        /**
         * Returns the version.
         *
         * @return the version, or null when unset
         */
        public String getVersion() {
            return version;
        }
    }

    /**
     * Returns the openapi value.
     *
     * @return the openapi value, or null when unset
     */
    public String getOpenapi() {
        return openapi;
    }

    /**
     * Sets the openapi value.
     *
     * @param openapi the openapi value
     */
    public void setOpenapi(String openapi) {
        this.openapi = openapi;
    }

    /**
     * Processes this element.
    *
    * @param openapi the value
    * @return the result
     */
    public OpenAPI openapi(String openapi) {
        this.openapi = openapi;
        return this;
    }

    /**
     * Returns the info value.
     *
     * @return the info value, or null when unset
     */
    public Info getInfo() {
        return info;
    }

    /**
     * Sets the info value.
     *
     * @param info the info value
     */
    public void setInfo(Info info) {
        this.info = info;
    }

    /**
     * Processes this element.
    *
    * @param info the value
    * @return the result
     */
    public OpenAPI info(Info info) {
        this.info = info;
        return this;
    }

    /**
     * Returns the info value.
     *
     * @return the info value, or null when unset
     */
    public String getJsonSchemaDialect() {
        return jsonSchemaDialect;
    }

    /**
     * Sets the json schema dialect value.
     *
     * @param jsonSchemaDialect the json schema dialect value
     */
    public void setJsonSchemaDialect(String jsonSchemaDialect) {
        this.jsonSchemaDialect = jsonSchemaDialect;
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
     * Returns the paths value.
     *
     * @return the paths value, or null when unset
     */
    public Paths getPaths() {
        return paths;
    }

    /**
     * Sets the paths value.
     *
     * @param paths the paths value
     */
    public void setPaths(Paths paths) {
        this.paths = paths;
    }

    /**
     * Processes this element.
    *
    * @param paths the value
    * @return the result
     */
    public OpenAPI paths(Paths paths) {
        this.paths = paths;
        return this;
    }

    /**
     * Returns the webhooks value.
     *
     * @return the webhooks value, or null when unset
     */
    public Map<String, PathItem> getWebhooks() {
        return webhooks;
    }

    /**
     * Sets the webhooks value.
     *
     * @param webhooks the webhooks value
     */
    public void setWebhooks(Map<String, PathItem> webhooks) {
        this.webhooks = webhooks;
    }

    /**
     * Adds an entry to the webhooks value.
     *
     * @param key the entry
     * @param webhooksItem the entry
     */
    public void addWebhooks(String key, PathItem webhooksItem) {
        if (this.webhooks == null) {
            this.webhooks = new LinkedHashMap<>();
        }
        this.webhooks.put(key, webhooksItem);
    }

    /**
     * Returns the components value.
     *
     * @return the components value, or null when unset
     */
    public Components getComponents() {
        return components;
    }

    /**
     * Sets the components value.
     *
     * @param components the components value
     */
    public void setComponents(Components components) {
        this.components = components;
    }

    /**
     * Processes this element.
    *
    * @param components the value
    * @return the result
     */
    public OpenAPI components(Components components) {
        this.components = components;
        return this;
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
     * Returns the tag names.
     *
     * @return the tag names, or null when unset
     */
    public List<Tag> getTags() {
        return tags;
    }

    /**
     * Sets the tag names.
     *
     * @param tags the tag names
     */
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Adds an entry to the tags value.
     *
     * @param tagsItem the entry
     */
    public void addTagsItem(Tag tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
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
     * Sets the external documentation, fluent style.
     *
     * @param externalDocs the external documentation
     * @return this instance
     */
    public OpenAPI externalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
        return this;
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
        if (!(o instanceof OpenAPI)) return false;
        OpenAPI openAPI = (OpenAPI) o;
        return Objects.equals(openapi, openAPI.openapi) &&
            Objects.equals(info, openAPI.info);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public int hashCode() {
        return Objects.hash(openapi, info);
    }

    /**
     * Processes this element.
    *
    * @return the result
     */
    @Override
    public String toString() {
        return "OpenAPI{" +
            "openapi='" + openapi + '\'' +
            ", info=" + info +
            '}';
    }
}
