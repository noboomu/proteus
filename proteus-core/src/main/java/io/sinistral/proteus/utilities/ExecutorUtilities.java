package io.sinistral.proteus.utilities;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorUtilities {

    public static ExecutorService virtualExecutor(String name) {
        return Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name(name, 0L).factory());
    }

    public static ExecutorService fixedVirtualExecutor(int threads, String name) {
        return new FixedVirtualThreadExecutorService(threads, name);
    }

    public static ExecutorService fixedVirtualExecutor(int threads) {
        return new FixedVirtualThreadExecutorService(threads);
    }

    public static ExecutorService fixedExecutor(int threads, String name) {
        return Executors.newFixedThreadPool(threads, new ThreadFactoryBuilder().setNameFormat(name).build());
    }
}
