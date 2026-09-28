package io.sinistral.proteus.openapi.integration;

import io.sinistral.proteus.openapi.models.OpenAPI;

import java.util.Map;
import java.util.Set;

/**
 * Configuration interface for OpenAPI generation
 */
public interface OpenAPIConfiguration {

    /**
     * Get the OpenAPI instance.
     *
     * @return the OpenAPI instance
     */
    OpenAPI getOpenAPI();

    /**
     * Get resource packages to scan.
     *
     * @return the resource packages
     */
    Set<String> getResourcePackages();

    /**
     * Get resource classes to scan.
     *
     * @return the resource classes
     */
    Set<String> getResourceClasses();

    /**
     * Get user-defined options.
     *
     * @return the user-defined options
     */
    Map<String, Object> getUserDefinedOptions();

    /**
     * Whether to read all resources (without @Operation annotation).
     *
     * @return true to read all resources
     */
    Boolean isReadAllResources();
}
