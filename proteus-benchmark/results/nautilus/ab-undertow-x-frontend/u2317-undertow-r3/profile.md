# CPU profile

8880 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 90.4 | 8024 | `proteus I/O-*` |
| 8.9 | 792 | `proteus task-*` |
| 0.6 | 53 | `main` |
| 0.1 | 6 | `proteus Accept` |
| 0.0 | 2 | `(unnamed virtual thread)` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 12.5 | 1110 | `sun.nio.ch.SocketDispatcher.read` |
| 11.3 | 1000 | `java.util.IdentityHashMap.get` |
| 4.9 | 435 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.9 | 435 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.1 | 361 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 3.2 | 281 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.0 | 264 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.3 | 206 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 2.2 | 195 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 2.1 | 189 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 2.0 | 179 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.9 | 173 | `java.util.TreeMap.put` |
| 1.8 | 164 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.6 | 145 | `io.undertow.util.HttpString.calcHashCode` |
| 1.5 | 134 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 1.4 | 123 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.3 | 116 | `java.util.TreeMap.getEntry` |
| 1.2 | 104 | `java.lang.AbstractStringBuilder.append` |
| 1.2 | 103 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 1.1 | 102 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.1 | 102 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 1.1 | 101 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 1.1 | 98 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.1 | 94 | `org.xnio.nio.WorkerThread.setOps` |
| 1.0 | 90 | `ch.qos.logback.classic.Logger.isTraceEnabled` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 21.9 | 1946 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 21.8 | 1933 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 18.0 | 1599 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 17.1 | 1516 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 15.3 | 1357 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 12.9 | 1147 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 12.7 | 1126 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 12.6 | 1123 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 12.6 | 1121 | `sun.nio.ch.IOUtil.read` |
| 12.5 | 1114 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 12.5 | 1114 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 12.5 | 1110 | `sun.nio.ch.SocketDispatcher.read` |
| 11.3 | 1001 | `java.util.IdentityHashMap.get` |
| 7.8 | 692 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 7.4 | 658 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 5.9 | 522 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 5.4 | 476 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 5.1 | 449 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 4.4 | 387 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.4 | 387 | `io.undertow.util.HttpString.equals` |
| 4.3 | 385 | `java.lang.StringBuilder.append` |
| 4.3 | 385 | `java.lang.AbstractStringBuilder.append` |
| 4.2 | 373 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 4.0 | 354 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.9 | 350 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |

