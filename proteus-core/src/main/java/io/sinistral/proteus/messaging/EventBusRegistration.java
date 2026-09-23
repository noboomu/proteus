package io.sinistral.proteus.messaging;

/**
 * Registration handle for one event bus consumer.
 *
 * @author jbauer
 */
public interface EventBusRegistration {
    /** Removes the consumer from its address. Idempotent. */
    void unregister();

    /** Returns the address this registration is bound to.
     *
     * @return the bound event bus address
     */
    String getAddress();

    /** Returns true while the registration is bound.
     *
     * @return true if still active, false after unregister
     */
    boolean isActive();
}
