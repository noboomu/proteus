# CPU profile

20341 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 99.7 | 20279 | `proteus I/O-*` |
| 0.3 | 52 | `main` |
| 0.0 | 7 | `proteus Accept` |
| 0.0 | 2 | `(unnamed virtual thread)` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 22.2 | 4511 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.1 | 2456 | `sun.nio.ch.SocketDispatcher.read` |
| 8.1 | 1640 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.5 | 908 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 3.7 | 760 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.5 | 718 | `java.util.TreeMap.put` |
| 3.2 | 645 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.4 | 492 | `io.undertow.util.HttpString.calcHashCode` |
| 2.2 | 443 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.9 | 395 | `sun.nio.ch.IOUtil.write` |
| 1.9 | 393 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.6 | 328 | `java.lang.AbstractStringBuilder.append` |
| 1.4 | 287 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.4 | 282 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.2 | 236 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 232 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.1 | 220 | `io.undertow.server.Connectors.verifyToken` |
| 1.1 | 217 | `java.util.TreeMap.deleteEntry` |
| 0.9 | 186 | `java.lang.StringBuilder.toString` |
| 0.9 | 181 | `org.xnio.SingleOption.cast` |
| 0.9 | 174 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.8 | 163 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.8 | 157 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 0.8 | 154 | `jdk.internal.misc.Unsafe.checkPrimitiveArray` |
| 0.7 | 149 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 32.0 | 6500 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 31.6 | 6429 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 31.4 | 6390 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 30.6 | 6215 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 22.6 | 4592 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 16.3 | 3323 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 16.3 | 3307 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 12.7 | 2578 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 12.2 | 2487 | `sun.nio.ch.IOUtil.read` |
| 12.1 | 2459 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 12.1 | 2456 | `sun.nio.ch.SocketDispatcher.read` |
| 9.6 | 1951 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 8.1 | 1640 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 5.3 | 1088 | `java.lang.AbstractStringBuilder.append` |
| 5.3 | 1088 | `java.lang.StringBuilder.append` |
| 4.7 | 951 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 4.6 | 935 | `io.undertow.util.HttpString.equals` |
| 4.6 | 935 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.5 | 908 | `java.lang.Long.toString` |
| 4.5 | 908 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 4.4 | 892 | `io.undertow.server.HttpServerExchange.<init>` |
| 4.2 | 852 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 4.0 | 805 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.9 | 798 | `sun.nio.ch.IOUtil.write` |
| 3.9 | 794 | `org.xnio.nio.WorkerThread.executeAfter` |

