# CPU profile

15276 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 86.2 | 13170 | `proteus I/O-*` |
| 7.2 | 1102 | `(unnamed virtual thread)` |
| 5.6 | 861 | `proteus task-*` |
| 0.7 | 101 | ` ForkJoinPool-1-worker-*` |
| 0.2 | 38 | `main` |
| 0.0 | 3 | `proteus Accept` |
| 0.0 | 2 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 24.3 | 3706 | `java.nio.ByteBuffer.position` |
| 12.9 | 1965 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.1 | 624 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.1 | 476 | `java.util.TreeMap.put` |
| 3.1 | 466 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.0 | 454 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.7 | 409 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.9 | 292 | `org.xnio.nio.WorkerThread.setOps` |
| 1.6 | 251 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.6 | 246 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.6 | 240 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.5 | 236 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.5 | 226 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.4 | 210 | `io.undertow.util.HttpString.calcHashCode` |
| 1.4 | 210 | `sun.nio.ch.SocketDispatcher.read` |
| 1.2 | 185 | `java.lang.AbstractStringBuilder.append` |
| 1.1 | 173 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.1 | 168 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.1 | 165 | `java.util.TreeMap.successor` |
| 1.0 | 147 | `sun.nio.ch.IOUtil.write` |
| 0.9 | 142 | `java.nio.ByteBuffer.putBuffer` |
| 0.9 | 131 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.8 | 127 | `java.util.IdentityHashMap.get` |
| 0.8 | 125 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 0.8 | 116 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 24.7 | 3766 | `java.nio.Buffer.<init>` |
| 24.7 | 3766 | `java.nio.ByteBuffer.<init>` |
| 24.6 | 3765 | `java.nio.ByteBuffer.position` |
| 24.4 | 3722 | `java.nio.HeapByteBuffer.<init>` |
| 24.1 | 3682 | `java.nio.ByteBuffer.allocate` |
| 21.0 | 3207 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 14.7 | 2253 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 13.7 | 2094 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 13.1 | 1995 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.9 | 1978 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 12.6 | 1929 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 7.2 | 1102 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 6.8 | 1042 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.6 | 1004 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.3 | 816 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.3 | 652 | `java.lang.StringBuilder.append` |
| 4.3 | 651 | `java.lang.AbstractStringBuilder.append` |
| 4.3 | 650 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.3 | 650 | `io.undertow.util.HttpString.equals` |
| 3.9 | 595 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 3.8 | 588 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 3.6 | 556 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.4 | 526 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 3.3 | 509 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.3 | 497 | `java.util.TreeSet.add` |

