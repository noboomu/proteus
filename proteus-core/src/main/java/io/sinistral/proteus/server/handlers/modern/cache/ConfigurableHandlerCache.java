package io.sinistral.proteus.server.handlers.modern.cache;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A configurable, thread-safe cache implementation for handler instances.
 * Supports multiple eviction policies, TTL, metrics, and async operations.
 * 
 * @since 1.0.0
 */
public class ConfigurableHandlerCache implements HandlerCache {
    
    private static final Logger log = LoggerFactory.getLogger(ConfigurableHandlerCache.class);
    
    private final ConcurrentHashMap<String, CacheEntry> cache;
    private final EvictionPolicy evictionPolicy;
    private final int maxSize;
    private final Duration ttl;
    private final boolean metricsEnabled;
    
    // Metrics
    private final AtomicLong hits = new AtomicLong(0);
    private final AtomicLong misses = new AtomicLong(0);
    private final AtomicLong evictions = new AtomicLong(0);
    private final AtomicLong totalLoadTime = new AtomicLong(0);
    
    // LRU support
    private final ConcurrentLinkedQueue<String> accessOrder = new ConcurrentLinkedQueue<>();
    private final ReentrantReadWriteLock accessLock = new ReentrantReadWriteLock();
    
    // TTL cleanup
    private final ScheduledExecutorService cleanupExecutor;
    private volatile boolean shutdown = false;
    
    /**
     * Creates a new configurable cache with the specified parameters.
     * 
     * @param maxSize maximum number of entries (0 for unlimited)
     * @param evictionPolicy eviction strategy to use
     * @param ttl time-to-live for entries (null for no expiration)
     * @param metricsEnabled whether to collect performance metrics
     */
    public ConfigurableHandlerCache(int maxSize, EvictionPolicy evictionPolicy, 
                                   Duration ttl, boolean metricsEnabled) {
        this.cache = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
        this.evictionPolicy = evictionPolicy;
        this.ttl = ttl;
        this.metricsEnabled = metricsEnabled;
        
        // Set up cleanup executor for TTL
        this.cleanupExecutor = new ScheduledThreadPoolExecutor(1, r -> {
            Thread t = new Thread(r, "handler-cache-cleanup");
            t.setDaemon(true);
            return t;
        });
        
        // Schedule periodic cleanup if TTL is enabled
        if (ttl != null) {
            long cleanupIntervalMs = Math.max(ttl.toMillis() / 4, 1000); // cleanup every 1/4 TTL, min 1s
            cleanupExecutor.scheduleWithFixedDelay(
                this::evictExpired, 
                cleanupIntervalMs, 
                cleanupIntervalMs, 
                TimeUnit.MILLISECONDS
            );
        }
    }
    
    @Override
    public Optional<Object> get(String key) {
        if (shutdown) {
            return Optional.empty();
        }
        
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            if (metricsEnabled) {
                misses.incrementAndGet();
            }
            return Optional.empty();
        }
        
        // Check TTL expiration
        if (isExpired(entry)) {
            cache.remove(key);
            if (metricsEnabled) {
                misses.incrementAndGet();
                evictions.incrementAndGet();
            }
            return Optional.empty();
        }
        
        // Update access time and order for LRU
        entry.lastAccessed = Instant.now();
        if (evictionPolicy == EvictionPolicy.LRU) {
            updateAccessOrder(key);
        }
        
        if (metricsEnabled) {
            hits.incrementAndGet();
        }
        
