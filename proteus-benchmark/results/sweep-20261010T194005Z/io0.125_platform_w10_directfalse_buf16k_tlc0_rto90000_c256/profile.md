# CPU profile

1788 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 82.6 | 1477 | `proteus I/O-*` |
| 9.8 | 176 | `(unnamed virtual thread)` |
| 3.4 | 61 | `proteus task-*` |
| 2.6 | 46 | ` ForkJoinPool-1-worker-*` |
| 1.6 | 28 | `main` |
| 0.1 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 16.7 | 299 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 11.4 | 204 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.3 | 94 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.7 | 67 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.9 | 51 | `io.undertow.util.HttpString.calcHashCode` |
| 2.8 | 50 | `java.util.TreeMap.remove` |
| 2.7 | 48 | `org.xnio.Buffers.copy` |
| 2.3 | 42 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 2.1 | 37 | `io.undertow.server.Connectors.verifyToken` |
| 2.0 | 36 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.8 | 32 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.8 | 32 | `java.lang.AbstractStringBuilder.append` |
| 1.7 | 31 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.6 | 29 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.6 | 28 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 1.4 | 25 | `java.util.TreeMap.put` |
| 1.3 | 24 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.3 | 24 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.2 | 22 | `java.nio.ByteBuffer.putBuffer` |
| 1.2 | 21 | `java.util.ArrayDeque.addLast` |
| 1.1 | 20 | `io.undertow.util.HttpString.higher` |
| 1.1 | 19 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.1 | 19 | `sun.nio.ch.SocketDispatcher.read` |
| 1.0 | 18 | `java.util.HashMap.getNode` |
| 1.0 | 17 | `io.undertow.server.protocol.http.HttpRequestParser.parsePathComplete` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 31.8 | 568 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 26.7 | 477 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 19.2 | 344 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 18.6 | 332 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 17.6 | 314 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 12.2 | 218 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 12.0 | 214 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 11.9 | 213 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 8.6 | 154 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 8.4 | 151 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 7.0 | 126 | `java.lang.AbstractStringBuilder.append` |
| 7.0 | 126 | `java.lang.StringBuilder.append` |
| 6.0 | 108 | `java.util.TreeMap.remove` |
| 6.0 | 108 | `java.util.TreeSet.remove` |
| 5.6 | 101 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 5.5 | 99 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 5.3 | 94 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 5.1 | 91 | `io.undertow.util.HttpString.bytesAreEqual` |
| 5.1 | 91 | `io.undertow.util.HttpString.equals` |
| 4.4 | 79 | `java.util.HashMap.getNode` |
| 4.4 | 79 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 4.4 | 78 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.cleanup` |
| 4.3 | 77 | `java.util.LinkedHashMap.get` |
| 4.3 | 77 | `io.undertow.util.HttpString.<init>` |
| 4.1 | 73 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |

