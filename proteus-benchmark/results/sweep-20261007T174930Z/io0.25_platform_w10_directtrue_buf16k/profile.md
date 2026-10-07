# CPU profile

58436 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 70.9 | 41414 | `proteus I/O-*` |
| 10.9 | 6362 | ` ForkJoinPool-1-worker-*` |
| 8.8 | 5117 | `proteus task-*` |
| 8.5 | 4985 | `(unnamed virtual thread)` |
| 0.6 | 366 | `Reference Handler` |
| 0.3 | 174 | `Finalizer` |
| 0.0 | 22 | `main` |
| 0.0 | 5 | `VirtualThread-unblocker` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 23.0 | 13413 | `java.util.WeakHashMap.hash` |
| 19.4 | 11314 | `java.util.Collections$SynchronizedMap.get` |
| 9.0 | 5270 | `java.util.WeakHashMap.get` |
| 4.4 | 2586 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 3.6 | 2075 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 2.8 | 1663 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.7 | 1582 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.5 | 1442 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.7 | 987 | `java.util.TreeMap.put` |
| 1.4 | 791 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.3 | 786 | `java.nio.Bits.setMemory` |
| 1.3 | 774 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.1 | 672 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.1 | 668 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.1 | 616 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.0 | 594 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.0 | 564 | `sun.nio.ch.IOUtil.configureBlocking` |
| 0.9 | 519 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 0.9 | 498 | `java.lang.Object.<init>` |
| 0.7 | 421 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.7 | 388 | `io.undertow.util.HttpString.calcHashCode` |
| 0.6 | 365 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 0.6 | 352 | `java.nio.Bits.tryReserveMemory` |
| 0.6 | 331 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 0.6 | 326 | `java.util.TreeMap.remove` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 53.7 | 31355 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 51.4 | 30045 | `java.util.Collections$SynchronizedMap.get` |
| 35.6 | 20831 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 32.1 | 18779 | `java.util.WeakHashMap.get` |
| 23.0 | 13432 | `java.util.WeakHashMap.hash` |
| 22.9 | 13372 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 16.5 | 9654 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 16.3 | 9506 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.5 | 7322 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 12.4 | 7269 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 10.5 | 6161 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 10.5 | 6114 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 8.4 | 4898 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 5.2 | 3027 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.7 | 2720 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.4 | 2586 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 3.6 | 2095 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 3.3 | 1941 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 3.1 | 1832 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 2.9 | 1706 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 2.8 | 1660 | `io.undertow.io.UndertowOutputStream.buffer` |
| 2.3 | 1348 | `java.nio.ByteBuffer.allocateDirect` |
| 2.3 | 1348 | `java.nio.DirectByteBuffer.<init>` |
| 2.2 | 1314 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 2.0 | 1153 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |

