# CPU profile

10698 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 90.5 | 9684 | `proteus I/O-*` |
| 5.6 | 604 | `proteus task-*` |
| 3.2 | 337 | `(unnamed virtual thread)` |
| 0.4 | 40 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 34 | `main` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 52.9 | 5663 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 5.9 | 627 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.7 | 392 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.4 | 261 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.4 | 259 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.2 | 239 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 1.6 | 171 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.4 | 151 | `org.xnio.nio.NioHandle.resume` |
| 1.3 | 141 | `java.nio.ByteBuffer.putBuffer` |
| 1.3 | 139 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.1 | 116 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.0 | 106 | `org.xnio.SingleOption.cast` |
| 1.0 | 104 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 101 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 0.9 | 93 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 0.9 | 91 | `java.util.TreeMap.deleteEntry` |
| 0.8 | 90 | `java.util.TreeMap.successor` |
| 0.8 | 86 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.7 | 77 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.7 | 73 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.6 | 63 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 0.6 | 61 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.5 | 55 | `sun.nio.ch.IOUtil.write` |
| 0.5 | 53 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.5 | 52 | `java.util.Arrays.copyOfRange` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 66.9 | 7159 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 56.1 | 6006 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 53.6 | 5729 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 53.0 | 5666 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 15.5 | 1654 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 12.9 | 1379 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 7.8 | 838 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.7 | 822 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 7.6 | 816 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.9 | 627 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.4 | 580 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.7 | 498 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.6 | 277 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 2.6 | 277 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 2.6 | 273 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 2.5 | 263 | `java.lang.Thread.run` |
| 2.4 | 258 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 2.3 | 248 | `sun.nio.ch.IOUtil.write` |
| 2.3 | 244 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.2 | 234 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 1.9 | 204 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.8 | 197 | `org.xnio.nio.NioHandle.resume` |
| 1.8 | 194 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 1.7 | 187 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 1.7 | 184 | `io.undertow.server.Connectors.setExchangeRequestPath` |

