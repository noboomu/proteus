# CPU profile

68557 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 57.3 | 39262 | `proteus I/O-*` |
| 28.1 | 19261 | ` ForkJoinPool-1-worker-*` |
| 14.6 | 10015 | `(unnamed virtual thread)` |
| 0.0 | 27 | `main` |
| 0.0 | 9 | `VirtualThread-unblocker` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 12.4 | 8505 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 8.7 | 5950 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 8.2 | 5651 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 5.7 | 3935 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 5.4 | 3715 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 3.9 | 2698 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 3.8 | 2633 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.3 | 2238 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 3.3 | 2229 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 3.1 | 2154 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 3.0 | 2038 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.5 | 1686 | `java.util.WeakHashMap.hash` |
| 2.4 | 1643 | `java.util.Collections$SynchronizedMap.get` |
| 2.0 | 1386 | `sun.nio.ch.SocketDispatcher.writev` |
| 1.5 | 998 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 1.4 | 993 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.3 | 918 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.3 | 894 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 1.3 | 876 | `java.util.concurrent.atomic.AtomicLongFieldUpdater.updateAndGet` |
| 1.3 | 860 | `sun.nio.ch.SocketDispatcher.read` |
| 1.0 | 714 | `java.util.HashMap.getNode` |
| 1.0 | 659 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.8 | 577 | `java.nio.ByteBuffer.putBuffer` |
| 0.8 | 574 | `java.util.concurrent.ConcurrentLinkedDeque.updateTail` |
| 0.8 | 555 | `sun.nio.ch.IOUtil.configureBlocking` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 27.2 | 18617 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 27.0 | 18478 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 17.6 | 12097 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 17.5 | 11983 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 17.4 | 11923 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 17.3 | 11870 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 15.8 | 10866 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 12.4 | 8505 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 10.5 | 7210 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 10.4 | 7134 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 8.4 | 5756 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 7.0 | 4779 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 6.8 | 4696 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 6.7 | 4586 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 6.5 | 4490 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 6.5 | 4490 | `java.util.concurrent.ForkJoinTask.doExec` |
| 6.0 | 4083 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 5.5 | 3794 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 5.3 | 3634 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 5.2 | 3552 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 5.2 | 3552 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |
| 5.2 | 3552 | `tools.jackson.core.util.RecyclerPool$ConcurrentDequePoolBase.acquirePooled` |
| 4.9 | 3349 | `java.util.Collections$SynchronizedMap.get` |
| 4.4 | 3011 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 4.0 | 2726 | `sun.nio.ch.SocketChannelImpl.implWrite` |

