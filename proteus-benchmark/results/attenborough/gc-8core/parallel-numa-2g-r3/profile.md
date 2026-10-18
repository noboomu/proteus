# CPU profile

8840 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 80.5 | 7120 | `proteus I/O-*` |
| 11.9 | 1053 | `proteus task-*` |
| 6.3 | 561 | `(unnamed virtual thread)` |
| 0.8 | 71 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 37 | `main` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 33.3 | 2946 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 9.3 | 821 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 4.8 | 421 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.3 | 377 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.7 | 326 | `java.util.IdentityHashMap.get` |
| 3.5 | 305 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 3.2 | 286 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.4 | 210 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 2.1 | 188 | `org.xnio.nio.NioHandle.resume` |
| 1.8 | 163 | `sun.nio.ch.SocketDispatcher.read` |
| 1.6 | 143 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.5 | 132 | `java.nio.ByteBuffer.putBuffer` |
| 1.4 | 122 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 1.1 | 101 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.1 | 99 | `java.util.TreeMap.deleteEntry` |
| 1.1 | 93 | `java.util.Arrays.copyOfRange` |
| 0.9 | 77 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.8 | 75 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 0.8 | 73 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.8 | 71 | `java.util.WeakHashMap.matchesKey` |
| 0.8 | 68 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.7 | 65 | `java.util.TreeMap.put` |
| 0.6 | 54 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 0.6 | 53 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 0.6 | 53 | `org.xnio.nio.WorkerThread.setOps` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 51.9 | 4590 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 38.1 | 3372 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 34.0 | 3004 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 33.4 | 2950 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 20.5 | 1813 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 16.9 | 1498 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 10.1 | 890 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 9.8 | 864 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 8.9 | 787 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 8.7 | 771 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.7 | 593 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.4 | 567 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 6.4 | 567 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 6.4 | 565 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 6.3 | 556 | `java.lang.Thread.run` |
| 5.2 | 464 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.8 | 425 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.6 | 410 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 3.7 | 326 | `java.util.IdentityHashMap.get` |
| 3.2 | 286 | `org.xnio.OptionMap.get` |
| 3.2 | 284 | `org.xnio.nio.NioHandle.resume` |
| 3.2 | 281 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 3.0 | 269 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 3.0 | 268 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 3.0 | 262 | `org.xnio.nio.NioSocketConduit.resumeReads` |

