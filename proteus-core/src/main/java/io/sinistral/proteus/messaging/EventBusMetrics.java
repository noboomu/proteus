package io.sinistral.proteus.messaging;

/**
 * Immutable point-in-time snapshot of event bus counters and latency gauges. Counters are
 * monotonic from service start.
 *
 * @param messagesSent cumulative send() calls
 * @param messagesReceived cumulative messages delivered to registered consumers
 * @param messagesPublished cumulative publish() calls
 * @param requestsSent cumulative request() calls
 * @param repliesReceived cumulative replies observed for requests
 * @param pendingRequests requests awaiting a reply at snapshot time
 * @param errorCount cumulative consumer-failure and decode-failure count
 * @param timeoutCount cumulative request timeouts
 * @param averageLatencyMs mean round-trip latency in milliseconds
 * @param maxLatencyMs maximum observed round-trip latency in milliseconds
 */
public record EventBusMetrics(
    long messagesSent,
    long messagesReceived,
    long messagesPublished,
    long requestsSent,
    long repliesReceived,
    long pendingRequests,
    long errorCount,
    long timeoutCount,
    double averageLatencyMs,
    long maxLatencyMs
) {}
