# CPU profile

15129 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 87.8 | 13286 | `proteus I/O-*` |
| 6.2 | 931 | `(unnamed virtual thread)` |
| 5.1 | 774 | `proteus task-*` |
| 0.6 | 92 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 41 | `main` |
| 0.0 | 5 | `VirtualThread-unblocker` |
| 0.0 | 4 | `proteus Accept` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 10.9 | 1650 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 10.8 | 1632 | `java.util.IdentityHashMap.get` |
| 10.4 | 1569 | `sun.nio.ch.SocketDispatcher.read` |
| 4.5 | 687 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 3.8 | 569 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.7 | 553 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 2.9 | 439 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.8 | 417 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.7 | 407 | `java.util.TreeMap.put` |
| 2.2 | 338 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 2.2 | 336 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.9 | 287 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.8 | 279 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.7 | 264 | `io.undertow.util.HttpString.calcHashCode` |
| 1.7 | 262 | `java.nio.ByteBuffer.allocate` |
| 1.6 | 240 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.2 | 183 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.2 | 176 | `sun.nio.ch.IOUtil.write` |
| 1.1 | 165 | `java.util.TreeMap.deleteEntry` |
| 1.1 | 162 | `org.xnio.Buffers.copy` |
| 1.0 | 148 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.9 | 138 | `java.lang.AbstractStringBuilder.append` |
| 0.9 | 137 | `java.util.TreeMap.successor` |
| 0.9 | 135 | `java.nio.ByteBuffer.putBuffer` |
| 0.8 | 126 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 26.1 | 3947 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 19.6 | 2966 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 19.4 | 2931 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 18.9 | 2858 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 15.9 | 2409 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 14.6 | 2205 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 12.9 | 1959 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 10.9 | 1652 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 10.8 | 1634 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 10.8 | 1633 | `java.util.IdentityHashMap.get` |
| 10.5 | 1584 | `sun.nio.ch.IOUtil.read` |
| 10.4 | 1569 | `sun.nio.ch.SocketDispatcher.read` |
| 10.4 | 1569 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 10.1 | 1521 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 10.0 | 1512 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 9.3 | 1406 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.9 | 1192 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.5 | 687 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.3 | 658 | `io.undertow.util.HttpString.equals` |
| 4.3 | 658 | `io.undertow.util.HttpString.bytesAreEqual` |
| 3.9 | 583 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 3.8 | 578 | `java.lang.StringBuilder.append` |
| 3.8 | 577 | `java.lang.AbstractStringBuilder.append` |
| 3.6 | 551 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.6 | 544 | `io.undertow.channels.DetachableStreamSinkChannel.write` |

