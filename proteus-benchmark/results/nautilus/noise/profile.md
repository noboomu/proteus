# CPU profile

13984 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 99.6 | 13927 | `proteus I/O-*` |
| 0.3 | 47 | `main` |
| 0.0 | 6 | `proteus Accept` |
| 0.0 | 4 | `(unnamed virtual thread)` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 23.1 | 3227 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 11.4 | 1593 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 6.1 | 851 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.7 | 519 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.3 | 463 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 3.0 | 425 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.3 | 328 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.2 | 305 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 2.0 | 286 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 2.0 | 283 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.9 | 260 | `io.undertow.util.HttpString.calcHashCode` |
| 1.6 | 228 | `java.util.TreeMap.put` |
| 1.6 | 227 | `java.util.IdentityHashMap.get` |
| 1.5 | 207 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 1.4 | 202 | `sun.nio.ch.IOUtil.write` |
| 1.3 | 181 | `java.lang.AbstractStringBuilder.append` |
| 1.3 | 179 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.2 | 166 | `io.undertow.server.Connectors.verifyToken` |
| 1.2 | 163 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 1.1 | 151 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 1.0 | 141 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 136 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 1.0 | 134 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 125 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.9 | 122 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 31.5 | 4399 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 31.3 | 4375 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 31.0 | 4336 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 30.3 | 4243 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 27.8 | 3883 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 23.9 | 3348 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 23.1 | 3227 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 15.4 | 2157 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 14.7 | 2049 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 12.4 | 1729 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 6.5 | 906 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 5.7 | 791 | `io.undertow.util.HttpString.bytesAreEqual` |
| 5.7 | 791 | `io.undertow.util.HttpString.equals` |
| 5.0 | 701 | `java.lang.StringBuilder.append` |
| 5.0 | 700 | `java.lang.AbstractStringBuilder.append` |
| 4.6 | 649 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 3.7 | 523 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 3.7 | 519 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.6 | 500 | `java.util.LinkedHashMap.get` |
| 3.5 | 485 | `java.util.HashMap.getNode` |
| 3.3 | 463 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 3.2 | 451 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 3.2 | 446 | `sun.nio.ch.IOUtil.write` |
| 3.2 | 444 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.1 | 428 | `java.lang.Long.toString` |

