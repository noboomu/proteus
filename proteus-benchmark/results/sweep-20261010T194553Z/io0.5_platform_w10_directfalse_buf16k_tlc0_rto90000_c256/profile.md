# CPU profile

5287 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 86.9 | 4594 | `proteus I/O-*` |
| 9.6 | 506 | `(unnamed virtual thread)` |
| 2.7 | 141 | ` ForkJoinPool-1-worker-*` |
| 0.5 | 24 | `proteus task-*` |
| 0.4 | 23 | `main` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 56.1 | 2968 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.6 | 245 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.1 | 216 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 2.1 | 113 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 1.9 | 103 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.5 | 77 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.4 | 75 | `org.xnio.Buffers.copy` |
| 1.4 | 74 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.2 | 66 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.1 | 59 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.0 | 53 | `java.util.TreeMap.getEntry` |
| 0.9 | 46 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 0.8 | 42 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.8 | 40 | `org.xnio.nio.NioHandle.resume` |
| 0.7 | 35 | `io.undertow.util.HttpString.calcHashCode` |
| 0.6 | 30 | `java.util.TreeMap.put` |
| 0.5 | 26 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 0.5 | 26 | `sun.nio.ch.IOUtil.write` |
| 0.5 | 26 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 0.5 | 25 | `java.util.HashMap.getNode` |
| 0.5 | 25 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.5 | 25 | `sun.nio.ch.EPollSelectorImpl.setEventOps` |
| 0.5 | 24 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 0.5 | 24 | `java.util.TreeMap.fixAfterDeletion` |
| 0.5 | 24 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 63.5 | 3357 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 61.0 | 3227 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 60.2 | 3184 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 56.1 | 2968 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 9.6 | 505 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 9.4 | 499 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 9.4 | 495 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 9.2 | 486 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 4.8 | 255 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.6 | 245 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.6 | 244 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 2.9 | 152 | `io.undertow.util.HttpString.equals` |
| 2.9 | 152 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.4 | 127 | `org.xnio.conduits.Conduits.writeFinalBasic` |
| 2.3 | 119 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 2.3 | 119 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 2.2 | 117 | `java.util.TreeSet.remove` |
| 2.2 | 117 | `java.util.TreeMap.remove` |
| 2.1 | 113 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 2.0 | 108 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 1.9 | 102 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 1.7 | 92 | `java.util.HashMap.getNode` |
| 1.6 | 86 | `org.xnio.nio.NioHandle.resume` |
| 1.6 | 85 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 1.6 | 82 | `io.undertow.channels.DetachableStreamSinkChannel.write` |

