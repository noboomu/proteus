# CPU profile

55107 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 71.0 | 39141 | `proteus I/O-*` |
| 10.8 | 5940 | ` ForkJoinPool-1-worker-*` |
| 8.8 | 4861 | `proteus task-*` |
| 8.5 | 4659 | `(unnamed virtual thread)` |
| 0.5 | 283 | `Reference Handler` |
| 0.4 | 195 | `Finalizer` |
| 0.0 | 26 | `main` |
| 0.0 | 4 | `JFR Periodic Tasks` |
| 0.0 | 2 | `VirtualThread-unblocker` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 21.8 | 12024 | `java.util.WeakHashMap.hash` |
| 17.9 | 9869 | `java.util.Collections$SynchronizedMap.get` |
| 6.6 | 3664 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 5.1 | 2837 | `java.util.WeakHashMap.get` |
| 3.4 | 1853 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 3.4 | 1848 | `java.lang.Object.<init>` |
| 3.1 | 1732 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.6 | 1409 | `java.lang.ref.ReferenceQueue.poll` |
| 1.9 | 1050 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.5 | 848 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.5 | 811 | `java.util.TreeMap.put` |
| 1.5 | 806 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.4 | 791 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.4 | 752 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 1.3 | 692 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.2 | 685 | `java.nio.Bits.setMemory` |
| 1.2 | 660 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 1.2 | 656 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.0 | 563 | `sun.nio.ch.IOUtil.configureBlocking` |
| 0.8 | 435 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.7 | 395 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.7 | 366 | `java.util.IdentityHashMap.get` |
| 0.6 | 358 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.6 | 355 | `io.undertow.util.HttpString.calcHashCode` |
| 0.6 | 325 | `java.util.TreeMap.remove` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 47.3 | 26048 | `java.util.Collections$SynchronizedMap.get` |
| 44.7 | 24659 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 32.6 | 17969 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 29.4 | 16220 | `java.util.WeakHashMap.get` |
| 21.8 | 12031 | `java.util.WeakHashMap.hash` |
| 19.1 | 10538 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 16.5 | 9118 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 16.3 | 8972 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.4 | 7358 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 10.4 | 5741 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 10.3 | 5688 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 9.6 | 5289 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 9.4 | 5173 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.7 | 3702 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 5.6 | 3110 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 5.3 | 2911 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.8 | 2671 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.8 | 2090 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 3.5 | 1930 | `java.lang.Object.<init>` |
| 3.5 | 1930 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalData.<init>` |
| 3.3 | 1808 | `io.undertow.io.UndertowOutputStream.buffer` |
| 3.3 | 1795 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 2.7 | 1467 | `java.util.WeakHashMap.getTable` |
| 2.7 | 1467 | `java.util.WeakHashMap.expungeStaleEntries` |
| 2.7 | 1461 | `java.lang.ref.ReferenceQueue.poll` |

