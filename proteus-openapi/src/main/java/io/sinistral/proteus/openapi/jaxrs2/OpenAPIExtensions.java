package io.sinistral.proteus.openapi.jaxrs2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Registry for OpenAPI extensions
 */
public class OpenAPIExtensions {
    private static List<OpenAPIExtension> extensions = new ArrayList<>();

    static {
        // Load extensions via ServiceLoader
        ServiceLoader<OpenAPIExtension> loader = ServiceLoader.load(OpenAPIExtension.class);
        for (OpenAPIExtension extension : loader) {
            extensions.add(extension);
        }

        // Add default ServerParameterExtension if not already loaded
        boolean hasServerExtension = false;
        for (OpenAPIExtension ext : extensions) {
            if (ext instanceof ServerParameterExtension) {
                hasServerExtension = true;
                break;
            }
        }
        if (!hasServerExtension) {
            extensions.add(new ServerParameterExtension());
        }
    }

    /**
     * Get an iterator over the extension chain
     */
    public static Iterator<OpenAPIExtension> chain() {
        return new ArrayList<>(extensions).iterator();
    }

    /**
     * Register an extension
     */
    public static void register(OpenAPIExtension extension) {
        extensions.add(extension);
    }

    /**
     * Clear all registered extensions
     */
    public static void clear() {
        extensions.clear();
    }

    /**
     * Get all registered extensions
     */
    public static List<OpenAPIExtension> getExtensions() {
        return new ArrayList<>(extensions);
    }
}
