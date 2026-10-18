package io.sinistral.proteus.test.server;

import static io.sinistral.proteus.server.ServerResponse.response;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.inject.Singleton;
import com.typesafe.config.ConfigFactory;
import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.server.ServerRequest;
import io.sinistral.proteus.server.ServerResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Wire-level behaviors that must match between the Undertow and Proteus front ends.
 * Each case opens a raw socket so framing, pipelining, and close semantics are observed
 * directly rather than through a client library.
 */
public class Http1ConformanceTest {

    private static final int MAX_ENTITY = 64;

    @Path("/c")
    @Produces(MediaType.TEXT_PLAIN)
    @Singleton
    public static class ConformanceController {

        @GET
        @Path("/hello")
        public ServerResponse<ByteBuffer> hello(ServerRequest request) {
            return response("hello").textPlain();
        }

        /** Reads the request body through the exchange's input stream, so chunked and
         *  100-continue framing is exercised by the request conduit rather than ServerRequest. */
        @POST
        @Path("/echo")
        @Consumes(MediaType.WILDCARD)
        @io.sinistral.proteus.annotations.Blocking
        public ServerResponse<ByteBuffer> echo(ServerRequest request) throws Exception {
            var exchange = request.getExchange();
            if (exchange.getRequestContentLength() > MAX_ENTITY) {
                return response("too large").status(413);
            }
            // ServerRequest pre-reads Content-Length bodies; chunked bodies arrive on the stream.
            ByteBuffer pre = exchange.getAttachment(ServerRequest.BYTE_BUFFER_KEY);
            byte[] bytes = pre != null ? pre.array() : exchange.getInputStream().readAllBytes();
            return response(ByteBuffer.wrap(bytes)).textPlain();
        }

        @GET
        @Path("/empty")
        public ServerResponse<ByteBuffer> empty(ServerRequest request) {
            return response().noContent();
        }
    }

    private ProteusApplication app;

    private int start(String frontEnd) {
        System.setProperty("undertow.frontEnd", frontEnd);
        System.setProperty("undertow.socket.readTimeout", "1500");
        ConfigFactory.invalidateCaches();
        app = new ProteusApplication();
        app.addController(ConformanceController.class);
        app.start();
        return app.getPorts().getFirst();
    }

    @AfterEach
    public void stop() throws Exception {
        if (app != null && app.isRunning()) {
            app.shutdown();
        }
        System.clearProperty("undertow.frontEnd");
        System.clearProperty("undertow.socket.readTimeout");
        ConfigFactory.invalidateCaches();
    }

    /** Sends raw bytes, reads until the server closes or {@code expectedResponses} heads arrive. */
    private static List<String> exchange(int port, String raw, int expectedResponses, boolean halfClose) throws IOException {
        try (Socket s = new Socket("localhost", port)) {
            s.setSoTimeout(5000);
            OutputStream out = s.getOutputStream();
            out.write(raw.getBytes(StandardCharsets.ISO_8859_1));
            out.flush();
            if (halfClose) {
                s.shutdownOutput();
            }
            InputStream in = s.getInputStream();
            ByteArrayOutputStream acc = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            try {
                while ((n = in.read(buf)) != -1) {
                    acc.write(buf, 0, n);
                    if (countResponses(acc.toString(StandardCharsets.ISO_8859_1)) >= expectedResponses) {
                        break;
                    }
                }
            } catch (java.net.SocketTimeoutException ignored) {
                // return what we have
            }
            return splitResponses(acc.toString(StandardCharsets.ISO_8859_1));
        }
    }

    private static int countResponses(String s) {
        return splitResponses(s).size();
    }

    /** Splits a byte stream into complete responses using Content-Length framing. */
    private static List<String> splitResponses(String s) {
        List<String> out = new ArrayList<>();
        int pos = 0;
        while (true) {
            int headEnd = s.indexOf("\r\n\r\n", pos);
            if (headEnd < 0) {
                break;
            }
            String head = s.substring(pos, headEnd);
            int len = 0;
            for (String line : head.split("\r\n")) {
                if (line.regionMatches(true, 0, "Content-Length:", 0, 15)) {
                    len = Integer.parseInt(line.substring(15).trim());
                }
            }
            int end = headEnd + 4 + len;
            if (end > s.length()) {
                break;
            }
            out.add(s.substring(pos, end));
            pos = end;
        }
        return out;
    }

    private static String status(String response) {
        return response.substring(0, response.indexOf("\r\n"));
    }

