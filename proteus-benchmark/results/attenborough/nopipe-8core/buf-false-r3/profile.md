# CPU profile

9351 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 81.3 | 7603 | `proteus I/O-*` |
| 11.2 | 1046 | `proteus task-*` |
| 6.3 | 592 | `(unnamed virtual thread)` |
| 0.9 | 81 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 36 | `main` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 32.6 | 3045 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.2 | 769 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.5 | 425 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.5 | 423 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.1 | 292 | `java.util.TreeMap.put` |
| 3.0 | 284 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.7 | 252 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 2.3 | 214 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.2 | 208 | `sun.nio.ch.IOUtil.write` |
| 2.0 | 191 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.8 | 165 | `java.util.IdentityHashMap.get` |
| 1.7 | 158 | `java.nio.ByteBuffer.putBuffer` |
| 1.3 | 125 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.3 | 122 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.3 | 121 | `org.xnio.SingleOption.cast` |
| 1.1 | 105 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.1 | 100 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.9 | 88 | `java.util.TreeMap.getEntry` |
| 0.9 | 80 | `java.util.Arrays.copyOfRange` |
| 0.8 | 74 | `java.util.WeakHashMap.matchesKey` |
| 0.7 | 70 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.7 | 69 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.7 | 67 | `sun.nio.ch.SocketDispatcher.read` |
| 0.7 | 65 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.6 | 60 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 52.8 | 4940 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 38.4 | 3592 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 35.3 | 3297 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 32.6 | 3045 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 22.0 | 2059 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.1 | 1601 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 9.2 | 858 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 8.6 | 806 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 8.6 | 804 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 8.2 | 769 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.0 | 563 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.8 | 545 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.1 | 474 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.1 | 474 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.0 | 469 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.9 | 455 | `java.lang.Thread.run` |
| 4.6 | 430 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.4 | 408 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 4.3 | 406 | `sun.nio.ch.IOUtil.write` |
| 3.6 | 337 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.4 | 318 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.2 | 299 | `java.util.TreeMap.put` |
| 3.2 | 298 | `java.util.TreeSet.add` |
| 2.7 | 255 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 2.6 | 247 | `sun.nio.ch.SocketChannelImpl.write` |

