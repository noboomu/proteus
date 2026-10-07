# CPU profile

74009 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 67.7 | 50071 | `proteus I/O-*` |
| 12.5 | 9249 | `proteus task-*` |
| 12.3 | 9109 | ` ForkJoinPool-1-worker-*` |
| 7.5 | 5570 | `(unnamed virtual thread)` |
| 0.0 | 23 | `main` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 11.6 | 8585 | `java.util.concurrent.locks.LockSupport.unpark` |
| 9.4 | 6990 | `java.util.IdentityHashMap.get` |
| 5.8 | 4308 | `java.util.WeakHashMap.hash` |
| 5.5 | 4082 | `java.lang.Thread.interrupted` |
| 4.7 | 3505 | `java.util.Collections$SynchronizedMap.get` |
| 4.4 | 3265 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 4.1 | 3013 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 3.7 | 2751 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 3.5 | 2584 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 2.5 | 1844 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 2.4 | 1772 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 2.3 | 1701 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors` |
| 2.3 | 1700 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.2 | 1606 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedPredecessors` |
| 2.1 | 1525 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.0 | 1479 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.9 | 1381 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 1.7 | 1226 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 1.6 | 1207 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 1.6 | 1199 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.6 | 1155 | `java.util.TreeMap.put` |
| 1.3 | 958 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 1.1 | 847 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.8 | 621 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.7 | 554 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 13.7 | 10149 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 12.5 | 9244 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.4 | 9195 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 11.7 | 8695 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 11.7 | 8653 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 11.6 | 8599 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 11.6 | 8597 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 11.6 | 8596 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 11.6 | 8589 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 11.6 | 8585 | `java.util.concurrent.locks.LockSupport.unpark` |
| 11.6 | 8585 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 11.2 | 8317 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 10.8 | 7996 | `java.util.Collections$SynchronizedMap.get` |
| 9.9 | 7311 | `tools.jackson.core.util.RecyclerPool$ConcurrentDequePoolBase.acquirePooled` |
| 9.9 | 7311 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |
| 9.9 | 7311 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 9.4 | 6991 | `java.util.IdentityHashMap.get` |
| 9.3 | 6919 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 9.3 | 6915 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 9.3 | 6913 | `java.lang.Thread.run` |
| 7.9 | 5869 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 7.9 | 5856 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 7.9 | 5841 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 7.9 | 5834 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.4 | 4749 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |

