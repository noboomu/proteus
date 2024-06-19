package io.sinistral.proteus.utilities;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class FixedVirtualThreadExecutorService implements ExecutorService {

    private final ExecutorService executorService;

    private Semaphore semaphore;

    private int poolSize;

    public FixedVirtualThreadExecutorService(int poolSize) {
        this.poolSize = poolSize;
        this.semaphore = new Semaphore(this.poolSize);
        this.executorService = Executors.newFixedThreadPool(poolSize);

    }

    public FixedVirtualThreadExecutorService(int poolSize, String name) {
        this.poolSize = poolSize;
        this.semaphore = new Semaphore(this.poolSize);
        this.executorService = name == null ? Executors.newFixedThreadPool(poolSize) : Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name(name, 0L).factory());
        
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public @NotNull List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public <T> @NotNull Future<T> submit(Callable<T> task) {
        try {
            semaphore.acquire();
            return executorService.submit(task);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            semaphore.release();
        }
    }

    @Override
    public <T> @NotNull Future<T> submit(Runnable task, T result) {
        try {
            semaphore.acquire();
            return executorService.submit(task, result);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            semaphore.release();
        }
    }

    @Override
    public @NotNull Future<?> submit(Runnable task) {
        try {
            semaphore.acquire();
            return executorService.submit(task);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            semaphore.release();
        }
    }

    @Override
    public <T> @NotNull List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        try {
            semaphore.acquire(tasks.size());
            return executorService.invokeAll(tasks);
        } finally {
            semaphore.release(tasks.size());
        }
    }

    @Override
    public <T> @NotNull List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        try {
            semaphore.acquire(tasks.size());
            return executorService.invokeAll(tasks, timeout, unit);
        } finally {
            semaphore.release(tasks.size());
        }
    }

    @Override
    public <T> @NotNull T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        try {
            semaphore.acquire(tasks.size());
            return executorService.invokeAny(tasks);
        } finally {
            semaphore.release(tasks.size());
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        try {
            semaphore.acquire(tasks.size());
            return executorService.invokeAny(tasks, timeout, unit);
        } finally {
            semaphore.release(tasks.size());
        }
    }

    @Override
    public void execute(@NotNull Runnable command) {
        try {
            semaphore.acquire();
            executorService.execute(command);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            semaphore.release();
        }
    }
}