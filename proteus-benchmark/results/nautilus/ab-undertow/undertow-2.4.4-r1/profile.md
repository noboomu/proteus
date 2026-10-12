# CPU profile

19005 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 87.8 | 16692 | `proteus I/O-*` |
| 6.8 | 1288 | `proteus task-*` |
| 4.7 | 885 | `(unnamed virtual thread)` |
| 0.5 | 92 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 51 | `main` |
| 0.0 | 3 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `Logging-Cleaner` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.1 | 2678 | `java.util.IdentityHashMap.get` |
| 6.7 | 1275 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 6.3 | 1189 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.1 | 785 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.1 | 777 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 3.7 | 705 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 3.6 | 678 | `java.util.regex.Pattern$GroupHead.match` |
| 3.3 | 625 | `java.util.HashMap.getNode` |
| 3.0 | 572 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.7 | 514 | `java.util.regex.Pattern$GroupTail.match` |
| 2.6 | 491 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 2.5 | 478 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 2.4 | 462 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.2 | 412 | `java.util.regex.Pattern$Branch.match` |
| 2.0 | 372 | `io.undertow.util.HttpString.higher` |
| 1.5 | 285 | `java.util.regex.Matcher.reset` |
| 1.4 | 260 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.3 | 246 | `java.util.TreeMap.put` |
| 1.3 | 239 | `java.util.TreeMap.getEntry` |
| 1.2 | 231 | `java.nio.ByteBuffer.allocate` |
| 0.9 | 163 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 0.8 | 160 | `io.undertow.util.HttpString.calcHashCode` |
| 0.8 | 155 | `sun.nio.ch.IOUtil.write` |
| 0.8 | 151 | `org.xnio.Buffers.copy` |
| 0.7 | 139 | `org.xnio.nio.NioHandle.resume` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 17.6 | 3347 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 16.5 | 3140 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 16.3 | 3105 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 15.6 | 2959 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.1 | 2681 | `java.util.IdentityHashMap.get` |
| 12.2 | 2313 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 12.0 | 2273 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 11.8 | 2250 | `java.util.regex.Pattern$GroupHead.match` |
| 10.2 | 1938 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 10.0 | 1903 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 8.8 | 1680 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 8.0 | 1524 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 7.3 | 1395 | `java.util.regex.Pattern$Branch.match` |
| 6.9 | 1311 | `java.lang.ThreadLocal.get` |
| 6.9 | 1302 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 6.7 | 1277 | `java.lang.ThreadLocal.getCarrierThreadLocal` |
| 6.7 | 1277 | `java.lang.System$1.getCarrierThreadLocal` |
| 6.6 | 1250 | `jdk.internal.misc.CarrierThreadLocal.get` |
| 6.4 | 1210 | `java.util.regex.Pattern$GroupTail.match` |
| 6.3 | 1191 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 4.8 | 921 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.8 | 910 | `java.lang.StringBuilder.append` |
| 4.8 | 909 | `java.lang.AbstractStringBuilder.append` |
| 4.2 | 805 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.1 | 785 | `java.lang.AbstractStringBuilder.needsNewBuffer` |

