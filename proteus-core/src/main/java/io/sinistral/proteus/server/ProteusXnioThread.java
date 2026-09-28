package io.sinistral.proteus.server;

import io.undertow.UndertowLogger;
import org.xnio.XnioExecutor;
import org.xnio.XnioIoThread;
import org.xnio.XnioWorker;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/** XNIO I/O thread that tolerates scheduling during worker shutdown. */
public class ProteusXnioThread extends XnioIoThread {

    /**
     * Schedules a task after a delay, swallowing rejection when the worker is shutting down.
     *
     * @param thread the target I/O thread
     * @param task the task to schedule
     * @param timeout the delay amount
     * @param timeUnit the delay unit
     * @return the executor key, or a no-op key during shutdown
     */
    public static XnioExecutor.Key executeAfter(ProteusXnioThread thread, Runnable task, long timeout, TimeUnit timeUnit) {
        try {
            return thread.executeAfter(task, timeout, timeUnit);
        } catch (RejectedExecutionException e) {
            if (thread.getWorker().isShutdown()) {
                UndertowLogger.ROOT_LOGGER.debugf(e, "Failed to schedule task %s as worker is shutting down", task);
                //we just return a bogus key in this case
                return new XnioExecutor.Key() {
                    /**
                     * Does nothing; the bogus key cannot remove a rejected task.
                     *
                     * @return always false
                     */
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

    /** Creates a numbered thread.
     *
     * @param worker the owning worker
     * @param number the thread number */
    protected ProteusXnioThread(XnioWorker worker, int number) {
        super(worker, number);
    }

    /** Creates a named thread.
     *
     * @param worker the owning worker
     * @param number the thread number
     * @param name the thread name */
    protected ProteusXnioThread(XnioWorker worker, int number, String name) {
        super(worker, number, name);
    }

    /** Creates a named thread in a group.
     *
     * @param worker the owning worker
     * @param number the thread number
     * @param group the thread group
     * @param name the thread name */
    protected ProteusXnioThread(XnioWorker worker, int number, ThreadGroup group, String name) {
        super(worker, number, group, name);
    }

    /** Creates a named thread with a stack size.
     *
     * @param worker the owning worker
     * @param number the thread number
     * @param group the thread group
     * @param name the thread name
     * @param stackSize the requested stack size */
    protected ProteusXnioThread(XnioWorker worker, int number, ThreadGroup group, String name, long stackSize) {
        super(worker, number, group, name, stackSize);
    }

    class RepeatKey implements Key, Runnable {
        /** the command. */
        private final Runnable command;
        /** the millis. */
        private final long millis;
        /** the current. */
        private final AtomicReference<Key> current = new AtomicReference<>();

        RepeatKey(final Runnable command, final long millis) {
            this.command = command;
            this.millis = millis;
        }

        /**
         * Returns the remove.
         *
         * @return the remove, or null when unset
         */
        public boolean remove() {
            final Key removed = current.getAndSet(this);
            // removed key should not be null because remove cannot be called before it is populated.
            assert removed != null;
            return removed != this && removed.remove();
        }

        void setFirst(Key key) {
            current.compareAndSet(null, key);
        }

        /**
         * Returns the run.
         *
         */
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

    /**
     * Sets the execute, fluent style.
     *
     * @param runnable the execute
     */
    @Override
    public void execute(Runnable runnable) {
        Thread.ofVirtual().start(runnable);
    }

    /**
     * Sets the execute after, fluent style.
     *
     * @param task the execute after
     * @param timeout the execute after
     * @param timeUnit the execute after
     * @return this instance
     */
    @Override
    public Key executeAfter(Runnable task, long timeout, TimeUnit timeUnit) {
        return executeAfter(this, task, timeout, timeUnit);
    }


    /**
     * Sets the execute at interval, fluent style.
     *
     * @param command the execute at interval
     * @param time the execute at interval
     * @param unit the execute at interval
     * @return this instance
     */
    @Override
    public Key executeAtInterval(Runnable command, long time, TimeUnit unit) {
        final long millis = unit.toMillis(time);
        final RepeatKey repeatKey = new RepeatKey(command, millis);
        final Key firstKey = executeAfter(repeatKey, millis, TimeUnit.MILLISECONDS);
        repeatKey.setFirst(firstKey);
        return repeatKey;
    }
}
