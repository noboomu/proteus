package io.sinistral.proteus.benchmarks;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.modules.ApplicationModule;
import io.sinistral.proteus.modules.ConfigModule;
import io.sinistral.proteus.server.handlers.HandlerGenerator;
import io.sinistral.proteus.test.controllers.Tests;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Simple benchmarks for measuring Proteus performance characteristics
 * Run with: mvn test-compile exec:java -Dexec.mainClass="io.sinistral.proteus.benchmarks.SimplePerformanceBenchmark" -Dexec.classpathScope="test"
 */
public class SimplePerformanceBenchmark {

    private static final int WARMUP_ITERATIONS = 5;
    private static final int MEASUREMENT_ITERATIONS = 20;

    public static void main(String[] args) throws Exception {
        SimplePerformanceBenchmark benchmark = new SimplePerformanceBenchmark();
        
        System.out.println("=== Proteus Performance Baseline Benchmarks ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Available Processors: " + Runtime.getRuntime().availableProcessors());
        System.out.println();
        
        benchmark.runHandlerGenerationBenchmark();
        benchmark.runMemoryUsageBenchmark();
        benchmark.runApplicationStartupBenchmark();
        benchmark.runThreadConfigurationBenchmark();
    }

    private void runHandlerGenerationBenchmark() throws Exception {
        System.out.println("=== Handler Generation Benchmark ===");
        
        com.typesafe.config.Config config = com.typesafe.config.ConfigFactory.load();
        Injector injector = Guice.createInjector(
            new ConfigModule(),
            new ApplicationModule(config)
        );
        
        // Warmup
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            generateHandler(injector);
        }
        
        // Measurement
        List<Long> times = new ArrayList<>();
        for (int i = 0; i < MEASUREMENT_ITERATIONS; i++) {
            long startTime = System.nanoTime();
            generateHandler(injector);
            long endTime = System.nanoTime();
            times.add(endTime - startTime);
        }
        
        printStatistics("Handler Generation", times);
    }
    
    private void generateHandler(Injector injector) throws Exception {
        HandlerGenerator generator = new HandlerGenerator(
            "io.sinistral.proteus.benchmarks.generated", 
            Tests.class
        );
        injector.injectMembers(generator);
        String sourceCode = generator.generateClassSource();
        // Simulate compilation time
        Thread.sleep(10); // Small delay to simulate actual work
    }

    private void runMemoryUsageBenchmark() throws Exception {
        System.out.println("\n=== Memory Usage Benchmark ===");
        
        Runtime runtime = Runtime.getRuntime();        
        com.typesafe.config.Config config = com.typesafe.config.ConfigFactory.load();
        Injector injector = Guice.createInjector(
            new ConfigModule(),
            new ApplicationModule(config)
        );
        
        // Force GC and measure baseline
        System.gc();
        Thread.sleep(100);
        long baselineMemory = runtime.totalMemory() - runtime.freeMemory();
        
        List<Long> memoryUsages = new ArrayList<>();
        
        for (int i = 0; i < 10; i++) {
            System.gc();
            long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
            
            generateHandler(injector);
            
            System.gc();
            long afterMemory = runtime.totalMemory() - runtime.freeMemory();
            
            memoryUsages.add(afterMemory - beforeMemory);
        }
        
        System.out.println("Baseline Memory Usage: " + formatBytes(baselineMemory));
        printMemoryStatistics("Handler Generation Memory Impact", memoryUsages);
    }

    private void runApplicationStartupBenchmark() throws Exception {
        System.out.println("\n=== Application Startup Benchmark ===");
        
        List<Long> startupTimes = new ArrayList<>();
        
        // Warmup
        for (int i = 0; i < 3; i++) {
            ProteusApplication app = new ProteusApplication()
                .addController(Tests.class);
            long startTime = System.currentTimeMillis();
            app.buildServer();
            long endTime = System.currentTimeMillis();
            // Don't count warmup times
        }
        
        // Measurement
        for (int i = 0; i < 10; i++) {
            ProteusApplication app = new ProteusApplication()
                .addController(Tests.class);
            
            long startTime = System.currentTimeMillis();
            app.buildServer();
            long endTime = System.currentTimeMillis();
            
            startupTimes.add((endTime - startTime) * 1_000_000L); // Convert to nanoseconds for consistency
        }
        
        printStatistics("Application Startup", startupTimes);
    }

    private void runThreadConfigurationBenchmark() {
        System.out.println("\n=== Thread Configuration Analysis ===");
        
        int processors = Runtime.getRuntime().availableProcessors();
        
        // Current default configuration from reference.conf
        int currentIoThreads = processors * 2;      // ioThreadsMultiplier=2
        int currentWorkerThreads = processors * 10; // workerThreadsMultiplier=10
        
        // Proposed virtual thread optimized configuration
        int optimizedIoThreads = processors;        // 1x multiplier for I/O
        int optimizedWorkerThreads = processors * 2; // Reduced worker threads (virtual threads handle blocking)
        
        System.out.println("Current Configuration:");
        System.out.println("  Processors: " + processors);
        System.out.println("  I/O Threads: " + currentIoThreads + " (2x multiplier)");
        System.out.println("  Worker Threads: " + currentWorkerThreads + " (10x multiplier)");
        System.out.println("  Total Platform Threads: " + (currentIoThreads + currentWorkerThreads));
        
        System.out.println("\nProposed Virtual Thread Optimized Configuration:");
        System.out.println("  Processors: " + processors);
        System.out.println("  I/O Threads: " + optimizedIoThreads + " (1x multiplier)");
        System.out.println("  Worker Threads: " + optimizedWorkerThreads + " (2x multiplier)");
        System.out.println("  Total Platform Threads: " + (optimizedIoThreads + optimizedWorkerThreads));
        System.out.println("  Virtual Threads: Unlimited (as needed)");
        
        System.out.println("\nExpected Benefits:");
        System.out.println("  Platform Thread Reduction: " + 
            (100 - ((optimizedIoThreads + optimizedWorkerThreads) * 100) / (currentIoThreads + currentWorkerThreads)) + "%");
        System.out.println("  Memory Saving: ~" + 
            formatBytes((currentWorkerThreads - optimizedWorkerThreads) * 2L * 1024 * 1024) + " (estimated)");
    }

    private void printStatistics(String operationName, List<Long> times) {
        times.sort(Long::compareTo);
        
        long min = times.get(0);
        long max = times.get(times.size() - 1);
        long median = times.get(times.size() / 2);
        double average = times.stream().mapToLong(Long::longValue).average().orElse(0.0);
        
        System.out.println(operationName + " Results:");
        System.out.println("  Iterations: " + times.size());
        System.out.println("  Min: " + formatTime(min));
        System.out.println("  Max: " + formatTime(max));
        System.out.println("  Median: " + formatTime(median));
        System.out.println("  Average: " + formatTime((long) average));
    }
    
    private void printMemoryStatistics(String operationName, List<Long> memoryUsages) {
        memoryUsages.sort(Long::compareTo);
        
        long min = memoryUsages.get(0);
        long max = memoryUsages.get(memoryUsages.size() - 1);
        long median = memoryUsages.get(memoryUsages.size() / 2);
        double average = memoryUsages.stream().mapToLong(Long::longValue).average().orElse(0.0);
        
        System.out.println(operationName + " Results:");
        System.out.println("  Iterations: " + memoryUsages.size());
        System.out.println("  Min: " + formatBytes(min));
        System.out.println("  Max: " + formatBytes(max));
        System.out.println("  Median: " + formatBytes(median));
        System.out.println("  Average: " + formatBytes((long) average));
    }

    private String formatTime(long nanoseconds) {
        if (nanoseconds < 1_000) {
            return nanoseconds + " ns";
        } else if (nanoseconds < 1_000_000) {
            return String.format("%.2f μs", nanoseconds / 1_000.0);
        } else if (nanoseconds < 1_000_000_000) {
            return String.format("%.2f ms", nanoseconds / 1_000_000.0);
        } else {
            return String.format("%.2f s", nanoseconds / 1_000_000_000.0);
        }
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.2f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
