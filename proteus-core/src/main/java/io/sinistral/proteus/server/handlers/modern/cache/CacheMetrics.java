package io.sinistral.proteus.server.handlers.modern.cache;

/**
 * Immutable metrics data for cache performance monitoring.
 * 
 * @since 1.0.0
 */
public class CacheMetrics {
    
    private final long hits;
    private final long misses;
    private final long evictions;
    private final long size;
    private final long maxSize;
    private final double hitRate;
    private final long totalLoadTime;
    private final double averageLoadTime;
    
    public CacheMetrics(long hits, long misses, long evictions, long size, long maxSize, 
                       long totalLoadTime) {
        this.hits = hits;
        this.misses = misses;
        this.evictions = evictions;
        this.size = size;
        this.maxSize = maxSize;
        this.totalLoadTime = totalLoadTime;
        
        // Calculate derived metrics
        long totalRequests = hits + misses;
        this.hitRate = totalRequests > 0 ? (double) hits / totalRequests : 0.0;
        this.averageLoadTime = misses > 0 ? (double) totalLoadTime / misses : 0.0;
    }
    
    /**
     * @return number of cache hits
     */
    public long getHits() {
        return hits;
    }
    
    /**
     * @return number of cache misses
     */
    public long getMisses() {
        return misses;
    }
    
    /**
     * @return number of evictions performed
     */
    public long getEvictions() {
        return evictions;
    }
    
    /**
     * @return current cache size
     */
    public long getSize() {
        return size;
    }
    
    /**
     * @return maximum cache size
     */
    public long getMaxSize() {
        return maxSize;
    }
    
    /**
     * @return cache hit rate (0.0 to 1.0)
     */
    public double getHitRate() {
        return hitRate;
    }
    
    /**
     * @return total time spent loading cache entries (in nanoseconds)
     */
    public long getTotalLoadTime() {
        return totalLoadTime;
    }
    
    /**
     * @return average time to load a cache entry (in nanoseconds)
     */
    public double getAverageLoadTime() {
        return averageLoadTime;
    }
    
    /**
     * @return total number of requests (hits + misses)
     */
    public long getTotalRequests() {
        return hits + misses;
    }
    
    @Override
    public String toString() {
        return String.format(
            "CacheMetrics{hits=%d, misses=%d, hitRate=%.2f%%, evictions=%d, size=%d/%d, avgLoadTime=%.2fms}",
            hits, misses, hitRate * 100, evictions, size, maxSize, averageLoadTime / 1_000_000.0
        );
    }
}
