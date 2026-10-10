# CPU profile

5456 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 55.2 | 3010 | `proteus I/O-*` |
| 35.8 | 1951 | `proteus task-*` |
| 4.2 | 231 | `(unnamed virtual thread)` |
| 3.9 | 215 | ` ForkJoinPool-1-worker-*` |
| 0.6 | 31 | `main` |
| 0.3 | 14 | `ForkJoinPool-1-delayScheduler` |
| 0.1 | 4 | `JFR Periodic Tasks` |
| 0.0 | 2 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 12.9 | 702 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 9.7 | 527 | `java.util.TreeMap.put` |
| 9.1 | 494 | `java.util.IdentityHashMap.get` |
| 4.4 | 241 | `java.util.TreeMap.getEntry` |
| 3.5 | 189 | `sun.nio.ch.IOUtil.write` |
| 3.3 | 179 | `java.lang.Thread.interrupted` |
| 2.7 | 150 | `java.util.concurrent.locks.LockSupport.unpark` |
| 2.7 | 145 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 2.3 | 124 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.1 | 113 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.0 | 109 | `java.util.TreeMap.remove` |
| 2.0 | 107 | `java.util.TreeMap.successor` |
| 1.9 | 105 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.6 | 87 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.6 | 86 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 1.3 | 72 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 1.3 | 70 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.3 | 70 | `io.undertow.util.HttpString.calcHashCode` |
| 1.2 | 65 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.2 | 65 | `io.undertow.util.HeaderValues.clear` |
| 1.1 | 62 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.1 | 59 | `java.util.TreeMap.fixAfterDeletion` |
| 1.1 | 58 | `org.xnio.nio.WorkerThread.executeAfter` |
| 1.0 | 57 | `org.xnio.nio.NioHandle.resume` |
| 1.0 | 56 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 23.8 | 1299 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 21.6 | 1180 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 15.4 | 839 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 13.3 | 727 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 13.2 | 718 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 12.8 | 701 | `org.xnio.nio.WorkerThread.executeAfter` |
| 11.2 | 612 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 10.4 | 568 | `java.util.TreeSet.add` |
| 10.3 | 563 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 10.3 | 563 | `java.util.TreeMap.put` |
| 9.1 | 494 | `java.util.IdentityHashMap.get` |
| 9.1 | 494 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 8.9 | 485 | `java.util.TreeMap.remove` |
| 8.9 | 485 | `java.util.TreeSet.remove` |
| 8.6 | 468 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 8.5 | 462 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 8.4 | 458 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 7.6 | 414 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.cleanup` |
| 5.9 | 323 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 5.9 | 322 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 5.8 | 316 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 5.6 | 303 | `io.undertow.server.Connectors.executeRootHandler` |
| 4.9 | 270 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.4 | 241 | `java.util.TreeMap.getEntry` |
| 4.3 | 237 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |

