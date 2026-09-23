package io.sinistral.proteus.messaging;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Thread-safe request latency recorder producing average and maximum gauges.
 *
 * @author jbauer
 */
final class LatencyRecorder {
    private final AtomicLong totalNanos = new AtomicLong();
    private final AtomicLong count = new AtomicLong();
    private final AtomicLong maxNanos = new AtomicLong();

    // Guards average consistency between total and count reads.
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    /** Records one round-trip latency. */
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

    /** @return the mean latency in milliseconds, or zero when nothing was recorded */
    double averageMs() {
        lock.readLock().lock();
        try {
            long samples = count.get();
            return samples == 0 ? 0.0 : (totalNanos.get() / (double) samples) / 1_000_000.0;
        } finally {
            lock.readLock().unlock();
        }
    }

    /** @return the maximum observed latency in milliseconds */
    long maxMs() {
        return maxNanos.get() / 1_000_000L;
    }
}
