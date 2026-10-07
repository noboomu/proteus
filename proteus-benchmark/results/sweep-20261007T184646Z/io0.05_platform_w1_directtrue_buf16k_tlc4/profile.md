# CPU profile

57619 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 68.7 | 39576 | `proteus I/O-*` |
| 10.4 | 6009 | `proteus task-*` |
| 10.3 | 5961 | ` ForkJoinPool-1-worker-*` |
| 9.6 | 5548 | `(unnamed virtual thread)` |
| 0.5 | 288 | `Reference Handler` |
| 0.4 | 219 | `Finalizer` |
| 0.0 | 22 | `main` |
| 0.0 | 3 | `VirtualThread-unblocker` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 20.6 | 11872 | `java.util.Collections$SynchronizedMap.get` |
| 18.7 | 10800 | `java.util.WeakHashMap.hash` |
| 6.3 | 3623 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 5.6 | 3202 | `java.util.WeakHashMap.get` |
| 5.0 | 2879 | `java.lang.ref.ReferenceQueue.poll` |
| 4.5 | 2618 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.8 | 1640 | `java.util.TreeMap.put` |
| 1.7 | 969 | `java.nio.ByteBuffer.putArray` |
| 1.6 | 920 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.5 | 880 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.5 | 870 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.5 | 852 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.4 | 825 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 1.4 | 790 | `java.nio.Bits.setMemory` |
| 1.3 | 775 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.2 | 700 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 1.2 | 684 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 0.9 | 538 | `java.nio.Bits.tryReserveMemory` |
| 0.9 | 536 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.9 | 499 | `sun.nio.ch.IOUtil.configureBlocking` |
| 0.8 | 470 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 0.8 | 441 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.6 | 368 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.6 | 317 | `java.util.TreeMap.successor` |
| 0.5 | 314 | `java.lang.Thread.interrupted` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 49.7 | 28638 | `java.util.Collections$SynchronizedMap.get` |
| 44.7 | 25759 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 29.8 | 17187 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 29.2 | 16811 | `java.util.WeakHashMap.get` |
| 20.0 | 11513 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 18.8 | 10811 | `java.util.WeakHashMap.hash` |
| 17.6 | 10164 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 14.2 | 8154 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.6 | 7266 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 11.3 | 6525 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 10.0 | 5737 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 9.9 | 5691 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 6.6 | 3820 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.4 | 3663 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 5.2 | 2970 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.1 | 2939 | `java.util.WeakHashMap.getTable` |
| 5.1 | 2939 | `java.util.WeakHashMap.expungeStaleEntries` |
| 5.1 | 2924 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 5.1 | 2924 | `java.lang.ref.ReferenceQueue.poll` |
| 4.8 | 2756 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.6 | 2657 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 3.2 | 1846 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.2 | 1838 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.0 | 1749 | `java.util.TreeMap.put` |
| 2.9 | 1689 | `java.util.TreeSet.add` |

