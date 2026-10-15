# CPU profile

15690 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 87.5 | 13731 | `proteus I/O-*` |
| 6.0 | 949 | `(unnamed virtual thread)` |
| 5.5 | 858 | `proteus task-*` |
| 0.7 | 114 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 41 | `main` |
| 0.0 | 2 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `VirtualThread-unblocker` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 10.8 | 1700 | `java.util.IdentityHashMap.get` |
| 10.3 | 1616 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 10.1 | 1589 | `sun.nio.ch.SocketDispatcher.read` |
| 5.0 | 777 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.5 | 712 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.5 | 545 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 3.0 | 477 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.7 | 429 | `java.util.TreeMap.put` |
| 2.2 | 348 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.1 | 329 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 1.9 | 295 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.7 | 272 | `io.undertow.util.HttpString.calcHashCode` |
| 1.7 | 265 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.6 | 250 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.5 | 242 | `java.nio.ByteBuffer.allocate` |
| 1.5 | 241 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.3 | 205 | `io.undertow.util.HttpString.higher` |
| 1.3 | 202 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 169 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 1.1 | 166 | `java.util.TreeMap.successor` |
| 1.0 | 153 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.0 | 153 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.0 | 151 | `java.lang.AbstractStringBuilder.append` |
| 0.9 | 148 | `org.xnio.Buffers.copy` |
| 0.9 | 135 | `io.undertow.server.Connectors.verifyToken` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 25.6 | 4011 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 20.7 | 3248 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 20.4 | 3206 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 18.7 | 2941 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 16.1 | 2532 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 13.8 | 2162 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 12.5 | 1964 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 10.8 | 1702 | `java.util.IdentityHashMap.get` |
| 10.6 | 1667 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 10.3 | 1617 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 10.2 | 1608 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 10.2 | 1603 | `sun.nio.ch.IOUtil.read` |
| 10.2 | 1593 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 10.1 | 1589 | `sun.nio.ch.SocketDispatcher.read` |
| 9.8 | 1538 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 9.8 | 1536 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 9.4 | 1476 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.0 | 777 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.4 | 683 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.4 | 683 | `io.undertow.util.HttpString.equals` |
| 4.3 | 672 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 4.0 | 630 | `java.lang.StringBuilder.append` |
| 4.0 | 630 | `java.lang.AbstractStringBuilder.append` |
| 3.7 | 584 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.5 | 544 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |

