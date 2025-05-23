package io.sinistral.proteus.test.server;

import java.util.List;

import io.restassured.parsing.Parser;
import io.sinistral.proteus.test.controllers.Tests;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.restassured.RestAssured;
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
        RestAssured.defaultParser = Parser.JSON;
        System.setProperty("logback.configurationFile", "./conf/logback-test.xml");
    }

    @Override
    public void beforeAll(ExtensionContext context) throws Exception
    {
        if (!started) {
            started = true;
            app = new ProteusApplication();
            app.addService(AssetsService.class);
            app.addController(Tests.class);
            app.start();

            int port = 0;
            try {
                Thread.sleep(5000);
                List<Integer> ports = app.getPorts();
                port = ports.get(0);
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
    public void afterAll(ExtensionContext context) throws Exception
    {
        if (app != null) {
            app.shutdown();
            started = false;
        }
    }
}
