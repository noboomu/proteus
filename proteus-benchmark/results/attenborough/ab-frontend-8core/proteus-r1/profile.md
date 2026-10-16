# CPU profile

9938 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 82.3 | 8179 | `proteus I/O-*` |
| 10.0 | 993 | `proteus task-*` |
| 6.5 | 649 | `(unnamed virtual thread)` |
| 0.9 | 89 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 29 | `main` |
| 0.0 | 4 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 32.3 | 3209 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 7.9 | 783 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.0 | 495 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.9 | 391 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.5 | 345 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 3.1 | 313 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.5 | 248 | `java.util.IdentityHashMap.get` |
| 2.3 | 231 | `org.xnio.nio.WorkerThread.setOps` |
| 1.9 | 190 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.7 | 164 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.5 | 152 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.5 | 151 | `sun.nio.ch.SocketDispatcher.read` |
| 1.5 | 147 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.5 | 146 | `java.nio.ByteBuffer.putBuffer` |
| 1.3 | 130 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.3 | 125 | `java.util.TreeMap.put` |
| 1.2 | 117 | `java.util.TreeMap.deleteEntry` |
| 1.0 | 97 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.9 | 93 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.9 | 90 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.8 | 83 | `java.util.TreeMap.successor` |
| 0.8 | 75 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.8 | 75 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.7 | 74 | `sun.nio.ch.IOUtil.write` |
| 0.7 | 73 | `io.undertow.server.Connectors.setExchangeRequestPath` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 50.5 | 5017 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 36.1 | 3587 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 32.9 | 3267 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 32.4 | 3216 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 20.1 | 2001 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.5 | 1738 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 11.6 | 1157 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 11.1 | 1102 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 11.0 | 1093 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 7.9 | 783 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 7.4 | 734 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 7.0 | 691 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.3 | 430 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.3 | 430 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.3 | 429 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.2 | 420 | `java.lang.Thread.run` |
| 3.9 | 391 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.2 | 322 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 3.1 | 305 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 2.9 | 292 | `sun.nio.ch.IOUtil.write` |
| 2.8 | 282 | `org.xnio.nio.NioHandle.resume` |
| 2.8 | 276 | `org.xnio.nio.WorkerThread.setOps` |
| 2.7 | 267 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 2.6 | 263 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 2.5 | 249 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |

