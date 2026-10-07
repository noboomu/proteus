package io.sinistral.proteus.benchmark;

import static io.sinistral.proteus.server.ServerResponse.response;

import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import com.google.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import tools.jackson.databind.ObjectMapper;

/**
 * Plaintext and JSON routes in the two generated-handler styles.
 *
 * <p>{@code exchange} completes the exchange directly on the I/O thread; {@code response}
 * returns a {@link ServerResponse} serialized by the generated handler on the I/O thread;
 * {@code blocking} is {@code @Blocking} so the generated handler moves it to a fresh virtual
 * thread; {@code worker} dispatches to the XNIO task pool whose thread type follows
 * {@code undertow.workerExecutor}.
 */
@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class BenchmarkController {

    /** Shared read-only plaintext body; each send duplicates the buffer position. */
    private static final ByteBuffer HELLO = ByteBuffer.wrap(
        "Hello, World!".getBytes(StandardCharsets.US_ASCII)
    ).asReadOnlyBuffer();

    /** Application JSON mapper. */
    @Inject
    protected ObjectMapper objectMapper;

    /** JSON body shape shared by all variants. */
    public record Message(String message) {}

    /**
     * Plaintext, exchange completed by the controller.
     *
     * @param exchange the server exchange
     */
    @GET
    @Path("/plaintext/exchange")
    @Produces(MediaType.TEXT_PLAIN)
    public void plaintextExchange(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(HELLO.duplicate());
    }

    /**
     * Plaintext via the response builder.
     *
     * @param request the server request
     * @return the plaintext response
     */
    @GET
    @Path("/plaintext/response")
    @Produces(MediaType.TEXT_PLAIN)
    public ServerResponse<ByteBuffer> plaintextResponse(ServerRequest request) {
        return response(HELLO.duplicate()).textPlain();
    }

    /**
     * Plaintext on a per-request virtual thread via {@code @Blocking}.
     *
     * @param request the server request
     * @return the plaintext response
     */
    @GET
    @Path("/plaintext/blocking")
    @Produces(MediaType.TEXT_PLAIN)
    @Blocking
    public ServerResponse<ByteBuffer> plaintextBlocking(ServerRequest request) {
        return response(HELLO.duplicate()).textPlain();
    }

    /** Completes a plaintext response; used after dispatch to the worker pool. */
    private static final HttpHandler PLAINTEXT_ON_WORKER = exchange -> {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(HELLO.duplicate());
    };

    /**
     * Plaintext dispatched to the XNIO worker task pool.
     *
     * @param exchange the server exchange
     */
    @GET
    @Path("/plaintext/worker")
    @Produces(MediaType.TEXT_PLAIN)
    public void plaintextWorker(HttpServerExchange exchange) {
        exchange.dispatch(PLAINTEXT_ON_WORKER);
    }

    /**
     * JSON, exchange completed by the controller after explicit serialization.
     *
     * @param exchange the server exchange
     */
    @GET
    @Path("/json/exchange")
    public void jsonExchange(HttpServerExchange exchange) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
        exchange
            .getResponseSender()
            .send(ByteBuffer.wrap(objectMapper.writeValueAsBytes(new Message("Hello, World!"))));
    }

    /**
     * JSON on a per-request virtual thread via {@code @Blocking}.
     *
     * @param request the server request
     * @return the JSON response
     */
    @GET
    @Path("/json/blocking")
    @Blocking
    public ServerResponse<Message> jsonBlocking(ServerRequest request) {
        return response(new Message("Hello, World!")).applicationJson();
    }

    /**
     * JSON dispatched to the XNIO worker task pool, serialized there.
     *
     * @param exchange the server exchange
     */
    @GET
    @Path("/json/worker")
    public void jsonWorker(HttpServerExchange exchange) {
        exchange.dispatch(ex -> {
            ex.getResponseHeaders().put(Headers.CONTENT_TYPE, "application/json");
            ex.getResponseSender()
                .send(ByteBuffer.wrap(objectMapper.writeValueAsBytes(new Message("Hello, World!"))));
        });
    }

    /**
     * JSON via the response builder with entity serialization by the generated handler.
     *
     * @param request the server request
     * @return the JSON response
     */
    @GET
    @Path("/json/response")
    public ServerResponse<Message> jsonResponse(ServerRequest request) {
        return response(new Message("Hello, World!")).applicationJson();
    }
}
