# CPU profile

58600 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 64.9 | 38030 | `proteus I/O-*` |
| 16.0 | 9396 | ` ForkJoinPool-1-worker-*` |
| 9.8 | 5771 | `proteus task-*` |
| 9.2 | 5374 | `(unnamed virtual thread)` |
| 0.0 | 26 | `main` |
| 0.0 | 10 | `VirtualThread-unblocker` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `SIGTERM handler` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 12.1 | 7096 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 8.3 | 4858 | `java.util.concurrent.locks.LockSupport.unpark` |
| 6.4 | 3773 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 5.1 | 2986 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 5.0 | 2919 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.1 | 2384 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 3.8 | 2251 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.8 | 2243 | `io.undertow.util.HttpString.bytesAreEqual` |
| 3.6 | 2131 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 3.6 | 2109 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 3.1 | 1821 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 2.7 | 1554 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.6 | 1552 | `java.util.Collections$SynchronizedMap.get` |
| 2.3 | 1354 | `java.util.WeakHashMap.hash` |
| 1.8 | 1028 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.6 | 956 | `tools.jackson.core.json.JsonWriteContext.writeValue` |
| 1.6 | 920 | `java.nio.ByteBuffer.putBuffer` |
| 1.5 | 901 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 1.5 | 854 | `java.util.HashMap.getNode` |
| 1.2 | 699 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.2 | 688 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.2 | 688 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.1 | 617 | `io.undertow.util.HttpString.calcHashCode` |
| 1.0 | 599 | `sun.nio.ch.SocketDispatcher.read` |
| 1.0 | 575 | `sun.nio.ch.IOUtil.configureBlocking` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 17.0 | 9979 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 17.0 | 9954 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 16.9 | 9924 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 15.3 | 8992 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 15.3 | 8976 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 15.2 | 8931 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 14.6 | 8544 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.1 | 7096 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 8.4 | 4899 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 8.3 | 4888 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 8.3 | 4882 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 8.3 | 4872 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 8.3 | 4858 | `java.util.concurrent.locks.LockSupport.unpark` |
| 8.3 | 4858 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 7.8 | 4581 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 7.3 | 4258 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 7.0 | 4111 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 6.9 | 4071 | `java.util.concurrent.ForkJoinTask.doExec` |
| 6.9 | 4071 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 6.7 | 3944 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 6.4 | 3724 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 6.1 | 3593 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 6.1 | 3592 | `tools.jackson.core.util.RecyclerPool$ConcurrentDequePoolBase.acquirePooled` |
| 6.1 | 3590 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |
| 5.4 | 3156 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |

