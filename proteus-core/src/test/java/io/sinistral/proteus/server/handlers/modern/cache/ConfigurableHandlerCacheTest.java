package io.sinistral.proteus.server.handlers.modern.cache;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for the new caching system (Phase 2).
 * Tests all eviction policies, TTL, metrics, and concurrent access.
 */
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class ConfigurableHandlerCacheTest {
    
    private HandlerCache cache;
    
    @BeforeEach
    void setUp() {
        // Default cache for most tests
        cache = new ConfigurableHandlerCache(10, EvictionPolicy.LRU, null, true);
    }
    
    @AfterEach
    void tearDown() {
        if (cache != null) {
            cache.shutdown();
        }
    }
    
    @Test
    void testBasicCacheOperations() {
        // Test put and get
        cache.put("key1", "handler1");
        Optional<Object> result = cache.get("key1");
        
        assertTrue(result.isPresent());
        assertEquals("handler1", result.get());
        assertEquals(1, cache.size());
        assertFalse(cache.isEmpty());
        
        // Test cache miss
        Optional<Object> miss = cache.get("nonexistent");
        assertFalse(miss.isPresent());
        
        // Test remove
        assertTrue(cache.remove("key1"));
        assertFalse(cache.remove("key1")); // already removed
        assertEquals(0, cache.size());
        assertTrue(cache.isEmpty());
    }
    
    @Test
    void testComputeIfAbsent() {
        AtomicInteger computeCount = new AtomicInteger(0);
        
        // First call should compute
        Object result1 = cache.computeIfAbsent("key1", k -> {
            computeCount.incrementAndGet();
            return "computed-" + k;
        });
        
        assertEquals("computed-key1", result1);
        assertEquals(1, computeCount.get());
        
        // Second call should use cached value
        Object result2 = cache.computeIfAbsent("key1", k -> {
            computeCount.incrementAndGet();
            return "computed-again-" + k;
        });
        
        assertEquals("computed-key1", result2);
        assertEquals(1, computeCount.get()); // Should not compute again
    }
    
    @Test
    void testAsyncComputeIfAbsent() throws Exception {
        AtomicInteger computeCount = new AtomicInteger(0);
        
        CompletableFuture<Object> future = cache.computeIfAbsentAsync("async-key", k -> 
            CompletableFuture.supplyAsync(() -> {
                computeCount.incrementAndGet();
                return "async-computed-" + k;
            })
        );
        
        Object result = future.get(1, TimeUnit.SECONDS);
        assertEquals("async-computed-async-key", result);
        assertEquals(1, computeCount.get());
        
        // Verify it's cached
        Optional<Object> cached = cache.get("async-key");
        assertTrue(cached.isPresent());
        assertEquals("async-computed-async-key", cached.get());
    }
    
    @Test
    void testLRUEviction() {
        // Fill cache to capacity
        for (int i = 0; i < 10; i++) {
            cache.put("key" + i, "handler" + i);
        }
        assertEquals(10, cache.size());
        
        // Access some keys to establish LRU order
        cache.get("key0"); // Make key0 recently used
        cache.get("key1"); // Make key1 recently used
        
        // Add one more to trigger eviction
        cache.put("key10", "handler10");
        
        // Cache should still be at max size
        assertEquals(10, cache.size());
        
        // key0 and key1 should still be there (recently used)
        assertTrue(cache.get("key0").isPresent());
        assertTrue(cache.get("key1").isPresent());
        
        // key10 should be there (just added)
        assertTrue(cache.get("key10").isPresent());
    }
    
    @Test
    void testTTLEviction() throws InterruptedException {
        // Create cache with short TTL
        cache.shutdown();
        cache = new ConfigurableHandlerCache(100, EvictionPolicy.TTL, Duration.ofMillis(100), true);
        
        cache.put("ttl-key", "ttl-handler");
        assertTrue(cache.get("ttl-key").isPresent());
        
        // Wait for TTL to expire
        Thread.sleep(150);
        
        // Should be evicted
        assertFalse(cache.get("ttl-key").isPresent());
    }
    
    @Test
    void testFIFOEviction() {
        cache.shutdown();
        cache = new ConfigurableHandlerCache(3, EvictionPolicy.FIFO, null, true);
        
        // Fill cache
        cache.put("first", "handler1");
        cache.put("second", "handler2"); 
        cache.put("third", "handler3");
        
        // Access first to make it recently used (shouldn't matter for FIFO)
        cache.get("first");
        
        // Add fourth item to trigger FIFO eviction
        cache.put("fourth", "handler4");
        
        // First item should be evicted (FIFO)
        assertFalse(cache.get("first").isPresent());
        assertTrue(cache.get("second").isPresent());
        assertTrue(cache.get("third").isPresent());
        assertTrue(cache.get("fourth").isPresent());
    }
    
    @Test
    void testNoEvictionPolicy() {
        cache.shutdown();
        cache = new ConfigurableHandlerCache(2, EvictionPolicy.NONE, null, true);
        
        // Fill beyond capacity
        cache.put("key1", "handler1");
        cache.put("key2", "handler2");
        cache.put("key3", "handler3");
        
        // All should be present (no eviction)
        assertEquals(3, cache.size());
        assertTrue(cache.get("key1").isPresent());
        assertTrue(cache.get("key2").isPresent());
        assertTrue(cache.get("key3").isPresent());
    }
    
    @Test
    void testMetrics() {
        // Perform operations to generate metrics
        cache.put("key1", "handler1");
        cache.get("key1"); // hit
        cache.get("nonexistent"); // miss
        
        // Force eviction for eviction metric
        for (int i = 0; i < 15; i++) {
            cache.put("key" + i, "handler" + i);
        }
        
        Optional<CacheMetrics> metricsOpt = cache.getMetrics();
        assertTrue(metricsOpt.isPresent());
        
        CacheMetrics metrics = metricsOpt.get();
        assertTrue(metrics.getHits() > 0);
        assertTrue(metrics.getMisses() > 0);
        assertTrue(metrics.getEvictions() > 0);
        assertTrue(metrics.getSize() > 0);
        assertEquals(10, metrics.getMaxSize()); // Our cache max size
        assertTrue(metrics.getHitRate() >= 0 && metrics.getHitRate() <= 1);
        assertTrue(metrics.getTotalRequests() > 0);
        
        // Test toString
        String metricsString = metrics.toString();
        assertNotNull(metricsString);
        assertTrue(metricsString.contains("hits="));
        assertTrue(metricsString.contains("misses="));
    }
    
    @Test
    void testConcurrentAccess() throws InterruptedException {
        int threadCount = 10;
        int operationsPerThread = 100;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        
        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    for (int i = 0; i < operationsPerThread; i++) {
                        String key = "thread" + threadId + "-key" + i;
                        String value = "handler-" + threadId + "-" + i;
                        
                        cache.put(key, value);
                        Optional<Object> retrieved = cache.get(key);
                        
                        if (retrieved.isPresent() && value.equals(retrieved.get())) {
                            successCount.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        assertTrue(latch.await(10, TimeUnit.SECONDS));
        executor.shutdown();
        
        // Should have high success rate (some may be evicted due to cache size limits)
        int totalOperations = threadCount * operationsPerThread;
        int successful = successCount.get();
        double successRate = (double) successful / totalOperations;
        
        // At least 50% should succeed (accounting for evictions)
        assertTrue(successRate > 0.5, 
            "Success rate too low: " + successRate + " (" + successful + "/" + totalOperations + ")");
    }
    
    @Test
    void testCacheFactoryMethods() {
        // Test factory methods
        HandlerCache simple = HandlerCacheFactory.createSimple();
        assertNotNull(simple);
        
        HandlerCache lru = HandlerCacheFactory.createLRU(100);
        assertNotNull(lru);
        
        HandlerCache ttl = HandlerCacheFactory.createTTL(Duration.ofMinutes(5));
        assertNotNull(ttl);
        
        HandlerCache lruTtl = HandlerCacheFactory.createLRUWithTTL(50, Duration.ofMinutes(2));
        assertNotNull(lruTtl);
        
        HandlerCache production = HandlerCacheFactory.createProduction();
        assertNotNull(production);
        
        HandlerCache productionCustom = HandlerCacheFactory.createProduction(500, Duration.ofMinutes(30));
        assertNotNull(productionCustom);
        
        // Clean up
        simple.shutdown();
        lru.shutdown();
        ttl.shutdown();
        lruTtl.shutdown();
        production.shutdown();
        productionCustom.shutdown();
    }
    
    @Test
    void testClearCache() {
        cache.put("key1", "handler1");
        cache.put("key2", "handler2");
        assertEquals(2, cache.size());
        
        cache.clear();
        assertEquals(0, cache.size());
        assertTrue(cache.isEmpty());
        assertFalse(cache.get("key1").isPresent());
        assertFalse(cache.get("key2").isPresent());
    }
    
    @Test
    void testShutdown() {
        cache.put("key1", "handler1");
        assertTrue(cache.get("key1").isPresent());
        
        cache.shutdown();
        
        // After shutdown, cache should not accept new entries
        cache.put("key2", "handler2");
        assertFalse(cache.get("key2").isPresent());
    }
}