        return Optional.of(entry.value);
    }
    
    @Override
    public void put(String key, Object handler) {
        if (shutdown) {
            return;
        }
        
        // Check if we need to evict before adding
        if (maxSize > 0 && cache.size() >= maxSize && !cache.containsKey(key)) {
            evictOne();
        }
        
        CacheEntry entry = new CacheEntry(handler, Instant.now());
        cache.put(key, entry);
        
        if (evictionPolicy == EvictionPolicy.LRU) {
            updateAccessOrder(key);
        }
        
        log.debug("Cached handler for key: {}", key);
    }
    
    @Override
    public Object computeIfAbsent(String key, Function<String, Object> supplier) {
        if (shutdown) {
            return supplier.apply(key);
        }
        
        Optional<Object> existing = get(key);
        if (existing.isPresent()) {
            return existing.get();
        }
        
        long startTime = metricsEnabled ? System.nanoTime() : 0;
        
        try {
            Object result = supplier.apply(key);
            put(key, result);
            
            if (metricsEnabled) {
                long loadTime = System.nanoTime() - startTime;
                totalLoadTime.addAndGet(loadTime);
            }
            
            return result;
        } catch (Exception e) {
            log.warn("Failed to compute handler for key: {}", key, e);
            throw e;
        }
    }
    
    @Override
    public CompletableFuture<Object> computeIfAbsentAsync(String key, 
                                                         Function<String, CompletableFuture<Object>> supplier) {
        if (shutdown) {
            return supplier.apply(key);
        }
        
        Optional<Object> existing = get(key);
        if (existing.isPresent()) {
            return CompletableFuture.completedFuture(existing.get());
        }
        
        long startTime = metricsEnabled ? System.nanoTime() : 0;
        
        return supplier.apply(key)
            .thenApply(result -> {
                put(key, result);
                
                if (metricsEnabled) {
                    long loadTime = System.nanoTime() - startTime;
                    totalLoadTime.addAndGet(loadTime);
                }
                
                return result;
            })
            .exceptionally(throwable -> {
                log.warn("Failed to compute handler asynchronously for key: {}", key, throwable);
                throw new RuntimeException(throwable);
            });
    }
    
    @Override
    public boolean remove(String key) {
        CacheEntry removed = cache.remove(key);
        if (removed != null && evictionPolicy == EvictionPolicy.LRU) {
            accessOrder.remove(key);
        }
        return removed != null;
    }
    
    @Override
    public void clear() {
        cache.clear();
        accessOrder.clear();
        
        // Reset metrics
        if (metricsEnabled) {
            hits.set(0);
            misses.set(0);
            evictions.set(0);
            totalLoadTime.set(0);
        }
        
        log.debug("Cache cleared");
    }
    
    @Override
    public int size() {
        return cache.size();
    }
    
    @Override
    public boolean isEmpty() {
        return cache.isEmpty();
    }
    
    @Override
    public Optional<CacheMetrics> getMetrics() {
        if (!metricsEnabled) {
            return Optional.empty();
        }
        
        return Optional.of(new CacheMetrics(
            hits.get(),
            misses.get(),
            evictions.get(),
            cache.size(),
            maxSize,
            totalLoadTime.get()
        ));
    }
    
    @Override
    public void evictExpired() {
        if (shutdown || ttl == null) {
            return;
        }
        
        int evicted = 0;
        Instant now = Instant.now();
        
        for (var entry : cache.entrySet()) {
            if (now.isAfter(entry.getValue().createdAt.plus(ttl))) {
                if (cache.remove(entry.getKey()) != null) {
                    evicted++;
                    if (evictionPolicy == EvictionPolicy.LRU) {
                        accessOrder.remove(entry.getKey());
                    }
                }
            }
        }
        
        if (evicted > 0) {
            if (metricsEnabled) {
                evictions.addAndGet(evicted);
            }
            log.debug("Evicted {} expired cache entries", evicted);
        }
    }
    
    @Override
    public void shutdown() {
        shutdown = true;
        cleanupExecutor.shutdown();
        
        try {
            if (!cleanupExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        clear();
        log.info("Handler cache shutdown completed");
    }
    
    private boolean isExpired(CacheEntry entry) {
        return ttl != null && Instant.now().isAfter(entry.createdAt.plus(ttl));
    }
    
    private void evictOne() {
        switch (evictionPolicy) {
            case LRU:
                evictLRU();
                break;
            case FIFO:
                evictFIFO();
                break;
            case TTL:
                evictExpired();
                break;
            case LFU:
                evictLFU();
                break;
            case NONE:
                // No eviction
                break;
        }
    }
    
    private void evictLRU() {
        accessLock.readLock().lock();
        try {
            String oldestKey = accessOrder.poll();
            if (oldestKey != null && cache.remove(oldestKey) != null) {
                if (metricsEnabled) {
                    evictions.incrementAndGet();
                }
                log.debug("Evicted LRU entry: {}", oldestKey);
            }
        } finally {
            accessLock.readLock().unlock();
        }
    }
    
    private void evictFIFO() {
        // Find the oldest entry by creation time
        String oldestKey = null;
        Instant oldestTime = Instant.now();
        
        for (var entry : cache.entrySet()) {
            if (entry.getValue().createdAt.isBefore(oldestTime)) {
                oldestTime = entry.getValue().createdAt;
                oldestKey = entry.getKey();
            }
        }
        
        if (oldestKey != null && cache.remove(oldestKey) != null) {
            if (metricsEnabled) {
                evictions.incrementAndGet();
            }
            log.debug("Evicted FIFO entry: {}", oldestKey);
        }
    }
    
    private void evictLFU() {
        // For simplicity, just evict the first entry found
        // A full LFU implementation would track access counts
        String firstKey = cache.keys().nextElement();
        if (firstKey != null && cache.remove(firstKey) != null) {
            if (metricsEnabled) {
                evictions.incrementAndGet();
            }
            log.debug("Evicted LFU entry: {}", firstKey);
        }
    }
    
    private void updateAccessOrder(String key) {
        accessLock.writeLock().lock();
        try {
            accessOrder.remove(key); // Remove if already present
            accessOrder.offer(key);  // Add to end
        } finally {
            accessLock.writeLock().unlock();
        }
    }
    
    /**
     * Internal cache entry wrapper.
     */
    private static class CacheEntry {
        final Object value;
        final Instant createdAt;
        volatile Instant lastAccessed;
        
        CacheEntry(Object value, Instant createdAt) {
            this.value = value;
            this.createdAt = createdAt;
            this.lastAccessed = createdAt;
        }
    }
}
