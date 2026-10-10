# CPU profile

8227 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 52.7 | 4339 | `proteus I/O-*` |
| 17.7 | 1456 | `proteus task-*` |
| 16.2 | 1330 | `(unnamed virtual thread)` |
| 11.3 | 933 | ` ForkJoinPool-1-worker-*` |
| 2.0 | 168 | `ForkJoinPool-1-delayScheduler` |
| 0.0 | 4 | `VirtualThread-unblocker` |
| 0.0 | 3 | `JFR Periodic Tasks` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 15.1 | 1239 | `sun.nio.ch.IOUtil.configureBlocking` |
| 10.0 | 823 | `java.util.TreeMap.put` |
| 4.9 | 403 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.8 | 313 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 3.5 | 286 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 3.4 | 282 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 3.4 | 281 | `java.util.concurrent.locks.LockSupport.unpark` |
| 2.5 | 204 | `java.util.TreeMap.getEntry` |
| 1.8 | 144 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.6 | 135 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.6 | 131 | `java.util.TreeMap.successor` |
| 1.6 | 129 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.6 | 128 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 1.4 | 114 | `java.util.TreeMap.getEntryUsingComparator` |
| 1.3 | 110 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 1.3 | 103 | `java.util.ArrayDeque.addLast` |
| 1.2 | 102 | `org.jboss.threads.EnhancedQueueExecutor.decreaseQueueSize` |
| 1.2 | 101 | `java.util.concurrent.ForkJoinPool$WorkQueue.push` |
| 1.1 | 93 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.1 | 90 | `java.lang.VirtualThread.parkNanos` |
| 1.1 | 90 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.1 | 89 | `java.util.HashMap.getNode` |
| 1.1 | 87 | `org.xnio.Buffers.copy` |
| 1.0 | 83 | `java.util.TreeMap.fixAfterDeletion` |
| 0.9 | 78 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 15.1 | 1239 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 15.1 | 1239 | `sun.nio.ch.IOUtil.configureBlocking` |
| 12.0 | 990 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 12.0 | 988 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 11.9 | 981 | `org.xnio.nio.WorkerThread.executeAfter` |
| 11.9 | 976 | `org.xnio.nio.NioSocketConduit.read` |
| 11.9 | 976 | `sun.nio.ch.SocketChannelImpl.read` |
| 10.6 | 870 | `java.util.TreeMap.put` |
| 10.2 | 843 | `java.util.TreeSet.add` |
| 9.5 | 778 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 8.5 | 699 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 7.7 | 633 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 7.3 | 601 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 6.5 | 534 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 6.5 | 533 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 6.2 | 513 | `java.util.TreeMap.remove` |
| 6.2 | 513 | `java.util.TreeSet.remove` |
| 6.2 | 512 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 5.4 | 448 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 5.4 | 442 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.3 | 435 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.2 | 426 | `io.undertow.util.HttpString.equals` |
| 5.2 | 426 | `io.undertow.util.HttpString.bytesAreEqual` |
| 5.1 | 420 | `org.xnio.nio.WorkerThread.run` |
| 4.4 | 366 | `java.util.HashMap.getNode` |

