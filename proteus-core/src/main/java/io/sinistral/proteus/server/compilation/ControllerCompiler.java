package io.sinistral.proteus.server.compilation;

import io.undertow.server.RoutingHandler;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Compiles generated routing suppliers into application-local classes.
 */
public interface ControllerCompiler {

    /**
     * Compiles the supplied generated source units.
     *
     * @param sources generated Java source indexed by binary class name
     * @param parentClassLoader parent for generated route supplier classes
     * @return loaded route supplier classes indexed by binary class name
     */
    Map<String, Class<? extends Supplier<RoutingHandler>>> compile(
        Map<String, String> sources,
        ClassLoader parentClassLoader
    );
}
