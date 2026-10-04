package io.sinistral.proteus.openapi.integration;

import io.sinistral.proteus.openapi.models.OpenAPI;

import java.util.*;

/**
 * Default implementation of OpenAPIConfiguration
 */
public class SwaggerConfiguration implements OpenAPIConfiguration {
    /** The open a p i. */
    private OpenAPI openAPI;
    /** The resource packages. */
    private Set<String> resourcePackages = new LinkedHashSet<>();
    /** The resource classes. */
    private Set<String> resourceClasses = new LinkedHashSet<>();
    /** The user defined options. */
    private Map<String, Object> userDefinedOptions = new LinkedHashMap<>();
    /** The read all resources. */
    private Boolean readAllResources = true;

    /** Creates a configuration with defaults. */
    public SwaggerConfiguration() {
    }

    /**
     * Processes this element.
    *
    * @param openAPI the value
    * @return the result
     */
    public SwaggerConfiguration openAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
        return this;
    }

    /**
     * Returns the open api value.
     *
     * @return the open api value, or null when unset
     */
    @Override
    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    /**
     * Sets the open api value.
     *
     * @param openAPI the open api value
     */
    public void setOpenAPI(OpenAPI openAPI) {
        this.openAPI = openAPI;
    }

    /**
     * Returns the open api value.
     *
     * @return the open api value, or null when unset
     */
    @Override
    public Set<String> getResourcePackages() {
        return resourcePackages;
    }

    /**
     * Sets the resource packages value.
     *
     * @param resourcePackages the resource packages value
     */
    public void setResourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
    }

    /**
     * Processes this element.
    *
    * @param resourcePackages the value
    * @return the result
     */
    public SwaggerConfiguration resourcePackages(Set<String> resourcePackages) {
        this.resourcePackages = resourcePackages;
        return this;
    }

    /**
     * Returns the resource packages value.
     *
     * @return the resource packages value, or null when unset
     */
    @Override
    public Set<String> getResourceClasses() {
        return resourceClasses;
    }

    /**
     * Sets the resource classes value.
     *
     * @param resourceClasses the resource classes value
     */
    public void setResourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
    }

    /**
     * Processes this element.
    *
    * @param resourceClasses the value
    * @return the result
     */
    public SwaggerConfiguration resourceClasses(Set<String> resourceClasses) {
        this.resourceClasses = resourceClasses;
        return this;
    }

    /**
     * Returns the resource classes value.
     *
     * @return the resource classes value, or null when unset
     */
    @Override
    public Map<String, Object> getUserDefinedOptions() {
        return userDefinedOptions;
    }

    /**
     * Sets the user defined options value.
     *
     * @param userDefinedOptions the user defined options value
     */
    public void setUserDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
    }

    /**
     * Processes this element.
    *
    * @param userDefinedOptions the value
    * @return the result
     */
    public SwaggerConfiguration userDefinedOptions(Map<String, Object> userDefinedOptions) {
        this.userDefinedOptions = userDefinedOptions;
        return this;
    }

    /**
     * Returns the user defined options value.
     *
     * @return the user defined options value, or null when unset
     */
    @Override
    public Boolean isReadAllResources() {
        return readAllResources;
    }

    /**
     * Sets the read all resources value.
     *
     * @param readAllResources the read all resources value
     */
    public void setReadAllResources(Boolean readAllResources) {
        this.readAllResources = readAllResources;
    }

    /**
     * Processes this element.
    *
    * @param readAllResources the value
    * @return the result
     */
    public SwaggerConfiguration readAllResources(Boolean readAllResources) {
        this.readAllResources = readAllResources;
        return this;
    }
}
