package io.sinistral.proteus.messaging.nats;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import io.nats.client.Nats;
import io.nats.client.Options;
import io.nats.client.impl.Headers;
import io.sinistral.proteus.messaging.DefaultEventBusService;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;

/**
 * NATS bridge forwarding event bus traffic across processes and machines.
 *
 * <p>Activates when {@code proteus.messaging.nats.enabled} is true. Event bus address
 * {@code a.b.c} maps to subject {@code <subjectPrefix>.a.b.c}. Two dispatchers cover the
 * delivery semantics: a queue-group dispatcher delivers each publish/send envelope to
 * exactly one process, and a plain dispatcher answers request-reply traffic from any
 * process with a local consumer. An origin header (outside the JSON envelope) suppresses
 * echo delivery of a process's own bridged messages.
 *
 * <p>Token auth wins when both token and user/password auth are configured. Envelope
 * decode failures are logged at warn level, increment the event bus error count, and are
 * not redelivered.
 *
 * @author jbauer
 */
@Singleton
public class DefaultNatsBridgeService implements NatsBridgeService {

    /** Class logger. */
    private static final Logger log = LoggerFactory.getLogger(DefaultNatsBridgeService.class);

    /** NATS message header carrying the bridge instance id, used for echo suppression. */
    static final String ORIGIN_HEADER = "X-Proteus-Bridge-Origin";

    /** Local event bus whose traffic is mirrored to NATS. */
    private final DefaultEventBusService eventBusService;
    /** Shared Jackson mapper for envelope serialization. */
    private final ObjectMapper objectMapper;
    /** Whether the bridge is enabled in configuration. */
    private final boolean enabled;
    /** NATS server URL. */
    private final String url;
    /** Subject prefix applied to every mirrored address. */
    private final String subjectPrefix;
    /** Queue group distributing inbound messages across cluster nodes. */
    private final String queueGroup;
    /** Request-reply timeout in milliseconds. */
    private final long requestTimeoutMs;
    /** Maximum NATS client reconnect attempts. */
    private final int maxReconnects;
    /** Delay between reconnect attempts in milliseconds. */
    private final long reconnectWaitMs;
    /** Bearer token when token auth is configured. */
    private final String authToken;
    /** Username when basic auth is configured. */
    private final String authUsername;
    /** Password when basic auth is configured. */
    private final String authPassword;
    /** Unique bridge instance id used for echo suppression. */
    private final String originId = UUID.randomUUID().toString();

    /** Active NATS connection; null when stopped. */
    private volatile Connection connection;
    /** Dispatcher dedicated to point-to-point sends. */
    private volatile Dispatcher sendDispatcher;
    /** Dispatcher dedicated to publish fan-out. */
    private volatile Dispatcher publishDispatcher;
    /** Dispatcher dedicated to request-reply calls. */
    private volatile Dispatcher requestDispatcher;

    // Handlers for messages arriving from NATS, keyed by event bus address.
    /** Registered inbound handlers keyed by event bus address. */
    private final Map<String, Consumer<JsonObject>> inboundHandlers = new ConcurrentHashMap<>();

    /** Creates the bridge from configuration.
     *
     * @param eventBusService local event bus to mirror
     * @param objectMapper shared Jackson mapper
     * @param config Typesafe config holding the proteus.messaging.nats settings
     */
    @Inject
    public DefaultNatsBridgeService(
            DefaultEventBusService eventBusService, ObjectMapper objectMapper, Config config) {
        this.eventBusService = eventBusService;
        this.objectMapper = objectMapper;
        this.enabled = config.hasPath("proteus.messaging.nats.enabled")
                && config.getBoolean("proteus.messaging.nats.enabled");
        this.url = config.hasPath("proteus.messaging.nats.url")
                ? config.getString("proteus.messaging.nats.url")
                : "nats://tachikoma:4232";
        this.subjectPrefix = config.hasPath("proteus.messaging.nats.subjectPrefix")
                ? config.getString("proteus.messaging.nats.subjectPrefix")
                : "proteus";
        this.queueGroup = config.hasPath("proteus.messaging.nats.queueGroup")
                ? config.getString("proteus.messaging.nats.queueGroup")
                : "proteus";
        this.requestTimeoutMs = config.hasPath("proteus.messaging.nats.requestTimeoutMs")
                ? config.getLong("proteus.messaging.nats.requestTimeoutMs")
                : 5_000L;
        this.maxReconnects = config.hasPath("proteus.messaging.nats.maxReconnects")
                ? config.getInt("proteus.messaging.nats.maxReconnects")
                : 60;
        this.reconnectWaitMs = config.hasPath("proteus.messaging.nats.reconnectWaitMs")
                ? config.getLong("proteus.messaging.nats.reconnectWaitMs")
                : 2_000L;
        this.authToken = config.hasPath("proteus.messaging.nats.auth.token")
                ? config.getString("proteus.messaging.nats.auth.token")
                : null;
        if (config.hasPath("proteus.messaging.nats.auth.username")
                && config.hasPath("proteus.messaging.nats.auth.password")) {
            this.authUsername = config.getString("proteus.messaging.nats.auth.username");
            this.authPassword = config.getString("proteus.messaging.nats.auth.password");
        } else {
            this.authUsername = null;
            this.authPassword = null;
        }
    }

