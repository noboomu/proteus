package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.models.OpenAPI;

/**
 * Listener interface for Reader scanning events
 */
public interface ReaderListener {

    /**
     * Called before scanning starts
     */
    void beforeScan(Reader reader, OpenAPI openAPI);

    /**
     * Called after scanning completes
     */
    void afterScan(Reader reader, OpenAPI openAPI);
}
