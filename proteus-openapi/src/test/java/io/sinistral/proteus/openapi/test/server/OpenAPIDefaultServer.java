package io.sinistral.proteus.openapi.test.server;

import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.openapi.services.OpenAPIService;
import io.sinistral.proteus.openapi.test.controllers.OpenAPITests;
import io.sinistral.proteus.openapi.test.controllers.SecurityMetadataTests;
import io.sinistral.proteus.openapi.test.modules.AuthorizationModule;
import io.sinistral.proteus.services.AssetsService;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OpenAPIDefaultServer implements BeforeAllCallback, AfterAllCallback {
    private static final Logger log = LoggerFactory.getLogger(OpenAPIDefaultServer.class.getCanonicalName());
    private static ProteusApplication app;
    private static boolean started = false;
    private static String baseURI;

    public static String getBaseURI() {
        return baseURI;
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        if (!started) {
            started = true;
            log.info("Starting Proteus application");

            app = new ProteusApplication();
            app.addModule(AuthorizationModule.class);
            app.addService(OpenAPIService.class);
            app.addService(AssetsService.class);
            app.addController(OpenAPITests.class);
            app.addController(SecurityMetadataTests.class);
            app.start();

            if (!app.isRunning()) {
                started = false;
                throw new IllegalStateException(
                    "Proteus test application did not start: " +
                    app.getServiceManager().servicesByState()
                );
            }

            int port = waitForBoundHttpPort();
            baseURI = String.format("http://localhost:%d/", port);
            log.info("Test server running at: {}", baseURI);

            OpenAPIService openAPIService = app.injector.getInstance(OpenAPIService.class);
            openAPIService.waitForSpecGeneration(15_000L);
        }
    }

    private static int waitForBoundHttpPort() throws InterruptedException {
        long deadline = System.nanoTime() + 15_000_000_000L;

        while (System.nanoTime() < deadline) {
            List<Integer> ports = app.getPorts();
            if (!ports.isEmpty()) {
                return ports.getFirst();
            }
            Thread.sleep(25L);
        }

        started = false;
        throw new IllegalStateException(
            "Proteus test application did not publish a bound HTTP port within 15 seconds"
        );
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (app != null) {
            app.shutdown();
            app = null;
            // reset so a later test class in the same fork starts a fresh server
            started = false;
            baseURI = null;
        }
    }
}
