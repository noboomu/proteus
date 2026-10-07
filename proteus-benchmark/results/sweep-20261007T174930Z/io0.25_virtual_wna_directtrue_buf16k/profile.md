# CPU profile

57883 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 60.7 | 35117 | `proteus I/O-*` |
| 22.1 | 12795 | ` ForkJoinPool-1-worker-*` |
| 15.4 | 8931 | `(unnamed virtual thread)` |
| 1.1 | 615 | `Reference Handler` |
| 0.7 | 388 | `Finalizer` |
| 0.0 | 26 | `main` |
| 0.0 | 17 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 20.1 | 11607 | `java.util.WeakHashMap.hash` |
| 14.6 | 8468 | `java.util.Collections$SynchronizedMap.get` |
| 9.5 | 5494 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 7.4 | 4304 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 6.8 | 3916 | `java.util.WeakHashMap.get` |
| 5.0 | 2877 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.7 | 1591 | `java.lang.Object.<init>` |
| 2.7 | 1538 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 2.0 | 1144 | `java.nio.Bits.setMemory` |
| 1.6 | 930 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.4 | 834 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.1 | 659 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.0 | 605 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 598 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.0 | 553 | `java.lang.ref.ReferenceQueue.poll` |
| 0.9 | 507 | `sun.nio.ch.IOUtil.configureBlocking` |
| 0.8 | 484 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 0.8 | 436 | `java.util.HashMap.getNode` |
| 0.7 | 418 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.7 | 384 | `java.util.Collections$SynchronizedMap.put` |
| 0.7 | 381 | `java.util.TreeMap.put` |
| 0.6 | 376 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 0.6 | 332 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.6 | 326 | `java.lang.ref.ReferenceQueue.enqueue` |
| 0.5 | 310 | `java.util.concurrent.atomic.AtomicLongFieldUpdater.updateAndGet` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 42.1 | 24357 | `java.util.Collections$SynchronizedMap.get` |
| 41.2 | 23823 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 32.5 | 18819 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 27.6 | 15981 | `java.util.WeakHashMap.get` |
| 21.5 | 12444 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 21.3 | 12336 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 20.1 | 11615 | `java.util.WeakHashMap.hash` |
| 18.4 | 10661 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 16.4 | 9479 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.9 | 7440 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 11.3 | 6513 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 9.6 | 5571 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 9.5 | 5494 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 7.5 | 4358 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 6.6 | 3835 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.5 | 2617 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.3 | 2511 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.1 | 2371 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.4 | 1982 | `io.undertow.io.UndertowOutputStream.buffer` |
| 3.1 | 1802 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalData.<init>` |
| 3.1 | 1802 | `java.lang.Object.<init>` |
| 3.1 | 1767 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 2.7 | 1579 | `java.nio.ByteBuffer.allocateDirect` |
| 2.7 | 1579 | `java.nio.DirectByteBuffer.<init>` |
| 2.1 | 1211 | `io.undertow.io.UndertowOutputStream.write` |

