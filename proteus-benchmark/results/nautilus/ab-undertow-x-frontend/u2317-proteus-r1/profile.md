# CPU profile

8663 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.9 | 7789 | `proteus I/O-*` |
| 9.5 | 827 | `proteus task-*` |
| 0.4 | 38 | `main` |
| 0.1 | 6 | `proteus Accept` |
| 0.0 | 3 | `(unnamed virtual thread)` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.7 | 1271 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 12.7 | 1101 | `sun.nio.ch.SocketDispatcher.read` |
| 7.9 | 686 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.9 | 341 | `sun.nio.ch.IOUtil.write` |
| 3.9 | 340 | `java.util.TreeMap.put` |
| 3.9 | 335 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 3.6 | 310 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 3.1 | 272 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.4 | 204 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 2.2 | 187 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.0 | 170 | `java.util.IdentityHashMap.get` |
| 1.9 | 163 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.5 | 129 | `java.util.TreeMap.getEntry` |
| 1.5 | 128 | `org.xnio.nio.WorkerThread.run` |
| 1.3 | 109 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 1.2 | 107 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.2 | 105 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 1.2 | 101 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 1.2 | 100 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.2 | 100 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 96 | `java.util.Arrays.copyOfRange` |
| 1.1 | 94 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.0 | 89 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.0 | 85 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.9 | 74 | `java.util.TreeMap.deleteEntry` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 24.7 | 2141 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 23.6 | 2047 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 20.8 | 1803 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 20.2 | 1752 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 19.2 | 1662 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 18.1 | 1570 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 15.3 | 1326 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 14.9 | 1293 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 12.8 | 1108 | `sun.nio.ch.IOUtil.read` |
| 12.7 | 1104 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 12.7 | 1103 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 12.7 | 1101 | `sun.nio.ch.SocketDispatcher.read` |
| 8.6 | 745 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 7.9 | 686 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.9 | 513 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 5.9 | 509 | `sun.nio.ch.IOUtil.write` |
| 5.1 | 438 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 4.9 | 425 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.6 | 401 | `sun.nio.ch.SocketChannelImpl.write` |
| 4.5 | 390 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 4.4 | 385 | `org.xnio.nio.WorkerThread.executeAfter` |
| 4.4 | 382 | `java.util.TreeSet.add` |
| 4.4 | 382 | `java.util.TreeMap.put` |
| 4.0 | 347 | `org.xnio.nio.NioSocketConduit.write` |
| 4.0 | 343 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |

