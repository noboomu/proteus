# CPU profile

8751 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.5 | 7829 | `proteus I/O-*` |
| 10.0 | 875 | `proteus task-*` |
| 0.5 | 41 | `main` |
| 0.0 | 3 | `(unnamed virtual thread)` |
| 0.0 | 3 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 17.4 | 1520 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 12.5 | 1097 | `sun.nio.ch.SocketDispatcher.read` |
| 6.5 | 567 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.4 | 300 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 3.4 | 296 | `java.util.TreeMap.put` |
| 2.9 | 258 | `java.util.IdentityHashMap.get` |
| 2.9 | 258 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.3 | 198 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 2.2 | 195 | `java.util.TreeMap.successor` |
| 2.1 | 187 | `java.util.TreeMap.deleteEntry` |
| 2.1 | 185 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 1.9 | 168 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.8 | 157 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 1.7 | 150 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.6 | 139 | `java.nio.ByteBuffer.putBuffer` |
| 1.6 | 137 | `sun.nio.ch.IOUtil.write` |
| 1.5 | 132 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.3 | 115 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.2 | 107 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.2 | 103 | `ch.qos.logback.classic.Logger.isInfoEnabled` |
| 1.1 | 97 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.0 | 88 | `io.undertow.util.HeaderValues.isEmpty` |
| 0.9 | 82 | `java.util.Arrays.copyOfRange` |
| 0.9 | 75 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.9 | 75 | `sun.nio.ch.SocketDispatcher.writev` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 23.8 | 2080 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 22.7 | 1986 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 22.6 | 1979 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 22.6 | 1976 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 21.5 | 1884 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 17.7 | 1549 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 17.4 | 1524 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 15.8 | 1384 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 12.6 | 1104 | `sun.nio.ch.IOUtil.read` |
| 12.5 | 1098 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 12.5 | 1097 | `sun.nio.ch.SocketDispatcher.read` |
| 12.5 | 1090 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 9.1 | 799 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 6.5 | 567 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.8 | 511 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 5.7 | 496 | `org.xnio.nio.WorkerThread.executeAfter` |
| 5.2 | 452 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.0 | 434 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 4.1 | 363 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 4.1 | 361 | `sun.nio.ch.IOUtil.write` |
| 4.0 | 346 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 3.6 | 315 | `java.util.TreeMap.put` |
| 3.6 | 315 | `java.util.TreeSet.add` |
| 3.5 | 302 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 3.4 | 296 | `org.xnio.nio.WorkerThread.run` |

