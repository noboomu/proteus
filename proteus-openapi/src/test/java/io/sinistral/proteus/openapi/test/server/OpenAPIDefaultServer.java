package io.sinistral.proteus.openapi.test.server;

import io.restassured.RestAssured;
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
                Thread.sleep(5000);
                log.info("ports: {}", app.getPorts());
                List<Integer> ports = app.getPorts();
                port = ports.getFirst();
            } catch (Exception e) {
                e.printStackTrace();
            }

            RestAssured.baseURI = String.format("http://localhost:%d/", port);
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

            while (!app.isRunning()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if (app != null) {
            app.shutdown();
        }
    }
}