    /** Returns the NATS subject mapped from an event bus address.
     *
     * @param address the event bus address
     * @return the derived NATS subject
     */
    public String subjectFor(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address must not be blank: " + address);
        }
        String subject = subjectPrefix + "." + address;
        // NATS subjects allow [A-Za-z0-9._-]; wildcards and spaces are illegal here.
        if (!subject.matches("[A-Za-z0-9._-]+")) {
            throw new IllegalArgumentException(
                "Address maps to an illegal NATS subject: " + subject);
        }
        return subject;
    }

    /** Returns the event bus address mapped from a NATS subject.
     *
     * @param subject the NATS subject
     * @return the mapped address, or null when the subject is unprefixed
     */
    public String addressFor(String subject) {
        String prefix = subjectPrefix + ".";
        if (!subject.startsWith(prefix)) {
            return null;
        }
        String rest = subject.substring(prefix.length());
        // Strip the internal routing token from send/publish subjects.
        if (rest.startsWith("send.")) {
            rest = rest.substring("send.".length());
        } else if (rest.startsWith("publish.")) {
            rest = rest.substring("publish.".length());
        }
        return rest;
    }

    @Override
    public void start() {
        if (!enabled) {
            log.info("NATS bridge disabled by configuration");
            return;
        }
        try {
            Options.Builder builder = new Options.Builder()
                    .server(url)
                    .maxReconnects(maxReconnects)
                    .reconnectWait(Duration.ofMillis(reconnectWaitMs))
                    .connectionListener((conn, event) ->
                            log.debug("NATS connection event: {}", event));
            if (authToken != null && !authToken.isBlank()) {
                builder.token(authToken.toCharArray());
            } else if (authUsername != null) {
                builder.userInfo(authUsername.toCharArray(), authPassword.toCharArray());
            }
            connection = Nats.connect(builder.build());
            String wildcard = subjectPrefix + ".>";
            // Send envelopes: queue-group subscription delivers each to exactly one process.
            sendDispatcher = connection.createDispatcher(this::handleDelivery)
                    .subscribe(sendSubject(wildcard), queueGroup);
            // Publish envelopes: plain subscription fans out to every process's consumers.
            publishDispatcher = connection.createDispatcher(this::handleDelivery)
                    .subscribe(publishSubject(wildcard));
            // Plain dispatcher: request-reply; every process with a local consumer answers.
            requestDispatcher = connection.createDispatcher(this::handleRequest)
                    .subscribe(wildcard);
            eventBusService.setBridgeHook(this::bridgeHook);
            log.info("NATS bridge subscribed to '{}' (send queue '{}', publish fan-out)",
                    wildcard, queueGroup);
        } catch (Exception e) {
            stop();
            throw new IllegalStateException("NATS bridge failed to start: " + url, e);
        }
    }

    @Override
    public void stop() {
        eventBusService.setBridgeHook(null);
        closeDispatcherQuietly(sendDispatcher);
        closeDispatcherQuietly(publishDispatcher);
        closeDispatcherQuietly(requestDispatcher);
        sendDispatcher = null;
        publishDispatcher = null;
        requestDispatcher = null;
        Connection current = connection;
        connection = null;
        if (current != null) {
            try {
                current.drain(Duration.ofSeconds(5));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.warn("NATS drain or close did not complete cleanly", e);
            }
        }
    }

    /** Closes one dispatcher, tolerating an already-closed connection.
     *
     * @param dispatcher the dispatcher to close, may be null
     */
    private void closeDispatcherQuietly(Dispatcher dispatcher) {
        if (dispatcher == null || connection == null) {
            return;
        }
        try {
            connection.closeDispatcher(dispatcher);
        } catch (RuntimeException ignored) {
            // connection already closed
        }
    }

    @Override
    public boolean isRunning() {
        Connection current = connection;
        return enabled && current != null
                && current.getStatus() == Connection.Status.CONNECTED;
    }

    @Override
    public void forwardPublish(String address, Object message) {
        requireRunning();
        byte[] body = envelopeBytes(address, message);
        // Fan-out subject so every process's publish dispatcher receives the envelope.
        String base = subjectFor(address);
        connection.publish(
                subjectPrefix + ".publish." + base.substring(subjectPrefix.length() + 1),
                originHeaders(), body);
        flushQuietly();
    }

    @Override
    public CompletableFuture<Object> forwardRequest(
            String address, Object message, long timeoutMs) {
        requireRunning();
        CompletableFuture<Object> result = new CompletableFuture<>();
        byte[] body = envelopeBytes(address, message);
        connection
                .request(subjectFor(address), originHeaders(), body)
                .thenAccept(reply -> {
                    try {
                        result.complete(NatsEnvelope
                                .fromJson(new String(reply.getData(), StandardCharsets.UTF_8),
                                        objectMapper)
                                .payload());
                    } catch (RuntimeException e) {
                        result.completeExceptionally(e);
                    }
                })
                .exceptionally(throwable -> {
                    result.completeExceptionally(throwable);
                    return null;
                });
        // Bound the wait: jnats request() future has no built-in deadline.
        return withTimeout(result, timeoutMs);
    }

    /** Completes the future exceptionally when the deadline elapses first.
     *
     * @param <T> future value type
     * @param future the unbounded source future
     * @param timeoutMs deadline in milliseconds
     * @return a future bounded by the deadline
     */
    private <T> CompletableFuture<T> withTimeout(CompletableFuture<T> future, long timeoutMs) {
        CompletableFuture<T> bounded = new CompletableFuture<>();
        future.whenComplete((value, throwable) -> {
            if (throwable != null) {
                bounded.completeExceptionally(throwable);
            } else {
                bounded.complete(value);
            }
        });
        scheduler().schedule(() -> bounded.completeExceptionally(
                new TimeoutException("NATS request timed out")), timeoutMs,
                java.util.concurrent.TimeUnit.MILLISECONDS);
        return bounded;
    }

    /** Shared single-thread scheduler for request deadlines. */
    private java.util.concurrent.ScheduledExecutorService schedulerHolder;

    /** Lazily creates the shared single-thread deadline scheduler.
     *
     * @return the shared scheduler instance
     */
    private java.util.concurrent.ScheduledExecutorService scheduler() {
        if (schedulerHolder == null) {
            synchronized (this) {
                if (schedulerHolder == null) {
                    schedulerHolder = java.util.concurrent.Executors.newSingleThreadScheduledExecutor(
                            Thread.ofVirtual().factory());
                }
            }
        }
        return schedulerHolder;
    }

    @Override
    public void subscribe(String address, Consumer<JsonObject> handler) {
        inboundHandlers.put(address, handler);
    }

    /** Returns the subject carrying send envelopes for the wildcard.
     *
     * @param wildcard the inbound wildcard subject
     * @return the send routing subject
     */
    private String sendSubject(String wildcard) {
        return subjectPrefix + ".send." + wildcard.substring(subjectPrefix.length() + 1);
    }

    /** Returns the subject carrying publish envelopes for the wildcard.
     *
     * @param wildcard the inbound wildcard subject
     * @return the publish routing subject
     */
    private String publishSubject(String wildcard) {
        return subjectPrefix + ".publish." + wildcard.substring(subjectPrefix.length() + 1);
    }

    /** Event bus hook: mirrors publish/send to NATS when running and not local-only.
     *
     * @param address the event bus address
     * @param message the outbound payload
     * @param send true for point-to-point, false for fan-out
     */
    private void bridgeHook(String address, Object message, boolean send) {
        if (!isRunning()) {
            return;
        }
        var options = eventBusService.optionsFor(address);
        if (options != null && options.local()) {
            return;
        }
        // Sends already delivered locally and route through the queue-group subject;
        // publishes fan out to every process that has consumers on the address.
        String base = subjectFor(address);
        String subject = send
                ? subjectPrefix + ".send." + base.substring(subjectPrefix.length() + 1)
                : subjectPrefix + ".publish." + base.substring(subjectPrefix.length() + 1);
        connection.publish(subject, originHeaders(), envelopeBytes(address, message));
        flushQuietly();
    }

    /** Queue-group delivery path for publish and send envelopes.
     *
     * @param message the inbound NATS message
     */
    private void handleDelivery(io.nats.client.Message message) {
        String address = addressFor(message.getSubject());
        if (address == null || isOwnEcho(message)) {
            return;
        }
        NatsEnvelope envelope = decodeOrNull(message);
        if (envelope == null) {
            return;
        }
        Consumer<JsonObject> handler = inboundHandlers.get(address);
        if (handler != null) {
            handler.accept(toJsonObject(envelope.payload()));
        }
        eventBusService.deliverRemote(address, envelope.payload());
    }

    /** Plain dispatcher path: answer remote requests from local consumers.
     *
     * @param message the inbound NATS request
     */
    private void handleRequest(io.nats.client.Message message) {
        if (message.getReplyTo() == null) {
            return;
        }
        String address = addressFor(message.getSubject());
        if (address == null || isOwnEcho(message) || !eventBusService.hasConsumer(address)) {
            return;
        }
        NatsEnvelope envelope = decodeOrNull(message);
        if (envelope == null) {
            return;
        }
        eventBusService
                .requestInternal(address, envelope.payload(), requestTimeoutMs)
                .whenComplete((reply, throwable) -> {
                    if (throwable != null) {
                        // No local answer; let the remote requester time out.
                        log.debug("No local answer for bridged request on {}", address);
                        return;
                    }
                    try {
                        connection.publish(message.getReplyTo(), envelopeBytes(address, reply));
                        flushQuietly();
                    } catch (RuntimeException e) {
                        eventBusService.recordError("NATS reply failed", e);
                    }
                });
    }

    /** Returns the decoded envelope or null after recording a decode failure.
     *
     * @param message the inbound NATS message
     * @return decoded envelope, or null on decode failure
     */
    private NatsEnvelope decodeOrNull(io.nats.client.Message message) {
        try {
            return NatsEnvelope.fromJson(
                    new String(message.getData(), StandardCharsets.UTF_8), objectMapper);
        } catch (RuntimeException e) {
            eventBusService.recordError("NATS envelope decode failed", e);
            log.warn("NATS envelope decode failed on {}", message.getSubject(), e);
            return null;
        }
    }

    /** Returns true when the message carries this instance's origin header.
     *
     * @param message the inbound NATS message
     * @return true when the message is this bridge's own echo
     */
    private boolean isOwnEcho(io.nats.client.Message message) {
        io.nats.client.impl.Headers headers = message.getHeaders();
        return headers != null && originId.equals(headers.getFirst(ORIGIN_HEADER));
    }

    /** Returns headers stamped with this bridge instance id.
     *
     * @return origin headers for outgoing publishes
     */
    private Headers originHeaders() {
        return new Headers().put(ORIGIN_HEADER, originId);
    }

    /** Serializes one envelope for the wire.
     *
     * @param address the event bus address
     * @param payload the outbound payload
     * @return the JSON envelope bytes
     */
    private byte[] envelopeBytes(String address, Object payload) {
        return NatsEnvelope.of(address, payload)
                .toJson(objectMapper)
                .getBytes(StandardCharsets.UTF_8);
    }

    /** Wraps a decoded payload as a JsonObject; scalars are wrapped under a value key.
     *
     * @param payload the decoded envelope payload
     * @return the payload as a JsonObject
     */
    @SuppressWarnings("unchecked")
    private JsonObject toJsonObject(Object payload) {
        if (payload instanceof Map<?, ?> map) {
            return new JsonObject((Map<String, Object>) map);
        }
        return new JsonObject().put("value", payload);
    }

    /** Flushes the NATS publish buffer so delivery is observable without sleeping. */
    private void flushQuietly() {
        try {
            connection.flush(Duration.ofSeconds(2));
        } catch (Exception e) {
            log.debug("NATS flush failed", e);
        }
    }

    /** Throws when the bridge is not connected. */
    private void requireRunning() {
        if (!isRunning()) {
            throw new IllegalStateException("NATS bridge is not running");
        }
    }
}
