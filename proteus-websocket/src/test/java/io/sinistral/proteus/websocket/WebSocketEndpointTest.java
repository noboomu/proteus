package io.sinistral.proteus.websocket;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.undertow.Undertow;
import io.undertow.server.RoutingHandler;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * End-to-end WebSocket tests over a real Undertow server with the JDK HTTP client.
 *
 * @author jbauer
 */
@Timeout(60)
public class WebSocketEndpointTest {

    private static Undertow server;
    private static DefaultWebSocketService service;
    private static int port;
    private final List<WebSocket> clients = new ArrayList<>();

    @BeforeAll
    static void startServer() {
        Injector injector =
                Guice.createInjector(new io.sinistral.proteus.modules.WebSocketModule());
        service = injector.getInstance(DefaultWebSocketService.class);
        service.registerEndpoint("/echo", new EchoEndpoint());
        service.registerEndpoint("/other", new OtherEndpoint());
        RoutingHandler router = new RoutingHandler();
        service.registerEndpoints(router);
        server = Undertow.builder()
                .addHttpListener(0, "localhost")
                .setHandler(router)
                .build();
        server.start();
        java.net.InetSocketAddress address =
                (java.net.InetSocketAddress) server.getListenerInfo().getFirst().getAddress();
        port = address.getPort();
    }

    @AfterAll
    static void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    @AfterEach
    void closeClients() {
        clients.forEach(WebSocket::abort);
        clients.clear();
    }

    /** Collects text and binary frames and close codes into queues. */
    static final class RecordingListener implements Listener {
        final BlockingQueue<String> text = new LinkedBlockingQueue<>();
        final BlockingQueue<byte[]> binary = new LinkedBlockingQueue<>();
        final BlockingQueue<Integer> closes = new LinkedBlockingQueue<>();
        final BlockingQueue<String> closeReasons = new LinkedBlockingQueue<>();
        final StringBuilder partial = new StringBuilder();

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            partial.append(data);
            if (last) {
                text.add(partial.toString());
                partial.setLength(0);
            }
            webSocket.request(1);
            return null;
        }

