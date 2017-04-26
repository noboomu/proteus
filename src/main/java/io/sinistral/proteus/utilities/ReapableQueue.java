/**
 * 
 */
package io.sinistral.proteus.utilities;

import java.util.concurrent.atomic.*;
  
/**
 * multiple threads can be 'add(...)' concurrently, but only one thread may
 * 'reap()' and/or 'end()' at the same time.
 * 
 * @author woshilaiceshide
 *
 * @param <T>
 */
public class ReapableQueue<T> {

	@SuppressWarnings("rawtypes")
	private static AtomicReferenceFieldUpdater<ReapableQueue, Node> head_updater = AtomicReferenceFieldUpdater
			.newUpdater(ReapableQueue.class, Node.class, "head");

	@SuppressWarnings("rawtypes")
	private static AtomicReferenceFieldUpdater<ReapableQueue, Node> tail_updater = AtomicReferenceFieldUpdater
			.newUpdater(ReapableQueue.class, Node.class, "tail");

	@SuppressWarnings("rawtypes")
	private static AtomicReferenceFieldUpdater<Node, Node> next_updater = AtomicReferenceFieldUpdater
			.newUpdater(Node.class, Node.class, "next");

	public static class ReapException extends Exception {
		private static final long serialVersionUID = 1L;

		public ReapException(String msg) {
			super(msg);
		}
	}

	public static class Node<T> {
		public final T value;
		volatile Node<T> next;

		Node(T value, Node<T> next) {
			this.value = value;
			this.next = next;
		}

		Node(T value) {
			this(value, null);
		}

		// please see
		// http://robsjava.blogspot.com/2013/06/a-faster-volatile.html
		void set_next(Node<T> new_next) {
			next_updater.set(this, new_next);
			return;
		}

		void set_next(Node<T> old_next, Node<T> new_next) {
			next_updater.compareAndSet(this, old_next, new_next);
			return;
		}

		@SuppressWarnings("unchecked")
		public Node<T> get_next() {
			return next_updater.get(this);
		}
	}

	public static class Reaped<T> {
		public Node<T> head;
		public final Node<T> tail;

		public Reaped(Node<T> head, Node<T> tail) {
			this.head = head;
			this.tail = tail;
		}

		public Node<T> get_current_and_advance() {
			if (this.head == null) {
				return null;
			} else {
				Node<T> tmp = this.head;
				if (this.head == this.tail) {
					this.head = null;
				} else {
					Node<T> next = this.head.get_next();
					int i = 0;
					while (true) {
						if (next != null)
							break;
						else
							next = this.head.get_next();

						if (i < 1024) {
							i++;
						} else {
							try {
								Thread.sleep(System.currentTimeMillis() % 10);
							} catch (Throwable throwable) {
								// omitted
							}
						}
					}
					this.head = next;
				}
				// for jvm's gc
				tmp.set_next(null);
				return tmp;
			}

		}

	}

	// track the chain of transformation
	private java.util.concurrent.atomic.AtomicInteger ended = new AtomicInteger(0);

	// kick false sharing
	//@sun.misc.Contended
	// @SuppressWarnings("unused")
	private volatile Node<T> head = null;

	// kick false sharing
	//@sun.misc.Contended
	// @SuppressWarnings("unused")
	private volatile Node<T> tail = null;

	/**
	 * this method may be invoked in multiple threads concurrently.
	 * 
	 * @param value
	 * @return if accepted and can be reaped, true is returned, otherwise false.
	 */
	@SuppressWarnings("unchecked")
	public boolean add(T value) {

		if (ended.get() != 0)
			return false;

		Node<T> new_tail = new Node<>(value);
		Node<T> old_tail = tail_updater.getAndSet(this, new_tail);
		if (null != old_tail) {
			// if reaped right now, then do not set_next(...)
			old_tail.set_next(null, new_tail);
		} else {
			head_updater.set(this, new_tail);
		}

		if (ended.get() == 0) {
			return true;

		} else if (ended.get() == 1) {
			return true;

		}

		// if ended is 2, then wait for 3.
		int i = 0;
		while (ended.get() != 3) {
			if (i < 1024) {
				i++;
			} else {
				try {
					Thread.sleep(System.currentTimeMillis() % 10);
				} catch (Throwable throwable) {
					// omitted
				}
			}
		}

		int j = 0;
		while (true) {

			if (tail_updater.get(this) == new_tail) {
				// it will not be reaped out.
				tail_updater.compareAndSet(this, new_tail, old_tail);
				return false;
			} else if (tail_updater.get(this) == null) {
				// it's reaped out already.
				return true;
			}

			if (j < 1024) {
				j++;
			} else {
				try {
					Thread.sleep(System.currentTimeMillis() % 10);
				} catch (Throwable throwable) {
					// omitted
				}
			}
		}

	}

	@SuppressWarnings("unchecked")
	/**
	 * DO NOT invoke 'reap(...)' and /or 'end()' in multiple threads at the same
	 * time.
	 * 
	 * @param is_last_reap
	 * @return
	 */
	public Reaped<T> reap(final boolean is_last_reap) throws ReapException {

		// do not re-order for readability
		if (is_last_reap) {
			if (!ended.compareAndSet(1, 2)) {
				// already ended and reaped.
				// return null;
				throw new ReapException("already ended and reaped");
			}
		}

		Node<T> old_head = head_updater.get(this);
		if (old_head != null) {

			Node<T> old_tail = tail_updater.getAndSet(this, null);
			if (old_tail == null) {
				// it can not happen!!!
				// if happened, this class is coded uncorrectly.
				throw new Error("supposed to be not here!!!");
			}
			// for jvm's gc.
			old_tail.set_next(old_tail);

			head_updater.compareAndSet(this, old_head, null);

			if (is_last_reap)
				ended.compareAndSet(2, 3);

			return new Reaped<>(old_head, old_tail);

		} else {

			return null;
		}

	}

	/**
	 * DO NOT invoke 'reap(...)' and /or 'end()' in multiple threads at the same
	 * time.
	 */
	public void end() {
		ended.compareAndSet(0, 1);
		// DO NOT reap here!!!
	}

}