package io.sinistral.proteus.benchmarks;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.sinistral.proteus.ProteusApplication;
import io.sinistral.proteus.modules.ApplicationModule;
import io.sinistral.proteus.modules.ConfigModule;
import io.sinistral.proteus.server.handlers.HandlerGenerator;
import io.sinistral.proteus.test.controllers.Tests;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Benchmarks for measuring Proteus performance characteristics
 * 
 * Run with: mvn exec:java -Dexec.mainClass="io.sinistral.proteus.benchmarks.ProteusPerformanceBenchmark"
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(2)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 2, timeUnit = TimeUnit.SECONDS)
public class ProteusPerformanceBenchmark {

    private Injector injector;
    private Class<?> testController = Tests.class;

    @Setup(Level.Trial)
    public void setup() {
        injector = Guice.createInjector(
            new ConfigModule(),
            new ApplicationModule()
        );
    }

    @Benchmark
    public void handlerGeneration(Blackhole bh) throws Exception {
        HandlerGenerator generator = new HandlerGenerator(
            "io.sinistral.proteus.benchmarks.generated", 
            testController
        );
        injector.injectMembers(generator);
        
        String sourceCode = generator.generateClassSource();
        bh.consume(sourceCode);
    }

    @Benchmark
    public void fullApplicationStartup(Blackhole bh) throws Exception {
        ProteusApplication app = new ProteusApplication()
            .addController(testController);
        
        long startTime = System.currentTimeMillis();
        app.buildServer();
        long endTime = System.currentTimeMillis();
        
        bh.consume(endTime - startTime);
    }

    /**
     * Benchmark memory usage during handler generation
     */
    @Benchmark
    public void memoryUsageDuringGeneration(Blackhole bh) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        
        // Force GC before measurement
        System.gc();
        long beforeMemory = runtime.totalMemory() - runtime.freeMemory();
        
        HandlerGenerator generator = new HandlerGenerator(
            "io.sinistral.proteus.benchmarks.generated", 
            testController
        );
        injector.injectMembers(generator);
        String sourceCode = generator.generateClassSource();
        
        // Measure after generation
        System.gc();
        long afterMemory = runtime.totalMemory() - runtime.freeMemory();
        
        bh.consume(afterMemory - beforeMemory);
        bh.consume(sourceCode);
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
            .include(ProteusPerformanceBenchmark.class.getSimpleName())
            .result("proteus-benchmark-results.json")
            .resultFormat(org.openjdk.jmh.results.format.ResultFormatType.JSON)
            .build();

        new Runner(opt).run();
    }
}
