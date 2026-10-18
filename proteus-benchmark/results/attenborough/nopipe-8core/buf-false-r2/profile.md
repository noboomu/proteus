# CPU profile

11158 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 90.8 | 10133 | `proteus I/O-*` |
| 5.1 | 568 | `proteus task-*` |
| 3.4 | 377 | `(unnamed virtual thread)` |
| 0.4 | 48 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 31 | `main` |
| 0.0 | 1 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 54.4 | 6067 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 5.4 | 603 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.2 | 357 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.8 | 315 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.2 | 240 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.0 | 227 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 1.5 | 163 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.4 | 155 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.4 | 155 | `java.util.IdentityHashMap.get` |
| 1.3 | 150 | `org.xnio.nio.NioHandle.resume` |
| 1.3 | 144 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.3 | 141 | `org.xnio.SingleOption.cast` |
| 1.1 | 122 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 114 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 0.9 | 104 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.9 | 96 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 0.7 | 81 | `java.util.Arrays.copyOfRange` |
| 0.7 | 79 | `java.util.TreeMap.deleteEntry` |
| 0.6 | 68 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.6 | 67 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 0.6 | 65 | `sun.nio.ch.IOUtil.write` |
| 0.6 | 63 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.6 | 63 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.5 | 56 | `sun.nio.ch.SocketDispatcher.read` |
| 0.4 | 48 | `io.undertow.server.Connectors.setExchangeRequestPath` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 67.6 | 7539 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 58.0 | 6472 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 55.7 | 6215 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 54.4 | 6070 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 14.9 | 1665 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 11.6 | 1290 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 8.3 | 927 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.8 | 865 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 7.7 | 860 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.4 | 607 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.4 | 603 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.7 | 530 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.5 | 281 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 2.4 | 271 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 2.4 | 266 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 2.4 | 265 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 2.3 | 257 | `java.lang.Thread.run` |
| 2.2 | 246 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 2.2 | 242 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.1 | 234 | `sun.nio.ch.IOUtil.write` |
| 2.1 | 233 | `org.xnio.OptionMap.get` |
| 1.9 | 215 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 1.9 | 209 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.8 | 205 | `org.xnio.nio.WorkerThread.run` |
| 1.8 | 200 | `sun.nio.ch.EPollSelectorImpl.doSelect` |

