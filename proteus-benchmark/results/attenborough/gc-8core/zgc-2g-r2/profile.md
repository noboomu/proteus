# CPU profile

9457 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 81.3 | 7685 | `proteus I/O-*` |
| 10.9 | 1034 | `proteus task-*` |
| 6.4 | 603 | `(unnamed virtual thread)` |
| 1.1 | 107 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 31 | `main` |
| 0.0 | 2 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 29.5 | 2789 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 8.0 | 755 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.0 | 383 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 3.8 | 358 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.2 | 301 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.9 | 278 | `sun.nio.ch.IOUtil.write` |
| 2.6 | 246 | `java.util.IdentityHashMap.get` |
| 2.1 | 202 | `org.xnio.nio.NioHandle.resume` |
| 2.1 | 201 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 2.1 | 196 | `java.nio.ByteBuffer.putBuffer` |
| 1.9 | 183 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.8 | 173 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.6 | 154 | `java.util.TreeMap.deleteEntry` |
| 1.6 | 149 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.4 | 135 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.3 | 126 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.2 | 113 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.append` |
| 1.1 | 104 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.0 | 98 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.9 | 89 | `org.xnio.SingleOption.cast` |
| 0.9 | 82 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 0.8 | 78 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.8 | 76 | `sun.nio.ch.SocketDispatcher.read` |
| 0.8 | 75 | `java.util.HashMap.getNode` |
| 0.8 | 75 | `java.util.TreeMap.put` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 49.4 | 4672 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 34.8 | 3291 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 31.4 | 2971 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 29.5 | 2789 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 22.4 | 2115 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.1 | 1619 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 8.0 | 755 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 7.3 | 695 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 6.5 | 618 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.5 | 616 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 6.1 | 579 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 6.0 | 572 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.7 | 535 | `sun.nio.ch.IOUtil.write` |
| 5.0 | 473 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.0 | 473 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.0 | 471 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.9 | 459 | `java.lang.Thread.run` |
| 4.1 | 386 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.0 | 374 | `sun.nio.ch.SocketChannelImpl.write` |
| 3.4 | 324 | `org.xnio.nio.NioSocketConduit.write` |
| 3.4 | 321 | `org.xnio.conduits.AbstractStreamSinkConduit.write` |
| 3.4 | 320 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.1 | 292 | `org.xnio.nio.NioHandle.resume` |
| 3.0 | 286 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 3.0 | 285 | `org.xnio.OptionMap.get` |

