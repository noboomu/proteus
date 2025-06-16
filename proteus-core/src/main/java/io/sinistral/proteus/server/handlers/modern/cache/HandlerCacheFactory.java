package io.sinistral.proteus.server.handlers.modern.cache;

import java.time.Duration;

/**
 * Factory for creating HandlerCache instances with common configurations.
 * 
 * @since 1.0.0
 */
public final class HandlerCacheFactory {
    
    private HandlerCacheFactory() {
        // Utility class
    }
    
    /**
     * Creates a simple unbounded cache with no eviction.
     * 
     * @return new cache instance
     */
    public static HandlerCache createSimple() {
        return new ConfigurableHandlerCache(0, EvictionPolicy.NONE, null, false);
    }
    
    /**
     * Creates an LRU cache with the specified maximum size.
     * 
     * @param maxSize maximum number of entries
     * @return new cache instance
     */
    public static HandlerCache createLRU(int maxSize) {
        return new ConfigurableHandlerCache(maxSize, EvictionPolicy.LRU, null, true);
    }
    
    /**
     * Creates a TTL-based cache with the specified expiration time.
     * 
     * @param ttl time-to-live for cache entries
     * @return new cache instance
     */
    public static HandlerCache createTTL(Duration ttl) {
        return new ConfigurableHandlerCache(0, EvictionPolicy.TTL, ttl, true);
    }
    
    /**
     * Creates an LRU cache with TTL and metrics enabled.
     * 
     * @param maxSize maximum number of entries
     * @param ttl time-to-live for cache entries
     * @return new cache instance
     */
    public static HandlerCache createLRUWithTTL(int maxSize, Duration ttl) {
        return new ConfigurableHandlerCache(maxSize, EvictionPolicy.LRU, ttl, true);
    }
    
    /**
     * Creates a production-ready cache with LRU eviction, TTL, and metrics.
     * Recommended for most use cases.
     * 
     * @param maxSize maximum number of entries (default: 1000)
     * @param ttl time-to-live for cache entries (default: 1 hour)
     * @return new cache instance
     */
    public static HandlerCache createProduction(int maxSize, Duration ttl) {
        return new ConfigurableHandlerCache(
            maxSize > 0 ? maxSize : 1000,
            EvictionPolicy.LRU,
            ttl != null ? ttl : Duration.ofHours(1),
            true
        );
    }
    
    /**
     * Creates a production-ready cache with default settings.
     * 
     * @return new cache instance with 1000 entries max and 1 hour TTL
     */
    public static HandlerCache createProduction() {
        return createProduction(1000, Duration.ofHours(1));
    }
}
