package io.sinistral.proteus.test.server;

import java.util.List;

import io.sinistral.proteus.test.controllers.Tests;
import io.sinistral.proteus.test.controllers.SecurityTestController;
import io.sinistral.proteus.test.controllers.BlockingTestController;
import io.sinistral.proteus.test.util.TestClient;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.services.AssetsService;

/**
 * JUnit 5 extension for starting and stopping the Proteus server.
 */
public class DefaultServer implements BeforeAllCallback, AfterAllCallback
{
    private static final Logger log = LoggerFactory.getLogger(DefaultServer.class.getCanonicalName());
    private static ProteusApplication app;
    private static boolean started = false;

    static {
        System.setProperty("logback.configurationFile", "./conf/logback-test.xml");
        System.setProperty("config.file", "src/test/resources/application.conf");
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception
    {
        if (!started) {
            started = true;
            app = new ProteusApplication();
            app.addService(AssetsService.class);
            app.addController(Tests.class);
            app.addController(SecurityTestController.class);
            app.addController(SecurityTestController.ClassDeniedController.class);
            app.addController(SecurityTestController.ClassAdminController.class);
            app.addController(SecurityTestController.ClassPublicController.class);
            app.addController(BlockingTestController.class);
            app.start();

            if (!app.isRunning()) {
                started = false;
                throw new IllegalStateException(
                    "Proteus test application did not start: " +
                    app.getServiceManager().servicesByState()
                );
            }

            int port = waitForBoundHttpPort();
            TestClient.setBaseUrl(String.format("http://localhost:%d", port));
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
    public void afterAll(ExtensionContext context) throws Exception
    {
        if (app != null) {
            app.shutdown();
            started = false;
        }
    }
}
