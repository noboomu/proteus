# CPU profile

9390 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 84.0 | 7884 | `proteus I/O-*` |
| 15.4 | 1447 | `proteus task-*` |
| 0.5 | 48 | `main` |
| 0.1 | 8 | `proteus Accept` |
| 0.0 | 2 | `(unnamed virtual thread)` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 24.8 | 2330 | `java.nio.ByteBuffer.position` |
| 7.0 | 658 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 6.5 | 615 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.5 | 516 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 3.8 | 356 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 3.1 | 288 | `java.util.IdentityHashMap.get` |
| 3.0 | 281 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.4 | 227 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 1.9 | 183 | `java.util.TreeMap.put` |
| 1.9 | 182 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.9 | 180 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.6 | 146 | `sun.nio.ch.IOUtil.write` |
| 1.5 | 144 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.3 | 122 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.2 | 108 | `java.nio.ByteBuffer.putBuffer` |
| 1.2 | 108 | `sun.nio.ch.SocketDispatcher.read` |
| 1.0 | 97 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.0 | 93 | `java.util.TreeMap.deleteEntry` |
| 1.0 | 91 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.9 | 88 | `org.xnio.nio.WorkerThread.setOps` |
| 0.9 | 82 | `java.util.Arrays.copyOfRange` |
| 0.9 | 81 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.8 | 78 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.8 | 76 | `io.undertow.util.HeaderValues.isEmpty` |
| 0.8 | 76 | `sun.nio.ch.EPollSelectorImpl.doSelect` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 27.2 | 2552 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 25.2 | 2363 | `java.nio.Buffer.<init>` |
| 25.2 | 2363 | `java.nio.ByteBuffer.position` |
| 25.2 | 2363 | `java.nio.ByteBuffer.<init>` |
| 24.9 | 2338 | `java.nio.HeapByteBuffer.<init>` |
| 24.7 | 2323 | `java.nio.ByteBuffer.allocate` |
| 22.8 | 2145 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 16.1 | 1510 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 12.7 | 1188 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 7.7 | 722 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 7.1 | 667 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.1 | 662 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 6.7 | 629 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 6.5 | 615 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.5 | 615 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.5 | 609 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 6.5 | 607 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.7 | 534 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 5.6 | 525 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.5 | 516 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 5.1 | 480 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.8 | 357 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 3.4 | 321 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.4 | 321 | `sun.nio.ch.IOUtil.write` |
| 3.1 | 291 | `java.util.IdentityHashMap.get` |

