# CPU profile

15130 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 86.5 | 13081 | `proteus I/O-*` |
| 7.0 | 1055 | `(unnamed virtual thread)` |
| 5.5 | 833 | `proteus task-*` |
| 0.7 | 101 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 54 | `main` |
| 0.0 | 7 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.1 | 2140 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 10.4 | 1569 | `sun.nio.ch.SocketDispatcher.read` |
| 9.7 | 1472 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.3 | 651 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.2 | 484 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 3.2 | 478 | `java.lang.Thread.interrupted` |
| 3.1 | 473 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.8 | 429 | `java.util.TreeMap.put` |
| 2.2 | 329 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 2.0 | 298 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.0 | 298 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.8 | 271 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.7 | 260 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.5 | 233 | `io.undertow.util.HttpString.calcHashCode` |
| 1.3 | 196 | `java.util.TreeMap.getEntry` |
| 1.3 | 190 | `java.lang.AbstractStringBuilder.append` |
| 1.2 | 180 | `java.util.IdentityHashMap.get` |
| 1.2 | 174 | `io.undertow.util.HttpString.higher` |
| 1.1 | 171 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.1 | 162 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 161 | `sun.nio.ch.IOUtil.write` |
| 1.0 | 157 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.0 | 144 | `org.xnio.Buffers.copy` |
| 0.9 | 138 | `io.undertow.server.Connectors.verifyToken` |
| 0.9 | 134 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 20.2 | 3060 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 20.2 | 3060 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 19.9 | 3007 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 19.7 | 2976 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 17.3 | 2617 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 14.5 | 2198 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.1 | 2140 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 12.9 | 1946 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 12.3 | 1868 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 11.8 | 1787 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 10.6 | 1603 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 10.5 | 1590 | `sun.nio.ch.IOUtil.read` |
| 10.4 | 1570 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 10.4 | 1569 | `sun.nio.ch.SocketDispatcher.read` |
| 9.8 | 1477 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 5.2 | 789 | `org.xnio.nio.WorkerThread.run` |
| 5.0 | 754 | `io.undertow.util.HttpString.bytesAreEqual` |
| 5.0 | 754 | `io.undertow.util.HttpString.equals` |
| 4.4 | 666 | `java.lang.StringBuilder.append` |
| 4.4 | 664 | `java.lang.AbstractStringBuilder.append` |
| 4.1 | 623 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 3.9 | 583 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 3.7 | 555 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 3.6 | 543 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.2 | 481 | `java.lang.Thread.interrupted` |

