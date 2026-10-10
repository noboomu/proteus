# CPU profile

3481 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 83.3 | 2899 | `proteus I/O-*` |
| 11.1 | 386 | `(unnamed virtual thread)` |
| 3.0 | 106 | `proteus task-*` |
| 1.9 | 67 | ` ForkJoinPool-1-worker-*` |
| 0.7 | 24 | `main` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 20.0 | 695 | `org.xnio.nio.NioSocketConduit.read` |
| 11.5 | 399 | `java.util.IdentityHashMap.get` |
| 3.6 | 125 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.5 | 123 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.8 | 99 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 2.4 | 82 | `io.undertow.util.HttpString.calcHashCode` |
| 2.3 | 81 | `java.util.TreeMap.put` |
| 2.2 | 78 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.2 | 76 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.2 | 76 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 2.2 | 76 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.suspendReads` |
| 2.1 | 74 | `org.xnio.Buffers.copy` |
| 1.9 | 67 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.9 | 65 | `java.util.TreeMap.getEntry` |
| 1.6 | 57 | `java.util.TreeMap.fixAfterDeletion` |
| 1.5 | 51 | `java.lang.AbstractStringBuilder.append` |
| 1.4 | 47 | `java.util.HashMap.getNode` |
| 1.3 | 45 | `io.undertow.server.Connectors.verifyToken` |
| 1.3 | 45 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 1.3 | 45 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.2 | 42 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.1 | 39 | `java.util.TreeMap.deleteEntry` |
| 1.1 | 37 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 31 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 0.9 | 31 | `java.util.concurrent.ConcurrentHashMap.addCount` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 36.4 | 1268 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 20.8 | 724 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 20.8 | 724 | `org.xnio.nio.NioSocketConduit.read` |
| 20.7 | 719 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 20.5 | 712 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 15.5 | 541 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 15.1 | 525 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 14.7 | 512 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.7 | 441 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 11.5 | 399 | `java.util.IdentityHashMap.get` |
| 11.3 | 392 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 11.2 | 389 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 6.9 | 239 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 5.5 | 190 | `io.undertow.util.HttpString.bytesAreEqual` |
| 5.5 | 190 | `io.undertow.util.HttpString.equals` |
| 5.2 | 181 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 5.1 | 178 | `java.lang.StringBuilder.append` |
| 5.1 | 177 | `java.lang.AbstractStringBuilder.append` |
| 5.0 | 175 | `java.util.HashMap.getNode` |
| 4.9 | 171 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 4.8 | 168 | `java.util.LinkedHashMap.get` |
| 4.8 | 166 | `java.util.TreeSet.remove` |
| 4.8 | 166 | `java.util.TreeMap.remove` |
| 4.5 | 156 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 3.9 | 135 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |

