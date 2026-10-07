# CPU profile

39128 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 51.0 | 19944 | `proteus I/O-*` |
| 18.7 | 7316 | `proteus task-*` |
| 14.8 | 5800 | `(unnamed virtual thread)` |
| 14.0 | 5468 | ` ForkJoinPool-1-worker-*` |
| 0.9 | 346 | `Reference Handler` |
| 0.6 | 240 | `Finalizer` |
| 0.1 | 21 | `main` |
| 0.0 | 4 | `VirtualThread-unblocker` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 6.8 | 2645 | `java.util.WeakHashMap.get` |
| 6.4 | 2500 | `java.util.WeakHashMap.hash` |
| 5.7 | 2231 | `java.util.TreeMap.put` |
| 5.5 | 2153 | `java.util.Collections$SynchronizedMap.get` |
| 4.8 | 1895 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.1 | 1619 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 4.0 | 1579 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.2 | 1250 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 2.6 | 1033 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.6 | 1010 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 2.1 | 840 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 2.1 | 825 | `java.nio.Bits.setMemory` |
| 2.1 | 813 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 2.0 | 776 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 1.8 | 721 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.8 | 694 | `java.lang.Object.<init>` |
| 1.7 | 678 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 1.6 | 607 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 1.3 | 507 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 1.2 | 459 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.2 | 450 | `java.util.TreeMap.remove` |
| 1.1 | 425 | `java.util.TreeMap.successor` |
| 1.1 | 424 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.1 | 414 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.1 | 411 | `java.util.concurrent.atomic.AtomicLongFieldUpdater.updateAndGet` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 18.9 | 7390 | `java.util.Collections$SynchronizedMap.get` |
| 18.7 | 7312 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 18.4 | 7213 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 13.7 | 5346 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 13.4 | 5262 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 13.4 | 5238 | `java.util.WeakHashMap.get` |
| 13.3 | 5210 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 10.8 | 4222 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 9.2 | 3583 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 8.9 | 3481 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 7.3 | 2866 | `org.xnio.nio.WorkerThread.executeAfter` |
| 7.3 | 2857 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 6.8 | 2670 | `java.util.TreeSet.add` |
| 6.4 | 2507 | `java.util.WeakHashMap.hash` |
| 6.1 | 2379 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 6.0 | 2355 | `java.util.TreeMap.put` |
| 5.5 | 2143 | `io.undertow.io.UndertowOutputStream.buffer` |
| 5.4 | 2128 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.4 | 2126 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 5.0 | 1964 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.9 | 1929 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.8 | 1885 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 4.6 | 1796 | `io.undertow.server.Connectors.executeRootHandler` |
| 4.1 | 1606 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 4.1 | 1601 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |

