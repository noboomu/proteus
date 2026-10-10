# CPU profile

6889 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 93.8 | 6463 | `proteus I/O-*` |
| 5.1 | 348 | `(unnamed virtual thread)` |
| 0.5 | 33 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 25 | `main` |
| 0.3 | 24 | `proteus task-*` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 64.2 | 4421 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.1 | 418 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.8 | 127 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.6 | 108 | `sun.nio.ch.SocketDispatcher.read` |
| 1.6 | 107 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 1.4 | 99 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.2 | 80 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.0 | 68 | `org.xnio.Buffers.copy` |
| 0.9 | 59 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.8 | 58 | `io.undertow.util.HttpString.calcHashCode` |
| 0.8 | 55 | `java.nio.ByteBuffer.putBuffer` |
| 0.8 | 54 | `java.util.TreeMap.successor` |
| 0.8 | 53 | `java.util.TreeMap.put` |
| 0.8 | 52 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.7 | 51 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 0.7 | 50 | `java.util.TreeMap.fixAfterDeletion` |
| 0.7 | 47 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 0.6 | 42 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 0.5 | 33 | `java.nio.ByteBuffer.allocate` |
| 0.4 | 29 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 0.4 | 29 | `java.util.HashMap.getNode` |
| 0.4 | 29 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 0.4 | 27 | `org.xnio.nio.WorkerThread.setOps` |
| 0.4 | 27 | `io.undertow.util.HttpString.higher` |
| 0.4 | 26 | `io.sinistral.proteus.server.ServerResponse.send` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 69.5 | 4788 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 67.1 | 4621 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 64.9 | 4471 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 64.2 | 4424 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 8.5 | 586 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 8.4 | 578 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 8.4 | 576 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.6 | 458 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 6.5 | 451 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.1 | 418 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.7 | 324 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 2.3 | 158 | `io.undertow.util.HttpString.equals` |
| 2.3 | 158 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.0 | 136 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 1.8 | 124 | `java.util.HashMap.getNode` |
| 1.8 | 122 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 1.7 | 119 | `java.util.TreeSet.remove` |
| 1.7 | 119 | `java.util.TreeMap.remove` |
| 1.7 | 117 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 1.6 | 109 | `sun.nio.ch.IOUtil.read` |
| 1.6 | 109 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 1.6 | 108 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 1.6 | 108 | `sun.nio.ch.SocketDispatcher.read` |
| 1.5 | 105 | `java.util.TreeMap.deleteEntry` |
| 1.5 | 105 | `java.util.LinkedHashMap.get` |

