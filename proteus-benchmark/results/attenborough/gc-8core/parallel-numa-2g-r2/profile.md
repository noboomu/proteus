# CPU profile

8832 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 81.0 | 7156 | `proteus I/O-*` |
| 11.6 | 1026 | `proteus task-*` |
| 6.1 | 539 | `(unnamed virtual thread)` |
| 0.9 | 79 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 37 | `main` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 32.3 | 2856 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 12.1 | 1065 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 5.0 | 440 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.6 | 403 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 4.2 | 367 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.9 | 343 | `java.util.IdentityHashMap.get` |
| 3.1 | 276 | `sun.nio.ch.IOUtil.write` |
| 2.9 | 256 | `java.util.TreeMap.put` |
| 1.8 | 163 | `sun.nio.ch.SocketDispatcher.read` |
| 1.4 | 126 | `java.nio.ByteBuffer.putBuffer` |
| 1.4 | 125 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.4 | 123 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.4 | 121 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.2 | 109 | `java.util.TreeMap.getEntry` |
| 1.2 | 103 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 0.9 | 79 | `java.util.TreeMap.deleteEntry` |
| 0.8 | 73 | `java.util.Arrays.copyOfRange` |
| 0.8 | 70 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.8 | 67 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.7 | 65 | `org.xnio.nio.WorkerThread.setOps` |
| 0.7 | 58 | `io.undertow.server.DefaultByteBufferPool.queueIfUnderMax` |
| 0.6 | 57 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.6 | 56 | `java.util.TreeSet.add` |
| 0.6 | 53 | `java.util.HashMap.getNode` |
| 0.6 | 52 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 51.6 | 4554 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 37.6 | 3320 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 32.6 | 2877 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 32.4 | 2858 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.3 | 1885 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 19.0 | 1677 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 12.6 | 1114 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 6.5 | 573 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 5.7 | 507 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 5.5 | 487 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.5 | 487 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.5 | 484 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.4 | 476 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.3 | 470 | `java.lang.Thread.run` |
| 5.2 | 461 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 5.2 | 457 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.1 | 452 | `sun.nio.ch.IOUtil.write` |
| 5.0 | 444 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.2 | 374 | `sun.nio.ch.SocketChannelImpl.write` |
| 3.9 | 343 | `java.util.IdentityHashMap.get` |
| 3.9 | 342 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.8 | 337 | `org.xnio.nio.NioSocketConduit.write` |
| 3.7 | 330 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.6 | 317 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.6 | 317 | `java.util.TreeSet.add` |

