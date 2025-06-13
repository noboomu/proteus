package io.sinistral.proteus.eventbus;

/**
 * Options for configuring event bus consumers.
 * 
 * @since 1.0
 */
public class EventBusConsumerOptions {
    
    private boolean blocking = false;
    private boolean ordered = false;
    private boolean local = true;
    private String codec = "";
    private long timeout = 30000; // 30 seconds default
    
    /**
     * Creates default consumer options.
     */
    public EventBusConsumerOptions() {
    }
    
    /**
     * Creates consumer options with specified blocking behavior.
     * 
     * @param blocking whether the consumer should be blocking
     */
    public EventBusConsumerOptions(boolean blocking) {
        this.blocking = blocking;
    }
    
    /**
     * Whether this consumer should be blocking.
     * 
     * @return true if blocking, false otherwise
     */
    public boolean isBlocking() {
        return blocking;
    }
    
    /**
     * Sets the blocking behavior.
     * 
     * @param blocking whether the consumer should be blocking
     * @return this options instance for chaining
     */
    public EventBusConsumerOptions setBlocking(boolean blocking) {
        this.blocking = blocking;
        return this;
    }
    
    /**
     * Whether messages should be processed in order.
     * 
     * @return true if ordered, false otherwise
     */
    public boolean isOrdered() {
        return ordered;
    }
    
    /**
     * Sets the ordering behavior.
     * 
     * @param ordered whether messages should be processed in order
     * @return this options instance for chaining
     */
    public EventBusConsumerOptions setOrdered(boolean ordered) {
        this.ordered = ordered;
        return this;
    }
    
    /**
     * Whether this consumer is local only.
     * 
     * @return true if local only, false for clustered
     */
    public boolean isLocal() {
        return local;
    }
    
    /**
     * Sets the locality behavior.
     * 
     * @param local whether the consumer should be local only
     * @return this options instance for chaining
     */
    public EventBusConsumerOptions setLocal(boolean local) {
        this.local = local;
        return this;
    }
    
    /**
     * Gets the codec name for message serialization.
     * 
     * @return the codec name, or empty string for default
     */
    public String getCodec() {
        return codec;
    }
    
    /**
     * Sets the codec name.
     * 
     * @param codec the codec name
     * @return this options instance for chaining
     */
    public EventBusConsumerOptions setCodec(String codec) {
        this.codec = codec;
        return this;
    }
    
    /**
     * Gets the request timeout in milliseconds.
     * 
     * @return the timeout in milliseconds
     */
    public long getTimeout() {
        return timeout;
    }
    
    /**
     * Sets the request timeout.
     * 
     * @param timeout the timeout in milliseconds
     * @return this options instance for chaining
     */
    public EventBusConsumerOptions setTimeout(long timeout) {
        this.timeout = timeout;
        return this;
    }
}
