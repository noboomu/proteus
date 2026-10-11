# CPU profile

24348 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 78.1 | 19005 | `proteus I/O-*` |
| 19.7 | 4800 | `(unnamed virtual thread)` |
| 2.1 | 502 | ` ForkJoinPool-1-worker-*` |
| 0.2 | 48 | `main` |
| 0.0 | 5 | `VirtualThread-unblocker` |
| 0.0 | 3 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 29.1 | 7094 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 11.0 | 2685 | `java.util.IdentityHashMap.get` |
| 6.3 | 1524 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 4.1 | 994 | `org.xnio.nio.WorkerThread.setOps` |
| 3.2 | 785 | `sun.nio.ch.SocketDispatcher.read` |
| 2.7 | 664 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.1 | 504 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.9 | 457 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.8 | 430 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.6 | 388 | `java.util.TreeMap.put` |
| 1.4 | 350 | `io.undertow.util.HttpString.calcHashCode` |
| 1.4 | 347 | `org.xnio.Buffers.copy` |
| 1.4 | 335 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.3 | 326 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 253 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.0 | 241 | `java.util.TreeMap.deleteEntry` |
| 1.0 | 238 | `sun.nio.ch.EPollSelectorImpl.doSelect` |
| 0.9 | 219 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 0.9 | 216 | `jdk.internal.misc.Unsafe.checkOffset` |
| 0.7 | 175 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.7 | 171 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.7 | 167 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.7 | 166 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.6 | 157 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 0.6 | 157 | `java.util.concurrent.ConcurrentHashMap.addCount` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 37.8 | 9201 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 31.5 | 7670 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 29.7 | 7230 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 29.2 | 7105 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 13.2 | 3209 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 13.0 | 3168 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 11.0 | 2687 | `java.util.IdentityHashMap.get` |
| 10.4 | 2538 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 9.5 | 2319 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 9.2 | 2237 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 9.2 | 2229 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 9.0 | 2187 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 6.3 | 1524 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 6.0 | 1473 | `sun.nio.ch.SocketChannelImpl.read` |
| 6.0 | 1457 | `org.xnio.nio.NioSocketConduit.read` |
| 6.0 | 1449 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 5.3 | 1280 | `org.xnio.nio.NioHandle.resume` |
| 5.3 | 1279 | `org.xnio.nio.WorkerThread.setOps` |
| 5.0 | 1216 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 4.7 | 1151 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 4.2 | 1012 | `org.xnio.conduits.AbstractSourceConduit.resumeReads` |
| 3.9 | 939 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 3.4 | 834 | `io.undertow.util.HttpString.bytesAreEqual` |
| 3.4 | 834 | `io.undertow.util.HttpString.equals` |
| 3.3 | 798 | `sun.nio.ch.IOUtil.read` |

