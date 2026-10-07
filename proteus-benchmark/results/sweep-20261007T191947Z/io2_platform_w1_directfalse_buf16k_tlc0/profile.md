# CPU profile

70844 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 76.6 | 54287 | `proteus I/O-*` |
| 13.1 | 9259 | ` ForkJoinPool-1-worker-*` |
| 7.7 | 5477 | `(unnamed virtual thread)` |
| 2.6 | 1807 | `proteus task-*` |
| 0.0 | 21 | `main` |
| 0.0 | 3 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 22.7 | 16105 | `java.util.concurrent.locks.LockSupport.unpark` |
| 6.9 | 4855 | `java.util.Collections$SynchronizedMap.get` |
| 6.8 | 4811 | `java.util.IdentityHashMap.get` |
| 6.4 | 4549 | `java.util.WeakHashMap.hash` |
| 5.2 | 3661 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.2 | 2954 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 3.2 | 2268 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 3.0 | 2145 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 2.3 | 1635 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 2.2 | 1564 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 2.2 | 1563 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 2.1 | 1461 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.7 | 1200 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 1.3 | 918 | `java.util.HashMap.getNode` |
| 1.3 | 910 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 1.2 | 869 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 1.2 | 836 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 1.0 | 726 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 719 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.9 | 665 | `io.undertow.server.handlers.GracefulShutdownHandler.handleRequest` |
| 0.9 | 633 | `org.xnio.Buffers.copy` |
| 0.8 | 590 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.8 | 546 | `sun.nio.ch.SocketDispatcher.read` |
| 0.7 | 515 | `org.jboss.threads.EnhancedQueueExecutor.decreaseQueueSize` |
| 0.7 | 513 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 23.0 | 16265 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 23.0 | 16263 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 22.9 | 16246 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 22.7 | 16105 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 22.7 | 16105 | `java.util.concurrent.locks.LockSupport.unpark` |
| 17.2 | 12185 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 16.0 | 11320 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 15.1 | 10720 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.7 | 9685 | `java.util.Collections$SynchronizedMap.get` |
| 12.6 | 8920 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 12.5 | 8869 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 10.8 | 7639 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 10.6 | 7478 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 10.2 | 7224 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 9.9 | 7013 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.8 | 4830 | `java.util.WeakHashMap.get` |
| 6.8 | 4811 | `java.util.IdentityHashMap.get` |
| 6.4 | 4553 | `java.util.WeakHashMap.hash` |
| 6.4 | 4517 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 6.4 | 4501 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 5.3 | 3724 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 5.1 | 3625 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 4.5 | 3173 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.5 | 3157 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 4.3 | 3068 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |

