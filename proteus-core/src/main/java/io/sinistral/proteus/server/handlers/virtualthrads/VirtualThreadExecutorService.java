package io.sinistral.proteus.server.handlers.virtualthrads;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

/**
 * Virtual thread executor service optimized for Proteus and Undertow integration.
 * 
 * <p>This service creates and manages virtual threads for blocking operations,
 * following best practices to avoid pinning and monopolization issues.
 * 
 * <p>Key features:
 * <ul>
 *   <li>Named virtual threads for debugging</li>
 *   <li>Monitoring and metrics collection</li>
 *   <li>Integration with Undertow's threading model</li>
 *   <li>Configuration-driven thread management</li>
 * </ul>
 */
public class VirtualThreadExecutorService {
    
    private static final Logger log = LoggerFactory.getLogger(VirtualThreadExecutorService.class);
    
    private final ExecutorService virtualThreadExecutor;
    private final VirtualThreadMetrics metrics;
    private final String namePrefix;
    private final boolean enableMetrics;
    
    /**
     * Creates a new virtual thread executor service.
     * 
     * @param config the configuration
     */
    public VirtualThreadExecutorService(Config config) {
        this.namePrefix = config.hasPath("proteus.virtual-threads.name-prefix") 
            ? config.getString("proteus.virtual-threads.name-prefix")
            : "proteus-virtual-thread-";
            
        this.enableMetrics = config.hasPath("proteus.virtual-threads.enable-metrics")
            ? config.getBoolean("proteus.virtual-threads.enable-metrics")
            : true;
            
        this.metrics = enableMetrics ? new VirtualThreadMetrics() : null;
        
        // Create virtual thread factory with naming and monitoring
        ThreadFactory virtualThreadFactory = Thread.ofVirtual()
            .name(namePrefix, 0)
            .uncaughtExceptionHandler(this::handleUncaughtException)
            .factory();
            
        this.virtualThreadExecutor = Executors.newThreadPerTaskExecutor(virtualThreadFactory);
        
        log.info("Virtual thread executor initialized with prefix: {}, metrics: {}", 
                namePrefix, enableMetrics);
    }
    
    /**
     * Executes a task on a virtual thread.
     * 
     * @param task the task to execute
     */
    public void execute(Runnable task) {
        if (enableMetrics) {
            metrics.incrementTasksSubmitted();
        }
        
        virtualThreadExecutor.execute(() -> {
            if (enableMetrics) {
                long startTime = System.nanoTime();
                try {
                    task.run();
                    metrics.recordTaskDuration(Duration.ofNanos(System.nanoTime() - startTime));
                } catch (Exception e) {
                    metrics.incrementTasksError();
                    throw e;
                } finally {
                    metrics.incrementTasksCompleted();
                }
            } else {
                task.run();
            }
        });
    }
    
    /**
     * Gets the underlying executor service.
     * 
     * @return the virtual thread executor
     */
    public ExecutorService getExecutor() {
        return virtualThreadExecutor;
    }
    
    /**
     * Gets the metrics collector.
     * 
     * @return the metrics, or null if disabled
     */
    public VirtualThreadMetrics getMetrics() {
        return metrics;
    }
    
    /**
     * Shuts down the virtual thread executor.
     */
    public void shutdown() {
        log.info("Shutting down virtual thread executor");
        virtualThreadExecutor.shutdown();
        
        if (enableMetrics) {
            log.info("Virtual thread metrics: {}", metrics.getSummary());
        }
    }
    
    /**
     * Handles uncaught exceptions in virtual threads.
     * 
     * @param thread the thread
     * @param exception the exception
     */
    private void handleUncaughtException(Thread thread, Throwable exception) {
        log.error("Uncaught exception in virtual thread: {}", thread.getName(), exception);
        
        if (enableMetrics) {
            metrics.incrementUncaughtExceptions();
        }
    }
    
    /**
     * Checks if virtual threads are available in the current JVM.
     * 
     * @return true if virtual threads are supported
     */
    public static boolean isVirtualThreadSupported() {
        try {
            // Try to create a virtual thread to check availability
            Thread.ofVirtual().start(() -> {}).join();
            return true;
        } catch (UnsupportedOperationException | NoSuchMethodError e) {
            return false;
        } catch (Exception e) {
            log.warn("Error checking virtual thread support", e);
            return false;
        }
    }
    
    /**
     * Metrics collector for virtual thread operations.
     */
    public static class VirtualThreadMetrics {
        private final AtomicLong tasksSubmitted = new AtomicLong();
        private final AtomicLong tasksCompleted = new AtomicLong();
        private final AtomicLong tasksError = new AtomicLong();
        private final AtomicLong uncaughtExceptions = new AtomicLong();
        private volatile long totalDurationNanos = 0;
        private volatile long maxDurationNanos = 0;
        
        public void incrementTasksSubmitted() {
            tasksSubmitted.incrementAndGet();
        }
        
        public void incrementTasksCompleted() {
            tasksCompleted.incrementAndGet();
        }
        
        public void incrementTasksError() {
            tasksError.incrementAndGet();
        }
        
        public void incrementUncaughtExceptions() {
            uncaughtExceptions.incrementAndGet();
        }
        
        public synchronized void recordTaskDuration(Duration duration) {
            long nanos = duration.toNanos();
            totalDurationNanos += nanos;
            if (nanos > maxDurationNanos) {
                maxDurationNanos = nanos;
            }
        }
        
        public long getTasksSubmitted() {
            return tasksSubmitted.get();
        }
        
        public long getTasksCompleted() {
            return tasksCompleted.get();
        }
        
        public long getTasksError() {
            return tasksError.get();
        }
        
        public long getUncaughtExceptions() {
            return uncaughtExceptions.get();
        }
        
        public synchronized Duration getAverageDuration() {
            long completed = tasksCompleted.get();
            return completed > 0 ? Duration.ofNanos(totalDurationNanos / completed) : Duration.ZERO;
        }
        
        public synchronized Duration getMaxDuration() {
            return Duration.ofNanos(maxDurationNanos);
        }
        
        public String getSummary() {
            return String.format(
                "Tasks: %d submitted, %d completed, %d errors, %d uncaught exceptions. " +
                "Average duration: %s, Max duration: %s",
                getTasksSubmitted(), getTasksCompleted(), getTasksError(), getUncaughtExceptions(),
                getAverageDuration(), getMaxDuration()
            );
        }
    }
}
