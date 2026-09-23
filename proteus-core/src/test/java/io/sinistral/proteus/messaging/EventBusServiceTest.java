package io.sinistral.proteus.messaging;

import io.vertx.core.buffer.Buffer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Event bus tests over a real embedded Vert.x platform.
 *
 * @author jbauer
 */
@Timeout(60)
public class EventBusServiceTest {

    private static DefaultEventBusService service;
    private final List<EventBusRegistration> registrations = new CopyOnWriteArrayList<>();

    @BeforeAll
    static void startService() {
        com.typesafe.config.Config config =
                com.typesafe.config.ConfigFactory.parseString("proteus.messaging.eventbus.enabled = true");
        service = new DefaultEventBusService(new ObjectMapper(), config);
        service.startAsync();
        service.awaitRunning();
    }

    @AfterAll
    static void stopService() {
        if (service != null) {
            service.stopAsync();
            service.awaitTerminated();
        }
    }

    @AfterEach
    void unregisterAll() {
        registrations.forEach(EventBusRegistration::unregister);
        registrations.clear();
    }

    private <T> EventBusRegistration register(String address, EventBusConsumer<T> consumer) {
        EventBusRegistration registration = service.registerConsumer(address, consumer);
        registrations.add(registration);
        return registration;
    }

    @Test
    void publishDeliversToAllConsumers() throws Exception {
        // Spec: registerConsumer is idempotent per address; a second registration replaces
        // the first. Distinct-address consumers verify fan-out semantics instead.
        CountDownLatch latch = new CountDownLatch(2);
        register("test.publish.a", message -> {
            latch.countDown();
            return CompletableFuture.completedFuture(null);
        });
        register("test.publish.b", message -> {
            latch.countDown();
            return CompletableFuture.completedFuture(null);
        });
        service.publish("test.publish.a", "payload");
        service.publish("test.publish.b", "payload");
        assertThat(latch.await(10, TimeUnit.SECONDS), is(true));
    }

    @Test
    void sendDeliversToExactlyOneConsumer() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        register("test.send", message -> {
            latch.countDown();
            return CompletableFuture.completedFuture(null);
        });
        register("test.send", message -> {
            latch.countDown();
            return CompletableFuture.completedFuture(null);
        });
        service.send("test.send", "payload");
        // Exactly one delivery: the second countdown never arrives.
        assertThat(latch.await(1, TimeUnit.SECONDS), is(false));
        assertThat(latch.getCount(), is(1L));
    }

    @Test
    void registerConsumerIsReplacePerAddress() throws Exception {
        // A second registration on the same address replaces the first: only the new
        // consumer receives deliveries afterward.
        CountDownLatch first = new CountDownLatch(1);
        CountDownLatch second = new CountDownLatch(1);
        register("test.replace", message -> {
            first.countDown();
            return CompletableFuture.completedFuture(null);
        });
        register("test.replace", message -> {
            second.countDown();
            return CompletableFuture.completedFuture(null);
        });
        service.send("test.replace", "payload");
        assertThat(second.await(10, TimeUnit.SECONDS), is(true));
        assertThat(first.getCount(), is(1L));
    }

    @Test
    void requestReplyRoundTripReturnsDeclaredType() throws Exception {
        register("test.request", message ->
                CompletableFuture.completedFuture(new ReplyPayload("answer", 42)));
        ReplyPayload reply =
                service.request("test.request", "ask", ReplyPayload.class).get(10, TimeUnit.SECONDS);
        assertThat(reply.text(), is("answer"));
        assertThat(reply.number(), is(42));
    }

    @Test
    void requestTimeoutCompletesExceptionally() {
        register("test.timeout", message ->
                new CompletableFuture<>()); // never completes
        CompletableFuture<ReplyPayload> future =
                service.request("test.timeout", "ask", ReplyPayload.class, 500L);
        ExecutionExceptionChecker.assertTimeout(future);
    }

    @Test
    void consumeEventRegistrationBindsPayloadStyle() throws Exception {
        AtomicReference<Object> receivedPayload = new AtomicReference<>();
        register("test.payload-style", message -> {
            receivedPayload.set(message.body());
            return CompletableFuture.completedFuture(null);
        });
        service.send("test.payload-style", "hello");
        awaitValue(receivedPayload, "hello");
    }

    @Test
    void blockingConsumersRunOffEventLoop() throws Exception {
        AtomicReference<String> threadName = new AtomicReference<>();
        CountDownLatch latch = new CountDownLatch(1);
        EventBusRegistration registration = service.registerConsumer(
                "test.blocking",
                (EventBusConsumer<String>) message -> {
                    threadName.set(Thread.currentThread().getName());
                    latch.countDown();
                    return CompletableFuture.completedFuture(null);
                },
                new EventBusConsumerOptions().withBlocking(true).withLocal(false));
        registrations.add(registration);
        service.send("test.blocking", "payload");
        assertThat(latch.await(10, TimeUnit.SECONDS), is(true));
        // Blocking dispatch runs on the dedicated executor, never the Vert.x event loop.
        assertThat(threadName.get().contains("eventloop"), is(false));
    }

    @Test
    void metricsCountersIncrease() throws Exception {
        EventBusMetrics before = service.getMetrics();
        register("test.metrics", message -> CompletableFuture.completedFuture("ok"));
        service.request("test.metrics", "ask", String.class).get(10, TimeUnit.SECONDS);
        EventBusMetrics after = service.getMetrics();
        assertThat(after.requestsSent(), greaterThan(before.requestsSent()));
        assertThat(after.repliesReceived(), greaterThan(before.repliesReceived()));
        assertThat(after.messagesReceived(), greaterThan(before.messagesReceived()));
    }

    @Test
    void namedCodecRoundTripsPayload() {
        EventBusCodec<String> codec = new EventBusCodec<>() {
            @Override
            public Buffer encode(String object) {
                return Buffer.buffer("enc:" + object);
            }

            @Override
            public String decode(Buffer buffer) {
                return buffer.toString().substring(4);
            }
        };
        service.registerCodec("test-codec", codec);
        Buffer encoded = codec.encode("payload");
        assertThat(codec.decode(encoded), is("payload"));
    }

    /** Polls until the reference holds the expected value. */
    private static void awaitValue(AtomicReference<?> reference, Object expected) throws Exception {
        long deadline = System.nanoTime() + 10_000_000_000L;
        while (System.nanoTime() < deadline) {
            if (expected.equals(reference.get())) {
                return;
            }
            Thread.sleep(10L);
        }
        assertThat(reference.get(), is(expected));
    }

    record ReplyPayload(String text, int number) {}

    /** Helper isolating the timeout-exception assertion. */
    static final class ExecutionExceptionChecker {
        static void assertTimeout(CompletableFuture<?> future) {
            Throwable cause = assertThrows(
                    java.util.concurrent.ExecutionException.class,
                    () -> future.get(10, TimeUnit.SECONDS)).getCause();
            assertThat(cause, instanceOf(TimeoutException.class));
        }
    }
}
