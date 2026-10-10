# CPU profile

11642 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 97.1 | 11300 | `proteus I/O-*` |
| 2.1 | 245 | `(unnamed virtual thread)` |
| 0.4 | 42 | `proteus task-*` |
| 0.2 | 29 | ` ForkJoinPool-1-worker-*` |
| 0.2 | 27 | `main` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 72.4 | 8425 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.1 | 707 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.1 | 248 | `sun.nio.ch.SocketDispatcher.read` |
| 1.6 | 187 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.2 | 134 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 0.9 | 110 | `java.util.TreeMap.getEntry` |
| 0.9 | 102 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.8 | 88 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.7 | 79 | `io.undertow.util.HttpString.calcHashCode` |
| 0.6 | 75 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.6 | 74 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 0.5 | 63 | `java.nio.ByteBuffer.allocate` |
| 0.5 | 57 | `java.util.HashMap.getNode` |
| 0.5 | 54 | `java.util.TreeMap.put` |
| 0.4 | 51 | `io.undertow.util.HttpString.higher` |
| 0.4 | 47 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 0.4 | 44 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 0.4 | 42 | `org.xnio.Buffers.copy` |
| 0.3 | 40 | `io.undertow.server.Connectors.verifyToken` |
| 0.3 | 37 | `java.util.TreeMap.fixAfterDeletion` |
| 0.3 | 32 | `io.sinistral.proteus.server.ServerResponse.send` |
| 0.3 | 32 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.2 | 29 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 0.2 | 28 | `java.lang.AbstractStringBuilder.append` |
| 0.2 | 27 | `java.util.TreeMap.successor` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 76.1 | 8865 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 73.6 | 8573 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 73.3 | 8528 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 72.4 | 8426 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 6.5 | 760 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 6.5 | 757 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.4 | 748 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.5 | 644 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.4 | 632 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 5.0 | 581 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 4.3 | 500 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 2.3 | 268 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 2.1 | 249 | `sun.nio.ch.IOUtil.read` |
| 2.1 | 248 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 2.1 | 248 | `sun.nio.ch.SocketDispatcher.read` |
| 1.7 | 202 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 1.6 | 190 | `java.util.HashMap.getNode` |
| 1.6 | 190 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.6 | 190 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.6 | 190 | `io.undertow.util.HttpString.equals` |
| 1.6 | 183 | `java.util.TreeSet.remove` |
| 1.6 | 183 | `java.util.TreeMap.remove` |
| 1.6 | 182 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 1.4 | 163 | `java.lang.StringBuilder.append` |
| 1.4 | 162 | `java.lang.AbstractStringBuilder.append` |

