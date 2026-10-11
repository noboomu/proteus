# CPU profile

18147 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 99.7 | 18096 | `proteus I/O-*` |
| 0.3 | 48 | `main` |
| 0.0 | 2 | `proteus Accept` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 21.1 | 3831 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 13.0 | 2353 | `sun.nio.ch.SocketDispatcher.read` |
| 7.7 | 1393 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 5.0 | 912 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.8 | 873 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 4.4 | 804 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.9 | 530 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 2.7 | 487 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 2.7 | 486 | `io.undertow.util.HttpString.calcHashCode` |
| 2.1 | 385 | `sun.nio.ch.IOUtil.write` |
| 2.1 | 383 | `java.lang.AbstractStringBuilder.append` |
| 1.6 | 291 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.4 | 249 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.4 | 245 | `io.undertow.server.Connectors.verifyToken` |
| 1.3 | 243 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.2 | 220 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 206 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.1 | 191 | `io.undertow.util.HttpString.higher` |
| 1.0 | 187 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 0.9 | 169 | `io.undertow.util.HttpString.checkForNewlines` |
| 0.9 | 166 | `java.util.WeakHashMap.hash` |
| 0.9 | 163 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 0.9 | 159 | `java.util.IdentityHashMap.get` |
| 0.9 | 156 | `java.lang.StringBuilder.toString` |
| 0.8 | 137 | `java.util.LinkedHashMap.afterNodeAccess` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 30.9 | 5599 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 30.2 | 5481 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 29.9 | 5435 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 29.2 | 5307 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 21.4 | 3892 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 21.2 | 3852 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 20.5 | 3716 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.2 | 2402 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 13.1 | 2375 | `sun.nio.ch.IOUtil.read` |
| 13.0 | 2358 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 13.0 | 2354 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 13.0 | 2353 | `sun.nio.ch.SocketDispatcher.read` |
| 7.7 | 1393 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 7.1 | 1297 | `java.lang.StringBuilder.append` |
| 7.1 | 1295 | `java.lang.AbstractStringBuilder.append` |
| 6.0 | 1094 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 5.8 | 1060 | `io.undertow.util.HttpString.bytesAreEqual` |
| 5.8 | 1060 | `io.undertow.util.HttpString.equals` |
| 5.8 | 1058 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 5.0 | 912 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.8 | 880 | `java.lang.Long.toString` |
| 4.8 | 880 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 4.7 | 859 | `io.undertow.server.HttpServerExchange.<init>` |
| 4.6 | 830 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 4.5 | 811 | `io.undertow.util.HttpString.bytesAreEquivalent` |

