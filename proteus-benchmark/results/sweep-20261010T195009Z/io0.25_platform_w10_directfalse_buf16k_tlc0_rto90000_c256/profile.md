# CPU profile

4361 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 65.9 | 2875 | `proteus I/O-*` |
| 30.6 | 1333 | `proteus task-*` |
| 1.9 | 81 | ` ForkJoinPool-1-worker-*` |
| 1.0 | 43 | `(unnamed virtual thread)` |
| 0.6 | 27 | `main` |
| 0.0 | 2 | `ForkJoinPool-1-delayScheduler` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 16.3 | 710 | `org.xnio.nio.NioSocketConduit.read` |
| 8.3 | 361 | `java.util.TreeMap.put` |
| 5.6 | 245 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.2 | 228 | `sun.nio.ch.IOUtil.configureBlocking` |
| 3.5 | 154 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.getNext` |
| 3.4 | 148 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 3.3 | 142 | `java.util.TreeMap.getEntry` |
| 3.0 | 130 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.5 | 108 | `java.util.TreeMap.remove` |
| 2.0 | 87 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.0 | 86 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 2.0 | 86 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 1.9 | 84 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.8 | 78 | `java.util.TreeMap.successor` |
| 1.7 | 75 | `io.undertow.util.HttpString.calcHashCode` |
| 1.7 | 74 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.7 | 73 | `org.jboss.threads.EnhancedQueueExecutor.increaseQueueSize` |
| 1.6 | 69 | `java.util.IdentityHashMap.get` |
| 1.3 | 56 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.2 | 54 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.2 | 53 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.2 | 51 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 1.1 | 49 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.1 | 48 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.0 | 43 | `java.util.HashMap.putVal` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 29.3 | 1279 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 18.2 | 794 | `org.xnio.nio.NioSocketConduit.read` |
| 16.8 | 731 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 16.7 | 729 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 16.3 | 711 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 11.9 | 520 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 10.6 | 463 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 10.5 | 458 | `org.xnio.nio.WorkerThread.executeAfter` |
| 9.3 | 405 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 9.0 | 392 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 8.9 | 390 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 8.8 | 384 | `java.util.TreeMap.put` |
| 8.7 | 378 | `java.util.TreeSet.add` |
| 8.7 | 378 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 8.4 | 367 | `java.util.TreeMap.remove` |
| 8.4 | 367 | `java.util.TreeSet.remove` |
| 8.4 | 366 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 7.6 | 333 | `org.xnio.XnioWorker.execute` |
| 6.6 | 290 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.cleanup` |
| 6.6 | 288 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.4 | 280 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.4 | 280 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 6.1 | 267 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 6.1 | 267 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 6.0 | 260 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |

