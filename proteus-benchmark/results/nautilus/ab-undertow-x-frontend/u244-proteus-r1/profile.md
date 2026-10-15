# CPU profile

10149 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 85.5 | 8674 | `proteus I/O-*` |
| 13.9 | 1409 | `proteus task-*` |
| 0.6 | 64 | `main` |
| 0.0 | 1 | `(unnamed virtual thread)` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 16.1 | 1629 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 8.7 | 880 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 7.7 | 785 | `org.xnio.nio.NioSocketConduit.read` |
| 5.7 | 582 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.6 | 463 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 3.1 | 318 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 3.1 | 313 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 3.1 | 310 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.8 | 286 | `sun.nio.ch.SocketDispatcher.read` |
| 2.7 | 271 | `java.util.TreeMap.put` |
| 2.0 | 203 | `java.util.IdentityHashMap.get` |
| 1.8 | 187 | `sun.nio.ch.SocketChannelImpl.read` |
| 1.7 | 176 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.6 | 166 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.4 | 138 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 1.3 | 129 | `org.xnio.SingleOption.cast` |
| 1.2 | 125 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.2 | 124 | `io.undertow.util.HeaderValues.isEmpty` |
| 1.2 | 118 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.1 | 115 | `sun.nio.ch.IOUtil.write` |
| 1.0 | 99 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.0 | 97 | `java.nio.ByteBuffer.putBuffer` |
| 0.9 | 95 | `java.util.TreeMap.successor` |
| 0.9 | 95 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.9 | 93 | `io.undertow.server.Connectors.setExchangeRequestPath` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 27.7 | 2813 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 26.0 | 2641 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 22.4 | 2270 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 21.4 | 2173 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 20.1 | 2040 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 16.3 | 1650 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 16.2 | 1642 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 14.9 | 1508 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 13.2 | 1342 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 11.4 | 1160 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 10.9 | 1111 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 9.6 | 976 | `org.xnio.nio.NioSocketConduit.read` |
| 9.6 | 974 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 9.3 | 945 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 8.7 | 887 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 5.7 | 582 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.6 | 564 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.5 | 563 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.3 | 536 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.3 | 534 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.7 | 480 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.6 | 466 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 4.3 | 437 | `sun.nio.ch.EPollSelectorImpl.doSelect` |
| 4.3 | 435 | `sun.nio.ch.SelectorImpl.lockAndDoSelect` |
| 4.3 | 434 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |

