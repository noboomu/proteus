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

    /** Returns true while the bridge is connected and subscribed.
     *
     * @return true when running
     */
    boolean isRunning();

    /** Publishes a JSON envelope to the NATS subject mapped from the address.
     *
     * @param address the event bus address
     * @param message the payload to mirror
     */
    void forwardPublish(String address, Object message);

    /** Performs a NATS request and completes with the decoded envelope payload.
     *
     * @param address the event bus address
     * @param message the request payload
     * @param timeoutMs reply wait in milliseconds
     * @return a future completed with the decoded reply payload
     */
    CompletableFuture<Object> forwardRequest(String address, Object message, long timeoutMs);

    /** Registers a handler invoked for messages arriving from NATS on the address.
     *
     * @param address the event bus address
     * @param handler callback receiving the decoded JSON envelope
     */
    void subscribe(String address, Consumer<JsonObject> handler);
}