    private static String body(String response) {
        return response.substring(response.indexOf("\r\n\r\n") + 4);
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void pipelinedRequestsAnswerInOrder(String frontEnd) throws Exception {
        int port = start(frontEnd);
        String raw = "GET /v1/c/hello HTTP/1.1\r\nHost: x\r\n\r\n"
            + "GET /v1/c/empty HTTP/1.1\r\nHost: x\r\n\r\n"
            + "GET /v1/c/hello HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n";
        List<String> r = exchange(port, raw, 3, false);
        assertEquals(3, r.size(), r.toString());
        assertEquals("HTTP/1.1 200 OK", status(r.get(0)));
        assertEquals("hello", body(r.get(0)));
        assertEquals("HTTP/1.1 204 No Content", status(r.get(1)));
        assertEquals("HTTP/1.1 200 OK", status(r.get(2)));
        assertTrue(r.get(2).contains("Connection: close"), r.get(2));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void contentLengthBodyIsEchoed(String frontEnd) throws Exception {
        int port = start(frontEnd);
        String raw = "POST /v1/c/echo HTTP/1.1\r\nHost: x\r\nContent-Type: text/plain\r\nContent-Length: 11\r\nConnection: close\r\n\r\nhello world";
        List<String> r = exchange(port, raw, 1, false);
        assertEquals(1, r.size(), r.toString());
        assertEquals("HTTP/1.1 200 OK", status(r.get(0)));
        assertEquals("hello world", body(r.get(0)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void chunkedBodyIsEchoed(String frontEnd) throws Exception {
        int port = start(frontEnd);
        String raw = "POST /v1/c/echo HTTP/1.1\r\nHost: x\r\nContent-Type: text/plain\r\nTransfer-Encoding: chunked\r\nConnection: close\r\n\r\n"
            + "5\r\nhello\r\n6\r\n world\r\n0\r\n\r\n";
        List<String> r = exchange(port, raw, 1, false);
        assertEquals(1, r.size(), r.toString());
        assertEquals("HTTP/1.1 200 OK", status(r.get(0)));
        assertEquals("hello world", body(r.get(0)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void oversizedBodyIsRejected(String frontEnd) throws Exception {
        int port = start(frontEnd);
        String big = "x".repeat(MAX_ENTITY + 1);
        String raw = "POST /v1/c/echo HTTP/1.1\r\nHost: x\r\nContent-Type: text/plain\r\nContent-Length: " + big.length() + "\r\n\r\n" + big;
        List<String> r = exchange(port, raw, 1, false);
        assertFalse(r.isEmpty(), "expected a response");
        assertEquals("HTTP/1.1 413 Request Entity Too Large", status(r.get(0)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void malformedRequestLineGets400(String frontEnd) throws Exception {
        int port = start(frontEnd);
        List<String> r = exchange(port, "GET\r\n\r\n", 1, false);
        assertFalse(r.isEmpty());
        assertEquals("HTTP/1.1 400 Bad Request", status(r.get(0)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void missingHostOnHttp11Gets400(String frontEnd) throws Exception {
        int port = start(frontEnd);
        List<String> r = exchange(port, "GET /v1/c/hello HTTP/1.1\r\n\r\n", 1, false);
        assertFalse(r.isEmpty());
        assertTrue(status(r.get(0)).startsWith("HTTP/1.1 400 "), status(r.get(0)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void http10WithoutHostIsServedAndClosed(String frontEnd) throws Exception {
        int port = start(frontEnd);
        List<String> r = exchange(port, "GET /v1/c/hello HTTP/1.0\r\n\r\n", 1, false);
        assertEquals(1, r.size(), r.toString());
        // Undertow 2.4 echoes HTTP/1.0 in the status line; 2.3 and the Proteus front end answer HTTP/1.1.
        assertTrue(status(r.get(0)).matches("HTTP/1\\.[01] 200 OK"), status(r.get(0)));
        assertEquals("hello", body(r.get(0)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void halfCloseStillReceivesResponse(String frontEnd) throws Exception {
        int port = start(frontEnd);
        List<String> r = exchange(port, "GET /v1/c/hello HTTP/1.1\r\nHost: x\r\n\r\n", 1, true);
        assertEquals(1, r.size(), r.toString());
        assertEquals("hello", body(r.get(0)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void expectContinueWithoutInterimStillCompletesOnceBodyArrives(String frontEnd) throws Exception {
        // Neither front end sends 100 Continue unless the application installs
        // HttpContinueReadHandler; a client that sends the body anyway gets the normal response.
        int port = start(frontEnd);
        String raw = "POST /v1/c/echo HTTP/1.1\r\nHost: x\r\nContent-Type: text/plain\r\nContent-Length: 5\r\nExpect: 100-continue\r\nConnection: close\r\n\r\nhello";
        List<String> r = exchange(port, raw, 1, false);
        assertEquals(1, r.size(), r.toString());
        assertEquals("HTTP/1.1 200 OK", status(r.get(0)));
        assertEquals("hello", body(r.get(0)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void idleKeepAliveIsClosedAfterReadTimeout(String frontEnd) throws Exception {
        int port = start(frontEnd);
        try (Socket s = new Socket("localhost", port)) {
            s.setSoTimeout(6000);
            s.getOutputStream().write("GET /v1/c/hello HTTP/1.1\r\nHost: x\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1));
            s.getOutputStream().flush();
            InputStream in = s.getInputStream();
            byte[] buf = new byte[1024];
            int n = in.read(buf);
            assertTrue(n > 0);
            // readTimeout is 1500 ms; the server must close the idle connection.
            long t = System.nanoTime();
            int eof = in.read(buf);
            long waited = (System.nanoTime() - t) / 1_000_000;
            assertEquals(-1, eof, "server should close idle connection");
            assertTrue(waited < 5000, "closed after " + waited + " ms");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "proteus" })
    public void bufferedPipelinedResponsesFlushBeforeWaitingOnPartialRequest(String frontEnd) throws Exception {
        // Two complete requests plus the first half of a third. The server must deliver both
        // responses while it waits for the rest; a client that withholds the tail until it sees
        // them would otherwise deadlock against the pipelining buffer.
        // Undertow 2.4.4's own listener fails this with BUFFER_PIPELINED_DATA on (its builder
        // default): HttpReadListener resumes reads without flushing, so nothing is sent until the
        // read timeout closes the connection.
        int port = start(frontEnd);
        try (Socket s = new Socket("localhost", port)) {
            s.setSoTimeout(3000);
            OutputStream out = s.getOutputStream();
            out.write(("GET /v1/c/hello HTTP/1.1\r\nHost: x\r\n\r\n"
                + "GET /v1/c/hello HTTP/1.1\r\nHost: x\r\n\r\n"
                + "GET /v1/c/hel").getBytes(StandardCharsets.ISO_8859_1));
            out.flush();
            InputStream in = s.getInputStream();
            ByteArrayOutputStream acc = new ByteArrayOutputStream();
            byte[] buf = new byte[4096];
            while (countResponses(acc.toString(StandardCharsets.ISO_8859_1)) < 2) {
                int n = in.read(buf);
                assertTrue(n > 0, "connection closed early: " + acc);
                acc.write(buf, 0, n);
            }
            out.write("lo HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n".getBytes(StandardCharsets.ISO_8859_1));
            out.flush();
            int n;
            while ((n = in.read(buf)) != -1) {
                acc.write(buf, 0, n);
            }
            List<String> r = splitResponses(acc.toString(StandardCharsets.ISO_8859_1));
            assertEquals(3, r.size(), r.toString());
            for (String one : r) {
                assertEquals("hello", body(one));
            }
        }
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void bodyRequestAfterBufferedPipelinedResponsesIsAnsweredInOrder(String frontEnd) throws Exception {
        int port = start(frontEnd);
        String raw = "GET /v1/c/hello HTTP/1.1\r\nHost: x\r\n\r\n"
            + "GET /v1/c/empty HTTP/1.1\r\nHost: x\r\n\r\n"
            + "POST /v1/c/echo HTTP/1.1\r\nHost: x\r\nContent-Type: text/plain\r\nContent-Length: 5\r\n\r\nworld"
            + "GET /v1/c/hello HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n";
        List<String> r = exchange(port, raw, 4, false);
        assertEquals(4, r.size(), r.toString());
        assertEquals("hello", body(r.get(0)));
        assertEquals("HTTP/1.1 204 No Content", status(r.get(1)));
        assertEquals("world", body(r.get(2)));
        assertEquals("hello", body(r.get(3)));
    }

    @ParameterizedTest
    @ValueSource(strings = { "undertow", "proteus" })
    public void largePipelinedBatchSpanningBuffersIsAnsweredCompletely(String frontEnd) throws Exception {
        // 400 requests overflow both the 16k read buffer and the pipelining write buffer.
        int port = start(frontEnd);
        StringBuilder raw = new StringBuilder();
        for (int i = 0; i < 399; i++) {
            raw.append("GET /v1/c/hello HTTP/1.1\r\nHost: x\r\n\r\n");
        }
        raw.append("GET /v1/c/hello HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n");
        List<String> r = exchange(port, raw.toString(), 400, false);
        assertEquals(400, r.size());
        for (String one : r) {
            assertEquals("hello", body(one));
        }
    }
}
