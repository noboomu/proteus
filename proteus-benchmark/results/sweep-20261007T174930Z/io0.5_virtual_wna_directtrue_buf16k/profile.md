# CPU profile

57004 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 61.5 | 35049 | `proteus I/O-*` |
| 21.0 | 11965 | ` ForkJoinPool-1-worker-*` |
| 15.5 | 8848 | `(unnamed virtual thread)` |
| 1.2 | 710 | `Reference Handler` |
| 0.7 | 406 | `Finalizer` |
| 0.0 | 22 | `main` |
| 0.0 | 16 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 21.2 | 12077 | `java.util.WeakHashMap.hash` |
| 14.9 | 8495 | `java.util.Collections$SynchronizedMap.get` |
| 8.6 | 4922 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 7.9 | 4519 | `java.util.WeakHashMap.get` |
| 6.2 | 3543 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 3.2 | 1817 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.9 | 1678 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 2.3 | 1285 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 2.0 | 1127 | `java.nio.Bits.setMemory` |
| 1.4 | 785 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.2 | 701 | `java.nio.Bits.tryReserveMemory` |
| 1.2 | 679 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 585 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.0 | 571 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.0 | 545 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.9 | 530 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.9 | 488 | `sun.nio.ch.IOUtil.configureBlocking` |
| 0.8 | 481 | `java.lang.Object.<init>` |
| 0.8 | 431 | `java.util.TreeMap.put` |
| 0.7 | 407 | `java.lang.ref.ReferenceQueue.enqueue` |
| 0.6 | 344 | `java.lang.ref.ReferenceQueue.poll` |
| 0.6 | 336 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.6 | 331 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.6 | 321 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 0.6 | 315 | `java.util.WeakHashMap.put` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 44.0 | 25080 | `java.util.Collections$SynchronizedMap.get` |
| 43.6 | 24830 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 32.9 | 18760 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 29.2 | 16668 | `java.util.WeakHashMap.get` |
| 21.2 | 12091 | `java.util.WeakHashMap.hash` |
| 20.4 | 11618 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 20.2 | 11522 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 19.7 | 11204 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 14.6 | 8340 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.2 | 6936 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 10.2 | 5839 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 9.5 | 5402 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 8.6 | 4922 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 6.3 | 3617 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 6.3 | 3570 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.3 | 2450 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.0 | 2283 | `java.nio.ByteBuffer.allocateDirect` |
| 3.8 | 2190 | `java.nio.DirectByteBuffer.<init>` |
| 3.7 | 2110 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 3.6 | 2047 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 3.6 | 2043 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 3.6 | 2035 | `java.util.concurrent.ForkJoinTask.doExec` |
| 3.3 | 1857 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 2.8 | 1623 | `io.undertow.io.UndertowOutputStream.buffer` |
| 2.6 | 1480 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |

