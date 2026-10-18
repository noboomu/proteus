# CPU profile

10168 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 91.0 | 9253 | `proteus I/O-*` |
| 5.3 | 541 | `proteus task-*` |
| 2.9 | 299 | `(unnamed virtual thread)` |
| 0.4 | 39 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 36 | `main` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 53.7 | 5464 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 6.0 | 607 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.1 | 313 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.4 | 245 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 2.4 | 240 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.2 | 223 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.5 | 157 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.5 | 151 | `org.xnio.nio.WorkerThread.setOps` |
| 1.4 | 138 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.2 | 119 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.1 | 112 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 105 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.0 | 102 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.9 | 91 | `org.xnio.SingleOption.cast` |
| 0.8 | 84 | `sun.nio.ch.SocketDispatcher.read` |
| 0.7 | 76 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.7 | 76 | `java.util.IdentityHashMap.get` |
| 0.7 | 76 | `java.util.TreeMap.deleteEntry` |
| 0.7 | 69 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.7 | 69 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 0.6 | 66 | `java.util.TreeMap.successor` |
| 0.6 | 63 | `tools.jackson.core.json.JsonWriteContext.writeValue` |
| 0.6 | 60 | `java.util.TreeMap.put` |
| 0.5 | 52 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 0.5 | 48 | `sun.nio.ch.IOUtil.write` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 67.3 | 6840 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 56.9 | 5788 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 54.3 | 5519 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 53.8 | 5468 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 15.1 | 1539 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 12.5 | 1269 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 7.6 | 769 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.3 | 745 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.3 | 740 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.0 | 607 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.9 | 500 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.4 | 446 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 2.7 | 270 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 2.7 | 270 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 2.6 | 269 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 2.4 | 247 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.4 | 245 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 2.2 | 219 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 2.1 | 217 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 2.1 | 209 | `sun.nio.ch.IOUtil.write` |
| 1.9 | 198 | `sun.nio.ch.EPollSelectorImpl.doSelect` |
| 1.9 | 196 | `sun.nio.ch.SelectorImpl.lockAndDoSelect` |
| 1.9 | 190 | `org.xnio.nio.WorkerThread.run` |
| 1.8 | 183 | `org.xnio.nio.NioHandle.resume` |
| 1.8 | 179 | `org.xnio.nio.WorkerThread.setOps` |

