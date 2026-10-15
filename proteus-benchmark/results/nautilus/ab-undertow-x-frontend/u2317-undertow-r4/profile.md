# CPU profile

8837 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 90.9 | 8032 | `proteus I/O-*` |
| 8.5 | 755 | `proteus task-*` |
| 0.5 | 41 | `main` |
| 0.0 | 3 | `(unnamed virtual thread)` |
| 0.0 | 3 | `proteus Accept` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 1 | `SIGTERM handler` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 18.5 | 1635 | `java.util.IdentityHashMap.get` |
| 8.4 | 741 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 4.8 | 422 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.4 | 393 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 3.5 | 312 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 3.2 | 284 | `sun.nio.ch.SocketDispatcher.read` |
| 3.1 | 277 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.5 | 222 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 2.3 | 199 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.2 | 196 | `java.util.TreeMap.put` |
| 1.9 | 170 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.9 | 167 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.8 | 156 | `io.undertow.util.HttpString.calcHashCode` |
| 1.4 | 128 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.4 | 126 | `sun.nio.ch.IOUtil.write` |
| 1.2 | 109 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 1.2 | 107 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 1.2 | 106 | `java.util.TreeMap.deleteEntry` |
| 1.2 | 105 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.1 | 100 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 1.1 | 94 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 1.1 | 93 | `sun.nio.ch.IOUtil.read` |
| 1.1 | 93 | `java.lang.AbstractStringBuilder.append` |
| 1.1 | 93 | `io.undertow.server.Connectors.verifyToken` |
| 1.0 | 92 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 24.0 | 2125 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 23.7 | 2097 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 21.5 | 1900 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 21.5 | 1896 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 18.5 | 1635 | `java.util.IdentityHashMap.get` |
| 15.3 | 1349 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 14.8 | 1310 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 14.7 | 1295 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 14.6 | 1291 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 9.4 | 832 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 6.7 | 589 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 6.1 | 535 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.5 | 482 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 5.4 | 479 | `io.undertow.util.HttpString.bytesAreEqual` |
| 5.4 | 479 | `io.undertow.util.HttpString.equals` |
| 5.4 | 476 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 5.2 | 462 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 4.5 | 400 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 4.4 | 392 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 4.3 | 384 | `sun.nio.ch.IOUtil.read` |
| 4.2 | 372 | `java.lang.StringBuilder.append` |
| 4.2 | 370 | `java.lang.AbstractStringBuilder.append` |
| 4.2 | 368 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 3.9 | 349 | `org.xnio.OptionMap.get` |
| 3.7 | 328 | `java.util.HashMap.getNode` |

