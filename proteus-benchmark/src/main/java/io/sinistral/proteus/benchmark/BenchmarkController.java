package io.sinistral.proteus.benchmark;

import static io.sinistral.proteus.server.ServerResponse.response;

import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
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
 * <p>The {@code exchange} variants complete the exchange directly; the {@code response}
 * variants return a {@link ServerResponse} and let the generated handler serialize it.
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