        @Override
        public CompletionStage<?> onBinary(WebSocket webSocket, ByteBuffer data, boolean last) {
            byte[] bytes = new byte[data.remaining()];
            data.get(bytes);
            binary.add(bytes);
            webSocket.request(1);
            return null;
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            closes.add(statusCode);
            closeReasons.add(reason);
            return null;
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
        }
    }

    private WebSocket connect(String path, RecordingListener listener) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        WebSocket socket = client.newWebSocketBuilder()
                .buildAsync(URI.create("ws://localhost:" + port + path), listener)
                .get(10, TimeUnit.SECONDS);
        clients.add(socket);
        return socket;
    }

    @Test
    void textEchoRoundTrip() throws Exception {
        RecordingListener listener = new RecordingListener();
        connect("/echo", listener);
        // OnOpen side effect: server sends a greeting on connect.
        assertThat(listener.text.poll(10, TimeUnit.SECONDS), is("connected"));
        sendTextAndWaitEcho(listener, "hello");
    }

    @Test
    void binaryEchoRoundTrip() throws Exception {
        RecordingListener listener = new RecordingListener();
        connect("/echo", listener);
        assertThat(listener.text.poll(10, TimeUnit.SECONDS), is("connected"));
        WebSocket socket = clients.getLast();
        socket.sendBinary(ByteBuffer.wrap("bin".getBytes(StandardCharsets.UTF_8)), true);
        byte[] echoed = listener.binary.poll(10, TimeUnit.SECONDS);
        assertThat(new String(echoed, StandardCharsets.UTF_8), is("bin"));
    }

    @Test
    void broadcastReachesAllClientsOnPath() throws Exception {
        RecordingListener first = new RecordingListener();
        RecordingListener second = new RecordingListener();
        connect("/echo", first);
        connect("/echo", second);
        assertThat(first.text.poll(10, TimeUnit.SECONDS), is("connected"));
        assertThat(second.text.poll(10, TimeUnit.SECONDS), is("connected"));
        service.broadcastText("/echo", "all").get(10, TimeUnit.SECONDS);
        assertThat(first.text.poll(10, TimeUnit.SECONDS), is("all"));
        assertThat(second.text.poll(10, TimeUnit.SECONDS), is("all"));
    }

    @Test
    void pathScopedBroadcastSkipsOtherPaths() throws Exception {
        RecordingListener echo = new RecordingListener();
        RecordingListener other = new RecordingListener();
        connect("/echo", echo);
        connect("/other", other);
        assertThat(echo.text.poll(10, TimeUnit.SECONDS), is("connected"));
        assertThat(other.text.poll(10, TimeUnit.SECONDS), is("connected"));
        service.broadcastText("/echo", "scoped").get(10, TimeUnit.SECONDS);
        assertThat(echo.text.poll(10, TimeUnit.SECONDS), is("scoped"));
        assertThat(
                "other path must not receive scoped broadcast",
                other.text.poll(500, TimeUnit.MILLISECONDS),
                equalTo(null));
    }

    @Test
    void broadcastJsonReachesClients() throws Exception {
        RecordingListener listener = new RecordingListener();
        connect("/echo", listener);
        assertThat(listener.text.poll(10, TimeUnit.SECONDS), is("connected"));
        service.broadcastJson("/echo", java.util.Map.of("k", "v")).get(10, TimeUnit.SECONDS);
        assertThat(listener.text.poll(10, TimeUnit.SECONDS), is("{\"k\":\"v\"}"));
    }

    @Test
    void broadcastBinaryReachesClients() throws Exception {
        RecordingListener listener = new RecordingListener();
        connect("/echo", listener);
        assertThat(listener.text.poll(10, TimeUnit.SECONDS), is("connected"));
        byte[] payload = {1, 2, 3};
        service.broadcastBinary("/echo", payload).get(10, TimeUnit.SECONDS);
        assertThat(listener.binary.poll(10, TimeUnit.SECONDS), is(payload));
    }

    @Test
    void closeReasonDeliversCodeAndReason() throws Exception {
        RecordingListener listener = new RecordingListener();
        WebSocket socket = connect("/echo", listener);
        assertThat(listener.text.poll(10, TimeUnit.SECONDS), is("connected"));
        // "close:1000:bye" is an echo-endpoint command that closes with the given reason.
        socket.sendText("close:1000:bye", true);
        assertThat(listener.closes.poll(10, TimeUnit.SECONDS), is(1000));
        assertThat(listener.closeReasons.poll(10, TimeUnit.SECONDS), is("bye"));
    }

    @Test
    void handlerExceptionClosesWith1011() throws Exception {
        RecordingListener listener = new RecordingListener();
        WebSocket socket = connect("/echo", listener);
        assertThat(listener.text.poll(10, TimeUnit.SECONDS), is("connected"));
        socket.sendText("boom", true);
        Integer code = listener.closes.poll(10, TimeUnit.SECONDS);
        assertThat(code, is(1011));
    }

    @Test
    void oversizedFrameIsRejected() throws Exception {
        // Test config caps proteus.websocket.maxFrameSizeBytes at 64 KiB; a larger text
        // frame must be rejected with close code 1009 (message too big).
        RecordingListener listener = new RecordingListener();
        WebSocket socket = connect("/echo", listener);
        assertThat(listener.text.poll(10, TimeUnit.SECONDS), is("connected"));
        socket.sendText("x".repeat(65_537), true);
        Integer code = listener.closes.poll(10, TimeUnit.SECONDS);
        assertThat(code, is(1009));
    }

    private void sendTextAndWaitEcho(RecordingListener listener, String message) throws Exception {
        WebSocket socket = clients.getLast();
        socket.sendText(message, true);
        assertThat(listener.text.poll(10, TimeUnit.SECONDS), is(message));
    }

    /** Secondary endpoint on a different path. */
    @io.sinistral.proteus.websocket.WebSocket("/other")
    static class OtherEndpoint {
        @OnOpen
        void open(WebSocketConnection connection) {
            connection.sendText("connected");
        }
    }

    /** Echo endpoint used by the tests. */
    @io.sinistral.proteus.websocket.WebSocket("/echo")
    static class EchoEndpoint {
        @OnOpen
        void open(WebSocketConnection connection) {
            connection.sendText("connected");
        }

        @OnMessage
        void text(WebSocketConnection connection, String message) {
            if (message.equals("boom")) {
                throw new IllegalStateException("boom");
            }
            if (message.startsWith("close:")) {
                String[] parts = message.split(":");
                connection.close(new CloseReason(Integer.parseInt(parts[1]), parts[2]));
                return;
            }
            connection.sendText(message);
        }

        @OnMessage
        void binary(WebSocketConnection connection, byte[] data) {
            connection.sendBinary(data);
        }
    }
}
