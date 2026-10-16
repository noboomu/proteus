# CPU profile

11085 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.4 | 9905 | `proteus I/O-*` |
| 6.4 | 709 | `proteus task-*` |
| 3.5 | 388 | `(unnamed virtual thread)` |
| 0.5 | 53 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 30 | `main` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 50.4 | 5589 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 6.4 | 705 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.8 | 418 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.3 | 365 | `sun.nio.ch.IOUtil.write` |
| 2.7 | 300 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.5 | 275 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 1.7 | 186 | `java.util.TreeMap.put` |
| 1.3 | 149 | `java.nio.ByteBuffer.putBuffer` |
| 1.3 | 139 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.2 | 137 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.2 | 132 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.1 | 122 | `java.util.IdentityHashMap.get` |
| 1.0 | 107 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.9 | 104 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 0.9 | 96 | `org.xnio.SingleOption.cast` |
| 0.8 | 92 | `sun.nio.ch.SocketDispatcher.read` |
| 0.8 | 92 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 0.8 | 89 | `java.util.TreeMap.deleteEntry` |
| 0.6 | 70 | `java.util.Arrays.copyOfRange` |
| 0.6 | 66 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.6 | 64 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.6 | 63 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 0.6 | 62 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 0.6 | 62 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.5 | 57 | `io.undertow.util.HttpString.bytesAreEqual` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 64.8 | 7178 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 53.8 | 5960 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 51.0 | 5655 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 50.5 | 5593 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 15.9 | 1757 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 13.1 | 1452 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 6.4 | 705 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.3 | 694 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 5.8 | 638 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 5.7 | 630 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.4 | 598 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 5.1 | 568 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.0 | 558 | `sun.nio.ch.IOUtil.write` |
| 4.0 | 440 | `sun.nio.ch.SocketChannelImpl.write` |
| 3.7 | 406 | `org.xnio.nio.NioSocketConduit.write` |
| 3.6 | 394 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.1 | 348 | `org.xnio.conduits.AbstractStreamSinkConduit.write` |
| 3.0 | 333 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 3.0 | 333 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 3.0 | 328 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 2.9 | 318 | `java.lang.Thread.run` |
| 2.7 | 303 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.1 | 230 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 2.0 | 224 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 2.0 | 219 | `org.xnio.nio.WorkerThread.executeAfter` |

