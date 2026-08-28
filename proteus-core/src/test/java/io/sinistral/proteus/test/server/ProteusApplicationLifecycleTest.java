package io.sinistral.proteus.test.server;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.inject.Singleton;
import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.annotations.security.Claim;
import io.sinistral.proteus.annotations.security.RolesAllowed;
import io.sinistral.proteus.services.DefaultService;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

public class ProteusApplicationLifecycleTest {

    @Test
    public void failedServiceStartupReleasesUndertowResources() {
        ProteusApplication application = new ProteusApplication();
        application.addService(FailingService.class);

        assertThrows(IllegalStateException.class, application::start);

        assertFalse(application.isRunning());
        assertTrue(application.getPorts().isEmpty());
        assertThrows(
            IllegalStateException.class,
            () -> application.undertow.getListenerInfo()
        );
        assertTrue(application.getWorker().isShutdown());
    }

    @Test
    public void normalShutdownReleasesApplicationOwnedXnioWorker()
        throws Exception {
        ProteusApplication application = new ProteusApplication();
        application.start();

        var worker = application.getWorker();
        application.shutdown();

        assertTrue(worker.isShutdown());
    }


    @Test
    public void invalidGeneratedHandlerFailsStartupInsteadOfOmittingController() {
        ProteusApplication application = new ProteusApplication();
        application.addController(UnsupportedClaimController.class);

        assertThrows(IllegalStateException.class, application::start);

        assertFalse(application.isRunning());
        assertTrue(application.getPorts().isEmpty());
    }

    @Test
    public void optionalPrimitiveClaimFailsStartup() {
        ProteusApplication application = new ProteusApplication();
        application.addController(OptionalPrimitiveClaimController.class);

        assertThrows(IllegalStateException.class, application::start);

        assertFalse(application.isRunning());
        assertTrue(application.getPorts().isEmpty());
    }

    @Test
    public void unsupportedSimpleGenericClaimFailsStartup() {
        ProteusApplication application = new ProteusApplication();
        application.addController(UnsupportedSimpleClaimController.class);

        assertThrows(IllegalStateException.class, application::start);

        assertFalse(application.isRunning());
        assertTrue(application.getPorts().isEmpty());
    }

    @Test
    public void blankRolePolicyFailsStartup() {
        ProteusApplication application = new ProteusApplication();
        application.addController(BlankRoleController.class);

        assertThrows(IllegalStateException.class, application::start);

        assertFalse(application.isRunning());
        assertTrue(application.getPorts().isEmpty());
    }

    @Singleton
    public static class FailingService extends DefaultService {
        @Override
        protected void startUp() {
            throw new IllegalStateException("expected startup failure");
        }
    }


    @Path("/unsupported-claim")
    public static class UnsupportedClaimController {
        @GET
        @Path("/nested")
        public String nested(
            @Claim(value = "items", required = false)
            Optional<List<String>> items
        ) {
            return "unreachable";
        }
    }

    @Path("/optional-primitive-claim")
    public static class OptionalPrimitiveClaimController {
        @GET
        @Path("/value")
        public String optionalPrimitive(
            @Claim(value = "absent", required = false) long absent
        ) {
            return Long.toString(absent);
        }
    }

    @Path("/unsupported-simple-claim")
    public static class UnsupportedSimpleClaimController {
        @GET
        @Path("/value")
        public String unsupported(
            @Claim(value = "ratio", required = false) Optional<Double> ratio
        ) {
            return ratio.map(Object::toString).orElse("unreachable");
        }
    }

    @Path("/blank-role")
    public static class BlankRoleController {
        @GET
        @Path("/value")
        @RolesAllowed(" ")
        public String blankRole() {
            return "unreachable";
        }
    }
}
