# CPU profile

7617 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 46.5 | 3539 | `proteus I/O-*` |
| 35.8 | 2729 | `(unnamed virtual thread)` |
| 17.2 | 1310 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 27 | `ForkJoinPool-1-delayScheduler` |
| 0.3 | 24 | `main` |
| 0.0 | 1 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.8 | 1131 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 10.1 | 766 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 9.4 | 713 | `java.util.IdentityHashMap.get` |
| 8.8 | 667 | `java.util.TreeMap.put` |
| 4.4 | 338 | `sun.nio.ch.IOUtil.configureBlocking` |
| 3.0 | 232 | `io.undertow.util.HeaderMap.getEntry` |
| 2.4 | 185 | `java.util.TreeMap.remove` |
| 2.0 | 153 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.8 | 138 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.7 | 131 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.6 | 125 | `java.util.concurrent.ConcurrentHashMap.replaceNode` |
| 1.6 | 120 | `java.util.TreeMap.getEntry` |
| 1.6 | 119 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.4 | 108 | `java.util.TreeMap.successor` |
| 1.4 | 103 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.3 | 101 | `java.util.concurrent.ConcurrentHashMap$KeySetView.add` |
| 1.2 | 93 | `io.undertow.server.handlers.GracefulShutdownHandler.handleRequest` |
| 1.2 | 90 | `java.lang.Thread.clearReferences` |
| 1.1 | 87 | `java.lang.VirtualThread.mount` |
| 1.1 | 80 | `io.undertow.util.HttpString.calcHashCode` |
| 1.0 | 75 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.9 | 69 | `java.util.concurrent.ConcurrentHashMap.putVal` |
| 0.9 | 65 | `jdk.internal.vm.Continuation.run` |
| 0.9 | 65 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 0.8 | 63 | `org.xnio.ChannelListeners.invokeChannelListener` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 19.9 | 1517 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 18.0 | 1371 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 14.8 | 1131 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 13.7 | 1045 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 13.2 | 1005 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 12.5 | 951 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 11.1 | 847 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 10.9 | 833 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 10.8 | 820 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 10.4 | 789 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 10.2 | 778 | `org.xnio.nio.WorkerThread.executeAfter` |
| 9.4 | 714 | `java.util.IdentityHashMap.get` |
| 9.3 | 708 | `java.util.TreeSet.add` |
| 9.3 | 708 | `java.util.TreeMap.put` |
| 8.5 | 649 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 8.5 | 648 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 7.1 | 537 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 6.1 | 468 | `java.util.TreeSet.remove` |
| 6.1 | 468 | `java.util.TreeMap.remove` |
| 6.1 | 462 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 4.5 | 342 | `java.util.concurrent.ForkJoinPool$WorkQueue.push` |
| 4.4 | 338 | `sun.nio.ch.IOUtil.configureBlocking` |
| 4.4 | 338 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 4.3 | 326 | `java.lang.VirtualThread.submit` |
| 4.3 | 326 | `java.util.concurrent.ForkJoinPool.poolSubmit` |

