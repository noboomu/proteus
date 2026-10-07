# CPU profile

51910 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 64.2 | 33305 | `proteus I/O-*` |
| 12.4 | 6431 | `(unnamed virtual thread)` |
| 11.8 | 6113 | ` ForkJoinPool-1-worker-*` |
| 11.6 | 6044 | `proteus task-*` |
| 0.0 | 18 | `main` |
| 0.0 | 5 | `VirtualThread-unblocker` |
| 0.0 | 2 | `proteus Accept` |
| 0.0 | 1 | `InnocuousThread-1` |
| 0.0 | 1 | `Reference Handler` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.8 | 7661 | `java.nio.Bits.tryReserveMemory` |
| 11.6 | 5997 | `jdk.internal.misc.Unsafe.freeMemory` |
| 5.5 | 2832 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.3 | 2728 | `java.nio.Bits.setMemory` |
| 5.0 | 2609 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 5.0 | 2607 | `java.nio.BufferCleaner.startCleaningThreadIfNeeded` |
| 4.6 | 2406 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 3.7 | 1921 | `java.nio.BufferCleaner$CleanerList.insert` |
| 3.5 | 1809 | `java.util.TreeMap.put` |
| 3.5 | 1798 | `java.nio.BufferCleaner$CleanerList.remove` |
| 3.0 | 1554 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 2.2 | 1161 | `java.util.Collections$SynchronizedMap.get` |
| 2.2 | 1151 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 1.9 | 1000 | `jdk.internal.misc.Unsafe.allocateMemory` |
| 1.5 | 784 | `java.util.TreeSet.add` |
| 1.2 | 627 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.1 | 581 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.0 | 498 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 0.9 | 486 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.9 | 474 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 0.8 | 438 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.8 | 394 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.7 | 362 | `java.util.TreeMap.successor` |
| 0.7 | 354 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 0.7 | 341 | `java.util.ArrayDeque.addLast` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 30.7 | 15919 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 30.7 | 15918 | `java.nio.ByteBuffer.allocateDirect` |
| 30.7 | 15918 | `java.nio.DirectByteBuffer.<init>` |
| 15.0 | 7795 | `java.nio.BufferCleaner$PhantomCleaner.clean` |
| 15.0 | 7795 | `jdk.internal.misc.Unsafe.invokeCleaner` |
| 14.8 | 7661 | `java.nio.Bits.reserveMemory` |
| 14.8 | 7661 | `java.nio.Bits.tryReserveMemory` |
| 12.1 | 6281 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 11.6 | 5997 | `java.nio.DirectByteBuffer$Deallocator.run` |
| 11.6 | 5997 | `jdk.internal.misc.Unsafe.freeMemory` |
| 11.2 | 5792 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 10.9 | 5672 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 10.2 | 5295 | `sun.misc.Unsafe.invokeCleaner` |
| 9.5 | 4948 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 8.7 | 4529 | `java.nio.BufferCleaner.register` |
| 7.3 | 3768 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.3 | 3265 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.3 | 3252 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.8 | 3009 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 5.6 | 2920 | `org.xnio.nio.WorkerThread.executeAfter` |
| 5.6 | 2903 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 5.4 | 2789 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 5.4 | 2778 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.3 | 2761 | `java.util.TreeSet.add` |
| 5.3 | 2730 | `java.nio.Bits.setMemory` |

