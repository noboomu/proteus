package io.sinistral.proteus.server.handlers.modern;

import java.time.Duration;

/**
 * Configuration for modern handler generation and compilation.
 * 
 * Phase 1 Implementation: Foundation and Testing
 * Provides centralized configuration management for all handler generation settings.
 */
public class ModernHandlerConfig {
    
    // Caching configuration
    private final boolean enableCaching;
    private final int maxCacheSize;
    private final Duration cacheTimeout;
    private final boolean enableCacheEviction;
    
    // Compilation configuration
    private final int compilationThreads;
    private final Duration compilationTimeout;
    private final boolean enableParallelCompilation;
    private final int parallelCompilationThreshold; // Threshold for parallel compilation
    
    // Debug and monitoring configuration
    private final boolean debugMode;
    private final boolean generateSourceFiles;
    private final String sourceOutputDirectory;
    private final boolean enableMetrics;
    private final boolean enableJmx;
    
    // Performance configuration
    private final boolean useVirtualThreads;
    private final int maxMemoryPerCompilation;
    private final boolean enableMemoryProfiling;
    
    private ModernHandlerConfig(Builder builder) {
        this.enableCaching = builder.enableCaching;
        this.maxCacheSize = builder.maxCacheSize;
        this.cacheTimeout = builder.cacheTimeout;
        this.enableCacheEviction = builder.enableCacheEviction;
        
        this.compilationThreads = builder.compilationThreads;
        this.compilationTimeout = builder.compilationTimeout;
        this.enableParallelCompilation = builder.enableParallelCompilation;
        
        this.debugMode = builder.debugMode;
        this.generateSourceFiles = builder.generateSourceFiles;
        this.sourceOutputDirectory = builder.sourceOutputDirectory;
        this.enableMetrics = builder.enableMetrics;
        this.enableJmx = builder.enableJmx;
        
        this.useVirtualThreads = builder.useVirtualThreads;
        this.maxMemoryPerCompilation = builder.maxMemoryPerCompilation;
        this.enableMemoryProfiling = builder.enableMemoryProfiling;
        this.parallelCompilationThreshold = builder.parallelCompilationThreshold;
    }
    
    // Getters
    
    public boolean isEnableCaching() { return enableCaching; }
    public int getMaxCacheSize() { return maxCacheSize; }
    public Duration getCacheTimeout() { return cacheTimeout; }
    public boolean isEnableCacheEviction() { return enableCacheEviction; }
    public int getParallelCompilationThreshold() { return parallelCompilationThreshold; }
    
    public int getCompilationThreads() { return compilationThreads; }
    public Duration getCompilationTimeout() { return compilationTimeout; }
    public boolean isEnableParallelCompilation() { return enableParallelCompilation; }
    
    public boolean isDebugMode() { return debugMode; }
    public boolean isGenerateSourceFiles() { return generateSourceFiles; }
    public String getSourceOutputDirectory() { return sourceOutputDirectory; }
    public boolean isEnableMetrics() { return enableMetrics; }
    public boolean isEnableJmx() { return enableJmx; }
    
    public boolean isUseVirtualThreads() { return useVirtualThreads; }
    public int getMaxMemoryPerCompilation() { return maxMemoryPerCompilation; }
    public boolean isEnableMemoryProfiling() { return enableMemoryProfiling; }
    
    /**
     * Creates a default configuration suitable for production use.
     */
    public static ModernHandlerConfig defaultConfig() {
        return new Builder()
            .enableCaching(true)
            .maxCacheSize(1000)
            .cacheTimeout(Duration.ofHours(1))
            .compilationThreads(Math.min(4, Runtime.getRuntime().availableProcessors()))
            .compilationTimeout(Duration.ofSeconds(30))
            .enableParallelCompilation(true)
            .debugMode(false)
            .enableMetrics(true)
            .useVirtualThreads(true)
            .parallelCompilationThreshold(5)
            .build();
    }
    
    /**
     * Creates a configuration suitable for development with enhanced debugging.
     */
    public static ModernHandlerConfig developmentConfig() {
        return new Builder()
            .enableCaching(true)
            .maxCacheSize(100)
            .cacheTimeout(Duration.ofMinutes(5))
            .compilationThreads(2)
            .compilationTimeout(Duration.ofSeconds(60))
            .enableParallelCompilation(false)
            .debugMode(true)
            .generateSourceFiles(true)
            .sourceOutputDirectory("target/generated-handlers")
            .enableMetrics(true)
            .enableJmx(true)
            .enableMemoryProfiling(true)
            .useVirtualThreads(true) // Easier debugging with platform threads
            .build();
    }
    
