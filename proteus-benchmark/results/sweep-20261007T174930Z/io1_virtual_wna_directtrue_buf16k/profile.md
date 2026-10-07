# CPU profile

55196 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 60.1 | 33161 | `proteus I/O-*` |
| 22.4 | 12346 | ` ForkJoinPool-1-worker-*` |
| 15.2 | 8378 | `(unnamed virtual thread)` |
| 1.5 | 833 | `Reference Handler` |
| 0.8 | 462 | `Finalizer` |
| 0.0 | 20 | `main` |
| 0.0 | 16 | `VirtualThread-unblocker` |
| 0.0 | 4 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 20.2 | 11126 | `java.util.WeakHashMap.hash` |
| 14.5 | 8018 | `java.util.Collections$SynchronizedMap.get` |
| 8.8 | 4846 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 6.5 | 3568 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 4.9 | 2709 | `java.lang.ref.ReferenceQueue.poll` |
| 4.0 | 2197 | `java.util.WeakHashMap.get` |
| 2.8 | 1569 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.8 | 1534 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 2.4 | 1352 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 2.0 | 1089 | `java.nio.Bits.setMemory` |
| 1.6 | 887 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 1.4 | 783 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.2 | 672 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.1 | 627 | `java.nio.Bits.tryReserveMemory` |
| 1.0 | 541 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 0.9 | 508 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.9 | 508 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 0.9 | 497 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 0.8 | 418 | `java.lang.ref.ReferenceQueue.enqueue` |
| 0.7 | 408 | `tools.jackson.core.json.JsonWriteContext.writeValue` |
| 0.7 | 390 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.7 | 365 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.6 | 355 | `java.nio.Bits.reserveMemory` |
| 0.6 | 345 | `java.util.TreeMap.put` |
| 0.5 | 281 | `java.util.TreeMap.remove` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 43.1 | 23772 | `java.util.Collections$SynchronizedMap.get` |
| 38.3 | 21165 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 30.2 | 16652 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 28.7 | 15851 | `java.util.WeakHashMap.get` |
| 21.8 | 12015 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 21.7 | 11950 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 20.2 | 11132 | `java.util.WeakHashMap.hash` |
| 17.4 | 9614 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 13.2 | 7276 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.2 | 6755 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.2 | 6714 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 9.4 | 5184 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 9.4 | 5169 | `java.util.concurrent.ForkJoinTask.doExec` |
| 9.0 | 4961 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 8.7 | 4780 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 7.2 | 3997 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.5 | 3568 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 5.1 | 2840 | `java.util.WeakHashMap.expungeStaleEntries` |
| 5.1 | 2840 | `java.util.WeakHashMap.getTable` |
| 5.1 | 2840 | `java.lang.ref.ReferenceQueue.poll` |
| 4.2 | 2301 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.1 | 2269 | `java.nio.ByteBuffer.allocateDirect` |
| 3.9 | 2165 | `java.nio.DirectByteBuffer.<init>` |
| 3.7 | 2038 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 3.2 | 1744 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |

