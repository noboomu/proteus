# CPU profile

51541 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 70.1 | 36141 | `proteus I/O-*` |
| 11.6 | 5961 | ` ForkJoinPool-1-worker-*` |
| 9.7 | 5000 | `(unnamed virtual thread)` |
| 7.4 | 3836 | `proteus task-*` |
| 0.7 | 357 | `Reference Handler` |
| 0.4 | 223 | `Finalizer` |
| 0.0 | 22 | `main` |
| 0.0 | 5 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 21.1 | 10862 | `java.util.WeakHashMap.hash` |
| 17.9 | 9219 | `java.util.Collections$SynchronizedMap.get` |
| 5.4 | 2764 | `java.util.WeakHashMap.get` |
| 4.7 | 2421 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.5 | 2328 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 3.1 | 1616 | `java.lang.ref.ReferenceQueue.poll` |
| 3.1 | 1573 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.9 | 1507 | `java.lang.Object.<init>` |
| 2.9 | 1483 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.8 | 905 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.5 | 760 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.5 | 753 | `java.nio.Bits.setMemory` |
| 1.4 | 740 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.3 | 671 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 1.2 | 633 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.1 | 559 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.0 | 528 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.9 | 487 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.8 | 402 | `java.util.TreeMap.put` |
| 0.8 | 400 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 0.8 | 399 | `java.nio.ByteBuffer.putArray` |
| 0.7 | 379 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.7 | 362 | `io.undertow.util.HttpString.calcHashCode` |
| 0.7 | 361 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.7 | 361 | `java.nio.Bits.tryReserveMemory` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 47.2 | 24326 | `java.util.Collections$SynchronizedMap.get` |
| 44.2 | 22763 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 32.9 | 16954 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 29.4 | 15151 | `java.util.WeakHashMap.get` |
| 21.1 | 10873 | `java.util.WeakHashMap.hash` |
| 18.6 | 9590 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 18.1 | 9354 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 15.9 | 8214 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.3 | 6336 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 11.8 | 6099 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 11.2 | 5764 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 11.1 | 5715 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 9.3 | 4793 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 5.3 | 2740 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.9 | 2523 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.9 | 2511 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 4.9 | 2504 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 4.9 | 2504 | `java.util.concurrent.ForkJoinTask.doExec` |
| 4.8 | 2469 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.6 | 2362 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.2 | 2141 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 3.7 | 1914 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 3.3 | 1676 | `java.util.WeakHashMap.getTable` |
| 3.3 | 1676 | `java.util.WeakHashMap.expungeStaleEntries` |
| 3.2 | 1675 | `java.lang.ref.ReferenceQueue.poll` |

