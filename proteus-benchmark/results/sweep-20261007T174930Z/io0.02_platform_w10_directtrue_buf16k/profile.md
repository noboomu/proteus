# CPU profile

40769 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 54.8 | 22351 | `proteus I/O-*` |
| 16.5 | 6739 | `proteus task-*` |
| 14.3 | 5848 | `(unnamed virtual thread)` |
| 12.8 | 5205 | ` ForkJoinPool-1-worker-*` |
| 0.9 | 378 | `Reference Handler` |
| 0.6 | 226 | `Finalizer` |
| 0.1 | 24 | `main` |
| 0.0 | 7 | `VirtualThread-unblocker` |
| 0.0 | 3 | `JFR Periodic Tasks` |
| 0.0 | 2 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 9.6 | 3929 | `java.util.WeakHashMap.get` |
| 7.9 | 3231 | `java.util.WeakHashMap.hash` |
| 6.1 | 2500 | `java.util.TreeMap.put` |
| 5.8 | 2378 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 5.0 | 2026 | `java.util.Collections$SynchronizedMap.get` |
| 4.5 | 1840 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.5 | 1820 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.5 | 1032 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 2.3 | 954 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.3 | 936 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.2 | 892 | `java.nio.Bits.setMemory` |
| 1.9 | 776 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.7 | 710 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 1.7 | 709 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.6 | 669 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.5 | 629 | `java.util.concurrent.atomic.AtomicLongFieldUpdater.updateAndGet` |
| 1.4 | 582 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.4 | 581 | `java.util.TreeMap.remove` |
| 1.3 | 516 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 1.2 | 507 | `java.util.TreeMap.successor` |
| 1.2 | 484 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 1.1 | 455 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.9 | 378 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 0.8 | 344 | `java.util.TreeMap.getEntry` |
| 0.8 | 340 | `io.undertow.util.HttpString.calcHashCode` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 22.8 | 9282 | `java.util.Collections$SynchronizedMap.get` |
| 22.3 | 9088 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 20.1 | 8180 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 17.8 | 7257 | `java.util.WeakHashMap.get` |
| 14.1 | 5747 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.9 | 5260 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.2 | 4972 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 12.0 | 4908 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 9.6 | 3921 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 8.0 | 3279 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 7.9 | 3239 | `java.util.WeakHashMap.hash` |
| 7.7 | 3141 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 7.0 | 2863 | `org.xnio.nio.WorkerThread.executeAfter` |
| 6.5 | 2650 | `java.util.TreeMap.put` |
| 6.3 | 2573 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 6.3 | 2571 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 6.3 | 2571 | `java.util.concurrent.ForkJoinTask.doExec` |
| 6.2 | 2545 | `java.util.TreeSet.add` |
| 5.8 | 2379 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 5.8 | 2349 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.2 | 2123 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.1 | 2088 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 5.0 | 2053 | `io.undertow.server.Connectors.executeRootHandler` |
| 4.9 | 2010 | `io.undertow.io.UndertowOutputStream.buffer` |
| 4.6 | 1878 | `java.util.concurrent.ForkJoinPool.deactivate` |

