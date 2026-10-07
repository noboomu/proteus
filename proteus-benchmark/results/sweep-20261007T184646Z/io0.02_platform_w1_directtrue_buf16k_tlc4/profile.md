# CPU profile

39113 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 51.9 | 20311 | `proteus I/O-*` |
| 17.0 | 6649 | `proteus task-*` |
| 15.0 | 5883 | ` ForkJoinPool-1-worker-*` |
| 14.6 | 5722 | `(unnamed virtual thread)` |
| 0.7 | 280 | `Reference Handler` |
| 0.6 | 244 | `Finalizer` |
| 0.1 | 25 | `main` |
| 0.0 | 9 | `VirtualThread-unblocker` |
| 0.0 | 2 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 11.1 | 4329 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 8.4 | 3266 | `java.util.WeakHashMap.hash` |
| 7.0 | 2737 | `java.util.WeakHashMap.get` |
| 6.0 | 2348 | `java.util.Collections$SynchronizedMap.get` |
| 5.4 | 2129 | `java.util.TreeMap.put` |
| 2.4 | 947 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.3 | 881 | `java.util.IdentityHashMap.get` |
| 2.3 | 881 | `java.nio.Bits.setMemory` |
| 2.1 | 837 | `java.nio.ByteBuffer.putArray` |
| 2.1 | 835 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.9 | 762 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 1.9 | 756 | `java.lang.ref.ReferenceQueue.poll` |
| 1.9 | 748 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.9 | 743 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.8 | 690 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 1.7 | 646 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.6 | 644 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 1.5 | 568 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.4 | 553 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.4 | 541 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.1 | 429 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 1.1 | 423 | `io.undertow.server.handlers.GracefulShutdownHandler.handleRequest` |
| 1.0 | 408 | `java.util.TreeMap.remove` |
| 1.0 | 406 | `java.util.TreeMap.successor` |
| 1.0 | 394 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 23.0 | 8989 | `java.util.Collections$SynchronizedMap.get` |
| 21.1 | 8264 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 18.7 | 7327 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 17.0 | 6643 | `java.util.WeakHashMap.get` |
| 15.9 | 6206 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 14.5 | 5655 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 14.4 | 5621 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 11.1 | 4360 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 10.6 | 4144 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 10.0 | 3923 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 9.4 | 3671 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 8.4 | 3275 | `java.util.WeakHashMap.hash` |
| 6.8 | 2642 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 6.3 | 2479 | `org.xnio.nio.WorkerThread.executeAfter` |
| 6.2 | 2425 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 6.0 | 2363 | `java.util.TreeMap.put` |
| 5.8 | 2276 | `java.util.TreeSet.add` |
| 5.5 | 2164 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.8 | 1895 | `io.undertow.io.UndertowOutputStream.buffer` |
| 4.8 | 1869 | `io.undertow.server.Connectors.executeRootHandler` |
| 4.2 | 1650 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.2 | 1630 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 4.2 | 1629 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 3.3 | 1284 | `java.nio.ByteBuffer.allocateDirect` |
| 3.3 | 1284 | `java.nio.DirectByteBuffer.<init>` |

