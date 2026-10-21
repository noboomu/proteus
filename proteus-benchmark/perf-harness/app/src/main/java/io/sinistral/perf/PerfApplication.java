package io.sinistral.perf;

import static io.sinistral.proteus.server.ServerResponse.response;

import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.annotations.Blocking;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/** Performance-contract workload: the four routes in specs/proteus_performance.md. */
public class PerfApplication {

    public static void main(String[] args) {
        ProteusApplication app = new ProteusApplication();
        app.addController(Routes.class);
        app.start();
    }

    @Path("")
    public static class Routes {

        private static final ByteBuffer HELLO = ByteBuffer.wrap("Hello, World!".getBytes(StandardCharsets.US_ASCII)).asReadOnlyBuffer();

        /** JSON body serialized per request by the injected ObjectMapper. */
        public record Message(String message) {}

        @GET
        @Path("/plaintext")
        @Produces(MediaType.TEXT_PLAIN)
        public ServerResponse<ByteBuffer> plaintext(ServerRequest request) {
            return response(HELLO.duplicate()).textPlain();
        }

        @GET
        @Path("/plaintext/exchange")
        @Produces(MediaType.TEXT_PLAIN)
        public void plaintextExchange(HttpServerExchange exchange) {
            exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
            exchange.getResponseSender().send(HELLO.duplicate());
        }

        @GET
        @Path("/json")
        @Produces(MediaType.APPLICATION_JSON)
        public ServerResponse<Message> json(ServerRequest request) {
            return response(new Message("Hello, World!")).applicationJson();
        }

        @GET
        @Path("/json/blocking")
        @Produces(MediaType.APPLICATION_JSON)
        @Blocking
        public ServerResponse<Message> jsonBlocking(ServerRequest request) {
            return response(new Message("Hello, World!")).applicationJson();
        }
    }
}
