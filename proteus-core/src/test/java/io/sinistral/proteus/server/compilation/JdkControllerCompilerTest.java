package io.sinistral.proteus.server.compilation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.undertow.server.RoutingHandler;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

/**
 * Verifies JDK-backed generated controller compilation.
 */
class JdkControllerCompilerTest {

    /** Compiles and loads multiple route suppliers in one compiler task. */
    @Test
    void compilesAndLoadsMultipleRouteSuppliers() {
        Map<String, String> sources = new LinkedHashMap<>();
        sources.put("generated.FirstRouteSupplier", source("FirstRouteSupplier"));
        sources.put("generated.SecondRouteSupplier", source("SecondRouteSupplier"));

        Map<String, Class<? extends Supplier<RoutingHandler>>> compiled =
            new JdkControllerCompiler().compile(sources, getClass().getClassLoader());

        assertEquals(sources.keySet(), compiled.keySet());
        assertTrue(Supplier.class.isAssignableFrom(compiled.get("generated.FirstRouteSupplier")));
        assertTrue(Supplier.class.isAssignableFrom(compiled.get("generated.SecondRouteSupplier")));
    }

    /** Returns diagnostics when one generated source is invalid. */
    @Test
    void reportsCompilerDiagnosticsForInvalidSource() {
        Map<String, String> sources = Map.of(
            "generated.InvalidRouteSupplier",
            "package generated; public class InvalidRouteSupplier { missing }"
        );

        ControllerCompilationException exception = assertThrows(
            ControllerCompilationException.class,
            () -> new JdkControllerCompiler().compile(
                sources,
                getClass().getClassLoader()
            )
        );

        assertTrue(exception.getMessage().contains("InvalidRouteSupplier"));
        assertTrue(exception.getMessage().contains("ERROR"));
    }

    /** Produces a minimal generated routing supplier source unit. */
    private String source(String simpleName) {
        return """
            package generated;
            import io.undertow.server.RoutingHandler;
            import java.util.function.Supplier;
            public class %s implements Supplier<RoutingHandler> {
                public RoutingHandler get() { return new RoutingHandler(); }
            }
            """.formatted(simpleName);
    }
}
