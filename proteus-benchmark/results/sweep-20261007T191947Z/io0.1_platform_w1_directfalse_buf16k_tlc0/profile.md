# CPU profile

70713 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 71.1 | 50285 | `proteus I/O-*` |
| 13.1 | 9235 | ` ForkJoinPool-1-worker-*` |
| 8.1 | 5720 | `(unnamed virtual thread)` |
| 7.7 | 5447 | `proteus task-*` |
| 0.0 | 26 | `main` |
| 0.0 | 6 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.2 | 10019 | `java.util.concurrent.locks.LockSupport.unpark` |
| 7.9 | 5565 | `java.util.IdentityHashMap.get` |
| 5.3 | 3721 | `java.util.WeakHashMap.hash` |
| 5.0 | 3519 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 4.6 | 3229 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 4.4 | 3110 | `java.util.Collections$SynchronizedMap.get` |
| 4.0 | 2838 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 3.7 | 2606 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 3.7 | 2604 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 2.8 | 1946 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 2.6 | 1871 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedPredecessors` |
| 2.5 | 1776 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 2.5 | 1750 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.1 | 1512 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors` |
| 2.0 | 1436 | `java.util.TreeMap.put` |
| 1.9 | 1362 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.9 | 1356 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.9 | 1349 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.7 | 1197 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 1.3 | 904 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 1.0 | 683 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.9 | 605 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.8 | 584 | `sun.nio.ch.IOUtil.configureBlocking` |
| 0.8 | 581 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 0.8 | 580 | `java.nio.ByteBuffer.putBuffer` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 15.5 | 10992 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 15.5 | 10989 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 15.5 | 10987 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 14.2 | 10019 | `java.util.concurrent.locks.LockSupport.unpark` |
| 14.2 | 10019 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 14.1 | 9972 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.8 | 9073 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 12.4 | 8791 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 12.4 | 8747 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 10.3 | 7301 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 10.2 | 7233 | `tools.jackson.core.util.RecyclerPool$ConcurrentDequePoolBase.acquirePooled` |
| 10.2 | 7232 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 10.2 | 7228 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |
| 10.0 | 7067 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 9.9 | 7021 | `java.util.Collections$SynchronizedMap.get` |
| 9.9 | 6985 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 9.7 | 6831 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 7.9 | 5565 | `java.util.IdentityHashMap.get` |
| 7.7 | 5478 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 7.6 | 5352 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 7.1 | 4996 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 7.0 | 4975 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 6.5 | 4626 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 5.5 | 3911 | `java.util.WeakHashMap.get` |
| 5.4 | 3804 | `tools.jackson.core.TokenStreamFactory._getBufferRecycler` |

