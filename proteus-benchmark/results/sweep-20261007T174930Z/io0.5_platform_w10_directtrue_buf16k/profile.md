# CPU profile

51393 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 69.5 | 35713 | `proteus I/O-*` |
| 12.1 | 6221 | ` ForkJoinPool-1-worker-*` |
| 9.4 | 4806 | `(unnamed virtual thread)` |
| 8.1 | 4159 | `proteus task-*` |
| 0.6 | 289 | `Reference Handler` |
| 0.3 | 175 | `Finalizer` |
| 0.0 | 24 | `main` |
| 0.0 | 13 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 21.0 | 10786 | `java.util.WeakHashMap.hash` |
| 17.8 | 9163 | `java.util.Collections$SynchronizedMap.get` |
| 7.4 | 3808 | `java.util.WeakHashMap.get` |
| 5.7 | 2928 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 3.6 | 1827 | `java.lang.Object.<init>` |
| 3.5 | 1785 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 3.0 | 1551 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.1 | 1065 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.7 | 852 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 1.5 | 769 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 1.5 | 751 | `java.nio.Bits.setMemory` |
| 1.4 | 710 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.3 | 687 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.3 | 676 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.2 | 602 | `java.util.TreeMap.put` |
| 1.1 | 566 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.1 | 558 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.1 | 553 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 537 | `java.util.concurrent.atomic.AtomicLongFieldUpdater.updateAndGet` |
| 0.9 | 483 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 0.9 | 445 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.8 | 407 | `java.util.IdentityHashMap.get` |
| 0.7 | 378 | `java.nio.Bits.tryReserveMemory` |
| 0.7 | 365 | `io.undertow.util.HttpString.calcHashCode` |
| 0.6 | 332 | `java.lang.ref.ReferenceQueue.poll` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 46.7 | 23996 | `java.util.Collections$SynchronizedMap.get` |
| 46.1 | 23674 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 32.2 | 16558 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 28.9 | 14870 | `java.util.WeakHashMap.get` |
| 21.3 | 10952 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 21.0 | 10793 | `java.util.WeakHashMap.hash` |
| 16.4 | 8427 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 16.0 | 8219 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.1 | 6725 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 11.9 | 6122 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 11.7 | 6011 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 11.6 | 5962 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 8.4 | 4300 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 5.7 | 2931 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 5.7 | 2928 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 5.0 | 2559 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.6 | 2386 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.7 | 1923 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalData.<init>` |
| 3.7 | 1923 | `java.lang.Object.<init>` |
| 3.6 | 1830 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 3.4 | 1722 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 3.1 | 1594 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 3.0 | 1541 | `io.undertow.io.UndertowOutputStream.buffer` |
| 2.4 | 1210 | `java.nio.DirectByteBuffer.<init>` |
| 2.4 | 1210 | `java.nio.ByteBuffer.allocateDirect` |

