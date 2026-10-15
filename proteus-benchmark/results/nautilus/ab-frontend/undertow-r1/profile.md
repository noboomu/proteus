# CPU profile

15851 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 86.4 | 13693 | `proteus I/O-*` |
| 7.0 | 1112 | `(unnamed virtual thread)` |
| 5.7 | 900 | `proteus task-*` |
| 0.7 | 108 | ` ForkJoinPool-1-worker-*` |
| 0.2 | 39 | `main` |
| 0.0 | 5 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 11.8 | 1876 | `java.util.IdentityHashMap.get` |
| 11.7 | 1854 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 8.8 | 1390 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 5.5 | 867 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.3 | 679 | `ch.qos.logback.classic.Logger.isInfoEnabled` |
| 3.9 | 620 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.1 | 499 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 3.1 | 490 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.0 | 323 | `java.util.TreeMap.getEntry` |
| 2.0 | 313 | `java.util.TreeMap.successor` |
| 2.0 | 310 | `org.xnio.nio.WorkerThread.setOps` |
| 1.9 | 300 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.7 | 276 | `java.util.TreeMap.put` |
| 1.7 | 275 | `io.undertow.util.HttpString.calcHashCode` |
| 1.7 | 271 | `java.util.HashMap.getNode` |
| 1.6 | 257 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.5 | 232 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 1.2 | 191 | `jdk.internal.misc.Unsafe.checkOffset` |
| 1.1 | 182 | `io.undertow.util.HttpString.higher` |
| 1.1 | 177 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 1.1 | 171 | `io.undertow.server.Connectors.verifyToken` |
| 1.1 | 168 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 166 | `sun.nio.ch.IOUtil.write` |
| 1.0 | 166 | `java.lang.AbstractStringBuilder.append` |
| 0.9 | 147 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 31.6 | 5002 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 26.1 | 4136 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 23.2 | 3672 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 20.5 | 3248 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 19.6 | 3108 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 19.6 | 3100 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 16.2 | 2572 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 11.8 | 1876 | `java.util.IdentityHashMap.get` |
| 10.2 | 1622 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 9.5 | 1499 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 9.2 | 1464 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 9.2 | 1460 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 8.8 | 1393 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 6.2 | 976 | `org.xnio.nio.WorkerThread.run` |
| 6.0 | 954 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 5.5 | 867 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.5 | 716 | `java.util.TreeSet.remove` |
| 4.5 | 711 | `java.util.TreeMap.remove` |
| 4.5 | 706 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 4.3 | 686 | `org.jboss.logging.DelegatingBasicLogger.tracef` |
| 4.3 | 682 | `org.jboss.logging.Logger.logf` |
| 4.3 | 681 | `org.jboss.logging.Slf4jLocationAwareLogger.isEnabled` |
| 4.3 | 679 | `ch.qos.logback.classic.Logger.isInfoEnabled` |
| 4.1 | 656 | `java.lang.AbstractStringBuilder.append` |
| 4.1 | 656 | `java.lang.StringBuilder.append` |

