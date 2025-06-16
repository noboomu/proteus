package io.sinistral.proteus.server.handlers.modern.cache;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Interface for caching compiled handler instances with configurable eviction policies.
 * Provides thread-safe operations with metrics support and async capabilities.
 * 
 * @since 1.0.0
 */
public interface HandlerCache {
    
    /**
     * Retrieves a cached handler by key.
     * 
     * @param key the cache key
     * @return optional containing the cached handler, or empty if not found
     */
    Optional<Object> get(String key);
    
    /**
     * Stores a handler in the cache with the given key.
     * 
     * @param key the cache key
     * @param handler the handler instance to cache
     */
    void put(String key, Object handler);
    
    /**
     * Retrieves a handler by key, computing and caching it if not present.
     * 
     * @param key the cache key
     * @param supplier function to compute the handler if not cached
     * @return the cached or computed handler
     */
    Object computeIfAbsent(String key, Function<String, Object> supplier);
    
    /**
     * Asynchronously retrieves a handler by key, computing and caching it if not present.
     * 
     * @param key the cache key
     * @param supplier function to compute the handler if not cached
     * @return CompletableFuture containing the cached or computed handler
     */
    CompletableFuture<Object> computeIfAbsentAsync(String key, Function<String, CompletableFuture<Object>> supplier);
    
    /**
     * Removes a handler from the cache.
     * 
     * @param key the cache key to remove
     * @return true if the key was present and removed, false otherwise
     */
    boolean remove(String key);
    
    /**
     * Removes all entries from the cache.
     */
    void clear();
    
    /**
     * Returns the current number of entries in the cache.
     * 
     * @return cache size
     */
    int size();
    
    /**
     * Checks if the cache is empty.
     * 
     * @return true if cache is empty, false otherwise
     */
    boolean isEmpty();
    
    /**
     * Gets cache metrics if available.
     * 
     * @return optional containing cache metrics, or empty if metrics not supported
     */
    Optional<CacheMetrics> getMetrics();
    
    /**
     * Invalidates expired entries based on the configured TTL.
     * This method may be called automatically by the cache implementation.
     */
    void evictExpired();
    
    /**
     * Shuts down the cache, cleaning up any resources.
     */
    void shutdown();
}
