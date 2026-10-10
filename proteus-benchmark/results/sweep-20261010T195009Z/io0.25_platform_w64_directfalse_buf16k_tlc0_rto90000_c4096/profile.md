# CPU profile

8551 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 50.6 | 4329 | `proteus I/O-*` |
| 17.9 | 1527 | `(unnamed virtual thread)` |
| 16.9 | 1442 | `proteus task-*` |
| 12.6 | 1074 | ` ForkJoinPool-1-worker-*` |
| 2.2 | 184 | `ForkJoinPool-1-delayScheduler` |
| 0.0 | 2 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.4 | 1231 | `sun.nio.ch.IOUtil.configureBlocking` |
| 8.7 | 740 | `java.util.TreeMap.put` |
| 5.4 | 466 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.4 | 377 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 3.7 | 319 | `java.util.concurrent.locks.LockSupport.unpark` |
| 3.7 | 317 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 3.4 | 292 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.8 | 237 | `java.util.TreeMap.remove` |
| 2.0 | 168 | `io.undertow.util.HeaderMap.getEntry` |
| 1.8 | 150 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 1.7 | 148 | `java.util.TreeMap.getEntry` |
| 1.7 | 144 | `java.util.TreeMap.successor` |
| 1.6 | 140 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.6 | 133 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 1.5 | 126 | `java.util.concurrent.ForkJoinPool$WorkQueue.push` |
| 1.4 | 123 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.3 | 115 | `org.xnio.Buffers.copy` |
| 1.3 | 110 | `java.lang.VirtualThread.parkNanos` |
| 1.2 | 104 | `io.undertow.util.HeaderValues.addAll` |
| 1.2 | 102 | `java.util.concurrent.DelayScheduler.loop` |
| 1.1 | 90 | `io.undertow.util.HttpString.calcHashCode` |
| 1.0 | 82 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 0.9 | 81 | `java.util.TreeMap.getEntryUsingComparator` |
| 0.9 | 80 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 0.9 | 77 | `java.util.TreeMap.fixAfterDeletion` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 14.4 | 1231 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 14.4 | 1231 | `sun.nio.ch.IOUtil.configureBlocking` |
| 11.2 | 957 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 11.0 | 942 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 11.0 | 942 | `org.xnio.nio.NioSocketConduit.read` |
| 11.0 | 940 | `sun.nio.ch.SocketChannelImpl.read` |
| 11.0 | 939 | `org.xnio.nio.WorkerThread.executeAfter` |
| 9.4 | 800 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 9.3 | 798 | `java.util.TreeSet.add` |
| 9.2 | 785 | `java.util.TreeMap.put` |
| 8.9 | 762 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 8.5 | 727 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 8.0 | 683 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 7.9 | 673 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 7.2 | 619 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 7.2 | 617 | `java.util.TreeSet.remove` |
| 7.2 | 617 | `java.util.TreeMap.remove` |
| 6.8 | 585 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 6.2 | 530 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.1 | 523 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.9 | 502 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.0 | 425 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.cleanup` |
| 4.2 | 356 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.9 | 330 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 3.8 | 329 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |

