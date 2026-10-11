# CPU profile

22814 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 78.7 | 17944 | `proteus I/O-*` |
| 12.2 | 2790 | `(unnamed virtual thread)` |
| 8.1 | 1846 | `proteus task-*` |
| 0.8 | 193 | ` ForkJoinPool-1-worker-*` |
| 0.2 | 51 | `main` |
| 0.0 | 3 | `VirtualThread-unblocker` |
| 0.0 | 3 | `JFR Periodic Tasks` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 20.6 | 4708 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 12.4 | 2834 | `java.util.IdentityHashMap.get` |
| 11.6 | 2641 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 2.9 | 659 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.6 | 597 | `org.xnio.nio.WorkerThread.setOps` |
| 2.1 | 476 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.9 | 438 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.8 | 420 | `org.xnio.Buffers.copy` |
| 1.8 | 412 | `java.util.TreeMap.deleteEntry` |
| 1.8 | 403 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.7 | 378 | `java.util.TreeMap.put` |
| 1.5 | 351 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.5 | 349 | `io.undertow.util.HttpString.calcHashCode` |
| 1.3 | 293 | `tools.jackson.core.json.JsonFactory._createUTF8Generator` |
| 1.2 | 282 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.1 | 259 | `io.undertow.util.HttpString.higher` |
| 1.0 | 225 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 0.9 | 215 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.9 | 214 | `jdk.internal.misc.Unsafe.checkOffset` |
| 0.9 | 208 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.9 | 197 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.8 | 192 | `java.lang.AbstractStringBuilder.append` |
| 0.8 | 177 | `sun.nio.ch.EPollSelectorImpl.doSelect` |
| 0.7 | 169 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.7 | 162 | `tools.jackson.core.util.BufferRecycler.releaseByteBuffer` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 40.2 | 9180 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 33.2 | 7566 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 32.3 | 7360 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 23.2 | 5290 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 20.7 | 4722 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 14.4 | 3288 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 14.3 | 3266 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.4 | 2834 | `java.util.IdentityHashMap.get` |
| 10.6 | 2429 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 10.3 | 2346 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 10.0 | 2290 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 4.2 | 955 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 3.8 | 863 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 3.7 | 852 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 3.7 | 834 | `org.xnio.nio.NioHandle.resume` |
| 3.4 | 774 | `io.undertow.util.HttpString.equals` |
| 3.4 | 773 | `io.undertow.util.HttpString.bytesAreEqual` |
| 3.4 | 772 | `org.xnio.nio.WorkerThread.setOps` |
| 3.3 | 761 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.3 | 746 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.2 | 728 | `org.xnio.nio.NioSocketConduit.resumeReads` |
| 3.0 | 681 | `java.util.TreeSet.remove` |
| 3.0 | 680 | `java.util.TreeMap.remove` |
| 2.9 | 670 | `java.util.HashMap.getNode` |
| 2.9 | 663 | `org.xnio.conduits.AbstractSourceConduit.resumeReads` |

