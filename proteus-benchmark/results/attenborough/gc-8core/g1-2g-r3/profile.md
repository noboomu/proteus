# CPU profile

8942 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 81.1 | 7249 | `proteus I/O-*` |
| 10.7 | 956 | `proteus task-*` |
| 6.9 | 615 | `(unnamed virtual thread)` |
| 0.9 | 82 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 39 | `main` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 33.3 | 2977 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.1 | 724 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.3 | 383 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.6 | 322 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.4 | 307 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 3.0 | 270 | `org.xnio.nio.WorkerThread.setOps` |
| 2.4 | 213 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 2.3 | 210 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.9 | 168 | `sun.nio.ch.SocketDispatcher.read` |
| 1.7 | 151 | `java.util.IdentityHashMap.get` |
| 1.6 | 144 | `sun.nio.ch.IOUtil.write` |
| 1.6 | 143 | `java.nio.ByteBuffer.putBuffer` |
| 1.6 | 141 | `java.util.TreeMap.getEntry` |
| 1.4 | 127 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.3 | 119 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.3 | 119 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.1 | 102 | `org.xnio.SingleOption.cast` |
| 1.1 | 94 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.0 | 92 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 85 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.8 | 75 | `java.util.HashMap.getNode` |
| 0.8 | 75 | `java.util.Arrays.copyOfRange` |
| 0.8 | 72 | `io.undertow.server.DefaultByteBufferPool.queueIfUnderMax` |
| 0.7 | 66 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.6 | 58 | `io.undertow.util.HeaderMap.getEntry` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 52.5 | 4697 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 37.3 | 3331 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 33.7 | 3011 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 33.3 | 2978 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 21.9 | 1957 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.6 | 1576 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 8.3 | 743 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 8.1 | 724 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 7.8 | 701 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 7.8 | 694 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.2 | 464 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.9 | 437 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.9 | 437 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.8 | 431 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.8 | 429 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.8 | 427 | `java.lang.Thread.run` |
| 4.3 | 388 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.0 | 355 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.8 | 341 | `sun.nio.ch.IOUtil.write` |
| 3.7 | 332 | `org.xnio.nio.NioHandle.resume` |
| 3.7 | 329 | `org.xnio.nio.WorkerThread.setOps` |
| 3.5 | 314 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 3.3 | 297 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 3.1 | 274 | `org.xnio.conduits.AbstractSourceConduit.resumeReads` |
| 2.6 | 233 | `java.util.TreeSet.remove` |

