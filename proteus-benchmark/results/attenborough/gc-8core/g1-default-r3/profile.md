# CPU profile

10520 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.7 | 9435 | `proteus I/O-*` |
| 5.7 | 601 | `proteus task-*` |
| 3.6 | 380 | `(unnamed virtual thread)` |
| 0.7 | 70 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 34 | `main` |
| 0.0 | 2 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 52.1 | 5481 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 5.7 | 601 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.4 | 357 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.3 | 245 | `sun.nio.ch.IOUtil.write` |
| 2.3 | 241 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 2.3 | 239 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 1.6 | 164 | `org.xnio.nio.NioHandle.resume` |
| 1.4 | 152 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.4 | 145 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.2 | 122 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.2 | 121 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 111 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.0 | 102 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 0.9 | 99 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.9 | 97 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 96 | `org.xnio.SingleOption.cast` |
| 0.9 | 96 | `java.util.TreeMap.deleteEntry` |
| 0.9 | 91 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.8 | 83 | `sun.nio.ch.SocketDispatcher.writev` |
| 0.7 | 70 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.7 | 69 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.5 | 56 | `io.sinistral.proteus.server.ServerResponse.send` |
| 0.5 | 51 | `jdk.internal.misc.Unsafe.checkPrimitiveArray` |
| 0.5 | 51 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.5 | 48 | `java.util.Arrays.copyOfRange` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 66.0 | 6938 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 55.3 | 5817 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 52.5 | 5527 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 52.1 | 5483 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 15.6 | 1641 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 12.3 | 1289 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 6.0 | 627 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 5.8 | 610 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.8 | 609 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 5.7 | 601 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.4 | 468 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.4 | 458 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 4.3 | 456 | `sun.nio.ch.IOUtil.write` |
| 3.3 | 346 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.1 | 328 | `sun.nio.ch.SocketChannelImpl.write` |
| 2.6 | 275 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 2.6 | 275 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 2.6 | 273 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 2.4 | 251 | `org.xnio.nio.NioSocketConduit.write` |
| 2.3 | 247 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.3 | 241 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 2.1 | 225 | `org.xnio.conduits.AbstractStreamSinkConduit.write` |
| 2.1 | 223 | `org.xnio.nio.NioHandle.resume` |
| 2.1 | 223 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 2.1 | 218 | `org.xnio.nio.NioSocketConduit.resumeReads` |

