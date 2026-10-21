package io.sinistral.proteus.test.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.typesafe.config.ConfigFactory;
import io.sinistral.proteus.ProteusApplication;
import io.undertow.connector.ByteBufferPool;
import io.undertow.connector.PooledByteBuffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * The listener buffer pool keeps a per-thread cache for platform threads and a shared, capped
 * queue for virtual threads, so a buffer freed by one virtual thread is reused by the next.
 */
public class BufferPoolTest {

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

    /** Allocates and frees one buffer on a new thread of the given kind and returns it. */
    private static ByteBuffer allocateAndFreeOn(Thread.Builder builder, ByteBufferPool pool) throws InterruptedException {
        AtomicReference<ByteBuffer> freed = new AtomicReference<>();
        Thread thread = builder.start(() -> {
            PooledByteBuffer pooled = pool.allocate();
            freed.set(pooled.getBuffer());
            pooled.close();
        });
        thread.join(10_000);
        return freed.get();
    }

    @Test
    public void bufferFreedByOneVirtualThreadIsReusedByTheNext() throws Exception {
        ByteBufferPool pool = start(64);

        ByteBuffer first = allocateAndFreeOn(Thread.ofVirtual(), pool);
        ByteBuffer second = allocateAndFreeOn(Thread.ofVirtual(), pool);

        assertSame(first, second);
    }

    @Test
    public void platformThreadReusesFromItsOwnCacheOnly() throws Exception {
        ByteBufferPool pool = start(64);

        // The freeing platform thread gets its buffer back from its own cache.
        AtomicReference<Boolean> sameOnOwner = new AtomicReference<>();
        AtomicReference<ByteBuffer> platform = new AtomicReference<>();
        Thread owner = Thread.ofPlatform().start(() -> {
            PooledByteBuffer pooled = pool.allocate();
            platform.set(pooled.getBuffer());
            pooled.close();
            try (PooledByteBuffer reused = pool.allocate()) {
                sameOnOwner.set(reused.getBuffer() == platform.get());
            }
        });
        owner.join(10_000);
        assertEquals(Boolean.TRUE, sameOnOwner.get());

        // Neither another platform thread nor a virtual thread receives it.
        assertNotSame(platform.get(), allocateAndFreeOn(Thread.ofPlatform(), pool));
        assertNotSame(platform.get(), allocateAndFreeOn(Thread.ofVirtual(), pool));
    }

    @Test
    public void virtualRetentionIsCappedByVirtualMaximumPoolSize() throws Exception {
        int cap = 4;
        int allocated = 16;
        ByteBufferPool pool = start(cap);

        Set<ByteBuffer> firstBuffers = Collections.newSetFromMap(new IdentityHashMap<>());
        AtomicInteger reused = new AtomicInteger();
        Thread thread = Thread.ofVirtual().start(() -> {
            List<PooledByteBuffer> first = new ArrayList<>();
            for (int i = 0; i < allocated; i++) {
                PooledByteBuffer pooled = pool.allocate();
                first.add(pooled);
                firstBuffers.add(pooled.getBuffer());
            }
            first.forEach(PooledByteBuffer::close);

            List<PooledByteBuffer> second = new ArrayList<>();
            for (int i = 0; i < allocated; i++) {
                PooledByteBuffer pooled = pool.allocate();
                second.add(pooled);
                if (firstBuffers.contains(pooled.getBuffer())) {
                    reused.incrementAndGet();
                }
            }
            second.forEach(PooledByteBuffer::close);
        });
        thread.join(10_000);

        // DefaultByteBufferPool admits a freed buffer while the queue holds no more than the cap.
        assertEquals(cap + 1, reused.get(), "reused of " + allocated + " with cap " + cap);
    }
}
