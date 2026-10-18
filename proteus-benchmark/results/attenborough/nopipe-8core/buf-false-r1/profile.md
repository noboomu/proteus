# CPU profile

9423 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 82.9 | 7812 | `proteus I/O-*` |
| 9.6 | 900 | `proteus task-*` |
| 6.4 | 602 | `(unnamed virtual thread)` |
| 0.8 | 79 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 33 | `main` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 32.9 | 3104 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.4 | 795 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.0 | 469 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.7 | 346 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.5 | 330 | `java.util.TreeMap.put` |
| 3.2 | 304 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 3.0 | 282 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.1 | 194 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.8 | 172 | `java.nio.ByteBuffer.putBuffer` |
| 1.7 | 162 | `sun.nio.ch.SocketDispatcher.read` |
| 1.5 | 139 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.4 | 128 | `java.util.TreeMap.getEntry` |
| 1.3 | 127 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.2 | 114 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.2 | 113 | `org.xnio.SingleOption.cast` |
| 1.0 | 91 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 91 | `java.util.Arrays.copyOfRange` |
| 0.9 | 89 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 0.8 | 74 | `java.util.WeakHashMap.matchesKey` |
| 0.8 | 74 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.8 | 72 | `tools.jackson.core.json.JsonWriteContext.writeValue` |
| 0.8 | 72 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 0.7 | 64 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.6 | 59 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 0.6 | 58 | `io.sinistral.proteus.server.ServerResponse.send` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 52.1 | 4914 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 36.9 | 3480 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 33.5 | 3161 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 33.0 | 3114 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.8 | 2056 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.5 | 1651 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 10.1 | 949 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 10.0 | 941 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 9.9 | 936 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 8.4 | 795 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.7 | 635 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.1 | 574 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.0 | 381 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.0 | 380 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.0 | 380 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 3.9 | 369 | `java.lang.Thread.run` |
| 3.7 | 351 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.7 | 347 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.7 | 347 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.5 | 334 | `java.util.TreeMap.put` |
| 3.5 | 333 | `java.util.TreeSet.add` |
| 3.2 | 297 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.0 | 282 | `sun.nio.ch.IOUtil.write` |
| 2.8 | 267 | `io.undertow.server.Connectors.executeRootHandler` |
| 2.6 | 249 | `io.undertow.channels.DetachableStreamSinkChannel.write` |

