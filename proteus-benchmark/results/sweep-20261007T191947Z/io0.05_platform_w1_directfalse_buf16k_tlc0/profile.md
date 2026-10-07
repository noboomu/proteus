# CPU profile

67963 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 66.8 | 45413 | `proteus I/O-*` |
| 13.4 | 9099 | ` ForkJoinPool-1-worker-*` |
| 11.0 | 7494 | `proteus task-*` |
| 8.7 | 5937 | `(unnamed virtual thread)` |
| 0.0 | 25 | `main` |
| 0.0 | 6 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 12.2 | 8325 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 6.8 | 4621 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 4.9 | 3341 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 4.5 | 3043 | `java.util.concurrent.locks.LockSupport.unpark` |
| 4.4 | 2974 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 4.2 | 2859 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.0 | 2696 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 3.5 | 2407 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 3.4 | 2330 | `java.util.TreeMap.put` |
| 3.2 | 2162 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 2.9 | 1979 | `java.util.Collections$SynchronizedMap.get` |
| 2.6 | 1796 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.4 | 1663 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 2.3 | 1556 | `java.util.WeakHashMap.hash` |
| 2.2 | 1490 | `sun.nio.ch.SocketDispatcher.read` |
| 1.5 | 1051 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.5 | 1027 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.4 | 981 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.4 | 918 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 1.2 | 815 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors` |
| 1.2 | 794 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.2 | 793 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.1 | 718 | `java.util.HashMap.getNode` |
| 0.9 | 638 | `java.util.TreeMap.remove` |
| 0.9 | 595 | `java.nio.ByteBuffer.putBuffer` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 17.5 | 11892 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 16.0 | 10890 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 15.9 | 10801 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 15.7 | 10653 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 14.8 | 10034 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 12.8 | 8693 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 12.7 | 8649 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 12.2 | 8325 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 10.2 | 6945 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 7.9 | 5337 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 7.2 | 4890 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 7.2 | 4867 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 7.2 | 4866 | `java.util.concurrent.ForkJoinTask.doExec` |
| 7.0 | 4741 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 6.8 | 4631 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 6.7 | 4538 | `tools.jackson.core.util.RecyclerPool$ConcurrentDequePoolBase.acquirePooled` |
| 6.7 | 4538 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 6.7 | 4538 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |
| 6.1 | 4166 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 6.0 | 4088 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 5.6 | 3781 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 5.4 | 3690 | `java.util.Collections$SynchronizedMap.get` |
| 5.1 | 3481 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 4.5 | 3076 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 4.5 | 3067 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |

