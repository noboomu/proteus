# CPU profile

9400 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 81.4 | 7649 | `proteus I/O-*` |
| 10.6 | 995 | `proteus task-*` |
| 6.9 | 644 | `(unnamed virtual thread)` |
| 0.9 | 85 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 29 | `main` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 30.8 | 2899 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 7.2 | 680 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.3 | 407 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.8 | 361 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.3 | 307 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.9 | 277 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.7 | 255 | `java.util.IdentityHashMap.get` |
| 2.1 | 200 | `org.xnio.nio.NioHandle.resume` |
| 2.0 | 187 | `java.nio.ByteBuffer.putBuffer` |
| 1.9 | 183 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.8 | 166 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.6 | 149 | `java.util.TreeMap.getEntry` |
| 1.5 | 144 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.4 | 128 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.4 | 128 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.4 | 127 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.3 | 125 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.1 | 108 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.append` |
| 1.0 | 93 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 89 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.9 | 88 | `java.util.WeakHashMap.matchesKey` |
| 0.9 | 81 | `io.undertow.util.HeaderMap.getEntry` |
| 0.9 | 80 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.8 | 76 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.8 | 73 | `java.util.HashMap.getNode` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 49.5 | 4656 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 35.9 | 3377 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 32.4 | 3046 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 30.9 | 2902 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.0 | 1978 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 16.4 | 1537 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 10.6 | 1000 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 9.9 | 932 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 9.8 | 923 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.2 | 680 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.4 | 600 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.4 | 598 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.3 | 408 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.3 | 408 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.3 | 403 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.1 | 389 | `java.lang.Thread.run` |
| 3.9 | 364 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.6 | 336 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.5 | 328 | `sun.nio.ch.IOUtil.write` |
| 3.1 | 289 | `org.xnio.nio.NioHandle.resume` |
| 3.0 | 279 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 2.9 | 269 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 2.8 | 266 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 2.7 | 258 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 2.7 | 258 | `io.undertow.channels.DetachableStreamSinkChannel.write` |

