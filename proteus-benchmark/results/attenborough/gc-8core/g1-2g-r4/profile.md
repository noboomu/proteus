# CPU profile

10213 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.5 | 9141 | `proteus I/O-*` |
| 6.1 | 622 | `proteus task-*` |
| 3.5 | 361 | `(unnamed virtual thread)` |
| 0.5 | 50 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 38 | `main` |
| 0.0 | 2 | `proteus Accept` |
| 0.0 | 2 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 54.6 | 5575 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 5.3 | 538 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 2.9 | 299 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.6 | 267 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.4 | 242 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.4 | 241 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.8 | 179 | `org.xnio.nio.NioHandle.resume` |
| 1.4 | 147 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.3 | 130 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.1 | 116 | `java.util.IdentityHashMap.get` |
| 1.1 | 109 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 105 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.0 | 98 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 0.9 | 93 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 89 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.9 | 87 | `org.xnio.SingleOption.cast` |
| 0.8 | 85 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 0.8 | 81 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.7 | 75 | `java.util.TreeMap.deleteEntry` |
| 0.6 | 57 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 0.5 | 54 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.5 | 50 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.5 | 48 | `java.util.WeakHashMap.matchesKey` |
| 0.5 | 47 | `sun.nio.ch.IOUtil.write` |
| 0.4 | 44 | `java.util.concurrent.locks.LockSupport.unpark` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 67.2 | 6866 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 57.5 | 5874 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 54.8 | 5601 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 54.6 | 5577 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 14.3 | 1459 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 11.5 | 1177 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 7.4 | 751 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.0 | 716 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 7.0 | 711 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.3 | 538 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.8 | 487 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.2 | 424 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.8 | 289 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 2.8 | 289 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 2.8 | 286 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 2.7 | 280 | `java.lang.Thread.run` |
| 2.6 | 270 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.2 | 229 | `org.xnio.nio.NioHandle.resume` |
| 2.2 | 223 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 2.2 | 221 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 2.2 | 221 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 2.1 | 215 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 2.0 | 204 | `org.xnio.conduits.AbstractSourceConduit.resumeReads` |
| 2.0 | 202 | `sun.nio.ch.IOUtil.write` |
| 1.8 | 185 | `org.xnio.conduits.ConduitStreamSourceChannel.resumeReads` |

