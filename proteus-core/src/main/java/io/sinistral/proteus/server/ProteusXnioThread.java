package io.sinistral.proteus.server;

import io.undertow.UndertowLogger;
import org.xnio.XnioExecutor;
import org.xnio.XnioIoThread;
import org.xnio.XnioWorker;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class ProteusXnioThread extends XnioIoThread {

    public static XnioExecutor.Key executeAfter(ProteusXnioThread thread, Runnable task, long timeout, TimeUnit timeUnit) {
        try {
            return thread.executeAfter(task, timeout, timeUnit);
        } catch (RejectedExecutionException e) {
            if (thread.getWorker().isShutdown()) {
                UndertowLogger.ROOT_LOGGER.debugf(e, "Failed to schedule task %s as worker is shutting down", task);
                //we just return a bogus key in this case
                return new XnioExecutor.Key() {
                    @Override
                    public boolean remove() {
                        return false;
                    }
                };
            } else {
                throw e;
            }
        }
    }

    protected ProteusXnioThread(XnioWorker worker, int number) {
        super(worker, number);
    }

    protected ProteusXnioThread(XnioWorker worker, int number, String name) {
        super(worker, number, name);
    }

    protected ProteusXnioThread(XnioWorker worker, int number, ThreadGroup group, String name) {
        super(worker, number, group, name);
    }

    protected ProteusXnioThread(XnioWorker worker, int number, ThreadGroup group, String name, long stackSize) {
        super(worker, number, group, name, stackSize);
    }

    class RepeatKey implements Key, Runnable {
        private final Runnable command;
        private final long millis;
        private final AtomicReference<Key> current = new AtomicReference<>();

        RepeatKey(final Runnable command, final long millis) {
            this.command = command;
            this.millis = millis;
        }

        public boolean remove() {
            final Key removed = current.getAndSet(this);
            // removed key should not be null because remove cannot be called before it is populated.
            assert removed != null;
            return removed != this && removed.remove();
        }

        void setFirst(Key key) {
            current.compareAndSet(null, key);
        }

        public void run() {
            try {
                command.run();
            } finally {
                Key o, n;
                o = current.get();
                if (o != this) {
                    n = executeAfter(this, millis, TimeUnit.MILLISECONDS);
                    if (!current.compareAndSet(o, n)) {
                        n.remove();
                    }
                }
            }
        }
    }

    @Override
    public void execute(Runnable runnable) {
        Thread.ofVirtual().start(runnable);
    }

    @Override
    public Key executeAfter(Runnable task, long timeout, TimeUnit timeUnit) {
        return executeAfter(this, task, timeout, timeUnit);
    }


    @Override
    public Key executeAtInterval(Runnable command, long time, TimeUnit unit) {
        final long millis = unit.toMillis(time);
        final RepeatKey repeatKey = new RepeatKey(command, millis);
        final Key firstKey = executeAfter(repeatKey, millis, TimeUnit.MILLISECONDS);
        repeatKey.setFirst(firstKey);
        return repeatKey;
    }
}
