# CPU profile

4114 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 66.3 | 2726 | `proteus I/O-*` |
| 30.9 | 1272 | `proteus task-*` |
| 1.1 | 47 | ` ForkJoinPool-1-worker-*` |
| 0.9 | 35 | `(unnamed virtual thread)` |
| 0.8 | 31 | `main` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 1 | `ForkJoinPool-1-delayScheduler` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 16.0 | 660 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 8.9 | 365 | `java.util.TreeMap.put` |
| 8.7 | 359 | `java.util.concurrent.locks.LockSupport.unpark` |
| 5.8 | 237 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.7 | 195 | `sun.nio.ch.IOUtil.configureBlocking` |
| 3.7 | 153 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 3.2 | 130 | `java.util.TreeMap.getEntry` |
| 2.7 | 112 | `java.util.TreeMap.remove` |
| 2.7 | 111 | `java.util.TreeMap.successor` |
| 2.7 | 111 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 2.5 | 103 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.3 | 93 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.7 | 70 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.6 | 65 | `io.undertow.util.HttpString.calcHashCode` |
| 1.6 | 65 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.5 | 62 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.5 | 61 | `io.undertow.server.handlers.GracefulShutdownHandler.handleRequest` |
| 1.3 | 54 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.2 | 49 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.2 | 48 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 1.0 | 43 | `java.util.TreeMap.getEntryUsingComparator` |
| 1.0 | 41 | `java.util.TreeMap.fixAfterDeletion` |
| 1.0 | 40 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 0.9 | 38 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 0.9 | 37 | `io.undertow.server.Connectors.verifyToken` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 30.2 | 1243 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 27.1 | 1113 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 19.4 | 799 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 18.1 | 746 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 11.5 | 474 | `org.xnio.nio.WorkerThread.executeAfter` |
| 11.4 | 471 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 11.1 | 457 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 9.7 | 400 | `java.util.TreeSet.add` |
| 9.6 | 395 | `java.util.TreeMap.put` |
| 9.6 | 394 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 9.5 | 391 | `java.util.TreeMap.remove` |
| 9.5 | 391 | `java.util.TreeSet.remove` |
| 9.2 | 377 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 9.1 | 375 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 8.8 | 360 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 8.7 | 359 | `java.util.concurrent.locks.LockSupport.unpark` |
| 8.7 | 359 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 6.5 | 267 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.cleanup` |
| 6.3 | 261 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.3 | 261 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 6.3 | 260 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.2 | 254 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.9 | 243 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 5.3 | 217 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.0 | 206 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |

