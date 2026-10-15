# CPU profile

15461 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 86.4 | 13363 | `proteus I/O-*` |
| 7.0 | 1079 | `(unnamed virtual thread)` |
| 5.5 | 846 | `proteus task-*` |
| 0.8 | 119 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 51 | `main` |
| 0.0 | 4 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 24.5 | 3782 | `java.nio.ByteBuffer.position` |
| 12.8 | 1974 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.2 | 645 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.1 | 482 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.0 | 468 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.8 | 437 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.7 | 418 | `java.util.IdentityHashMap.get` |
| 2.5 | 393 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 2.0 | 312 | `java.util.TreeMap.put` |
| 2.0 | 309 | `io.undertow.util.HttpString.calcHashCode` |
| 1.6 | 255 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.6 | 242 | `java.util.TreeMap.deleteEntry` |
| 1.4 | 220 | `sun.nio.ch.SocketDispatcher.read` |
| 1.4 | 220 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.4 | 212 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.3 | 197 | `org.xnio.nio.NioHandle.resume` |
| 1.2 | 178 | `sun.nio.ch.IOUtil.write` |
| 1.1 | 169 | `org.xnio.nio.WorkerThread.setOps` |
| 1.1 | 166 | `java.lang.AbstractStringBuilder.append` |
| 1.1 | 164 | `io.undertow.server.Connectors.verifyToken` |
| 1.1 | 164 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 0.9 | 146 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.9 | 141 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.9 | 138 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.9 | 134 | `ch.qos.logback.classic.Logger.isTraceEnabled` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 24.8 | 3830 | `java.nio.ByteBuffer.position` |
| 24.7 | 3826 | `java.nio.ByteBuffer.<init>` |
| 24.7 | 3826 | `java.nio.Buffer.<init>` |
| 24.5 | 3791 | `java.nio.HeapByteBuffer.<init>` |
| 24.3 | 3760 | `java.nio.ByteBuffer.allocate` |
| 22.7 | 3506 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 14.9 | 2300 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 14.7 | 2271 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.4 | 2224 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 13.8 | 2128 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 12.9 | 1990 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 7.3 | 1136 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 7.0 | 1087 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 5.8 | 901 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.9 | 759 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.3 | 666 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 4.2 | 651 | `io.undertow.util.HttpString.equals` |
| 4.2 | 650 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.2 | 648 | `java.lang.StringBuilder.append` |
| 4.2 | 648 | `java.lang.AbstractStringBuilder.append` |
| 3.9 | 607 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 3.6 | 562 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 3.4 | 523 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.1 | 482 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.0 | 468 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |

