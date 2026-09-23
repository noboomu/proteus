package io.sinistral.proteus.messaging.nats;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.sinistral.proteus.messaging.DefaultEventBusService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * NATS bridge integration tests against the live server at tachikoma:4232.
 *
 * <p>Two full bridge stacks (event bus + bridge each) simulate two processes to verify
 * cross-process delivery, queue-group semantics, request forwarding, and reconnect.
 *
 * @author jbauer
 */
@Timeout(120)
public class NatsBridgeServiceTest {

    private static final String URL = "nats://tachikoma:4232";

    private static DefaultEventBusService firstBus;
    private static DefaultEventBusService secondBus;
    private static DefaultNatsBridgeService firstBridge;
    private static DefaultNatsBridgeService secondBridge;

    @BeforeAll
    static void startStacks() {
        Config config = ConfigFactory.parseString(
                "proteus.messaging.nats.enabled = true\nproteus.messaging.nats.url = \"" + URL + "\"");
        firstBus = startBus();
        secondBus = startBus();
        firstBridge = new DefaultNatsBridgeService(firstBus, new ObjectMapper(), config);
        secondBridge = new DefaultNatsBridgeService(secondBus, new ObjectMapper(), config);
        firstBridge.start();
        secondBridge.start();
    }

    private static DefaultEventBusService startBus() {
        DefaultEventBusService bus = new DefaultEventBusService(
                new ObjectMapper(), ConfigFactory.parseString("proteus.messaging.eventbus.enabled = true"));
        bus.startAsync();
        bus.awaitRunning();
        return bus;
    }

    @AfterAll
    static void stopStacks() {
        stopQuietly(firstBridge);
        stopQuietly(secondBridge);
        stopBusQuietly(firstBus);
        stopBusQuietly(secondBus);
    }

    private static void stopQuietly(DefaultNatsBridgeService bridge) {
        if (bridge != null) {
            try {
                bridge.stop();
            } catch (RuntimeException ignored) {
                // drain races with a closed connection; harmless in teardown
            }
        }
    }

    private static void stopBusQuietly(DefaultEventBusService bus) {
        if (bus != null) {
            bus.stopAsync();
            bus.awaitTerminated();
        }
    }

    private static String uniquePrefix() {
        return "test-" + UUID.randomUUID();
    }

    @Test
    void subjectMappingRoundTrips() {
        // Not tied to a live bridge; validates mapping and validation rules.
        Config config = ConfigFactory.parseString(
                "proteus.messaging.nats.enabled = false");
        DefaultNatsBridgeService bridge =
                new DefaultNatsBridgeService(firstBus, new ObjectMapper(), config);
        assertThat(bridge.subjectFor("a.b.c"), is("proteus.a.b.c"));
        assertThat(bridge.addressFor("proteus.a.b.c"), is("a.b.c"));
        assertThat(bridge.addressFor("other.a.b.c"), is(nullValue()));
        assertThrows(IllegalArgumentException.class, () -> bridge.subjectFor("a b"));
        assertThrows(IllegalArgumentException.class, () -> bridge.subjectFor(""));
    }

    @Test
    void publishCrossesBridges() throws Exception {
        String address = "test.bridge." + uniquePrefix();
        CountDownLatch latch = new CountDownLatch(1);
        secondBus.registerConsumer(address, message -> {
            latch.countDown();
            return CompletableFuture.completedFuture(null);
        });
        // Bridged publishes deliver through the queue-group dispatcher with echo suppression.
        firstBridge.forwardPublish(address, "hello");
        assertThat(latch.await(10, TimeUnit.SECONDS), is(true));
    }

    @Test
    void queueGroupDeliversExactlyOnce() throws Exception {
        String address = "test.bridge." + uniquePrefix();
        AtomicInteger deliveries = new AtomicInteger();
        CountDownLatch latch = new CountDownLatch(1);
        firstBus.registerConsumer(address, message -> {
            deliveries.incrementAndGet();
            latch.countDown();
            return CompletableFuture.completedFuture(null);
        });
        secondBus.registerConsumer(address, message -> {
            deliveries.incrementAndGet();
            latch.countDown();
            return CompletableFuture.completedFuture(null);
        });
        // A foreign NATS client publishes one envelope on the send subject: the queue
        // group must deliver it to exactly one of the two bridge instances.
        String envelope = "{\"address\":\"" + address
                + "\",\"contentType\":\"application/json\",\"sentAt\":1,\"payload\":\"once\"}";
        try (io.nats.client.Connection nats = io.nats.client.Nats.connect(URL)) {
            nats.publish("proteus.send." + address,
                    envelope.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            nats.flush(Duration.ofSeconds(5));
        }
        assertThat(latch.await(10, TimeUnit.SECONDS), is(true));
        // Queue group: exactly one of the two processes delivered the message.
        Thread.sleep(500);
        assertThat(deliveries.get(), is(1));
    }

    @Test
    void forwardRequestReturnsRemoteReply() throws Exception {
        String address = "test.bridge." + uniquePrefix();
        secondBus.registerConsumer(address, message ->
                CompletableFuture.completedFuture("remote-reply"));
        Object reply = firstBridge.forwardRequest(address, "ask", 10_000L)
                .get(10, TimeUnit.SECONDS);
        assertThat(reply, is("remote-reply"));
    }

    @Test
    void restartPreservesRegistrations() throws Exception {
        String address = "test.bridge." + uniquePrefix();
        CountDownLatch latch = new CountDownLatch(1);
        secondBus.registerConsumer(address, message -> {
            latch.countDown();
            return CompletableFuture.completedFuture(null);
        });
        firstBridge.stop();
        firstBridge.start();
        firstBridge.forwardPublish(address, "after-restart");
        assertThat(latch.await(10, TimeUnit.SECONDS), is(true));
    }

    @Test
    void echoSuppressionBlocksOwnDelivery() throws Exception {
        // A bridged publish from stack one must not loop back to stack one's consumers.
        String address = "test.bridge." + uniquePrefix();
        AtomicReference<Object> echo = new AtomicReference<>("not-delivered");
        firstBus.registerConsumer(address, message -> {
            echo.set("echoed");
            return CompletableFuture.completedFuture(null);
        });
        firstBridge.forwardPublish(address, "hello");
        Thread.sleep(1000);
        assertThat(echo.get(), is("not-delivered"));
    }
}
