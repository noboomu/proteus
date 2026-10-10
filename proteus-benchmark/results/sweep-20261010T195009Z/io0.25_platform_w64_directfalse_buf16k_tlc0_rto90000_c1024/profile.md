# CPU profile

5359 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 60.0 | 3218 | `proteus I/O-*` |
| 30.4 | 1628 | `proteus task-*` |
| 4.9 | 264 | `(unnamed virtual thread)` |
| 3.7 | 196 | ` ForkJoinPool-1-worker-*` |
| 0.5 | 28 | `main` |
| 0.4 | 24 | `ForkJoinPool-1-delayScheduler` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 9.1 | 487 | `java.util.TreeMap.put` |
| 7.6 | 409 | `org.xnio.nio.NioSocketConduit.read` |
| 6.9 | 370 | `java.util.concurrent.locks.LockSupport.unpark` |
| 6.7 | 359 | `sun.nio.ch.IOUtil.read` |
| 5.6 | 302 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.3 | 233 | `java.util.TreeMap.remove` |
| 3.5 | 190 | `sun.nio.ch.IOUtil.configureBlocking` |
| 3.3 | 178 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 2.8 | 148 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.2 | 120 | `java.util.TreeMap.successor` |
| 2.2 | 117 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 2.0 | 107 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.9 | 100 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.8 | 97 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.8 | 96 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.8 | 95 | `java.util.IdentityHashMap.get` |
| 1.7 | 92 | `sun.nio.ch.SocketDispatcher.read` |
| 1.5 | 83 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 1.5 | 79 | `io.undertow.util.HttpString.calcHashCode` |
| 1.3 | 70 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.3 | 70 | `java.util.TreeMap.fixAfterDeletion` |
| 1.3 | 68 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.1 | 58 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.0 | 53 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.9 | 48 | `java.util.TreeMap.getEntry` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 18.9 | 1013 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.5 | 777 | `org.xnio.nio.NioSocketConduit.read` |
| 14.3 | 769 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 12.3 | 661 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 11.6 | 623 | `org.xnio.nio.WorkerThread.executeAfter` |
| 10.0 | 537 | `java.util.TreeSet.add` |
| 9.7 | 522 | `java.util.TreeMap.put` |
| 9.6 | 516 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 8.9 | 478 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 8.5 | 458 | `java.util.TreeSet.remove` |
| 8.5 | 457 | `java.util.TreeMap.remove` |
| 8.5 | 455 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 8.4 | 451 | `sun.nio.ch.IOUtil.read` |
| 8.4 | 450 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 8.2 | 440 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 8.2 | 439 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 8.1 | 435 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 7.8 | 419 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 7.4 | 395 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.3 | 391 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 7.3 | 390 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 6.9 | 372 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 6.9 | 370 | `java.util.concurrent.locks.LockSupport.unpark` |
| 6.9 | 370 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 6.9 | 368 | `sun.nio.ch.SocketChannelImpl.read` |

