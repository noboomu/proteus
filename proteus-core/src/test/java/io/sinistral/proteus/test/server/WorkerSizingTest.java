package io.sinistral.proteus.test.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.typesafe.config.ConfigFactory;
import io.sinistral.proteus.ProteusApplication;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.xnio.Options;

/**
 * The application-owned XNIO worker is sized from {@code undertow.ioThreadsMultiplier} and
 * {@code undertow.workerThreadsMultiplier}, as Undertow sizes the worker it would create itself.
 */
public class WorkerSizingTest {

    private ProteusApplication application;

    @AfterEach
    public void stop() throws Exception {
        if (application != null && application.isRunning()) {
            application.shutdown();
        }
        System.clearProperty("undertow.ioThreadsMultiplier");
        System.clearProperty("undertow.workerThreadsMultiplier");
        ConfigFactory.invalidateCaches();
    }

    private void start(double ioMultiplier, double workerMultiplier) {
        System.setProperty("undertow.ioThreadsMultiplier", Double.toString(ioMultiplier));
        System.setProperty("undertow.workerThreadsMultiplier", Double.toString(workerMultiplier));
        ConfigFactory.invalidateCaches();
        application = new ProteusApplication();
        application.start();
    }

    @Test
    public void ioThreadCountIsProcessorsTimesMultiplier() throws Exception {
        start(2, 1);
        int processors = Runtime.getRuntime().availableProcessors();
        assertEquals(processors * 2, application.getWorker().getIoThreadCount());
        assertEquals(processors * 2, application.getWorker().getOption(Options.WORKER_IO_THREADS));
    }

    @Test
    public void fractionalMultiplierRoundsWithMinimumOneThread() throws Exception {
        int processors = Runtime.getRuntime().availableProcessors();
        start(0.25, 0.5);
        assertEquals(Math.max(1, Math.round(processors * 0.25)), application.getWorker().getIoThreadCount());
        assertEquals((int) Math.max(1, Math.round(processors * 0.5)), application.getWorker().getOption(Options.WORKER_TASK_MAX_THREADS));
        application.shutdown();

        start(0.0001, 0.0001);
        assertEquals(1, application.getWorker().getIoThreadCount());
        assertEquals(1, application.getWorker().getOption(Options.WORKER_TASK_MAX_THREADS));
    }

    @Test
    public void taskPoolRunsProcessorsTimesMultiplierThreadsConcurrently() throws Exception {
        start(1, 2);
        int expected = Runtime.getRuntime().availableProcessors() * 2;
        assertEquals(expected, application.getWorker().getOption(Options.WORKER_TASK_MAX_THREADS));

        // Block every task until all expected tasks are running at once: proves the pool really
        // has that many threads, not just that the option was recorded.
        CountDownLatch running = new CountDownLatch(expected);
        CountDownLatch release = new CountDownLatch(1);
        Set<Thread> threads = ConcurrentHashMap.newKeySet();
        for (int i = 0; i < expected; i++) {
            application.getWorker().execute(() -> {
                threads.add(Thread.currentThread());
                running.countDown();
                try {
                    release.await(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        boolean allRunning = running.await(10, TimeUnit.SECONDS);
        release.countDown();
        assertEquals(true, allRunning, "only " + (expected - running.getCount()) + " of " + expected + " tasks ran concurrently");
        assertEquals(expected, threads.size());
    }
}
