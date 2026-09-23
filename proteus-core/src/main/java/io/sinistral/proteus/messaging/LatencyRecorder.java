package io.sinistral.proteus.messaging;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe request latency recorder producing average and maximum gauges.
 *
 * @author jbauer
 */
final class LatencyRecorder {
    /** Cumulative recorded nanoseconds. */
    private final AtomicLong totalNanos = new AtomicLong();
    /** Number of recorded samples. */
    private final AtomicLong count = new AtomicLong();
    /** Maximum observed sample in nanoseconds. */
    private final AtomicLong maxNanos = new AtomicLong();

    // Guards average consistency between total and count reads.
    /** Serializes total/count updates for consistent averages. */
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /** Records one round-trip latency.
     *
     * @param nanos the round-trip duration in nanoseconds
     */
    void record(long nanos) {
        lock.writeLock().lock();
        try {
            totalNanos.addAndGet(nanos);
            count.incrementAndGet();
            maxNanos.accumulateAndGet(nanos, Math::max);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /** Returns the mean latency in milliseconds, or zero when nothing was recorded.
     *
     * @return mean latency in milliseconds
     */
    double averageMs() {
        lock.readLock().lock();
        try {
            long samples = count.get();
            return samples == 0 ? 0.0 : (totalNanos.get() / (double) samples) / 1_000_000.0;
        } finally {
            lock.readLock().unlock();
        }
    }

    /** Returns the maximum observed latency in milliseconds.
     *
     * @return maximum latency in milliseconds
     */
    long maxMs() {
        return maxNanos.get() / 1_000_000L;
    }

    /** Default constructor for a fresh, empty recorder. */
    LatencyRecorder() {
    }
}
