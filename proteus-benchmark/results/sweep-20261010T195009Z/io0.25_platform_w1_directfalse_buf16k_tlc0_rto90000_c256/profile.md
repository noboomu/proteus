# CPU profile

4366 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 64.0 | 2796 | `proteus I/O-*` |
| 32.6 | 1424 | `proteus task-*` |
| 1.7 | 76 | ` ForkJoinPool-1-worker-*` |
| 1.0 | 44 | `(unnamed virtual thread)` |
| 0.5 | 21 | `main` |
| 0.1 | 4 | `ForkJoinPool-1-delayScheduler` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.1 | 617 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 11.8 | 515 | `java.util.TreeMap.put` |
| 7.8 | 339 | `java.util.concurrent.locks.LockSupport.unpark` |
| 6.0 | 264 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.2 | 227 | `sun.nio.ch.IOUtil.configureBlocking` |
| 3.6 | 155 | `java.util.TreeMap.getEntry` |
| 3.3 | 144 | `java.lang.Thread.interrupted` |
| 2.6 | 113 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.4 | 103 | `io.undertow.util.HeaderMap.getEntry` |
| 2.3 | 100 | `java.util.TreeMap.remove` |
| 2.0 | 88 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 2.0 | 86 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.8 | 80 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.7 | 75 | `java.util.TreeMap.successor` |
| 1.6 | 71 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.6 | 70 | `io.undertow.util.HttpString.calcHashCode` |
| 1.4 | 60 | `java.util.TreeMap.getEntryUsingComparator` |
| 1.3 | 55 | `java.util.TreeMap.deleteEntry` |
| 1.3 | 55 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 1.1 | 49 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.1 | 47 | `org.xnio.nio.WorkerThread.executeAfter` |
| 1.1 | 47 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 1.1 | 46 | `java.util.TreeMap.fixAfterDeletion` |
| 1.0 | 45 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 40 | `io.undertow.util.HeaderMap.getOrCreateEntry` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 28.4 | 1242 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 24.7 | 1080 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 18.4 | 802 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 15.1 | 661 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 14.7 | 641 | `org.xnio.nio.WorkerThread.executeAfter` |
| 14.7 | 641 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 12.7 | 554 | `java.util.TreeSet.add` |
| 12.6 | 552 | `java.util.TreeMap.put` |
| 11.2 | 488 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 10.0 | 438 | `java.util.TreeMap.remove` |
| 10.0 | 438 | `java.util.TreeSet.remove` |
| 10.0 | 436 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 8.0 | 350 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 8.0 | 350 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 7.8 | 341 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 7.8 | 339 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 7.8 | 339 | `java.util.concurrent.locks.LockSupport.unpark` |
| 7.3 | 319 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.cleanup` |
| 7.0 | 304 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.9 | 302 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 6.8 | 299 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.5 | 284 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 6.5 | 283 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 5.3 | 233 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.2 | 227 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |

