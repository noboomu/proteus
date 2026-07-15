package io.sinistral.proteus.openapi.test.server;

import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.openapi.services.OpenAPIService;
import io.sinistral.proteus.openapi.test.controllers.OpenAPITests;
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
            app.start();

            int port = 0;
            try {
                List<Integer> ports = app.getPorts();
                if (ports.isEmpty()) {
                    log.info("Ports list empty, waiting for server to bind...");
                    Thread.sleep(2000);
                    ports = app.getPorts();
                }
                port = ports.isEmpty() ? 8080 : ports.getFirst();
                log.info("Using port: {}", port);
            } catch (Exception e) {
                e.printStackTrace();
            }

            baseURI = String.format("http://localhost:%d/", port);
            log.info("Test server running at: {}", baseURI);

            while (!app.isRunning()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // Wait for OpenAPI spec generation (async) to complete
            log.info("Waiting 10 seconds for OpenAPI spec generation to complete...");
            Thread.sleep(10000);
            log.info("Wait complete, tests will now run");
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (app != null) {
            app.shutdown();
        }
    }
}
