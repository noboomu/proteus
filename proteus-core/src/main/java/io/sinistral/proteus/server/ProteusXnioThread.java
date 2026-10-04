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
        /** The command. */
        private final Runnable command;
        /** The millis. */
        private final long millis;
        /** The current. */
        private final AtomicReference<Key> current = new AtomicReference<>();

        RepeatKey(final Runnable command, final long millis) {
            this.command = command;
            this.millis = millis;
        }

        /**
         * Returns whether this ProteusXnioThread remove.
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
     * Runs the task on a new virtual thread.
     *
     * @param runnable the task to run
     */
    @Override
    public void execute(Runnable runnable) {
        Thread.ofVirtual().start(runnable);
    }

    /**
     * Schedules a one-shot task after a delay.
     *
     * @param task the task to schedule
     * @param timeout the delay amount
     * @param timeUnit the delay unit
     * @return the executor key
     */
    @Override
    public Key executeAfter(Runnable task, long timeout, TimeUnit timeUnit) {
        return executeAfter(this, task, timeout, timeUnit);
    }


    /**
     * Schedules a repeating task at a fixed interval.
     *
     * @param command the task to repeat
     * @param time the interval amount
     * @param unit the interval unit
     * @return the executor key
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
