# CPU profile

9550 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 84.1 | 8028 | `proteus I/O-*` |
| 15.4 | 1467 | `proteus task-*` |
| 0.5 | 51 | `main` |
| 0.0 | 2 | `(unnamed virtual thread)` |
| 0.0 | 2 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 25.3 | 2416 | `java.nio.ByteBuffer.position` |
| 7.1 | 676 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.5 | 621 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 5.3 | 508 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 3.2 | 302 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 3.0 | 288 | `java.util.TreeMap.put` |
| 2.2 | 208 | `java.util.IdentityHashMap.get` |
| 2.1 | 203 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.9 | 185 | `java.util.TreeMap.successor` |
| 1.6 | 157 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 1.6 | 149 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.6 | 149 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.5 | 148 | `java.util.TreeMap.deleteEntry` |
| 1.5 | 141 | `sun.nio.ch.IOUtil.write` |
| 1.4 | 138 | `sun.nio.ch.SocketDispatcher.read` |
| 1.3 | 124 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.2 | 111 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.2 | 110 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 107 | `sun.nio.ch.EPollSelectorImpl.doSelect` |
| 1.1 | 103 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.0 | 97 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 92 | `java.util.Arrays.copyOfRange` |
| 0.9 | 90 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.9 | 90 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.9 | 86 | `io.undertow.util.HeaderMap.getOrCreateEntry` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 25.7 | 2458 | `java.nio.ByteBuffer.position` |
| 25.7 | 2454 | `java.nio.ByteBuffer.<init>` |
| 25.7 | 2454 | `java.nio.Buffer.<init>` |
| 25.4 | 2422 | `java.nio.HeapByteBuffer.<init>` |
| 25.3 | 2414 | `java.nio.ByteBuffer.allocate` |
| 22.4 | 2135 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 18.4 | 1761 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 15.0 | 1437 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 9.5 | 912 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 7.7 | 733 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 7.5 | 720 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.1 | 676 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.7 | 638 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.6 | 629 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 6.4 | 610 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 6.2 | 596 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 6.2 | 595 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.9 | 565 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.4 | 517 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 5.3 | 510 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 5.3 | 508 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 5.2 | 498 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.0 | 476 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.9 | 375 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 3.5 | 330 | `java.util.TreeMap.put` |

