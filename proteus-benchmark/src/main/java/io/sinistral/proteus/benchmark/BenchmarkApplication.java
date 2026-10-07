package io.sinistral.proteus.benchmark;

import io.sinistral.proteus.ProteusApplication;
import io.undertow.server.HttpHandler;
import io.undertow.util.Headers;
import io.undertow.util.Methods;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Starts the benchmark server with the controller routes plus two raw Undertow handlers as the
 * floor: {@code /plaintext/undertow} and {@code /json/undertow}.
 */
public class BenchmarkApplication {

    /** Pre-encoded JSON body for the raw Undertow route. */
    private static final ByteBuffer JSON = ByteBuffer.wrap(
        "{\"message\":\"Hello, World!\"}".getBytes(StandardCharsets.US_ASCII)
    ).asReadOnlyBuffer();

    /** Pre-encoded plaintext body for the raw Undertow route. */
    private static final ByteBuffer HELLO = ByteBuffer.wrap(
        "Hello, World!".getBytes(StandardCharsets.US_ASCII)
    ).asReadOnlyBuffer();

    /** Creates the launcher. */
    public BenchmarkApplication() {}

    /**
     * Registers routes and starts the server; blocks until the process is terminated.
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        ProteusApplication app = new ProteusApplication();
        app.addController(BenchmarkController.class);

        HttpHandler plaintext = exchange -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send(HELLO.duplicate());
        };
        HttpHandler json = exchange -> {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            exchange.getResponseSender().send(JSON.duplicate());
        };
        app.getRouter().add(Methods.GET, "/plaintext/undertow", plaintext);
        app.getRouter().add(Methods.GET, "/json/undertow", json);

        app.start();
    }
}
