package io.sinistral.proteus.messaging;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.eventbus.Message;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Verifies {@link ConsumeEventProcessor} binds annotated methods in both parameter
 * styles on Guice-bound singletons.
 *
 * @author jbauer
 */
@Timeout(60)
public class ConsumeEventProcessorTest {

    /** Payload-style bean: single payload parameter. */
    static class PayloadStyleBean {
        final AtomicReference<Object> received = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);

        @ConsumeEvent("processor.payload.style")
        void onPayload(String payload) {
            received.set(payload);
            latch.countDown();
        }
    }

    /** Message-style bean: single Message parameter. */
    static class MessageStyleBean {
        final AtomicReference<Object> received = new AtomicReference<>();
        final CountDownLatch latch = new CountDownLatch(1);

        @ConsumeEvent("processor.message.style")
        CompletableFuture<Void> onMessage(Message<String> message) {
            received.set(message.body());
            latch.countDown();
            return CompletableFuture.completedFuture(null);
        }
    }

    /** Reply bean: returned value answers request-reply. */
    static class ReplyBean {
        @ConsumeEvent("processor.reply")
        String reply(String request) {
            return "echo:" + request;
        }
    }

    private static DefaultEventBusService service;
    private static ConsumeEventProcessor processor;
    private static Injector injector;
    private static PayloadStyleBean payloadBean;
    private static MessageStyleBean messageBean;

    @BeforeAll
    static void start() {
        com.typesafe.config.Config config =
                com.typesafe.config.ConfigFactory.parseString("proteus.messaging.eventbus.enabled = true");
        service = new DefaultEventBusService(new tools.jackson.databind.ObjectMapper(), config);
        service.startAsync();
        service.awaitRunning();
        injector = Guice.createInjector(binder -> {
            binder.bind(DefaultEventBusService.class).toInstance(service);
            binder.bind(EventBusService.class).toInstance(service);
            binder.bind(PayloadStyleBean.class).in(com.google.inject.Singleton.class);
            binder.bind(MessageStyleBean.class).in(com.google.inject.Singleton.class);
        });
        payloadBean = injector.getInstance(PayloadStyleBean.class);
        messageBean = injector.getInstance(MessageStyleBean.class);
        processor = new ConsumeEventProcessor(injector, service);
        int registered = processor.process(java.util.List.of(
                PayloadStyleBean.class, MessageStyleBean.class, ReplyBean.class));
        assertThat(registered, is(3));
    }

    @AfterAll
    static void stop() {
        if (processor != null) {
            processor.unregisterAll();
        }
        if (service != null) {
            service.stopAsync();
            service.awaitTerminated();
        }
    }

    @Test
    void payloadStyleBindingDeliversBody() throws Exception {
        service.send("processor.payload.style", "payload-body");
        assertThat(payloadBean.latch.await(10, TimeUnit.SECONDS), is(true));
        assertThat(payloadBean.received.get(), is("payload-body"));
    }

    @Test
    void messageStyleBindingDeliversMessage() throws Exception {
        service.send("processor.message.style", "message-body");
        assertThat(messageBean.latch.await(10, TimeUnit.SECONDS), is(true));
        assertThat(messageBean.received.get(), is("message-body"));
    }

    @Test
    void replyValueAnswersRequestReply() throws Exception {
        String reply = service.request("processor.reply", "ask", String.class)
                .get(10, TimeUnit.SECONDS);
        assertThat(reply, is("echo:ask"));
    }
}
