package io.sinistral.proteus.messaging.nats;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import io.vertx.core.json.JsonObject;

/**
 * Forwards event bus traffic across processes via NATS.
 *
 * @author jbauer
 */
public interface NatsBridgeService {
    /** Connects the NATS connection and opens the shared subscription. */
    void start();

    /** Drains and closes the connection; registered subscriptions are dropped. */
    void stop();

    /** @return true while the bridge is connected and subscribed */
    boolean isRunning();

    /** Publishes a JSON envelope to the NATS subject mapped from the address. */
    void forwardPublish(String address, Object message);

    /** Performs a NATS request and completes with the decoded envelope payload. */
    CompletableFuture<Object> forwardRequest(String address, Object message, long timeoutMs);

    /** Registers a handler invoked for messages arriving from NATS on the address. */
    void subscribe(String address, Consumer<JsonObject> handler);
}
