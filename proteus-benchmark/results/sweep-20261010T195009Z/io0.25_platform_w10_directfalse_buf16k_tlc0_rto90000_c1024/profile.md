# CPU profile

4824 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 59.6 | 2873 | `proteus I/O-*` |
| 32.0 | 1542 | `proteus task-*` |
| 4.2 | 204 | `(unnamed virtual thread)` |
| 3.4 | 166 | ` ForkJoinPool-1-worker-*` |
| 0.6 | 28 | `main` |
| 0.1 | 7 | `ForkJoinPool-1-delayScheduler` |
| 0.1 | 3 | `JFR Periodic Tasks` |
| 0.0 | 1 | `Reference Handler` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 11.1 | 535 | `java.util.TreeMap.put` |
| 9.7 | 467 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 6.7 | 324 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.9 | 234 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 4.3 | 208 | `sun.nio.ch.IOUtil.configureBlocking` |
| 3.8 | 183 | `java.util.TreeMap.remove` |
| 3.4 | 163 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.getNext` |
| 2.6 | 127 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 2.4 | 116 | `java.util.TreeMap.getEntry` |
| 2.2 | 108 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 2.2 | 108 | `java.util.TreeMap.successor` |
| 2.2 | 107 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.2 | 107 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 2.2 | 106 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 2.1 | 103 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.9 | 93 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.6 | 76 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 1.5 | 74 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.4 | 69 | `java.util.TreeMap.deleteEntry` |
| 1.4 | 69 | `java.util.TreeMap.fixAfterDeletion` |
| 1.3 | 65 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.2 | 57 | `io.undertow.util.HttpString.calcHashCode` |
| 1.1 | 53 | `java.util.TreeMap.getEntryUsingComparator` |
| 1.0 | 50 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.0 | 49 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 25.1 | 1213 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 22.4 | 1080 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 16.7 | 807 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 14.2 | 687 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 14.2 | 684 | `org.xnio.nio.WorkerThread.executeAfter` |
| 11.8 | 567 | `java.util.TreeSet.add` |
| 11.7 | 564 | `java.util.TreeMap.put` |
| 11.3 | 543 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 10.8 | 523 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 10.7 | 518 | `java.util.TreeMap.remove` |
| 10.7 | 518 | `java.util.TreeSet.remove` |
| 8.5 | 412 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 7.9 | 382 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.cleanup` |
| 7.8 | 378 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 7.8 | 376 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.4 | 358 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 7.4 | 358 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 7.3 | 350 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 7.0 | 340 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 7.0 | 340 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.8 | 327 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 6.1 | 295 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.7 | 277 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.6 | 272 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 5.6 | 270 | `org.xnio.XnioWorker.execute` |

