# CPU profile

50797 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 70.0 | 35542 | `proteus I/O-*` |
| 12.2 | 6176 | ` ForkJoinPool-1-worker-*` |
| 9.5 | 4826 | `(unnamed virtual thread)` |
| 7.4 | 3765 | `proteus task-*` |
| 0.6 | 295 | `Reference Handler` |
| 0.3 | 167 | `Finalizer` |
| 0.0 | 24 | `main` |
| 0.0 | 8 | `VirtualThread-unblocker` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 20.9 | 10623 | `java.util.WeakHashMap.hash` |
| 18.9 | 9595 | `java.util.Collections$SynchronizedMap.get` |
| 5.8 | 2952 | `java.lang.ref.ReferenceQueue.poll` |
| 4.8 | 2428 | `java.util.WeakHashMap.get` |
| 3.8 | 1926 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 3.5 | 1796 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 3.3 | 1661 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 3.1 | 1574 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 2.0 | 1041 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.6 | 830 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.5 | 742 | `java.nio.Bits.setMemory` |
| 1.4 | 730 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.4 | 711 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 1.4 | 705 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.3 | 683 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.1 | 566 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.1 | 562 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.0 | 525 | `java.util.TreeMap.put` |
| 0.9 | 436 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.8 | 419 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.7 | 351 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.7 | 346 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.6 | 310 | `java.nio.Bits.tryReserveMemory` |
| 0.6 | 309 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.6 | 302 | `java.util.HashMap.getNode` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 50.1 | 25472 | `java.util.Collections$SynchronizedMap.get` |
| 44.4 | 22574 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 32.4 | 16468 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 31.3 | 15877 | `java.util.WeakHashMap.get` |
| 20.9 | 10630 | `java.util.WeakHashMap.hash` |
| 18.8 | 9569 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 16.8 | 8559 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.2 | 7218 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 11.8 | 6010 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 11.7 | 5962 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 10.8 | 5466 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 10.3 | 5220 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 9.8 | 4957 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 5.8 | 2953 | `java.util.WeakHashMap.getTable` |
| 5.8 | 2953 | `java.util.WeakHashMap.expungeStaleEntries` |
| 5.8 | 2952 | `java.lang.ref.ReferenceQueue.poll` |
| 5.6 | 2830 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.8 | 2455 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.4 | 2228 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.3 | 2202 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 4.0 | 2010 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 3.8 | 1926 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 3.5 | 1796 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 3.2 | 1637 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 3.2 | 1631 | `io.undertow.io.UndertowOutputStream.buffer` |

