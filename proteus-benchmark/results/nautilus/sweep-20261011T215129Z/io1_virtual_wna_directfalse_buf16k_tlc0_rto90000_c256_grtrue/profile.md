# CPU profile

24687 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 76.9 | 18975 | `proteus I/O-*` |
| 20.8 | 5126 | `(unnamed virtual thread)` |
| 2.2 | 543 | ` ForkJoinPool-1-worker-*` |
| 0.2 | 57 | `main` |
| 0.0 | 6 | `proteus Accept` |
| 0.0 | 6 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 30.0 | 7407 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 11.1 | 2751 | `java.util.IdentityHashMap.get` |
| 8.0 | 1979 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 3.8 | 939 | `org.xnio.nio.WorkerThread.setOps` |
| 2.8 | 690 | `java.lang.ThreadLocal.get` |
| 2.8 | 687 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.6 | 401 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.5 | 365 | `java.util.TreeMap.put` |
| 1.5 | 362 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.5 | 360 | `org.xnio.Buffers.copy` |
| 1.4 | 340 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.2 | 295 | `sun.nio.ch.EPollSelectorImpl.doSelect` |
| 1.1 | 265 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.0 | 256 | `io.undertow.util.HttpString.calcHashCode` |
| 1.0 | 252 | `java.util.TreeMap.deleteEntry` |
| 0.9 | 232 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 0.9 | 226 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.9 | 214 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |
| 0.8 | 191 | `jdk.internal.misc.Unsafe.checkOffset` |
| 0.7 | 176 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.7 | 173 | `io.undertow.util.HttpString.higher` |
| 0.7 | 171 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.7 | 165 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.7 | 164 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 0.6 | 159 | `io.undertow.server.Connectors.verifyToken` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 45.7 | 11273 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 39.2 | 9678 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 38.1 | 9396 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 30.0 | 7417 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 17.8 | 4393 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.6 | 3364 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 13.6 | 3361 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 11.1 | 2751 | `java.util.IdentityHashMap.get` |
| 9.9 | 2436 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 9.6 | 2379 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 8.7 | 2139 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 4.8 | 1193 | `org.xnio.nio.NioHandle.resume` |
| 4.8 | 1192 | `org.xnio.nio.WorkerThread.setOps` |
| 4.7 | 1149 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 4.3 | 1059 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 3.9 | 954 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 3.9 | 952 | `org.xnio.conduits.AbstractSourceConduit.resumeReads` |
| 3.0 | 736 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.0 | 731 | `java.lang.ThreadLocal.get` |
| 2.8 | 694 | `tools.jackson.core.util.JsonRecyclerPools$ThreadLocalPool.acquirePooled` |
| 2.7 | 673 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 2.7 | 659 | `io.undertow.util.HttpString.equals` |
| 2.7 | 659 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.6 | 651 | `tools.jackson.core.util.RecyclerPool$ThreadLocalPoolBase.acquireAndLinkPooled` |
| 2.6 | 635 | `io.undertow.channels.DetachableStreamSinkChannel.write` |

