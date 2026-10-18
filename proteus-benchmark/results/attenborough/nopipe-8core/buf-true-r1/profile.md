# CPU profile

9481 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 82.7 | 7839 | `proteus I/O-*` |
| 10.2 | 963 | `proteus task-*` |
| 5.9 | 557 | `(unnamed virtual thread)` |
| 0.9 | 90 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 33 | `main` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 33.0 | 3127 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 7.9 | 752 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.6 | 440 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.8 | 358 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.5 | 334 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.8 | 266 | `java.util.TreeMap.put` |
| 2.2 | 212 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.8 | 175 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.8 | 173 | `java.util.IdentityHashMap.get` |
| 1.8 | 169 | `sun.nio.ch.SocketDispatcher.read` |
| 1.7 | 163 | `java.nio.ByteBuffer.putBuffer` |
| 1.6 | 149 | `sun.nio.ch.IOUtil.write` |
| 1.4 | 136 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.3 | 126 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.3 | 123 | `java.util.TreeMap.deleteEntry` |
| 1.1 | 101 | `org.xnio.SingleOption.cast` |
| 1.0 | 99 | `java.util.TreeMap.successor` |
| 1.0 | 97 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 0.9 | 83 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.8 | 79 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.8 | 76 | `io.undertow.util.HeaderMap.getEntry` |
| 0.8 | 75 | `sun.nio.ch.SocketDispatcher.writev` |
| 0.7 | 71 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.7 | 65 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.7 | 63 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 51.7 | 4902 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 37.5 | 3552 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 33.7 | 3191 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 33.0 | 3129 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 20.7 | 1960 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.2 | 1634 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 9.6 | 907 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 8.9 | 847 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 8.7 | 829 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.9 | 752 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.4 | 604 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.9 | 561 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.3 | 409 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.3 | 409 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.3 | 407 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.3 | 406 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 4.1 | 392 | `java.lang.Thread.run` |
| 4.1 | 391 | `sun.nio.ch.IOUtil.write` |
| 4.0 | 378 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.8 | 361 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.8 | 359 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.0 | 286 | `java.util.TreeSet.add` |
| 2.9 | 273 | `java.util.TreeMap.put` |
| 2.7 | 255 | `io.undertow.server.Connectors.executeRootHandler` |
| 2.5 | 234 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |

