# CPU profile

9377 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 80.7 | 7566 | `proteus I/O-*` |
| 11.7 | 1101 | `proteus task-*` |
| 6.5 | 609 | `(unnamed virtual thread)` |
| 0.8 | 76 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 28 | `main` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 29.0 | 2716 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.0 | 754 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.7 | 445 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.7 | 445 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.1 | 294 | `sun.nio.ch.IOUtil.write` |
| 2.5 | 238 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.3 | 217 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 2.2 | 203 | `java.nio.ByteBuffer.putBuffer` |
| 2.0 | 187 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.7 | 163 | `org.xnio.nio.NioHandle.resume` |
| 1.7 | 162 | `java.util.TreeMap.deleteEntry` |
| 1.6 | 146 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.4 | 129 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.3 | 121 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.2 | 117 | `java.util.TreeMap.getEntry` |
| 1.2 | 112 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.append` |
| 1.2 | 109 | `java.util.Arrays.copyOfRange` |
| 1.1 | 104 | `org.xnio.SingleOption.cast` |
| 1.1 | 104 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 97 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.9 | 87 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 86 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.7 | 70 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.7 | 69 | `java.util.HashMap.getNode` |
| 0.7 | 68 | `io.sinistral.proteus.server.ServerResponse.send` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 48.9 | 4584 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 34.1 | 3194 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 31.4 | 2941 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 29.0 | 2724 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.8 | 2042 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 16.5 | 1551 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 8.5 | 798 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 8.4 | 792 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 8.1 | 761 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 8.0 | 754 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.4 | 598 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.8 | 547 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 5.8 | 544 | `sun.nio.ch.IOUtil.write` |
| 5.4 | 509 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.4 | 509 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.4 | 507 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.3 | 493 | `java.lang.Thread.run` |
| 4.8 | 447 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.4 | 413 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.6 | 336 | `sun.nio.ch.SocketChannelImpl.write` |
| 3.3 | 314 | `java.util.TreeSet.remove` |
| 3.3 | 312 | `java.util.TreeMap.remove` |
| 3.3 | 311 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 3.2 | 298 | `org.xnio.nio.NioSocketConduit.write` |
| 3.2 | 297 | `org.xnio.conduits.AbstractStreamSinkConduit.write` |

