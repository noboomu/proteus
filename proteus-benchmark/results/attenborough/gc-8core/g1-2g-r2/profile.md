# CPU profile

9069 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 80.1 | 7262 | `proteus I/O-*` |
| 11.1 | 1005 | `proteus task-*` |
| 7.5 | 684 | `(unnamed virtual thread)` |
| 1.0 | 89 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 37 | `main` |
| 0.0 | 1 | `Finalizer` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 29.6 | 2682 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 7.8 | 705 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.5 | 410 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.2 | 383 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.2 | 294 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 3.2 | 294 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 3.2 | 290 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.6 | 235 | `org.xnio.nio.WorkerThread.setOps` |
| 1.9 | 168 | `java.nio.ByteBuffer.putBuffer` |
| 1.8 | 162 | `java.util.IdentityHashMap.get` |
| 1.8 | 159 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.5 | 132 | `java.util.TreeMap.getEntry` |
| 1.4 | 130 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.2 | 111 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.2 | 109 | `org.xnio.SingleOption.cast` |
| 1.2 | 108 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.2 | 105 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.0 | 95 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 0.9 | 81 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.9 | 79 | `java.util.Arrays.copyOfRange` |
| 0.8 | 76 | `sun.nio.ch.IOUtil.read` |
| 0.8 | 75 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.8 | 73 | `java.util.WeakHashMap.matchesKey` |
| 0.7 | 65 | `java.util.HashMap.getNode` |
| 0.7 | 65 | `sun.nio.ch.IOUtil.write` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 50.1 | 4541 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 36.1 | 3277 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 32.8 | 2976 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 29.6 | 2683 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 23.0 | 2082 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 16.0 | 1454 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 9.9 | 898 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 9.4 | 854 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 9.4 | 852 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.8 | 705 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.8 | 528 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.7 | 519 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.0 | 453 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.0 | 453 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.0 | 449 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.9 | 443 | `java.lang.Thread.run` |
| 4.5 | 412 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.5 | 320 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.4 | 305 | `sun.nio.ch.IOUtil.write` |
| 3.0 | 274 | `org.xnio.nio.NioHandle.resume` |
| 3.0 | 273 | `org.xnio.nio.WorkerThread.setOps` |
| 2.9 | 264 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 2.9 | 262 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 2.7 | 247 | `io.sinistral.proteus.server.ServerResponse.send` |
| 2.6 | 238 | `org.xnio.conduits.AbstractSourceConduit.resumeReads` |

