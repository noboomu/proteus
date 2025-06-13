package io.sinistral.proteus.eventbus;

/**
 * Metrics for monitoring event bus performance.
 * 
 * @since 1.0
 */
public class EventBusMetrics {
    
    private final long messagesSent;
    private final long messagesReceived;
    private final long messagesPublished;
    private final long requestsSent;
    private final long repliesReceived;
    private final long pendingRequests;
    private final long averageLatencyMs;
    private final long maxLatencyMs;
    private final long errorCount;
    private final long timeoutCount;
    
    public EventBusMetrics(long messagesSent, long messagesReceived, long messagesPublished,
                          long requestsSent, long repliesReceived, long pendingRequests,
                          long averageLatencyMs, long maxLatencyMs, long errorCount, long timeoutCount) {
        this.messagesSent = messagesSent;
        this.messagesReceived = messagesReceived;
        this.messagesPublished = messagesPublished;
        this.requestsSent = requestsSent;
        this.repliesReceived = repliesReceived;
        this.pendingRequests = pendingRequests;
        this.averageLatencyMs = averageLatencyMs;
        this.maxLatencyMs = maxLatencyMs;
        this.errorCount = errorCount;
        this.timeoutCount = timeoutCount;
    }
    
    /**
     * Total number of messages sent via send().
     */
    public long getMessagesSent() {
        return messagesSent;
    }
    
    /**
     * Total number of messages received by consumers.
     */
    public long getMessagesReceived() {
        return messagesReceived;
    }
    
    /**
     * Total number of messages published via publish().
     */
    public long getMessagesPublished() {
        return messagesPublished;
    }
    
    /**
     * Total number of requests sent via request().
     */
    public long getRequestsSent() {
        return requestsSent;
    }
    
    /**
     * Total number of replies received.
     */
    public long getRepliesReceived() {
        return repliesReceived;
    }
    
    /**
     * Current number of pending requests.
     */
    public long getPendingRequests() {
        return pendingRequests;
    }
    
    /**
     * Average request/reply latency in milliseconds.
     */
    public long getAverageLatencyMs() {
        return averageLatencyMs;
    }
    
    /**
     * Maximum request/reply latency in milliseconds.
     */
    public long getMaxLatencyMs() {
        return maxLatencyMs;
    }
    
    /**
     * Total number of errors (failed sends, timeouts, etc.).
     */
    public long getErrorCount() {
        return errorCount;
    }
    
    /**
     * Total number of request timeouts.
     */
    public long getTimeoutCount() {
        return timeoutCount;
    }
    
    @Override
    public String toString() {
        return "EventBusMetrics{" +
                "messagesSent=" + messagesSent +
                ", messagesReceived=" + messagesReceived +
                ", messagesPublished=" + messagesPublished +
                ", requestsSent=" + requestsSent +
                ", repliesReceived=" + repliesReceived +
                ", pendingRequests=" + pendingRequests +
                ", averageLatencyMs=" + averageLatencyMs +
                ", maxLatencyMs=" + maxLatencyMs +
                ", errorCount=" + errorCount +
                ", timeoutCount=" + timeoutCount +
                '}';
    }
}
