package io.sinistral.proteus.eventbus;

/**
 * Registration handle for event bus consumers.
 * Can be used to unregister the consumer from the event bus.
 * 
 * @since 1.0
 */
public interface EventBusRegistration {
    
    /**
     * Unregisters the consumer from the event bus.
     * After this call, the consumer will no longer receive messages.
     */
    void unregister();
    
    /**
     * Gets the address this registration is associated with.
     * 
     * @return the event bus address
     */
    String getAddress();
    
    /**
     * Checks if this registration is still active.
     * 
     * @return true if the consumer is still registered, false otherwise
     */
    boolean isActive();
}
