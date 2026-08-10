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
    private String openapi = "3.1.0";
    private Info info;
    private String jsonSchemaDialect;
    private List<Server> servers;
    private Paths paths;
    private Map<String, PathItem> webhooks;
    private Components components;
    private List<SecurityRequirement> security;
    private List<Tag> tags;
    private ExternalDocumentation externalDocs;
    private Map<String, Object> extensions;

    public OpenAPI() {
    }

    public OpenAPI(SpecVersion specVersion) {
        if (specVersion != null) {
            this.openapi = specVersion.getVersion();
        }
    }

    public enum SpecVersion {
        V30("3.0.0"),
        V31("3.1.0");

        private final String version;

        SpecVersion(String version) {
            this.version = version;
        }

        public String getVersion() {
            return version;
        }
    }

    public String getOpenapi() {
        return openapi;
    }

    public void setOpenapi(String openapi) {
        this.openapi = openapi;
    }

    public OpenAPI openapi(String openapi) {
        this.openapi = openapi;
        return this;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public OpenAPI info(Info info) {
        this.info = info;
        return this;
    }

    public String getJsonSchemaDialect() {
        return jsonSchemaDialect;
    }

    public void setJsonSchemaDialect(String jsonSchemaDialect) {
        this.jsonSchemaDialect = jsonSchemaDialect;
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

    public Paths getPaths() {
        return paths;
    }

    public void setPaths(Paths paths) {
        this.paths = paths;
    }

    public OpenAPI paths(Paths paths) {
        this.paths = paths;
        return this;
    }

    public Map<String, PathItem> getWebhooks() {
        return webhooks;
    }

    public void setWebhooks(Map<String, PathItem> webhooks) {
        this.webhooks = webhooks;
    }

    public void addWebhooks(String key, PathItem webhooksItem) {
        if (this.webhooks == null) {
            this.webhooks = new LinkedHashMap<>();
        }
        this.webhooks.put(key, webhooksItem);
    }

    public Components getComponents() {
        return components;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public OpenAPI components(Components components) {
        this.components = components;
        return this;
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

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTagsItem(Tag tagsItem) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tagsItem);
    }

    public ExternalDocumentation getExternalDocs() {
        return externalDocs;
    }

    public void setExternalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
    }

    public OpenAPI externalDocs(ExternalDocumentation externalDocs) {
        this.externalDocs = externalDocs;
        return this;
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
        if (!(o instanceof OpenAPI)) return false;
        OpenAPI openAPI = (OpenAPI) o;
        return Objects.equals(openapi, openAPI.openapi) &&
            Objects.equals(info, openAPI.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openapi, info);
    }

    @Override
    public String toString() {
        return "OpenAPI{" +
            "openapi='" + openapi + '\'' +
            ", info=" + info +
            '}';
    }
}
