# CPU profile

9609 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 81.7 | 7846 | `proteus I/O-*` |
| 11.2 | 1073 | `proteus task-*` |
| 5.9 | 563 | `(unnamed virtual thread)` |
| 0.9 | 89 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 35 | `main` |
| 0.0 | 3 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 30.7 | 2952 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.3 | 800 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.6 | 441 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.4 | 419 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.6 | 350 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 3.1 | 301 | `java.util.IdentityHashMap.get` |
| 2.5 | 239 | `org.xnio.nio.WorkerThread.setOps` |
| 2.3 | 221 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.1 | 198 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 2.0 | 188 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.8 | 171 | `sun.nio.ch.IOUtil.write` |
| 1.8 | 170 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.7 | 166 | `java.nio.ByteBuffer.putBuffer` |
| 1.6 | 149 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.4 | 138 | `java.util.TreeMap.deleteEntry` |
| 1.3 | 127 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.1 | 102 | `java.util.TreeMap.getEntry` |
| 1.0 | 94 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.9 | 82 | `java.util.WeakHashMap.matchesKey` |
| 0.8 | 77 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.8 | 77 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.7 | 71 | `sun.nio.ch.SocketDispatcher.read` |
| 0.7 | 70 | `java.util.Arrays.copyOfRange` |
| 0.7 | 69 | `java.util.HashMap.getNode` |
| 0.7 | 65 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 51.4 | 4942 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 36.7 | 3529 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 32.8 | 3151 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 30.7 | 2953 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 22.1 | 2126 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 18.1 | 1740 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 10.2 | 980 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 9.6 | 919 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 9.4 | 907 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 8.3 | 800 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.4 | 615 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.8 | 557 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.2 | 503 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.2 | 503 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.2 | 498 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.1 | 489 | `java.lang.Thread.run` |
| 4.4 | 424 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.3 | 411 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 4.2 | 402 | `sun.nio.ch.IOUtil.write` |
| 3.1 | 301 | `java.util.IdentityHashMap.get` |
| 3.0 | 293 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 3.0 | 290 | `org.xnio.nio.NioHandle.resume` |
| 2.9 | 279 | `org.xnio.nio.WorkerThread.setOps` |
| 2.9 | 275 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 2.9 | 275 | `java.util.TreeSet.remove` |