    /**
     * Creates a configuration suitable for testing with minimal overhead.
     */
    public static ModernHandlerConfig testConfig() {
        return new Builder()
            .enableCaching(false)
            .compilationThreads(1)
            .compilationTimeout(Duration.ofSeconds(10))
            .enableParallelCompilation(false)
            .debugMode(true)
            .enableMetrics(false)
            .enableJmx(false)
            .useVirtualThreads(true)
            .build();
    }
    
    public static class Builder {
        private boolean enableCaching = true;
        private int maxCacheSize = 1000;
        private Duration cacheTimeout = Duration.ofHours(1);
        private boolean enableCacheEviction = true;
        
        private int compilationThreads = Runtime.getRuntime().availableProcessors();
        private Duration compilationTimeout = Duration.ofSeconds(30);
        private boolean enableParallelCompilation = true;
        
        private boolean debugMode = false;
        private boolean generateSourceFiles = false;
        private String sourceOutputDirectory = "target/generated-handlers";
        private boolean enableMetrics = true;
        private boolean enableJmx = false;
        
        private boolean useVirtualThreads = true;
        private int maxMemoryPerCompilation = 64 * 1024 * 1024; // 64MB
        private boolean enableMemoryProfiling = false;
        private int parallelCompilationThreshold = 5; // Default threshold for parallel compilation
        
        public Builder enableCaching(boolean enableCaching) {
            this.enableCaching = enableCaching;
            return this;
        }

        public Builder parallelCompilationThreshold(int parallelCompilationThreshold) {
            this.parallelCompilationThreshold = parallelCompilationThreshold;
            return this;
        }

        public Builder maxCacheSize(int maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }
        
        public Builder cacheTimeout(Duration cacheTimeout) {
            this.cacheTimeout = cacheTimeout;
            return this;
        }
        
        public Builder enableCacheEviction(boolean enableCacheEviction) {
            this.enableCacheEviction = enableCacheEviction;
            return this;
        }
        
        public Builder compilationThreads(int compilationThreads) {
            this.compilationThreads = compilationThreads;
            return this;
        }
        
        public Builder compilationTimeout(Duration compilationTimeout) {
            this.compilationTimeout = compilationTimeout;
            return this;
        }
        
        public Builder enableParallelCompilation(boolean enableParallelCompilation) {
            this.enableParallelCompilation = enableParallelCompilation;
            return this;
        }
        
        public Builder debugMode(boolean debugMode) {
            this.debugMode = debugMode;
            return this;
        }
        
        public Builder generateSourceFiles(boolean generateSourceFiles) {
            this.generateSourceFiles = generateSourceFiles;
            return this;
        }
        
        public Builder sourceOutputDirectory(String sourceOutputDirectory) {
            this.sourceOutputDirectory = sourceOutputDirectory;
            return this;
        }
        
        public Builder enableMetrics(boolean enableMetrics) {
            this.enableMetrics = enableMetrics;
            return this;
        }
        
        public Builder enableJmx(boolean enableJmx) {
            this.enableJmx = enableJmx;
            return this;
        }
        
        public Builder useVirtualThreads(boolean useVirtualThreads) {
            this.useVirtualThreads = useVirtualThreads;
            return this;
        }
        
        public Builder maxMemoryPerCompilation(int maxMemoryPerCompilation) {
            this.maxMemoryPerCompilation = maxMemoryPerCompilation;
            return this;
        }
        
        public Builder enableMemoryProfiling(boolean enableMemoryProfiling) {
            this.enableMemoryProfiling = enableMemoryProfiling;
            return this;
        }
        
        public ModernHandlerConfig build() {
            return new ModernHandlerConfig(this);
        }
    }
    
    @Override
    public String toString() {
        return String.format(
            "ModernHandlerConfig{caching=%s, cacheSize=%d, compilationThreads=%d, " +
            "parallelCompilation=%s, debug=%s, metrics=%s, virtualThreads=%s}",
            enableCaching, maxCacheSize, compilationThreads, enableParallelCompilation,
            debugMode, enableMetrics, useVirtualThreads
        );
    }
}