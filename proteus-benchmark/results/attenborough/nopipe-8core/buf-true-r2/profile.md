# CPU profile

9758 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 82.9 | 8090 | `proteus I/O-*` |
| 9.9 | 962 | `proteus task-*` |
| 6.1 | 593 | `(unnamed virtual thread)` |
| 0.8 | 80 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 37 | `main` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 33.5 | 3269 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.0 | 782 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.5 | 440 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.6 | 354 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.4 | 333 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 3.0 | 297 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.3 | 224 | `org.xnio.nio.WorkerThread.setOps` |
| 2.1 | 209 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.6 | 157 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.6 | 155 | `java.nio.ByteBuffer.putBuffer` |
| 1.5 | 145 | `org.xnio.SingleOption.cast` |
| 1.4 | 140 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.4 | 139 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.4 | 133 | `sun.nio.ch.SocketDispatcher.read` |
| 1.4 | 132 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.3 | 130 | `java.util.IdentityHashMap.get` |
| 1.2 | 118 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.1 | 108 | `java.util.TreeMap.put` |
| 1.0 | 96 | `java.util.TreeMap.deleteEntry` |
| 0.9 | 83 | `sun.nio.ch.IOUtil.write` |
| 0.8 | 81 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.8 | 79 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.8 | 77 | `sun.nio.ch.SocketDispatcher.writev` |
| 0.7 | 68 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.7 | 67 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 52.5 | 5120 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 37.9 | 3702 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 34.2 | 3337 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 33.5 | 3272 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 22.1 | 2159 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.9 | 1748 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 10.6 | 1039 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 10.1 | 982 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 10.0 | 980 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 8.0 | 782 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.8 | 667 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.4 | 628 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.6 | 446 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.6 | 445 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.5 | 440 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.4 | 429 | `java.lang.Thread.run` |
| 3.7 | 358 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.4 | 334 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.3 | 320 | `sun.nio.ch.IOUtil.write` |
| 2.9 | 286 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 2.8 | 274 | `org.xnio.nio.NioHandle.resume` |
| 2.8 | 273 | `org.xnio.nio.WorkerThread.setOps` |
| 2.7 | 259 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 2.6 | 253 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 2.5 | 245 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |

