# CPU profile

9482 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 82.2 | 7791 | `proteus I/O-*` |
| 9.7 | 920 | `proteus task-*` |
| 6.8 | 643 | `(unnamed virtual thread)` |
| 1.0 | 93 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 35 | `main` |
| 0.0 | 2 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 33.4 | 3171 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.0 | 762 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.3 | 412 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.9 | 367 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 3.6 | 345 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.9 | 276 | `sun.nio.ch.IOUtil.write` |
| 2.7 | 253 | `org.xnio.nio.WorkerThread.setOps` |
| 1.9 | 179 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.5 | 142 | `java.nio.ByteBuffer.putBuffer` |
| 1.5 | 138 | `sun.nio.ch.SocketDispatcher.read` |
| 1.4 | 137 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.4 | 132 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.3 | 121 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.2 | 118 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.2 | 116 | `org.xnio.SingleOption.cast` |
| 1.1 | 103 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.1 | 103 | `java.util.TreeMap.deleteEntry` |
| 0.9 | 88 | `java.util.TreeMap.put` |
| 0.9 | 86 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.9 | 84 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.9 | 81 | `java.util.IdentityHashMap.get` |
| 0.8 | 78 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.8 | 78 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.8 | 74 | `sun.nio.ch.SocketDispatcher.writev` |
| 0.8 | 73 | `java.util.WeakHashMap.matchesKey` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 52.5 | 4976 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 38.0 | 3605 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 34.0 | 3227 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 33.5 | 3172 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.8 | 2064 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.6 | 1672 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 8.0 | 762 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 7.6 | 720 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.3 | 692 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.0 | 665 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.0 | 569 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.5 | 521 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 5.3 | 499 | `sun.nio.ch.IOUtil.write` |
| 4.5 | 428 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.5 | 428 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.5 | 426 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.0 | 376 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.9 | 373 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.9 | 367 | `sun.nio.ch.SocketChannelImpl.write` |
| 3.9 | 367 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 3.2 | 304 | `org.xnio.nio.NioHandle.resume` |
| 3.2 | 302 | `org.xnio.nio.WorkerThread.setOps` |
| 3.2 | 300 | `org.xnio.nio.NioSocketConduit.write` |
| 3.1 | 291 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 3.0 | 284 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |

