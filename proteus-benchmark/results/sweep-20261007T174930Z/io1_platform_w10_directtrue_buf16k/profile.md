# CPU profile

51211 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 69.6 | 35651 | `proteus I/O-*` |
| 12.5 | 6396 | ` ForkJoinPool-1-worker-*` |
| 9.3 | 4772 | `(unnamed virtual thread)` |
| 7.5 | 3845 | `proteus task-*` |
| 0.6 | 332 | `Reference Handler` |
| 0.4 | 188 | `Finalizer` |
| 0.0 | 24 | `main` |
| 0.0 | 10 | `VirtualThread-unblocker` |
| 0.0 | 2 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 21.0 | 10756 | `java.util.WeakHashMap.hash` |
| 17.3 | 8844 | `java.util.Collections$SynchronizedMap.get` |
| 8.2 | 4186 | `java.util.WeakHashMap.get` |
| 4.1 | 2119 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 3.5 | 1787 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 2.7 | 1383 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 2.6 | 1336 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 2.3 | 1200 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 2.1 | 1066 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.7 | 864 | `java.lang.ref.ReferenceQueue.poll` |
| 1.5 | 743 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.4 | 725 | `java.nio.Bits.setMemory` |
| 1.4 | 713 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.4 | 699 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.3 | 659 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.3 | 655 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.2 | 620 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.1 | 569 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 515 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 0.9 | 462 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.8 | 430 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.8 | 402 | `io.undertow.util.HttpString.calcHashCode` |
| 0.8 | 386 | `io.undertow.server.handlers.GracefulShutdownHandler.handleRequest` |
| 0.7 | 375 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.7 | 348 | `java.util.HashMap.getNode` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 50.4 | 25835 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 48.0 | 24574 | `java.util.Collections$SynchronizedMap.get` |
| 33.9 | 17362 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 30.8 | 15783 | `java.util.WeakHashMap.get` |
| 21.0 | 10764 | `java.util.WeakHashMap.hash` |
| 21.0 | 10757 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 17.3 | 8841 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 15.1 | 7726 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.2 | 6231 | `io.undertow.server.DefaultByteBufferPool$DefaultPooledBuffer.close` |
| 12.1 | 6219 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 12.1 | 6185 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 10.5 | 5389 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 9.7 | 4991 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.5 | 3334 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 5.2 | 2646 | `io.undertow.server.protocol.http.HttpResponseConduit.bufferDone` |
| 4.3 | 2200 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 4.0 | 2055 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 3.8 | 1946 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 3.8 | 1946 | `java.util.concurrent.ForkJoinTask.doExec` |
| 3.0 | 1553 | `io.undertow.io.UndertowOutputStream.buffer` |
| 2.9 | 1495 | `java.nio.ByteBuffer.allocateDirect` |
| 2.7 | 1385 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 2.7 | 1383 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 2.7 | 1383 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 2.7 | 1373 | `java.util.concurrent.ForkJoinPool.deactivate` |

