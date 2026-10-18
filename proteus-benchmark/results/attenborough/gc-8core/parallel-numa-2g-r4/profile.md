# CPU profile

8895 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 80.3 | 7140 | `proteus I/O-*` |
| 11.9 | 1059 | `proteus task-*` |
| 6.6 | 587 | `(unnamed virtual thread)` |
| 0.9 | 83 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 27 | `main` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 1 | `VirtualThread-unblocker` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 32.2 | 2865 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 10.7 | 956 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 4.5 | 402 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.4 | 394 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 4.2 | 373 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.6 | 317 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.9 | 261 | `java.util.IdentityHashMap.get` |
| 2.7 | 242 | `org.xnio.nio.NioHandle.resume` |
| 1.8 | 161 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.7 | 150 | `sun.nio.ch.SocketDispatcher.read` |
| 1.6 | 139 | `org.xnio.SingleOption.cast` |
| 1.4 | 123 | `java.nio.ByteBuffer.putBuffer` |
| 1.3 | 117 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.3 | 113 | `java.util.TreeMap.deleteEntry` |
| 1.0 | 86 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.9 | 83 | `java.util.TreeMap.successor` |
| 0.9 | 79 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.9 | 76 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.8 | 74 | `java.util.Arrays.copyOfRange` |
| 0.8 | 71 | `io.undertow.server.DefaultByteBufferPool.queueIfUnderMax` |
| 0.8 | 70 | `io.undertow.util.HeaderMap.getEntry` |
| 0.8 | 67 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 0.7 | 63 | `java.util.TreeMap.put` |
| 0.7 | 60 | `org.xnio.nio.WorkerThread.setOps` |
| 0.6 | 57 | `sun.nio.ch.SocketDispatcher.writev` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 50.3 | 4470 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 37.5 | 3333 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 32.7 | 2912 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 32.3 | 2871 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 19.7 | 1753 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.6 | 1563 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 11.6 | 1030 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 10.3 | 914 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 9.1 | 810 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 9.1 | 807 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.6 | 585 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.5 | 487 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.5 | 487 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.4 | 484 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.3 | 475 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.3 | 474 | `java.lang.Thread.run` |
| 4.5 | 404 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.1 | 361 | `org.xnio.OptionMap.get` |
| 3.9 | 347 | `org.xnio.nio.NioHandle.resume` |
| 3.8 | 336 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 3.7 | 330 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 3.4 | 304 | `org.xnio.conduits.AbstractSourceConduit.resumeReads` |
| 3.2 | 285 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 3.2 | 283 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 2.9 | 261 | `java.util.IdentityHashMap.get` |

