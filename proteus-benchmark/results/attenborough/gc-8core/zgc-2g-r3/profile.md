# CPU profile

9608 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 80.7 | 7757 | `proteus I/O-*` |
| 10.7 | 1029 | `proteus task-*` |
| 7.3 | 699 | `(unnamed virtual thread)` |
| 1.0 | 92 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 31 | `main` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 29.7 | 2852 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.3 | 802 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.2 | 402 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 4.1 | 390 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.9 | 283 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 2.9 | 278 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.5 | 242 | `org.xnio.nio.WorkerThread.setOps` |
| 2.3 | 219 | `java.nio.ByteBuffer.putBuffer` |
| 2.2 | 216 | `java.util.IdentityHashMap.get` |
| 2.0 | 193 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.9 | 187 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.9 | 179 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.8 | 171 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.7 | 166 | `sun.nio.ch.SocketDispatcher.read` |
| 1.6 | 157 | `java.util.TreeMap.getEntry` |
| 1.6 | 152 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.2 | 112 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.append` |
| 1.0 | 98 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 0.9 | 82 | `io.undertow.util.HeaderMap.getEntry` |
| 0.8 | 79 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.7 | 71 | `sun.nio.ch.IOUtil.write` |
| 0.7 | 65 | `java.util.TreeSet.add` |
| 0.7 | 64 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.7 | 63 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.6 | 62 | `io.undertow.server.DefaultByteBufferPool.queueIfUnderMax` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 48.1 | 4621 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 33.4 | 3211 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 30.0 | 2887 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 29.7 | 2852 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.5 | 2061 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.4 | 1670 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 10.4 | 998 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 9.7 | 936 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 9.7 | 930 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 8.3 | 802 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.2 | 595 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 6.0 | 574 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.7 | 455 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.7 | 454 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.7 | 451 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.2 | 407 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.2 | 402 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 3.7 | 355 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.7 | 351 | `sun.nio.ch.IOUtil.write` |
| 3.2 | 305 | `org.xnio.nio.NioHandle.resume` |
| 3.1 | 298 | `org.xnio.nio.WorkerThread.setOps` |
| 3.0 | 293 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 2.9 | 280 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 2.6 | 253 | `org.xnio.conduits.AbstractSourceConduit.resumeReads` |
| 2.6 | 248 | `io.undertow.channels.DetachableStreamSinkChannel.write` |

