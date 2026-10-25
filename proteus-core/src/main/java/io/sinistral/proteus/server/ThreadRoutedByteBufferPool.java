package io.sinistral.proteus.server;

import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.server.XnioByteBufferPool;
import org.xnio.BufferAllocator;
import org.xnio.ByteBufferSlicePool;

/**
 * Listener buffer pool that routes each allocation by the calling thread's kind.
 *
 * <p>Platform threads (XNIO I/O and worker threads) use XNIO's {@link ByteBufferSlicePool}, the
 * pool Undertow 2.2 used: its per-thread cache is a real {@link ThreadLocal}. Undertow 2.3's
 * {@link DefaultByteBufferPool} keys its per-thread cache by a synchronized map, which every I/O
 * thread then contends on for each allocate and free. Virtual threads use a
 * {@link DefaultByteBufferPool} with no per-thread cache and a capped shared queue: a virtual
 * thread lives for one request, so a per-thread cache never hits on it.
 *
 * <p>Each buffer returns to the pool that allocated it, whichever thread frees it.
 */
@SuppressWarnings("deprecation")
public final class ThreadRoutedByteBufferPool implements ByteBufferPool {

    private static final int BUFFERS_PER_REGION = 64;
    private static final int PLATFORM_THREAD_CACHE = 4;

    private final ByteBufferSlicePool platformSlices;
    private final XnioByteBufferPool platformPool;
    private final DefaultByteBufferPool virtualPool;

    /**
     * Creates the pool.
     *
     * @param direct whether buffers are direct
     * @param bufferSize the size of each buffer in bytes
     * @param virtualMaximumPoolSize buffers the virtual-thread pool keeps on its shared queue
     */
    public ThreadRoutedByteBufferPool(boolean direct, int bufferSize, int virtualMaximumPoolSize) {
        this.platformSlices = new ByteBufferSlicePool(
            direct ? BufferAllocator.DIRECT_BYTE_BUFFER_ALLOCATOR : BufferAllocator.BYTE_BUFFER_ALLOCATOR,
            bufferSize,
            bufferSize * BUFFERS_PER_REGION,
            PLATFORM_THREAD_CACHE
        );
        this.platformPool = new XnioByteBufferPool(platformSlices);
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

    /** Releases the platform slices for reuse and closes the virtual-thread pool. */
    @Override
    public void close() {
        platformPool.close();
        platformSlices.clean();
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
