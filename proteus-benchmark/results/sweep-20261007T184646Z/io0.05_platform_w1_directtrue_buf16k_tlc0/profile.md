# CPU profile

56651 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 67.4 | 38165 | `proteus I/O-*` |
| 11.9 | 6744 | ` ForkJoinPool-1-worker-*` |
| 11.0 | 6213 | `(unnamed virtual thread)` |
| 9.7 | 5516 | `proteus task-*` |
| 0.0 | 18 | `main` |
| 0.0 | 1 | `VirtualThread-unblocker` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `InnocuousThread-1` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `Reference Handler` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 12.8 | 7236 | `java.nio.ByteBuffer.allocateDirect` |
| 11.2 | 6370 | `jdk.internal.misc.Unsafe.freeMemory` |
| 8.5 | 4790 | `java.nio.BufferCleaner.startCleaningThreadIfNeeded` |
| 6.7 | 3770 | `java.nio.BufferCleaner$CleanerList.insert` |
| 6.6 | 3724 | `java.nio.BufferCleaner$CleanerList.remove` |
| 5.8 | 3295 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 5.3 | 3025 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.2 | 2918 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 4.0 | 2287 | `java.nio.Bits.setMemory` |
| 3.7 | 2100 | `java.util.TreeMap.put` |
| 2.7 | 1538 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 1.6 | 907 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 1.4 | 775 | `java.util.Collections$SynchronizedMap.get` |
| 1.3 | 718 | `java.nio.Bits.tryReserveMemory` |
| 1.2 | 660 | `jdk.internal.misc.Unsafe.allocateMemory` |
| 0.9 | 511 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 0.9 | 482 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.8 | 479 | `sun.nio.ch.IOUtil.configureBlocking` |
| 0.8 | 478 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.7 | 402 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 0.6 | 361 | `io.undertow.io.UndertowOutputStream.buffer` |
| 0.6 | 359 | `java.util.TreeMap.remove` |
| 0.6 | 342 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.6 | 321 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 0.6 | 319 | `java.util.ArrayDeque.addLast` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 34.4 | 19465 | `java.nio.ByteBuffer.allocateDirect` |
| 34.4 | 19465 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 21.6 | 12230 | `java.nio.DirectByteBuffer.<init>` |
| 17.8 | 10099 | `java.nio.BufferCleaner$PhantomCleaner.clean` |
| 17.8 | 10098 | `jdk.internal.misc.Unsafe.invokeCleaner` |
| 17.1 | 9696 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 15.1 | 8563 | `java.nio.BufferCleaner.register` |
| 14.5 | 8220 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 13.4 | 7579 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 12.5 | 7091 | `sun.misc.Unsafe.invokeCleaner` |
| 12.0 | 6823 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 12.0 | 6782 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 11.4 | 6437 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 11.2 | 6370 | `jdk.internal.misc.Unsafe.freeMemory` |
| 11.2 | 6370 | `java.nio.DirectByteBuffer$Deallocator.run` |
| 11.2 | 6332 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 8.5 | 4790 | `java.nio.BufferCleaner.startCleaningThreadIfNeeded` |
| 6.7 | 3770 | `java.nio.BufferCleaner$CleanerList.insert` |
| 6.6 | 3729 | `jdk.internal.reflect.DirectMethodHandleAccessor.invoke` |
| 6.6 | 3724 | `java.nio.BufferCleaner$CleanerList.remove` |
| 5.9 | 3332 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 5.5 | 3107 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 5.3 | 2979 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.3 | 2975 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 4.9 | 2802 | `io.undertow.channels.DetachableStreamSinkChannel.write` |

