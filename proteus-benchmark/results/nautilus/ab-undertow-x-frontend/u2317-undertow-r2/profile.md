# CPU profile

9074 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 90.1 | 8172 | `proteus I/O-*` |
| 9.4 | 850 | `proteus task-*` |
| 0.5 | 47 | `main` |
| 0.0 | 4 | `(unnamed virtual thread)` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 28.6 | 2598 | `java.nio.ByteBuffer.position` |
| 5.2 | 469 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.5 | 320 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 3.2 | 286 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.1 | 281 | `java.util.IdentityHashMap.get` |
| 3.1 | 277 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.2 | 199 | `java.util.TreeMap.put` |
| 2.2 | 196 | `java.util.TreeMap.successor` |
| 2.1 | 193 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 2.1 | 191 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 2.1 | 188 | `io.undertow.util.HttpString.calcHashCode` |
| 1.9 | 172 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.5 | 139 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 1.5 | 136 | `sun.nio.ch.SocketDispatcher.read` |
| 1.5 | 134 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.4 | 129 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.4 | 123 | `sun.nio.ch.IOUtil.write` |
| 1.1 | 96 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.0 | 95 | `io.undertow.server.Connectors.verifyToken` |
| 1.0 | 94 | `java.lang.AbstractStringBuilder.append` |
| 1.0 | 90 | `ch.qos.logback.classic.Logger.isInfoEnabled` |
| 0.9 | 79 | `java.nio.ByteBuffer.putBuffer` |
| 0.8 | 73 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.7 | 68 | `org.xnio.SingleOption.cast` |
| 0.7 | 65 | `sun.nio.ch.SocketDispatcher.writev` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 29.0 | 2634 | `java.nio.Buffer.<init>` |
| 29.0 | 2634 | `java.nio.ByteBuffer.<init>` |
| 29.0 | 2633 | `java.nio.ByteBuffer.position` |
| 28.8 | 2610 | `java.nio.HeapByteBuffer.<init>` |
| 28.5 | 2589 | `java.nio.ByteBuffer.allocate` |
| 14.5 | 1319 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 14.3 | 1299 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.6 | 1236 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 8.0 | 727 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 7.7 | 697 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.7 | 610 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 6.4 | 584 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 6.1 | 554 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.4 | 493 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 5.0 | 450 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 4.6 | 415 | `io.undertow.util.HttpString.equals` |
| 4.6 | 414 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.5 | 408 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 4.2 | 385 | `java.lang.StringBuilder.append` |
| 4.2 | 383 | `java.lang.AbstractStringBuilder.append` |
| 4.0 | 360 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.5 | 321 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 3.2 | 286 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.1 | 282 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 3.1 | 281 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |

