# CPU profile

70885 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 57.6 | 40836 | `proteus I/O-*` |
| 24.9 | 17639 | ` ForkJoinPool-1-worker-*` |
| 17.4 | 12357 | `(unnamed virtual thread)` |
| 0.1 | 37 | `VirtualThread-unblocker` |
| 0.0 | 29 | `main` |
| 0.0 | 3 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 10.5 | 7439 | `java.util.IdentityHashMap.get` |
| 8.6 | 6119 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 7.7 | 5462 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 6.9 | 4858 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 5.2 | 3692 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 3.9 | 2769 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 3.5 | 2507 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 3.0 | 2113 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.9 | 2091 | `java.util.TreeMap.put` |
| 2.6 | 1857 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.1 | 1520 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.1 | 1486 | `java.util.WeakHashMap.hash` |
| 2.0 | 1405 | `java.util.Collections$SynchronizedMap.get` |
| 1.7 | 1237 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 1.6 | 1163 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors` |
| 1.5 | 1079 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 1.5 | 1029 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 1.4 | 967 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 1.3 | 922 | `sun.nio.ch.SocketDispatcher.writev` |
| 1.2 | 882 | `java.util.HashMap.getNode` |
| 1.2 | 872 | `java.nio.ByteBuffer.putBuffer` |
| 1.2 | 856 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.1 | 752 | `sun.nio.ch.SocketDispatcher.read` |
| 1.0 | 734 | `io.undertow.server.handlers.GracefulShutdownHandler.handleRequest` |
| 1.0 | 694 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 23.9 | 16960 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 23.8 | 16854 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 14.8 | 10517 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.1 | 10014 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 14.0 | 9953 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 10.5 | 7439 | `java.util.IdentityHashMap.get` |
| 10.1 | 7149 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 9.7 | 6870 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 9.6 | 6840 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 9.1 | 6475 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 8.6 | 6119 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 8.4 | 5989 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 8.4 | 5989 | `java.util.concurrent.ForkJoinTask.doExec` |
| 7.9 | 5594 | `tools.jackson.core.util.RecyclerPool$ConcurrentDequePoolBase.acquirePooled` |
| 7.9 | 5594 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |
| 7.9 | 5592 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 7.7 | 5471 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 5.9 | 4175 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 5.7 | 4035 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 5.6 | 3948 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 5.3 | 3738 | `tools.jackson.core.TokenStreamFactory._getBufferRecycler` |
| 4.7 | 3339 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 4.4 | 3108 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.3 | 3075 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 4.1 | 2897 | `java.util.Collections$SynchronizedMap.get` |

