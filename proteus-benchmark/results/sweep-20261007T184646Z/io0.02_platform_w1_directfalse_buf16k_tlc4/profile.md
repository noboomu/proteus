# CPU profile

41447 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 53.7 | 22268 | `proteus I/O-*` |
| 17.4 | 7215 | `proteus task-*` |
| 14.4 | 5968 | ` ForkJoinPool-1-worker-*` |
| 12.5 | 5181 | `(unnamed virtual thread)` |
| 1.2 | 487 | `Reference Handler` |
| 0.7 | 303 | `Finalizer` |
| 0.1 | 26 | `main` |
| 0.0 | 8 | `VirtualThread-unblocker` |
| 0.0 | 5 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 8.6 | 3578 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 7.1 | 2934 | `java.util.WeakHashMap.hash` |
| 6.0 | 2499 | `java.util.TreeMap.put` |
| 4.8 | 1975 | `java.lang.ref.ReferenceQueue.poll` |
| 4.7 | 1954 | `java.util.WeakHashMap.get` |
| 4.0 | 1645 | `java.util.concurrent.locks.LockSupport.unpark` |
| 3.5 | 1446 | `java.util.Collections$SynchronizedMap.get` |
| 3.0 | 1254 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.0 | 1239 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 2.3 | 934 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 2.2 | 931 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.2 | 892 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.9 | 806 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.5 | 636 | `java.nio.ByteBuffer.allocate` |
| 1.5 | 633 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 1.5 | 612 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.4 | 595 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 1.3 | 554 | `java.util.TreeMap.remove` |
| 1.3 | 541 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.3 | 541 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.3 | 538 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.2 | 485 | `java.util.TreeMap.getEntry` |
| 1.2 | 483 | `java.util.TreeMap.successor` |
| 1.1 | 454 | `sun.nio.ch.SocketDispatcher.writev` |
| 1.1 | 443 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 19.5 | 8086 | `java.util.Collections$SynchronizedMap.get` |
| 16.0 | 6643 | `java.util.WeakHashMap.get` |
| 15.0 | 6206 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 13.8 | 5731 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 13.7 | 5674 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 13.3 | 5515 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 9.0 | 3724 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 8.9 | 3688 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 7.9 | 3279 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 7.9 | 3272 | `org.xnio.nio.WorkerThread.executeAfter` |
| 7.4 | 3080 | `java.util.TreeSet.add` |
| 7.1 | 2941 | `java.util.WeakHashMap.hash` |
| 7.1 | 2928 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 6.8 | 2822 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.4 | 2650 | `java.util.TreeMap.put` |
| 5.3 | 2208 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.0 | 2064 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.8 | 1990 | `java.util.WeakHashMap.expungeStaleEntries` |
| 4.8 | 1990 | `java.util.WeakHashMap.getTable` |
| 4.8 | 1978 | `java.lang.ref.ReferenceQueue.poll` |
| 4.7 | 1947 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 4.5 | 1848 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 4.4 | 1805 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 4.4 | 1803 | `java.util.TreeMap.remove` |
| 4.4 | 1803 | `java.util.TreeSet.remove` |

