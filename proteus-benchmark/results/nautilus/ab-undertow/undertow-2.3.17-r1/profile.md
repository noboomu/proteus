# CPU profile

15266 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 87.0 | 13279 | `proteus I/O-*` |
| 6.7 | 1017 | `(unnamed virtual thread)` |
| 5.4 | 829 | `proteus task-*` |
| 0.6 | 94 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 47 | `main` |
| 0.0 | 1 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 10.8 | 1653 | `java.util.IdentityHashMap.get` |
| 10.0 | 1523 | `sun.nio.ch.SocketDispatcher.read` |
| 9.3 | 1422 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.5 | 681 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.4 | 672 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.2 | 637 | `ch.qos.logback.classic.Logger.isInfoEnabled` |
| 3.2 | 495 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.1 | 470 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.9 | 445 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.5 | 385 | `java.util.TreeMap.put` |
| 2.1 | 322 | `org.xnio.nio.WorkerThread.setOps` |
| 2.0 | 300 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.8 | 280 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.8 | 277 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.7 | 265 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.7 | 255 | `io.undertow.util.HttpString.calcHashCode` |
| 1.6 | 244 | `java.util.TreeMap.deleteEntry` |
| 1.4 | 209 | `java.lang.AbstractStringBuilder.append` |
| 1.3 | 198 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.1 | 168 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 154 | `io.undertow.server.Connectors.verifyToken` |
| 1.0 | 150 | `org.xnio.Buffers.copy` |
| 0.8 | 126 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 0.8 | 121 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 0.8 | 115 | `java.util.WeakHashMap.hash` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 20.1 | 3074 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 19.7 | 3011 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 18.7 | 2849 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 14.6 | 2226 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.1 | 2153 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 12.5 | 1908 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 11.4 | 1741 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 10.8 | 1654 | `java.util.IdentityHashMap.get` |
| 10.1 | 1548 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 10.1 | 1547 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 10.1 | 1541 | `sun.nio.ch.IOUtil.read` |
| 10.0 | 1534 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 10.0 | 1524 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 10.0 | 1523 | `sun.nio.ch.SocketDispatcher.read` |
| 9.5 | 1445 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 9.3 | 1425 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 8.6 | 1312 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.2 | 952 | `org.xnio.nio.WorkerThread.run` |
| 4.7 | 718 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.7 | 718 | `io.undertow.util.HttpString.equals` |
| 4.6 | 705 | `java.lang.StringBuilder.append` |
| 4.6 | 705 | `java.lang.AbstractStringBuilder.append` |
| 4.4 | 672 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.4 | 672 | `org.jboss.logging.DelegatingBasicLogger.tracef` |
| 4.3 | 664 | `org.jboss.logging.Logger.logf` |

