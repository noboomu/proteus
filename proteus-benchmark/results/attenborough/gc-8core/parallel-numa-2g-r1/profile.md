# CPU profile

8930 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 80.8 | 7212 | `proteus I/O-*` |
| 11.4 | 1016 | `proteus task-*` |
| 6.7 | 600 | `(unnamed virtual thread)` |
| 0.8 | 70 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 38 | `main` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 31.4 | 2808 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 11.6 | 1034 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 4.9 | 436 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 4.7 | 420 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.2 | 372 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.2 | 286 | `sun.nio.ch.IOUtil.write` |
| 3.0 | 269 | `org.xnio.nio.WorkerThread.setOps` |
| 2.7 | 245 | `java.util.IdentityHashMap.get` |
| 1.7 | 154 | `sun.nio.ch.SocketDispatcher.read` |
| 1.7 | 149 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.5 | 131 | `java.nio.ByteBuffer.putBuffer` |
| 1.4 | 123 | `java.util.TreeMap.deleteEntry` |
| 1.3 | 118 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.2 | 104 | `java.util.TreeMap.put` |
| 1.2 | 103 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.1 | 97 | `org.xnio.SingleOption.cast` |
| 1.0 | 92 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.0 | 85 | `java.util.TreeMap.successor` |
| 0.9 | 80 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.8 | 75 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 0.8 | 68 | `io.undertow.server.DefaultByteBufferPool.queueIfUnderMax` |
| 0.8 | 68 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.7 | 63 | `java.util.Arrays.copyOfRange` |
| 0.6 | 53 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 0.6 | 51 | `sun.nio.ch.SocketDispatcher.writev` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 50.8 | 4537 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 37.2 | 3326 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 32.0 | 2862 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 31.5 | 2809 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.2 | 1892 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 19.1 | 1704 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 12.3 | 1097 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 6.5 | 583 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 6.0 | 539 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 5.9 | 523 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.8 | 517 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.8 | 517 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.7 | 513 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.7 | 507 | `java.lang.Thread.run` |
| 5.4 | 478 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 5.4 | 478 | `sun.nio.ch.IOUtil.write` |
| 5.1 | 456 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.8 | 425 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.4 | 396 | `sun.nio.ch.SocketChannelImpl.write` |
| 4.0 | 354 | `org.xnio.nio.NioSocketConduit.write` |
| 3.9 | 351 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.7 | 331 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 3.7 | 326 | `org.xnio.conduits.AbstractStreamSinkConduit.write` |
| 3.6 | 324 | `org.xnio.OptionMap.get` |
| 3.6 | 322 | `org.xnio.nio.NioHandle.resume` |

