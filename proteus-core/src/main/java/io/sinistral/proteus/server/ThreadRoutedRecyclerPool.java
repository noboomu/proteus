package io.sinistral.proteus.server;

import java.io.Serial;
import tools.jackson.core.util.BufferRecycler;
import tools.jackson.core.util.JsonRecyclerPools;
import tools.jackson.core.util.RecyclerPool;

/**
 * Jackson buffer recycler pool that routes each acquisition by the calling thread's kind.
 *
 * <p>Platform threads use {@link JsonRecyclerPools#threadLocalPool()}, one recycler per thread
 * with no shared state. Virtual threads use a lock-free deque: a virtual thread lives for one
 * request, so a thread-local recycler would allocate new char and byte buffers on every request.
 *
 * <p>A recycler acquired on a virtual thread is linked to the deque and returns to it on release,
 * whichever thread releases it. Thread-local recyclers are never linked and never shared.
 */
public final class ThreadRoutedRecyclerPool implements RecyclerPool<BufferRecycler> {

    @Serial
    private static final long serialVersionUID = 1L;

    /** The shared instance. */
    public static final ThreadRoutedRecyclerPool INSTANCE = new ThreadRoutedRecyclerPool();

    private final transient RecyclerPool<BufferRecycler> platformPool = JsonRecyclerPools.threadLocalPool();
    private final transient RecyclerPool<BufferRecycler> virtualPool = JsonRecyclerPools.newConcurrentDequePool();

    private ThreadRoutedRecyclerPool() {
    }

    /** Acquires from the deque on a virtual thread, linked so release returns it there. */
    @Override
    public BufferRecycler acquireAndLinkPooled() {
        return Thread.currentThread().isVirtual() ? virtualPool.acquireAndLinkPooled() : platformPool.acquireAndLinkPooled();
    }

    /** Acquires without linking, routed the same way. */
    @Override
    public BufferRecycler acquirePooled() {
        return Thread.currentThread().isVirtual() ? virtualPool.acquirePooled() : platformPool.acquirePooled();
    }

    /**
     * Drops the recycler. Recyclers are linked to the delegate that issued them, so this pool is
     * only reached for unlinked recyclers, whose origin cannot be known; a thread-local recycler
     * must never enter the shared deque.
     */
    @Override
    public void releasePooled(BufferRecycler pooled) {
    }

    /** Returns the number of recyclers waiting in the virtual-thread deque. */
    @Override
    public int pooledCount() {
        return virtualPool.pooledCount();
    }

    /** Empties the virtual-thread deque; thread-local recyclers cannot be cleared. */
    @Override
    public boolean clear() {
        return virtualPool.clear();
    }

    @Serial
    private Object readResolve() {
        return INSTANCE;
    }
}
