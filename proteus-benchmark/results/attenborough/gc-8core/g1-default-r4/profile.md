# CPU profile

9409 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 81.1 | 7629 | `proteus I/O-*` |
| 10.7 | 1007 | `proteus task-*` |
| 6.8 | 641 | `(unnamed virtual thread)` |
| 1.0 | 95 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 38 | `main` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 31.7 | 2978 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.5 | 797 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.2 | 393 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.0 | 375 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.5 | 329 | `sun.nio.ch.IOUtil.write` |
| 3.4 | 316 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 3.2 | 302 | `java.util.TreeMap.put` |
| 2.1 | 195 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.6 | 146 | `java.nio.ByteBuffer.putBuffer` |
| 1.5 | 142 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.5 | 139 | `sun.nio.ch.SocketDispatcher.read` |
| 1.4 | 136 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.3 | 126 | `java.util.TreeMap.deleteEntry` |
| 1.3 | 125 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.1 | 108 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.1 | 103 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.0 | 98 | `org.xnio.SingleOption.cast` |
| 1.0 | 94 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 1.0 | 92 | `java.util.IdentityHashMap.get` |
| 0.9 | 85 | `java.util.TreeMap.successor` |
| 0.8 | 77 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.8 | 74 | `java.util.WeakHashMap.matchesKey` |
| 0.8 | 73 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.8 | 72 | `sun.nio.ch.SocketDispatcher.writev` |
| 0.8 | 71 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 50.7 | 4769 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 35.9 | 3377 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 32.3 | 3039 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 31.7 | 2978 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.0 | 1973 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.7 | 1666 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 8.5 | 797 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 7.2 | 673 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 6.7 | 628 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.6 | 622 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 6.5 | 613 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 5.9 | 553 | `sun.nio.ch.IOUtil.write` |
| 5.9 | 551 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.9 | 458 | `sun.nio.ch.SocketChannelImpl.write` |
| 4.7 | 442 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.7 | 442 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.7 | 441 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.7 | 439 | `java.lang.Thread.run` |
| 4.2 | 394 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.2 | 393 | `org.xnio.nio.NioSocketConduit.write` |
| 4.0 | 377 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.9 | 370 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.7 | 350 | `org.xnio.conduits.AbstractStreamSinkConduit.write` |
| 3.5 | 334 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.3 | 309 | `java.util.TreeMap.put` |

