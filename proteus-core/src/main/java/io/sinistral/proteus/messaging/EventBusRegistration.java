package io.sinistral.proteus.messaging;

/**
 * Registration handle for one event bus consumer.
 *
 * @author jbauer
 */
public interface EventBusRegistration {
    /** Removes the consumer from its address. Idempotent. */
    void unregister();

    /** @return the address this registration is bound to */
    String getAddress();

    /** @return true while the registration is bound */
    boolean isActive();
}
