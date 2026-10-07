# CPU profile

53035 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 70.7 | 37498 | `proteus I/O-*` |
| 11.8 | 6260 | ` ForkJoinPool-1-worker-*` |
| 9.0 | 4764 | `(unnamed virtual thread)` |
| 7.6 | 4011 | `proteus task-*` |
| 0.5 | 287 | `Reference Handler` |
| 0.4 | 190 | `Finalizer` |
| 0.0 | 24 | `main` |
| 0.0 | 6 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 21.7 | 11513 | `java.util.WeakHashMap.hash` |
| 18.4 | 9757 | `java.util.Collections$SynchronizedMap.get` |
| 7.2 | 3811 | `java.util.WeakHashMap.get` |
| 4.7 | 2495 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 3.7 | 1974 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 3.3 | 1769 | `java.lang.Object.<init>` |
| 3.0 | 1594 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.9 | 1522 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.6 | 853 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.4 | 753 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 1.3 | 710 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 1.3 | 685 | `java.nio.Bits.setMemory` |
| 1.2 | 658 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.2 | 656 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.1 | 585 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.1 | 559 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 1.0 | 553 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 514 | `java.util.TreeMap.put` |
| 0.9 | 501 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.9 | 480 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 0.9 | 472 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.8 | 440 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.8 | 421 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 0.7 | 365 | `java.util.TreeMap.remove` |
| 0.6 | 335 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 47.4 | 25146 | `java.util.Collections$SynchronizedMap.get` |
| 47.2 | 25007 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 32.9 | 17449 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 29.1 | 15435 | `java.util.WeakHashMap.get` |
| 21.7 | 11522 | `java.util.WeakHashMap.hash` |
| 21.5 | 11395 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 17.9 | 9511 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 15.6 | 8263 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.7 | 6721 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 11.9 | 6313 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 11.8 | 6262 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 11.5 | 6088 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 11.4 | 6035 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 6.5 | 3430 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 5.5 | 2896 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 5.0 | 2668 | `java.util.concurrent.ForkJoinTask.doExec` |
| 5.0 | 2668 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 4.9 | 2593 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.4 | 2352 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.1 | 2160 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 3.8 | 2039 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 3.6 | 1889 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalData.<init>` |
| 3.6 | 1889 | `java.lang.Object.<init>` |
| 3.2 | 1721 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 3.1 | 1660 | `io.undertow.io.UndertowOutputStream.buffer` |

