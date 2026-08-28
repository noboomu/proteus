package io.sinistral.proteus.openapi.jaxrs2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Process-global registry for OpenAPI extensions.
 *
 * <p>The registry is initialized once from {@link ServiceLoader}, then ensures the default
 * {@link ServerParameterExtension} is present. {@link #register(OpenAPIExtension)} preserves
 * the first instance of each implementation class; it does not replace an existing instance.
 * {@link #chain()} and {@link #getExtensions()} return snapshots. {@link #clear()} removes all
 * entries, including service-loaded defaults, and does not reload them.
 *
 * <p>Registration is synchronized. Callers that clear or register extensions while another
 * thread is generating a specification are responsible for external coordination.
 *
 * @since 0.9.5
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
     * Returns an iterator over a snapshot of the current extension chain.
     *
     * @return snapshot iterator
     */
    public static Iterator<OpenAPIExtension> chain() {
        return new ArrayList<>(extensions).iterator();
    }

    /**
     * Registers an extension if no instance of its implementation class is already present.
     *
     * @param extension extension to register
     */
    public static synchronized void register(OpenAPIExtension extension) {
        boolean alreadyRegistered = extensions.stream()
            .anyMatch(existing -> existing.getClass().equals(extension.getClass()));
        if (!alreadyRegistered) {
            extensions.add(extension);
        }
    }

    /**
     * Removes every registered extension without reloading defaults.
     */
    public static void clear() {
        extensions.clear();
    }

    /**
     * Returns a snapshot of all currently registered extensions.
     *
     * @return immutable-by-copy extension snapshot
     */
    public static List<OpenAPIExtension> getExtensions() {
        return new ArrayList<>(extensions);
    }
}
