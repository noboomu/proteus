# CPU profile

70288 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 64.5 | 45335 | `proteus I/O-*` |
| 14.4 | 10124 | `proteus task-*` |
| 12.5 | 8776 | ` ForkJoinPool-1-worker-*` |
| 8.6 | 6040 | `(unnamed virtual thread)` |
| 0.0 | 21 | `main` |
| 0.0 | 3 | `VirtualThread-unblocker` |
| 0.0 | 1 | `Reference Handler` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 10.6 | 7426 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 5.3 | 3735 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 4.9 | 3438 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 4.7 | 3326 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 3.8 | 2684 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 3.7 | 2587 | `java.util.TreeMap.put` |
| 3.5 | 2463 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 3.4 | 2380 | `io.undertow.util.HeaderMap.getEntry` |
| 3.3 | 2349 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 3.3 | 2316 | `java.util.concurrent.locks.LockSupport.unpark` |
| 2.9 | 2039 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors` |
| 2.8 | 2003 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 2.6 | 1815 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.4 | 1696 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.4 | 1652 | `java.util.Collections$SynchronizedMap.get` |
| 2.0 | 1439 | `java.util.WeakHashMap.hash` |
| 1.7 | 1182 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.6 | 1152 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedPredecessors` |
| 1.6 | 1124 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 1.5 | 1031 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.3 | 937 | `tools.jackson.core.json.JsonWriteContext.writeValue` |
| 1.3 | 879 | `java.util.IdentityHashMap.get` |
| 1.2 | 851 | `io.undertow.server.protocol.http2.Http2UpgradeHandler.handleRequest` |
| 1.1 | 785 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 728 | `java.util.TreeMap.remove` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 17.9 | 12581 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.8 | 9682 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 13.7 | 9622 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 13.6 | 9591 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.6 | 8851 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 12.1 | 8471 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 11.9 | 8368 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 11.8 | 8326 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 10.7 | 7526 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |
| 10.7 | 7526 | `tools.jackson.core.util.RecyclerPool$ConcurrentDequePoolBase.acquirePooled` |
| 10.7 | 7526 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 10.6 | 7426 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 7.7 | 5395 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 6.3 | 4419 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 6.0 | 4198 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 5.9 | 4181 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 5.9 | 4144 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 5.7 | 4006 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 5.7 | 3996 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 5.7 | 3996 | `java.util.concurrent.ForkJoinTask.doExec` |
| 5.5 | 3895 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 5.4 | 3797 | `tools.jackson.core.TokenStreamFactory._getBufferRecycler` |
| 5.3 | 3721 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 4.4 | 3113 | `java.util.Collections$SynchronizedMap.get` |
| 4.2 | 2982 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |

