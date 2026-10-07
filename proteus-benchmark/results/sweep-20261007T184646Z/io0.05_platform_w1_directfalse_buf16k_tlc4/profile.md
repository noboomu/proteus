# CPU profile

56475 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 69.5 | 39255 | `proteus I/O-*` |
| 11.0 | 6215 | ` ForkJoinPool-1-worker-*` |
| 10.3 | 5809 | `proteus task-*` |
| 7.9 | 4471 | `(unnamed virtual thread)` |
| 0.8 | 427 | `Reference Handler` |
| 0.5 | 269 | `Finalizer` |
| 0.0 | 19 | `main` |
| 0.0 | 12 | `VirtualThread-unblocker` |
| 0.0 | 2 | `proteus Accept` |
| 0.0 | 2 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 20.2 | 11411 | `java.util.Collections$SynchronizedMap.get` |
| 19.1 | 10763 | `java.util.WeakHashMap.hash` |
| 9.0 | 5055 | `java.util.WeakHashMap.get` |
| 3.4 | 1932 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 2.9 | 1653 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.9 | 1633 | `java.util.TreeMap.put` |
| 2.6 | 1473 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 2.0 | 1132 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 1.9 | 1060 | `java.util.IdentityHashMap.get` |
| 1.8 | 1012 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 1.8 | 1001 | `java.lang.ref.ReferenceQueue.poll` |
| 1.6 | 895 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.5 | 845 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.4 | 815 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 1.4 | 763 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.1 | 629 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.1 | 628 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.1 | 620 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 1.1 | 619 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.1 | 596 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.9 | 512 | `sun.nio.ch.IOUtil.configureBlocking` |
| 0.6 | 366 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.6 | 356 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.6 | 319 | `java.util.TreeMap.successor` |
| 0.6 | 311 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 49.7 | 28044 | `java.util.Collections$SynchronizedMap.get` |
| 48.0 | 27119 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 29.5 | 16633 | `java.util.WeakHashMap.get` |
| 28.5 | 16081 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 24.5 | 13852 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 19.1 | 10780 | `java.util.WeakHashMap.hash` |
| 17.5 | 9910 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 15.7 | 8890 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 14.7 | 8321 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.6 | 8269 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 10.6 | 5999 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 10.5 | 5950 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 6.9 | 3925 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.3 | 3558 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.5 | 2538 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 3.9 | 2178 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.6 | 2035 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 3.5 | 1962 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 3.2 | 1811 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.2 | 1811 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.1 | 1734 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 3.1 | 1728 | `java.util.TreeMap.put` |
| 3.0 | 1683 | `java.util.TreeSet.add` |
| 2.8 | 1555 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 2.6 | 1473 | `java.util.concurrent.ForkJoinPool.signalWork` |

