package io.sinistral.proteus.test.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import io.sinistral.proteus.modules.JacksonModule;
import com.google.inject.Guice;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import tools.jackson.core.util.BufferRecycler;
import tools.jackson.core.util.RecyclerPool;
import tools.jackson.databind.ObjectMapper;

/**
 * The injected ObjectMapper's recycler pool hands one recycler per platform thread and shares
 * released recyclers across virtual threads.
 */
public class RecyclerPoolTest {

    private final RecyclerPool<BufferRecycler> pool = Guice.createInjector(new JacksonModule())
        .getInstance(ObjectMapper.class)
        .tokenStreamFactory()
        ._getRecyclerPool();

    /** Acquires and releases one recycler on a new thread of the given kind and returns it. */
    private BufferRecycler acquireAndReleaseOn(Thread.Builder builder) throws InterruptedException {
        AtomicReference<BufferRecycler> held = new AtomicReference<>();
        builder.start(() -> {
            BufferRecycler recycler = pool.acquireAndLinkPooled();
            held.set(recycler);
            recycler.releaseToPool();
        }).join(10_000);
        return held.get();
    }

    @Test
    public void recyclerReleasedByOneVirtualThreadIsAcquiredByTheNext() throws Exception {
        pool.clear();
        assertSame(acquireAndReleaseOn(Thread.ofVirtual()), acquireAndReleaseOn(Thread.ofVirtual()));
    }

    @Test
    public void platformRecyclerStaysWithItsThread() throws Exception {
        pool.clear();
        AtomicReference<Boolean> sameOnOwner = new AtomicReference<>();
        AtomicReference<BufferRecycler> platform = new AtomicReference<>();
        Thread.ofPlatform().start(() -> {
            BufferRecycler first = pool.acquireAndLinkPooled();
            first.releaseToPool();
            platform.set(first);
            BufferRecycler second = pool.acquireAndLinkPooled();
            second.releaseToPool();
            sameOnOwner.set(first == second);
        }).join(10_000);

        assertEquals(Boolean.TRUE, sameOnOwner.get());
        assertEquals(0, pool.pooledCount());
        assertNotSame(platform.get(), acquireAndReleaseOn(Thread.ofVirtual()));
        assertNotSame(platform.get(), acquireAndReleaseOn(Thread.ofPlatform()));
    }

    @Test
    public void mapperSerializesOnVirtualThreadsThroughTheSharedRecycler() throws Exception {
        pool.clear();
        ObjectMapper mapper = Guice.createInjector(new JacksonModule()).getInstance(ObjectMapper.class);
        AtomicReference<String> json = new AtomicReference<>();
        Thread.ofVirtual().start(() -> json.set(new String(mapper.writeValueAsBytes(java.util.Map.of("message", "Hello, World!"))))).join(10_000);

        assertEquals("{\"message\":\"Hello, World!\"}", json.get());
        assertEquals(1, mapper.tokenStreamFactory()._getRecyclerPool().pooledCount());
    }
}
