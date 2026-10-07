# CPU profile

40871 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 42.6 | 17401 | `proteus I/O-*` |
| 27.5 | 11231 | ` ForkJoinPool-1-worker-*` |
| 26.5 | 10844 | `(unnamed virtual thread)` |
| 2.4 | 966 | `Reference Handler` |
| 1.0 | 413 | `Finalizer` |
| 0.1 | 21 | `main` |
| 0.0 | 12 | `VirtualThread-unblocker` |
| 0.0 | 3 | `proteus Accept` |
| 0.0 | 3 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.8 | 6068 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 9.7 | 3946 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 7.4 | 3038 | `java.util.WeakHashMap.hash` |
| 6.0 | 2447 | `java.util.WeakHashMap.get` |
| 3.6 | 1454 | `java.lang.ref.ReferenceQueue.poll` |
| 3.4 | 1391 | `java.util.Collections$SynchronizedMap.get` |
| 3.4 | 1385 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 3.1 | 1254 | `java.nio.Bits.setMemory` |
| 2.6 | 1073 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.4 | 987 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.1 | 872 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.8 | 724 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.6 | 671 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.5 | 604 | `java.util.concurrent.atomic.AtomicLongFieldUpdater.updateAndGet` |
| 1.4 | 554 | `java.lang.ref.ReferenceQueue.enqueue` |
| 1.3 | 533 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.1 | 449 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.1 | 435 | `java.util.TreeMap.put` |
| 1.0 | 428 | `java.util.Collections$SynchronizedMap.put` |
| 1.0 | 409 | `java.util.TreeMap.remove` |
| 0.9 | 382 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.9 | 365 | `java.nio.BufferCleaner.register` |
| 0.8 | 341 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.8 | 319 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.8 | 317 | `io.undertow.util.HttpString.calcHashCode` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 26.5 | 10822 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 26.2 | 10720 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 21.1 | 8638 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 19.4 | 7926 | `java.util.Collections$SynchronizedMap.get` |
| 16.3 | 6664 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 16.0 | 6535 | `java.util.WeakHashMap.get` |
| 14.8 | 6068 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 14.0 | 5734 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 9.8 | 4002 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 8.0 | 3276 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 7.9 | 3209 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 7.5 | 3050 | `java.util.WeakHashMap.hash` |
| 6.7 | 2748 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 5.9 | 2410 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.1 | 2104 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.1 | 2073 | `io.undertow.io.UndertowOutputStream.buffer` |
| 4.6 | 1895 | `java.nio.DirectByteBuffer.<init>` |
| 4.6 | 1894 | `java.nio.ByteBuffer.allocateDirect` |
| 4.6 | 1860 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 3.8 | 1561 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 3.8 | 1554 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 3.6 | 1489 | `java.util.WeakHashMap.expungeStaleEntries` |
| 3.6 | 1489 | `java.util.WeakHashMap.getTable` |
| 3.6 | 1482 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 3.6 | 1454 | `java.lang.ref.ReferenceQueue.poll` |

