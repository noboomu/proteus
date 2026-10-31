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

/** Performance-contract workload plus server-side stage histograms for /json/blocking, served at /diag. */
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
            HttpServerExchange exchange = request.getExchange();
            long running = System.nanoTime();
            long parsed = exchange.getRequestStartTime();
            Diag.QUEUE.recordValue(Math.max(0, running - parsed) / 1000);
            exchange.addExchangeCompleteListener((ex, next) -> {
                long done = System.nanoTime();
                Diag.COMPLETE.recordValue(Math.max(0, done - running) / 1000);
                Diag.TOTAL.recordValue(Math.max(0, done - parsed) / 1000);
                next.proceed();
            });
            return response(new Message("Hello, World!")).applicationJson();
        }

        /** Prints and resets stage histograms (microseconds). */
        @GET
        @Path("/diag")
        @Produces(MediaType.TEXT_PLAIN)
        public void diag(HttpServerExchange exchange) {
            exchange.getResponseSender().send(Diag.report());
        }
    }

    /** Per-stage latency histograms for /json/blocking, recorded server-side. */
    static final class Diag {
        static final org.HdrHistogram.ConcurrentHistogram QUEUE = new org.HdrHistogram.ConcurrentHistogram(3);
        static final org.HdrHistogram.ConcurrentHistogram COMPLETE = new org.HdrHistogram.ConcurrentHistogram(3);
        static final org.HdrHistogram.ConcurrentHistogram TOTAL = new org.HdrHistogram.ConcurrentHistogram(3);

        /** Formats p50/p90/p99/p999/max per stage, then resets. */
        static String report() {
            StringBuilder sb = new StringBuilder();
            for (var e : java.util.Map.of("queue", QUEUE, "complete", COMPLETE, "total", TOTAL).entrySet()) {
                var h = e.getValue();
                sb.append(String.format("%-8s n=%d p50=%d p90=%d p99=%d p999=%d max=%d us%n", e.getKey(), h.getTotalCount(),
                    h.getValueAtPercentile(50), h.getValueAtPercentile(90), h.getValueAtPercentile(99), h.getValueAtPercentile(99.9), h.getMaxValue()));
                h.reset();
            }
            return sb.toString();
        }
    }
}
