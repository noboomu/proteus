package io.sinistral.proteus.test.server;

import io.sinistral.proteus.server.ProteusXnioThread;
import org.xnio.ManagementRegistration;
import org.xnio.XnioIoThread;
import org.xnio.XnioWorker;
import org.xnio.management.XnioServerMXBean;
import org.xnio.management.XnioWorkerMXBean;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProteusXnioWorker extends XnioWorker {

    /**
     * Construct a new instance.  Intended to be called only from implementations.
     *
     * @param builder the worker builder
     */

    ExecutorService executorService;
    Map<Integer,XnioIoThread> ioThreadMap = new ConcurrentHashMap<>();

    protected ProteusXnioWorker(Builder builder) {
        super(builder);
         executorService = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name(builder.getWorkerName()).factory());
    }

    @Override
    public void shutdown() {
        executorService.shutdown();

    }

    @Override
    public List<Runnable> shutdownNow() {
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
        return executorService.awaitTermination(timeout,unit);
    }

    @Override
    public void awaitTermination() throws InterruptedException {
        executorService.awaitTermination(10000L,TimeUnit.MILLISECONDS);
    }

    @Override
    public XnioIoThread getIoThread(int hashCode) {

        if(ioThreadMap.containsKey(hashCode))
        {
            return ioThreadMap.get(hashCode);
        }

        return null;
    }

    @Override
    public void execute(Runnable command) {
        executorService.execute(command);
    }

    @Override
    public int getIoThreadCount() {
        return ioThreadMap.size();
    }

    @Override
    protected XnioIoThread chooseThread() {
        return ProteusXnioThread.getInstance(this,0);
    }

    @Override
    public XnioWorkerMXBean getMXBean() {
        return null;
    }

    @Override
    protected ManagementRegistration registerServerMXBean(XnioServerMXBean metrics) {
        return null;
    }
}
