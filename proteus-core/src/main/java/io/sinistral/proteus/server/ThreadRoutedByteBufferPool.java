package io.sinistral.proteus.server;

import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.DefaultByteBufferPool;

/**
 * Listener buffer pool that routes each allocation by the calling thread's kind.
 *
 * <p>Platform threads (XNIO I/O and worker threads) use a pool with Undertow's own default
 * parameters, whose per-thread cache serves a long-lived thread well. Virtual threads use a pool
 * with no per-thread cache and a capped shared queue: a virtual thread lives for one request, so a
 * per-thread cache only registers a new cache entry and allocates a fresh buffer every time.
 *
 * <p>Each buffer returns to the pool that allocated it, whichever thread frees it.
 */
public final class ThreadRoutedByteBufferPool implements ByteBufferPool {

    private final DefaultByteBufferPool platformPool;
    private final DefaultByteBufferPool virtualPool;

    /**
     * Creates the pool.
     *
     * @param direct whether buffers are direct
     * @param bufferSize the size of each buffer in bytes
     * @param virtualMaximumPoolSize buffers the virtual-thread pool keeps on its shared queue
     */
    public ThreadRoutedByteBufferPool(boolean direct, int bufferSize, int virtualMaximumPoolSize) {
        this.platformPool = new DefaultByteBufferPool(direct, bufferSize, -1, 4);
        this.virtualPool = new DefaultByteBufferPool(direct, bufferSize, virtualMaximumPoolSize, 0);
    }

    /** Allocates from the virtual-thread pool on a virtual thread, else from the platform pool. */
    @Override
    public PooledByteBuffer allocate() {
        return Thread.currentThread().isVirtual() ? virtualPool.allocate() : platformPool.allocate();
    }

    /** Returns the platform pool's array-backed pool. */
    @Override
    public ByteBufferPool getArrayBackedPool() {
        return platformPool.getArrayBackedPool();
    }

    /** Closes both pools. */
    @Override
    public void close() {
        platformPool.close();
        virtualPool.close();
    }

    /** Returns the buffer size shared by both pools. */
    @Override
    public int getBufferSize() {
        return platformPool.getBufferSize();
    }

    /** Returns whether both pools hand out direct buffers. */
    @Override
    public boolean isDirect() {
        return platformPool.isDirect();
    }
}
