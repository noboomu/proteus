# CPU profile

9360 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 80.5 | 7534 | `proteus I/O-*` |
| 11.3 | 1059 | `proteus task-*` |
| 6.9 | 649 | `(unnamed virtual thread)` |
| 1.0 | 89 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 33 | `main` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 29.1 | 2722 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.8 | 826 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.6 | 426 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.3 | 404 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.7 | 342 | `sun.nio.ch.IOUtil.write` |
| 3.3 | 312 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 3.0 | 283 | `org.xnio.nio.WorkerThread.setOps` |
| 2.6 | 243 | `java.util.IdentityHashMap.get` |
| 2.0 | 184 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.8 | 165 | `sun.nio.ch.SocketDispatcher.read` |
| 1.7 | 158 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.7 | 157 | `java.nio.ByteBuffer.putBuffer` |
| 1.6 | 154 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.4 | 130 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.4 | 127 | `java.util.TreeMap.getEntry` |
| 1.3 | 118 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.1 | 101 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.1 | 100 | `io.undertow.server.DefaultByteBufferPool.queueIfUnderMax` |
| 1.1 | 99 | `java.util.TreeMap.put` |
| 1.0 | 98 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 1.0 | 93 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 0.8 | 71 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.7 | 70 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.7 | 68 | `java.util.HashMap.getNode` |
| 0.7 | 68 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 48.3 | 4521 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 32.9 | 3080 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 29.5 | 2758 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 29.1 | 2724 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 22.1 | 2068 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 18.4 | 1726 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 8.8 | 826 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 8.0 | 749 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.4 | 693 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.3 | 687 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.6 | 621 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 6.5 | 608 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 5.9 | 550 | `sun.nio.ch.IOUtil.write` |
| 5.0 | 466 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.0 | 466 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.9 | 462 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.8 | 447 | `java.lang.Thread.run` |
| 4.7 | 440 | `sun.nio.ch.SocketChannelImpl.write` |
| 4.3 | 406 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.3 | 403 | `org.xnio.nio.NioSocketConduit.write` |
| 3.9 | 369 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.8 | 354 | `org.xnio.conduits.AbstractStreamSinkConduit.write` |
| 3.5 | 326 | `org.xnio.nio.NioHandle.resume` |
| 3.5 | 324 | `org.xnio.nio.WorkerThread.setOps` |
| 3.3 | 312 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |

