# CPU profile

9841 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 91.0 | 8952 | `proteus I/O-*` |
| 6.9 | 678 | `(unnamed virtual thread)` |
| 1.2 | 118 | ` ForkJoinPool-1-worker-*` |
| 0.7 | 66 | `proteus task-*` |
| 0.3 | 28 | `main` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 56.2 | 5529 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 5.9 | 581 | `java.util.IdentityHashMap.get` |
| 5.5 | 540 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 1.8 | 180 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.6 | 162 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.5 | 148 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.5 | 143 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.4 | 142 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.3 | 124 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.0 | 95 | `java.util.TreeMap.getEntry` |
| 0.9 | 93 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.9 | 93 | `org.xnio.nio.WorkerThread.setOps` |
| 0.9 | 89 | `org.xnio.Buffers.copy` |
| 0.9 | 88 | `java.util.TreeMap.put` |
| 0.9 | 87 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.7 | 71 | `io.undertow.util.HttpString.calcHashCode` |
| 0.6 | 57 | `java.util.HashMap.getNode` |
| 0.6 | 56 | `java.nio.ByteBuffer.putBuffer` |
| 0.6 | 55 | `sun.nio.ch.SocketDispatcher.read` |
| 0.5 | 52 | `sun.nio.ch.EPollSelectorImpl.setEventOps` |
| 0.5 | 49 | `sun.nio.ch.IOUtil.write` |
| 0.5 | 48 | `java.util.TreeMap.deleteEntry` |
| 0.5 | 46 | `java.util.TreeMap.fixAfterDeletion` |
| 0.4 | 44 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.4 | 43 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 65.4 | 6438 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 57.0 | 5614 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 56.6 | 5573 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 56.2 | 5529 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 11.6 | 1145 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 9.8 | 965 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 9.5 | 932 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.9 | 581 | `java.util.IdentityHashMap.get` |
| 5.8 | 574 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 5.8 | 571 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 5.8 | 571 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 5.8 | 570 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 5.7 | 562 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 5.6 | 547 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 3.9 | 388 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 2.3 | 230 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.3 | 230 | `io.undertow.util.HttpString.equals` |
| 2.3 | 227 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 2.1 | 210 | `java.util.TreeSet.remove` |
| 2.1 | 210 | `java.util.TreeMap.remove` |
| 2.1 | 207 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 2.1 | 202 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 2.0 | 192 | `java.util.HashMap.getNode` |
| 1.9 | 189 | `java.lang.StringBuilder.append` |
| 1.9 | 189 | `java.lang.AbstractStringBuilder.append` |

