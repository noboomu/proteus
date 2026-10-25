package io.sinistral.proteus.test.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.typesafe.config.ConfigFactory;
import io.sinistral.proteus.ProteusApplication;
import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * The listener buffer pool keeps a per-thread cache for platform threads and a shared, capped
 * queue for virtual threads. Buffers are identified by a tag written into their memory, because a
 * pool may hand out a new {@code ByteBuffer} view of the same memory on each allocation.
 */
public class BufferPoolTest {

    private static final AtomicLong TAGS = new AtomicLong(System.nanoTime());

    private ProteusApplication application;

    @AfterEach
    public void stop() throws Exception {
        if (application != null && application.isRunning()) {
            application.shutdown();
        }
        System.clearProperty("undertow.bufferPool.virtualMaximumPoolSize");
        ConfigFactory.invalidateCaches();
    }

    private ByteBufferPool start(int virtualMaximumPoolSize) {
        System.setProperty("undertow.bufferPool.virtualMaximumPoolSize", Integer.toString(virtualMaximumPoolSize));
        ConfigFactory.invalidateCaches();
        application = new ProteusApplication();
        application.start();
        return application.getByteBufferPool();
    }

    /** Allocates a buffer, returns the tag it already held, writes a fresh tag, frees it, returns the fresh tag. */
    private static long[] allocateTagAndFree(ByteBufferPool pool) {
        try (PooledByteBuffer pooled = pool.allocate()) {
            long previous = pooled.getBuffer().getLong(0);
            long tag = TAGS.incrementAndGet();
            pooled.getBuffer().putLong(0, tag);
            return new long[] {previous, tag};
        }
    }

    /** Runs {@link #allocateTagAndFree} on a new thread of the given kind. */
    private static long[] allocateTagAndFreeOn(Thread.Builder builder, ByteBufferPool pool) throws InterruptedException {
        AtomicReference<long[]> result = new AtomicReference<>();
        builder.start(() -> result.set(allocateTagAndFree(pool))).join(10_000);
        return result.get();
    }

    @Test
    public void bufferFreedByOneVirtualThreadIsReusedByTheNext() throws Exception {
        ByteBufferPool pool = start(64);

        long freedTag = allocateTagAndFreeOn(Thread.ofVirtual(), pool)[1];
        long seenByNext = allocateTagAndFreeOn(Thread.ofVirtual(), pool)[0];

        assertEquals(freedTag, seenByNext);
    }

    @Test
    public void platformThreadReusesFromItsOwnCacheOnly() throws Exception {
        ByteBufferPool pool = start(64);

        // The freeing platform thread gets its buffer back from its own cache.
        AtomicReference<long[]> owner = new AtomicReference<>();
        Thread.ofPlatform().start(() -> {
            long freedTag = allocateTagAndFree(pool)[1];
            long seenAgain = allocateTagAndFree(pool)[0];
            owner.set(new long[] {freedTag, seenAgain});
        }).join(10_000);
        assertEquals(owner.get()[0], owner.get()[1]);

        // The owner's cached buffer holds its second tag; no other thread receives it.
        long cachedTag = TAGS.get();
        assertNotEquals(cachedTag, allocateTagAndFreeOn(Thread.ofPlatform(), pool)[0]);
        assertNotEquals(cachedTag, allocateTagAndFreeOn(Thread.ofVirtual(), pool)[0]);
    }

    @Test
    public void virtualRetentionIsCappedByVirtualMaximumPoolSize() throws Exception {
        int cap = 4;
        int allocated = 16;
        ByteBufferPool pool = start(cap);

        AtomicInteger reused = new AtomicInteger();
        Thread.ofVirtual().start(() -> {
            Set<Long> firstTags = new HashSet<>();
            List<PooledByteBuffer> first = new ArrayList<>();
            for (int i = 0; i < allocated; i++) {
                PooledByteBuffer pooled = pool.allocate();
                long tag = TAGS.incrementAndGet();
                pooled.getBuffer().putLong(0, tag);
                firstTags.add(tag);
                first.add(pooled);
            }
            first.forEach(PooledByteBuffer::close);

            List<PooledByteBuffer> second = new ArrayList<>();
            for (int i = 0; i < allocated; i++) {
                PooledByteBuffer pooled = pool.allocate();
                if (firstTags.contains(pooled.getBuffer().getLong(0))) {
                    reused.incrementAndGet();
                }
                second.add(pooled);
            }
            second.forEach(PooledByteBuffer::close);
        }).join(10_000);

        // DefaultByteBufferPool admits a freed buffer while the queue holds no more than the cap.
        assertEquals(cap + 1, reused.get(), "reused of " + allocated + " with cap " + cap);
    }
}
