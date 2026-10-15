# CPU profile

15178 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 86.4 | 13109 | `proteus I/O-*` |
| 7.1 | 1085 | `(unnamed virtual thread)` |
| 5.5 | 828 | `proteus task-*` |
| 0.7 | 110 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 45 | `main` |
| 0.1 | 8 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 12.6 | 1906 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.2 | 1855 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 11.9 | 1802 | `java.util.IdentityHashMap.get` |
| 4.8 | 734 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.3 | 659 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 3.1 | 469 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.7 | 404 | `java.util.HashMap.getNode` |
| 1.8 | 279 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.8 | 279 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.7 | 258 | `org.xnio.nio.WorkerThread.setOps` |
| 1.7 | 256 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.6 | 244 | `io.undertow.util.HttpString.calcHashCode` |
| 1.5 | 232 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.4 | 216 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.4 | 208 | `java.util.TreeMap.put` |
| 1.2 | 176 | `sun.nio.ch.IOUtil.write` |
| 1.1 | 160 | `java.lang.AbstractStringBuilder.append` |
| 1.0 | 157 | `java.util.TreeMap.successor` |
| 1.0 | 156 | `jdk.internal.misc.Unsafe.checkOffset` |
| 0.9 | 139 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 137 | `java.nio.ByteBuffer.putBuffer` |
| 0.9 | 133 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.9 | 131 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.8 | 117 | `io.undertow.util.HttpString.higher` |
| 0.7 | 112 | `io.undertow.server.Connectors.verifyToken` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 32.6 | 4941 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 26.8 | 4061 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 26.0 | 3950 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 24.9 | 3778 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 20.9 | 3179 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 20.7 | 3141 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 14.2 | 2152 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 12.3 | 1872 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 11.9 | 1804 | `java.util.IdentityHashMap.get` |
| 10.5 | 1590 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 10.2 | 1541 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 10.1 | 1538 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 9.1 | 1376 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.6 | 701 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 4.6 | 694 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 4.3 | 659 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.1 | 629 | `java.lang.AbstractStringBuilder.append` |
| 4.1 | 629 | `java.lang.StringBuilder.append` |
| 4.0 | 610 | `java.util.HashMap.getNode` |
| 3.7 | 564 | `java.util.LinkedHashMap.get` |
| 3.5 | 527 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.4 | 520 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 3.1 | 472 | `io.undertow.util.HttpString.equals` |
| 3.1 | 472 | `io.undertow.util.HttpString.bytesAreEqual` |
| 3.1 | 469 | `java.lang.AbstractStringBuilder.needsNewBuffer` |

