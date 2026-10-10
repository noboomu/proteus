# CPU profile

9338 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 45.7 | 4270 | `proteus I/O-*` |
| 34.0 | 3173 | `(unnamed virtual thread)` |
| 18.5 | 1726 | ` ForkJoinPool-1-worker-*` |
| 1.9 | 175 | `ForkJoinPool-1-delayScheduler` |
| 0.1 | 7 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 15.7 | 1464 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 9.0 | 837 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 8.5 | 791 | `java.util.TreeMap.put` |
| 6.7 | 621 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 3.9 | 367 | `sun.nio.ch.IOUtil.configureBlocking` |
| 3.2 | 299 | `io.undertow.util.HeaderMap.getEntry` |
| 2.8 | 265 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.7 | 163 | `java.util.TreeMap.getEntry` |
| 1.7 | 159 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.5 | 143 | `java.util.TreeMap.remove` |
| 1.5 | 136 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 1.3 | 124 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.2 | 116 | `java.util.concurrent.ConcurrentHashMap.addCount` |
| 1.2 | 111 | `java.util.TreeMap.getEntryUsingComparator` |
| 1.1 | 107 | `java.util.HashMap.clear` |
| 1.1 | 107 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.1 | 102 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 1.0 | 98 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.0 | 97 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 1.0 | 93 | `java.util.concurrent.DelayScheduler.loop` |
| 1.0 | 91 | `jdk.internal.vm.Continuation.run` |
| 1.0 | 90 | `java.util.concurrent.ConcurrentHashMap.putVal` |
| 0.9 | 88 | `io.undertow.server.Connectors.executeRootHandler` |
| 0.9 | 87 | `org.xnio.SingleOption.cast` |
| 0.9 | 87 | `java.lang.VirtualThread.mount` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 16.2 | 1515 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 15.7 | 1464 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 15.0 | 1404 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 14.5 | 1358 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 13.9 | 1299 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 11.8 | 1104 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 11.5 | 1070 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 10.8 | 1009 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 9.7 | 907 | `org.xnio.nio.WorkerThread.executeAfter` |
| 9.2 | 862 | `java.util.TreeSet.add` |
| 9.0 | 845 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 9.0 | 844 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 9.0 | 838 | `java.util.TreeMap.put` |
| 8.3 | 773 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 6.8 | 633 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.7 | 621 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 6.0 | 559 | `java.util.TreeMap.remove` |
| 6.0 | 559 | `java.util.TreeSet.remove` |
| 6.0 | 558 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 5.6 | 521 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 5.0 | 463 | `org.xnio.nio.WorkerThread.run` |
| 4.9 | 454 | `java.util.concurrent.ForkJoinPool$WorkQueue.push` |
| 4.6 | 430 | `java.util.concurrent.ForkJoinPool.poolSubmit` |
| 4.6 | 430 | `java.util.concurrent.ForkJoinPool.submit` |
| 4.6 | 428 | `java.lang.VirtualThread.submit` |

