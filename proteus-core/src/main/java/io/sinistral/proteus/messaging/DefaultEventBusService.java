package io.sinistral.proteus.messaging;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.eventbus.ReplyFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Guava idle service embedding the Vert.x platform and exposing the typed event bus.
 *
 * <p>Local delivery passes the payload by reference without encoding. The blocking
 * executor is virtual-thread backed. Delivery failures inside a consumer increment the
 * error metric and log at warn level.
 *
 * @author jbauer
 */
@Singleton
public class DefaultEventBusService extends AbstractIdleService
        implements EventBusService, io.sinistral.proteus.services.BaseService {

    private static final Logger log = LoggerFactory.getLogger(DefaultEventBusService.class);

    private static final long DEFAULT_REQUEST_TIMEOUT_MS = 30_000L;

    private final ObjectMapper objectMapper;
    private final boolean enabled;
    private final int blockingPoolSize;

    private volatile Vertx vertx;
    private volatile ExecutorService blockingExecutor;

    // Counters are monotonic from service start.
    private final AtomicLong messagesSent = new AtomicLong();
    private final AtomicLong messagesReceived = new AtomicLong();
    private final AtomicLong messagesPublished = new AtomicLong();
    private final AtomicLong requestsSent = new AtomicLong();
    private final AtomicLong repliesReceived = new AtomicLong();
    private final AtomicLong errorCount = new AtomicLong();
    private final AtomicLong timeoutCount = new AtomicLong();
    private final AtomicLong pendingRequests = new AtomicLong();
    private final LatencyRecorder latencyRecorder = new LatencyRecorder();

    private final Map<String, MessageConsumer<Object>> registrations = new ConcurrentHashMap<>();
    private final Map<String, RegistrationHandle> handles = new ConcurrentHashMap<>();
    private final Map<String, EventBusConsumerOptions> optionsByAddress = new ConcurrentHashMap<>();
    private final Map<String, EventBusCodec<?>> codecs = new ConcurrentHashMap<>();

    // Bridge hook installed by the NATS bridge when active; send=false for publish.
    private final AtomicReference<BridgeHook> bridgeHook = new AtomicReference<>();

    @Inject
    public DefaultEventBusService(ObjectMapper objectMapper, Config config) {
        this.objectMapper = objectMapper;
        this.enabled = !config.hasPath("proteus.messaging.eventbus.enabled")
                || config.getBoolean("proteus.messaging.eventbus.enabled");
        this.blockingPoolSize = config.hasPath("proteus.messaging.eventbus.blockingPoolSize")
                ? config.getInt("proteus.messaging.eventbus.blockingPoolSize")
                : 32;
    }

    /** Installs the bridge dispatch hook consulted by publish and send. */
    public void setBridgeHook(BridgeHook hook) {
        bridgeHook.set(hook);
    }

    /** Callback consulted by publish and send to mirror traffic to a remote bridge. */
    @FunctionalInterface
    public interface BridgeHook {
        /** Mirrors one publish (send=false) or send (send=true) to the bridge. */
        void dispatch(String address, Object message, boolean send);
    }

    /** @return the embedded Vert.x instance, or null when the service is not running */
    public Vertx vertx() {
        return vertx;
    }

    @Override
    public void configure(Binder binder) {
        // no additional bindings; this module only participates in service lifecycle
    }

    @Override
    protected void startUp() {
        if (!enabled) {
            log.info("Event bus disabled by configuration");
            return;
        }
        // Pool size bounds concurrent blocking consumers; threads themselves are virtual.
        blockingExecutor = Executors.newFixedThreadPool(
                blockingPoolSize, Thread.ofVirtual().factory());
        vertx = Vertx.vertx(new VertxOptions());
        // Default wire format for every payload type: JSON via the application mapper.
        // Local delivery stays by reference (codec transform is identity).
        vertx.eventBus()
                .registerCodec(new JsonMessageCodec(objectMapper))
                .codecSelector(object -> JsonMessageCodec.CODEC_NAME);
        log.info("Event bus started with blocking pool size {}", blockingPoolSize);
    }

    @Override
    protected void shutDown() {
        registrations.values().forEach(MessageConsumer::unregister);
        registrations.clear();
        handles.clear();
        optionsByAddress.clear();
        if (vertx != null) {
            vertx.close();
            vertx = null;
        }
        if (blockingExecutor != null) {
            blockingExecutor.shutdownNow();
            blockingExecutor = null;
        }
    }

    @Override
    public boolean isAvailable() {
        return enabled && isRunning() && vertx != null;
    }

    @Override
    public <T> void publish(String address, T message) {
        requireAvailable();
        vertx.eventBus().publish(address, message);
        messagesPublished.incrementAndGet();
        bridgeDispatch(address, message, false);
    }

    @Override
    public <T> void send(String address, T message) {
        requireAvailable();
        vertx.eventBus().send(address, message);
        messagesSent.incrementAndGet();
        bridgeDispatch(address, message, true);
    }

    @Override
    public <T, R> CompletableFuture<R> request(String address, T message, Class<R> responseType) {
        return request(address, message, responseType, DEFAULT_REQUEST_TIMEOUT_MS);
    }

    @Override
    public <T, R> CompletableFuture<R> request(
            String address, T message, Class<R> responseType, long timeoutMs) {
        requireAvailable();
        CompletableFuture<R> result = new CompletableFuture<>();
        requestsSent.incrementAndGet();
        pendingRequests.incrementAndGet();
        long start = System.nanoTime();
        vertx.eventBus()
                .request(address, message, new DeliveryOptions().setSendTimeout((int) timeoutMs))
                .onComplete(ar -> {
                    pendingRequests.decrementAndGet();
                    if (ar.succeeded()) {
                        repliesReceived.incrementAndGet();
                        latencyRecorder.record(System.nanoTime() - start);
                        result.complete(convert(ar.result().body(), responseType));
                    } else {
                        recordRequestFailure(ar.cause(), result);
                    }
                });
        return result;
    }

    @Override
    public <T> EventBusRegistration registerConsumer(
            String address, EventBusConsumer<T> consumer) {
        return registerConsumer(address, consumer, new EventBusConsumerOptions());
    }

    @Override
    public <T> EventBusRegistration registerConsumer(
            String address, EventBusConsumer<T> consumer, EventBusConsumerOptions options) {
        requireAvailable();
        unregister(address);
        Handler<Message<Object>> handler = wrapConsumer(consumer, options);
        MessageConsumer<Object> vertxConsumer = options.local()
                ? vertx.eventBus().localConsumer(address, handler)
                : vertx.eventBus().consumer(address, handler);
        registrations.put(address, vertxConsumer);
        optionsByAddress.put(address, options);
        RegistrationHandle handle = new RegistrationHandle(address);
        handles.put(address, handle);
        return handle;
    }

    @Override
    public <T> void registerCodec(String codecName, EventBusCodec<T> codec) {
        codecs.put(codecName, codec);
    }

    /** @return options registered for the address, or null when no consumer is registered */
    public EventBusConsumerOptions optionsFor(String address) {
        return optionsByAddress.get(address);
    }

    /** @return true when a consumer registration is currently bound to the address */
    public boolean hasConsumer(String address) {
        return registrations.containsKey(address);
    }

    /**
     * Bridged-request entry point: asks local consumers of the address for a reply without
     * NATS forwarding. Returns a failed future when the bus is unavailable; a request to an
     * address with no consumer fails on timeout.
     */
    public CompletableFuture<Object> requestInternal(
            String address, Object message, long timeoutMs) {
        if (!isAvailable()) {
            return CompletableFuture.failedFuture(
                new IllegalStateException("Event bus is not available"));
        }
        return request(address, message, Object.class, timeoutMs);
    }

    /** Records one bridge or decode failure against the error metric with a warn log. */
    public void recordError(String context, Throwable cause) {
        errorCount.incrementAndGet();
        log.warn("{}: {}", context, String.valueOf(cause.getMessage()));
    }

    /**
     * Delivers a payload that arrived from the NATS bridge to local consumers of the address.
     * Local-only consumers receive it because the delivery itself happens in-process.
     */
    public void deliverRemote(String address, Object payload) {
        if (!isAvailable()) {
            return;
        }
        MessageConsumer<Object> consumer = registrations.get(address);
        if (consumer == null) {
            return;
        }
        // Deliver through the Vert.x event bus so dispatch and blocking policies apply.
        vertx.eventBus().publish(address, payload);
        messagesReceived.incrementAndGet();
        messagesPublished.incrementAndGet();
    }

    @Override
    public EventBusMetrics getMetrics() {
        return new EventBusMetrics(
                messagesSent.get(),
                messagesReceived.get(),
                messagesPublished.get(),
                requestsSent.get(),
                repliesReceived.get(),
                pendingRequests.get(),
                errorCount.get(),
                timeoutCount.get(),
                latencyRecorder.averageMs(),
                latencyRecorder.maxMs());
    }

    private void unregister(String address) {
        MessageConsumer<Object> previous = registrations.remove(address);
        if (previous != null) {
            previous.unregister();
        }
        handles.remove(address);
        optionsByAddress.remove(address);
    }

    private void requireAvailable() {
        if (!isAvailable()) {
            throw new IllegalStateException("Event bus is not available");
        }
    }

    /** Wraps a typed consumer with metrics, error policy, and executor dispatch. */
    @SuppressWarnings("unchecked")
    private <T> Handler<Message<Object>> wrapConsumer(
            EventBusConsumer<T> consumer, EventBusConsumerOptions options) {
        return message -> {
            messagesReceived.incrementAndGet();
            if (options.blocking() && blockingExecutor != null) {
                blockingExecutor.execute(() -> invokeConsumer(consumer, message));
            } else {
                invokeConsumer(consumer, message);
            }
        };
    }

    /** Runs one consumer callback; a non-null result or future value becomes the reply. */
    @SuppressWarnings("unchecked")
    private <T> void invokeConsumer(EventBusConsumer<T> consumer, Message<Object> message) {
        try {
            CompletableFuture<?> processing = consumer.handle((Message<T>) message);
            processing.whenComplete((ignored, throwable) -> {
                if (throwable != null) {
                    failDelivery(message, throwable);
                    return;
                }
                Object reply = processing.join();
                if (message.replyAddress() != null && reply != null) {
                    try {
                        message.reply(reply);
                    } catch (RuntimeException e) {
                        // Reply codec failures must surface in the error metric, not vanish.
                        failDelivery(message, e);
                    }
                }
            });
        } catch (RuntimeException e) {
            failDelivery(message, e);
        }
    }

    /** Marks one delivery failed: error metric, warn log, and 500 reply when expected. */
    private void failDelivery(Message<Object> message, Throwable cause) {
        errorCount.incrementAndGet();
        log.warn("Event bus consumer failed on address {}", message.address(), cause);
        if (message.replyAddress() != null) {
            message.fail(500, String.valueOf(cause.getMessage()));
        }
    }

    /** Converts a reply body to the declared response type using the Jackson mapper. */
    @SuppressWarnings("unchecked")
    private <R> R convert(Object body, Class<R> responseType) {
        if (body == null || responseType.isInstance(body)) {
            return (R) body;
        }
        return objectMapper.convertValue(body, responseType);
    }

    private void recordRequestFailure(Throwable cause, CompletableFuture<?> result) {
        if (cause instanceof ReplyException replyException
                && replyException.failureType() == ReplyFailure.TIMEOUT) {
            timeoutCount.incrementAndGet();
            result.completeExceptionally(new TimeoutException("Event bus request timed out"));
        } else {
            errorCount.incrementAndGet();
            result.completeExceptionally(cause);
        }
    }

    private void bridgeDispatch(String address, Object message, boolean send) {
        BridgeHook hook = bridgeHook.get();
        if (hook != null) {
            hook.dispatch(address, message, send);
        }
    }

    /** Registration handle bound to the service registration map. */
    private final class RegistrationHandle implements EventBusRegistration {
        private final String address;
        private final AtomicBoolean active = new AtomicBoolean(true);

        RegistrationHandle(String address) {
            this.address = address;
        }

        @Override
        public void unregister() {
            if (active.compareAndSet(true, false)) {
                DefaultEventBusService.this.unregister(address);
            }
        }

        @Override
        public String getAddress() {
            return address;
        }

        @Override
        public boolean isActive() {
            return active.get() && registrations.containsKey(address);
        }
    }
}
