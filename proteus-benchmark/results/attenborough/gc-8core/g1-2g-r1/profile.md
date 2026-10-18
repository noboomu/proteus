# CPU profile

8465 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 78.9 | 6676 | `proteus I/O-*` |
| 13.0 | 1101 | `proteus task-*` |
| 6.6 | 562 | `(unnamed virtual thread)` |
| 1.1 | 96 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 31 | `main` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 31.9 | 2702 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 7.8 | 659 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.6 | 470 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.2 | 357 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.3 | 282 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 3.1 | 260 | `java.util.TreeMap.put` |
| 2.4 | 205 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.3 | 197 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.7 | 146 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.6 | 133 | `sun.nio.ch.IOUtil.write` |
| 1.5 | 131 | `java.util.IdentityHashMap.get` |
| 1.5 | 131 | `java.util.TreeMap.getEntry` |
| 1.5 | 123 | `java.nio.ByteBuffer.putBuffer` |
| 1.4 | 116 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.3 | 112 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.2 | 99 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.1 | 96 | `org.xnio.SingleOption.cast` |
| 1.1 | 91 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.1 | 90 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.9 | 74 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.9 | 74 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.8 | 64 | `sun.nio.ch.SocketDispatcher.read` |
| 0.7 | 60 | `java.util.Arrays.copyOfRange` |
| 0.6 | 55 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.6 | 54 | `org.xnio.nio.WorkerThread.setOps` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 51.3 | 4343 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 37.2 | 3146 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 33.6 | 2848 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 31.9 | 2702 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.4 | 1808 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.0 | 1443 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 9.4 | 798 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 8.8 | 744 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 8.8 | 741 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 7.8 | 659 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.3 | 532 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 6.3 | 532 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 6.2 | 528 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 6.1 | 520 | `java.lang.Thread.run` |
| 5.9 | 502 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.6 | 475 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 5.6 | 470 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.8 | 319 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.5 | 300 | `sun.nio.ch.IOUtil.write` |
| 3.5 | 299 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.3 | 282 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.2 | 267 | `java.util.TreeMap.put` |
| 3.2 | 267 | `java.util.TreeSet.add` |
| 2.7 | 225 | `java.util.TreeSet.remove` |
| 2.7 | 225 | `java.util.TreeMap.remove` |

