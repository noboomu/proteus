# CPU profile

6648 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 48.1 | 3198 | `proteus I/O-*` |
| 34.5 | 2292 | `(unnamed virtual thread)` |
| 17.1 | 1138 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 28 | `main` |
| 0.0 | 2 | `ForkJoinPool-1-delayScheduler` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 13.5 | 899 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 9.6 | 641 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 8.5 | 567 | `java.util.TreeMap.put` |
| 7.9 | 523 | `org.xnio.nio.NioSocketConduit.read` |
| 5.2 | 344 | `sun.nio.ch.SocketDispatcher.read` |
| 4.2 | 276 | `java.util.concurrent.ForkJoinTask$InterruptibleTask.exec` |
| 2.8 | 185 | `sun.nio.ch.IOUtil.write` |
| 2.8 | 183 | `java.util.TreeMap.getEntry` |
| 2.5 | 165 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.1 | 142 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.9 | 125 | `io.undertow.util.HeaderMap.getEntry` |
| 1.7 | 115 | `java.util.TreeMap.successor` |
| 1.6 | 109 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.5 | 100 | `java.util.concurrent.ConcurrentHashMap.replaceNode` |
| 1.5 | 99 | `java.util.concurrent.ConcurrentHashMap$KeySetView.add` |
| 1.4 | 90 | `java.util.TreeMap.remove` |
| 1.4 | 90 | `io.undertow.io.AsyncSenderImpl.send` |
| 1.2 | 78 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.0 | 67 | `io.undertow.util.HeaderValues.addAll` |
| 1.0 | 64 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.0 | 64 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.9 | 63 | `java.util.concurrent.ConcurrentHashMap.putVal` |
| 0.9 | 61 | `java.util.TreeMap.fixAfterDeletion` |
| 0.9 | 61 | `jdk.internal.vm.ThreadContainers$RootContainer$TrackingRootContainer.onExit` |
| 0.9 | 60 | `jdk.internal.vm.Continuation.run` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 17.4 | 1155 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.3 | 953 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 14.1 | 937 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 13.5 | 899 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 12.0 | 799 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 12.0 | 796 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 11.9 | 790 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 11.7 | 779 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 10.3 | 682 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 10.2 | 680 | `org.xnio.nio.WorkerThread.executeAfter` |
| 9.6 | 641 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 9.0 | 597 | `java.util.TreeMap.put` |
| 9.0 | 597 | `java.util.TreeSet.add` |
| 8.6 | 574 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 8.5 | 564 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 8.0 | 529 | `org.xnio.nio.NioSocketConduit.read` |
| 8.0 | 529 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 7.1 | 473 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.9 | 458 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 6.7 | 447 | `java.util.concurrent.ForkJoinTask.doExec` |
| 6.5 | 429 | `java.util.TreeSet.remove` |
| 6.5 | 429 | `java.util.TreeMap.remove` |
| 6.4 | 425 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 6.0 | 400 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 5.4 | 358 | `sun.nio.ch.IOUtil.read` |

