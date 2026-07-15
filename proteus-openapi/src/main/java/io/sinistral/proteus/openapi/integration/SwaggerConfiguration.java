package io.sinistral.proteus.openapi.integration;

import io.sinistral.proteus.openapi.models.OpenAPI;

import java.util.*;

/**
 * Default implementation of OpenAPIConfiguration
 */
public class SwaggerConfiguration implements OpenAPIConfiguration {
    private OpenAPI openAPI;
    private Set<String> resourcePackages = new LinkedHashSet<>();
    private Set<String> resourceClasses = new LinkedHashSet<>();
    private Map<String, Object> userDefinedOptions = new LinkedHashMap<>();
    private Boolean readAllResources = true;

    public SwaggerConfiguration() {
    }

    public SwaggerConfiguration openAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
        return this;
    }

    @Override
    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public void setOpenAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    @Override
    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    public void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    public SwaggerConfiguration resourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
        return this;
    }

    @Override
    public Set<String> getResourceClasses() {
        return resourceClasses;
    }

    public void setResourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    public SwaggerConfiguration resourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
        return this;
    }

    @Override
    public Map<String, Object> getUserDefinedOptions() {
        return userDefinedOptions;
    }

    public void setUserDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
    }

    public SwaggerConfiguration userDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
        return this;
    }

    @Override
    public Boolean isReadAllResources() {
        return readAllResources;
    }

    public void setReadAllResources(Boolean readAllResources) {
        this.readAllResources = readAllResources;
    }

    public SwaggerConfiguration readAllResources(Boolean readAllResources) {
        this.readAllResources = readAllResources;
        return this;
    }
}
