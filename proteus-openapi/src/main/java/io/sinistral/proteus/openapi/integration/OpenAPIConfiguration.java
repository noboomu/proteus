package io.sinistral.proteus.openapi.integration;

import io.sinistral.proteus.openapi.models.OpenAPI;

import java.util.Map;
import java.util.Set;

/**
 * Configuration interface for OpenAPI generation
 */
public interface OpenAPIConfiguration {

    /**
     * Get the OpenAPI instance
     */
    OpenAPI getOpenAPI();

    /**
     * Get resource packages to scan
     */
    Set<String> getResourcePackages();

    /**
     * Get resource classes to scan
     */
    Set<String> getResourceClasses();

    /**
     * Get user-defined options
     */
    Map<String, Object> getUserDefinedOptions();

    /**
     * Whether to read all resources (without @Operation annotation)
     */
    Boolean isReadAllResources();
}
