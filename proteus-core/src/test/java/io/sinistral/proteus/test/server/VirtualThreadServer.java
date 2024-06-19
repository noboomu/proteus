/**
 *
 */
package io.sinistral.proteus.test.server;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.services.AssetsService;
import io.sinistral.proteus.test.controllers.Tests;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author jbauer
 */
public class VirtualThreadServer extends BlockJUnit4ClassRunner {
    private static Logger log = LoggerFactory.getLogger(VirtualThreadServer.class.getCanonicalName());

    static {
        RestAssured.defaultParser = Parser.JSON;
        System.setProperty("logback.configurationFile", "./conf/logback-test.xml");
        System.setProperty("config.file", "./src/test/resources/application-vt.conf");

    }

    private static boolean first = true;

    /**
     * @param clazz
     * @throws InitializationError
     */
    public VirtualThreadServer(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    public void run(final RunNotifier notifier) {
        notifier.addListener(new RunListener() {
            @Override
            public void testStarted(Description description) throws Exception {

                super.testStarted(description);
            }

            @Override
            public void testFinished(Description description) throws Exception {

                super.testFinished(description);
            }
        });

        runInternal(notifier);

        super.run(notifier);
    }

    private static void runInternal(final RunNotifier notifier) {

        if (first) {

            first = false;

            final ProteusApplication app = new ProteusApplication();

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

            log.info("listening on port {}", port);

            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

            while (!app.isRunning()) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            notifier.addListener(new RunListener() {
                @Override
                public void testRunFinished(final Result result) throws Exception {
                    app.shutdown();
                }

                ;
            });
        }

    }

    public static void main(String[] args) {
        try {
            final ProteusApplication app = new ProteusApplication();

            app.addService(AssetsService.class);

            app.addController(Tests.class);

            app.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
