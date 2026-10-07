# CPU profile

53959 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 69.4 | 37471 | `proteus I/O-*` |
| 12.1 | 6506 | ` ForkJoinPool-1-worker-*` |
| 8.8 | 4762 | `proteus task-*` |
| 8.7 | 4709 | `(unnamed virtual thread)` |
| 0.6 | 327 | `Reference Handler` |
| 0.3 | 159 | `Finalizer` |
| 0.0 | 24 | `main` |
| 0.0 | 7 | `VirtualThread-unblocker` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 24.1 | 13009 | `java.util.Collections$SynchronizedMap.get` |
| 21.6 | 11648 | `java.util.WeakHashMap.hash` |
| 4.7 | 2524 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 4.6 | 2456 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 2.5 | 1332 | `java.nio.ByteBuffer.allocateDirect` |
| 2.0 | 1102 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.9 | 1031 | `java.util.WeakHashMap.get` |
| 1.5 | 812 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.4 | 754 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.4 | 750 | `java.nio.Bits.setMemory` |
| 1.4 | 730 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.3 | 725 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.3 | 705 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.2 | 655 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 1.2 | 645 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.1 | 607 | `java.util.TreeMap.put` |
| 1.1 | 582 | `java.util.TreeMap.deleteEntry` |
| 0.9 | 511 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 0.9 | 503 | `java.util.ArrayDeque.addLast` |
| 0.9 | 499 | `java.util.HashMap.getNode` |
| 0.9 | 491 | `java.lang.Object.<init>` |
| 0.8 | 442 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.8 | 431 | `java.nio.Bits.tryReserveMemory` |
| 0.8 | 429 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 0.7 | 399 | `io.undertow.util.HttpString.bytesAreEqual` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 47.7 | 25737 | `java.util.Collections$SynchronizedMap.get` |
| 47.5 | 25613 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 33.9 | 18279 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 23.7 | 12763 | `java.util.WeakHashMap.get` |
| 21.6 | 11659 | `java.util.WeakHashMap.hash` |
| 21.4 | 11568 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 16.9 | 9119 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 16.0 | 8624 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 14.6 | 7854 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 12.0 | 6473 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 11.7 | 6308 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 11.6 | 6267 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 10.1 | 5426 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.7 | 3612 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 5.0 | 2689 | `java.nio.ByteBuffer.allocateDirect` |
| 4.7 | 2551 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.7 | 2524 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 4.6 | 2491 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 3.8 | 2049 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 2.9 | 1553 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 2.7 | 1449 | `io.undertow.io.UndertowOutputStream.buffer` |
| 2.5 | 1357 | `java.nio.DirectByteBuffer.<init>` |
| 2.3 | 1265 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 2.2 | 1208 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 2.2 | 1188 | `io.undertow.channels.DetachableStreamSinkChannel.write` |

