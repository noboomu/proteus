# CPU profile

55926 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 59.6 | 33334 | `proteus I/O-*` |
| 21.2 | 11876 | ` ForkJoinPool-1-worker-*` |
| 16.8 | 9391 | `(unnamed virtual thread)` |
| 1.6 | 878 | `Reference Handler` |
| 0.8 | 432 | `Finalizer` |
| 0.0 | 19 | `main` |
| 0.0 | 7 | `VirtualThread-unblocker` |
| 0.0 | 2 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 19.3 | 10775 | `java.util.WeakHashMap.hash` |
| 15.0 | 8412 | `java.util.Collections$SynchronizedMap.get` |
| 13.5 | 7565 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.4 | 2461 | `java.util.WeakHashMap.get` |
| 4.3 | 2422 | `java.lang.ref.ReferenceQueue.poll` |
| 3.7 | 2084 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 2.9 | 1642 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.3 | 1309 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 2.1 | 1174 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 2.1 | 1155 | `java.nio.Bits.setMemory` |
| 1.3 | 752 | `java.nio.Bits.tryReserveMemory` |
| 1.3 | 741 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.1 | 634 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 0.9 | 521 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 0.9 | 514 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.9 | 506 | `java.nio.ByteBuffer.allocateDirect` |
| 0.8 | 462 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 0.8 | 460 | `java.lang.ref.ReferenceQueue.enqueue` |
| 0.7 | 418 | `java.util.TreeMap.getEntry` |
| 0.7 | 380 | `io.undertow.util.HttpString.calcHashCode` |
| 0.7 | 368 | `java.nio.ByteBuffer.putArray` |
| 0.7 | 364 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 0.6 | 362 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.6 | 320 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 0.6 | 311 | `java.util.TreeMap.put` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 42.3 | 23672 | `java.util.Collections$SynchronizedMap.get` |
| 38.3 | 21438 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 31.5 | 17642 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 27.4 | 15341 | `java.util.WeakHashMap.get` |
| 20.6 | 11544 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 20.4 | 11432 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 19.3 | 10783 | `java.util.WeakHashMap.hash` |
| 16.2 | 9063 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 13.9 | 7764 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 13.7 | 7680 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 12.9 | 7187 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 9.1 | 5087 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 8.4 | 4720 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 8.2 | 4579 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 5.0 | 2794 | `java.nio.ByteBuffer.allocateDirect` |
| 4.6 | 2572 | `java.util.WeakHashMap.expungeStaleEntries` |
| 4.6 | 2572 | `java.util.WeakHashMap.getTable` |
| 4.6 | 2572 | `java.lang.ref.ReferenceQueue.poll` |
| 4.4 | 2485 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 4.4 | 2473 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 4.4 | 2463 | `java.util.concurrent.ForkJoinTask.doExec` |
| 4.2 | 2354 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.1 | 2288 | `java.nio.DirectByteBuffer.<init>` |
| 4.0 | 2249 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 3.0 | 1694 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |

