# CPU profile

66650 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 74.2 | 49473 | `proteus I/O-*` |
| 15.7 | 10435 | ` ForkJoinPool-1-worker-*` |
| 7.4 | 4959 | `(unnamed virtual thread)` |
| 2.6 | 1765 | `proteus task-*` |
| 0.0 | 24 | `main` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 24.2 | 16130 | `java.util.concurrent.locks.LockSupport.unpark` |
| 6.4 | 4258 | `java.util.Collections$SynchronizedMap.get` |
| 6.3 | 4197 | `java.util.WeakHashMap.hash` |
| 6.0 | 3970 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 5.1 | 3405 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 4.7 | 3102 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 2.5 | 1686 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.3 | 1540 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 2.2 | 1480 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 2.1 | 1389 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 2.1 | 1376 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.8 | 1201 | `java.util.IdentityHashMap.get` |
| 1.4 | 913 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 1.3 | 889 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.3 | 838 | `java.lang.Thread.interrupted` |
| 1.1 | 750 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 1.1 | 744 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.1 | 725 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 1.1 | 717 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 1.1 | 709 | `java.util.concurrent.atomic.AtomicLongFieldUpdater.updateAndGet` |
| 1.1 | 705 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 0.9 | 613 | `java.nio.ByteBuffer.putBuffer` |
| 0.8 | 540 | `java.util.HashMap.getNode` |
| 0.8 | 537 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 0.8 | 534 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 24.2 | 16152 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 24.2 | 16151 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 24.2 | 16150 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 24.2 | 16130 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 24.2 | 16130 | `java.util.concurrent.locks.LockSupport.unpark` |
| 16.5 | 10979 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 15.4 | 10280 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 15.2 | 10113 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 15.1 | 10063 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 13.4 | 8904 | `java.util.Collections$SynchronizedMap.get` |
| 11.4 | 7578 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 10.2 | 6782 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 10.1 | 6764 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 9.6 | 6401 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 7.8 | 5224 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 7.7 | 5147 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.0 | 4656 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 7.0 | 4646 | `java.util.WeakHashMap.get` |
| 6.3 | 4205 | `java.util.WeakHashMap.hash` |
| 6.2 | 4158 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.0 | 3970 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 5.5 | 3675 | `java.util.concurrent.ForkJoinTask.doExec` |
| 5.5 | 3675 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 4.7 | 3161 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 4.7 | 3102 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |

