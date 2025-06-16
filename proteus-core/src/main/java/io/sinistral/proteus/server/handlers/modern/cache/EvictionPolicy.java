package io.sinistral.proteus.server.handlers.modern.cache;

/**
 * Enumeration of cache eviction policies supported by the handler cache.
 * 
 * @since 1.0.0
 */
public enum EvictionPolicy {
    
    /**
     * Least Recently Used - evicts the least recently accessed entry.
     */
    LRU,
    
    /**
     * Least Frequently Used - evicts the entry with the lowest access count.
     */
    LFU,
    
    /**
     * First In, First Out - evicts the oldest entry by insertion time.
     */
    FIFO,
    
    /**
     * Time To Live - evicts entries based on expiration time.
     */
    TTL,
    
    /**
     * No eviction - cache grows without bounds (use with caution).
     */
    NONE
}
