# CPU profile

9604 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 85.0 | 8163 | `proteus I/O-*` |
| 14.3 | 1376 | `proteus task-*` |
| 0.6 | 54 | `main` |
| 0.1 | 7 | `proteus Accept` |
| 0.0 | 3 | `(unnamed virtual thread)` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 27.4 | 2627 | `java.nio.ByteBuffer.position` |
| 7.1 | 682 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 6.5 | 620 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.9 | 474 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 3.1 | 302 | `java.util.IdentityHashMap.get` |
| 2.9 | 282 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.3 | 221 | `java.util.TreeMap.getEntry` |
| 1.9 | 185 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.7 | 163 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 1.6 | 155 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.6 | 152 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 1.5 | 144 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.5 | 143 | `java.util.TreeMap.put` |
| 1.4 | 138 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.3 | 128 | `sun.nio.ch.IOUtil.write` |
| 1.2 | 114 | `java.util.TreeMap.deleteEntry` |
| 1.2 | 112 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 103 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 1.0 | 100 | `sun.nio.ch.SocketDispatcher.read` |
| 1.0 | 98 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.0 | 97 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.0 | 96 | `java.util.Arrays.copyOfRange` |
| 1.0 | 94 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.9 | 87 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.9 | 84 | `sun.nio.ch.SocketDispatcher.writev` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 27.7 | 2656 | `java.nio.ByteBuffer.<init>` |
| 27.7 | 2656 | `java.nio.Buffer.<init>` |
| 27.6 | 2655 | `java.nio.ByteBuffer.position` |
| 27.4 | 2633 | `java.nio.HeapByteBuffer.<init>` |
| 27.2 | 2614 | `java.nio.ByteBuffer.allocate` |
| 24.4 | 2348 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 19.1 | 1835 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 14.3 | 1372 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 10.7 | 1027 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 8.6 | 824 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 7.2 | 693 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.1 | 686 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 6.5 | 620 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.3 | 607 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 6.3 | 603 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 5.9 | 570 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.8 | 554 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.7 | 552 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.1 | 486 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.9 | 474 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 4.9 | 468 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.8 | 459 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.3 | 409 | `java.util.TreeSet.remove` |
| 4.2 | 408 | `java.util.TreeMap.remove` |
| 4.0 | 387 | `org.xnio.nio.WorkerThread$TimeKey.remove` |

