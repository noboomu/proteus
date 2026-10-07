package io.sinistral.proteus.test.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.typesafe.config.ConfigFactory;
import io.sinistral.proteus.ProteusApplication;
import io.undertow.server.HttpHandler;
import io.undertow.util.Methods;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * Proves the external XNIO worker honors the configured thread counts and executor type.
 */
public class WorkerConfigurationTest {

    private ProteusApplication application;

    @AfterEach
    public void stop() throws Exception {
        if (application != null && application.isRunning()) {
            application.shutdown();
        }
        System.clearProperty("undertow.ioThreadsMultiplier");
        System.clearProperty("undertow.workerThreadsMultiplier");
        System.clearProperty("undertow.workerExecutor");
        ConfigFactory.invalidateCaches();
    }

    /** Sets a config override; Typesafe caches system properties, so the cache is dropped. */
    private static void override(String path, String value) {
        System.setProperty(path, value);
        ConfigFactory.invalidateCaches();
    }

    /** Registers a route that reports the dispatching thread's virtual flag after a worker dispatch. */
    private static void addProbe(ProteusApplication app) {
        HttpHandler onWorker = exchange ->
            exchange.getResponseSender().send(String.valueOf(Thread.currentThread().isVirtual()));
        app.getRouter().add(Methods.GET, "/probe/worker", exchange -> {
            if (exchange.isInIoThread()) {
                exchange.dispatch(onWorker);
                return;
            }
            onWorker.handleRequest(exchange);
        });
    }

    private static String get(ProteusApplication app, String path) throws Exception {
        int port = app.getPorts().getFirst();
        HttpResponse<String> response = HttpClient.newHttpClient().send(
            HttpRequest.newBuilder(URI.create("http://localhost:" + port + path)).GET().build(),
            HttpResponse.BodyHandlers.ofString()
        );
        assertEquals(200, response.statusCode());
        return response.body();
    }

    @Test
    public void ioThreadCountFollowsMultiplier() {
        override("undertow.ioThreadsMultiplier", "0.5");
        application = new ProteusApplication();
        application.start();

        int expected = Math.max(1, (int) Math.round(Runtime.getRuntime().availableProcessors() * 0.5));
        assertEquals(expected, application.getWorker().getIoThreadCount());
    }

    @Test
    public void platformExecutorDispatchesToPlatformThreads() throws Exception {
        override("undertow.workerExecutor", "platform");
        application = new ProteusApplication();
        addProbe(application);
        application.start();

        assertEquals("false", get(application, "/probe/worker"));
    }

    @Test
    public void virtualExecutorDispatchesToVirtualThreads() throws Exception {
        override("undertow.workerExecutor", "virtual");
        application = new ProteusApplication();
        addProbe(application);
        application.start();

        assertEquals("true", get(application, "/probe/worker"));

        var worker = application.getWorker();
        application.shutdown();
        assertTrue(worker.isShutdown());
    }

    @Test
    public void unknownExecutorFailsStartup() {
        override("undertow.workerExecutor", "fibers");
        application = new ProteusApplication();

        assertThrows(IllegalStateException.class, application::start);
        assertFalse(application.isRunning());
    }
}
