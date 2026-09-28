package io.sinistral.proteus.openapi.jaxrs2;

import io.sinistral.proteus.openapi.models.OpenAPI;

/**
 * Listener interface for Reader scanning events
 */
public interface ReaderListener {

    /**
     * Called before scanning starts.
     *
     * @param reader the reader about to scan
     * @param openAPI the target document
     */
    void beforeScan(Reader reader, OpenAPI openAPI);

    /**
     * Called after scanning completes.
     *
     * @param reader the reader that scanned
     * @param openAPI the populated document
     */
    void afterScan(Reader reader, OpenAPI openAPI);
}
