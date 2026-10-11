# CPU profile

21637 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 99.8 | 21585 | `proteus I/O-*` |
| 0.2 | 42 | `main` |
| 0.0 | 4 | `proteus Accept` |
| 0.0 | 3 | `(unnamed virtual thread)` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `Logging-Cleaner` |
| 0.0 | 1 | `JFR Shutdown Hook` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 19.5 | 4223 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 10.1 | 2195 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 6.7 | 1443 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 6.5 | 1410 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 4.0 | 867 | `java.nio.ByteBuffer.allocate` |
| 2.9 | 634 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.9 | 630 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.8 | 611 | `java.util.TreeMap.put` |
| 2.0 | 424 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.8 | 391 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.7 | 371 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.7 | 360 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.6 | 343 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.6 | 340 | `java.util.HashMap.getNode` |
| 1.5 | 329 | `sun.nio.ch.SocketDispatcher.read` |
| 1.5 | 328 | `java.nio.ByteBuffer.putBuffer` |
| 1.5 | 323 | `io.undertow.util.HttpString.calcHashCode` |
| 1.0 | 227 | `org.xnio.SingleOption.cast` |
| 1.0 | 221 | `java.lang.AbstractStringBuilder.append` |
| 1.0 | 218 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 1.0 | 213 | `sun.nio.ch.SocketDispatcher.writev` |
| 1.0 | 210 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 0.9 | 190 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.8 | 178 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 0.8 | 176 | `java.util.TreeMap.getEntry` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 29.5 | 6381 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 29.0 | 6274 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 28.9 | 6247 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 28.8 | 6242 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 23.5 | 5085 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 21.4 | 4638 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 19.7 | 4255 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 13.4 | 2898 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 12.8 | 2776 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 10.8 | 2338 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 8.5 | 1846 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 8.0 | 1733 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 6.7 | 1460 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 6.7 | 1452 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 6.6 | 1420 | `sun.nio.ch.SocketChannelImpl.read` |
| 6.6 | 1419 | `org.xnio.nio.NioSocketConduit.read` |
| 6.6 | 1418 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 4.2 | 919 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 4.0 | 871 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 4.0 | 870 | `java.nio.ByteBuffer.allocate` |
| 4.0 | 855 | `java.lang.AbstractStringBuilder.append` |
| 4.0 | 855 | `java.lang.StringBuilder.append` |
| 3.4 | 736 | `io.undertow.util.HttpString.equals` |
| 3.4 | 736 | `io.undertow.util.HttpString.bytesAreEqual` |
| 3.2 | 684 | `sun.nio.ch.SocketChannelImpl.implWrite` |

