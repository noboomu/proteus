# CPU profile

15242 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 86.4 | 13173 | `proteus I/O-*` |
| 7.4 | 1121 | `(unnamed virtual thread)` |
| 5.2 | 788 | `proteus task-*` |
| 0.8 | 115 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 44 | `main` |
| 0.0 | 3 | `proteus Accept` |
| 0.0 | 3 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 15.9 | 2416 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 13.0 | 1974 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 9.7 | 1485 | `sun.nio.ch.SocketDispatcher.read` |
| 6.2 | 940 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.2 | 483 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.7 | 418 | `java.util.TreeMap.put` |
| 2.7 | 406 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.6 | 396 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.0 | 300 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.9 | 292 | `org.xnio.nio.WorkerThread.setOps` |
| 1.8 | 268 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.6 | 250 | `java.util.IdentityHashMap.get` |
| 1.6 | 249 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.5 | 228 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.4 | 219 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.3 | 194 | `jdk.internal.misc.Unsafe.checkOffset` |
| 1.2 | 179 | `java.nio.ByteBuffer.putBuffer` |
| 1.2 | 177 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 1.1 | 172 | `sun.nio.ch.IOUtil.write` |
| 1.1 | 169 | `org.xnio.nio.NioSocketConduit.read` |
| 1.0 | 159 | `java.util.TreeMap.deleteEntry` |
| 1.0 | 158 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.0 | 149 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.9 | 136 | `java.util.Arrays.copyOfRange` |
| 0.9 | 131 | `io.undertow.util.HeaderValues.isEmpty` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 29.9 | 4550 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 22.3 | 3404 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 21.0 | 3203 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 20.6 | 3137 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 19.8 | 3013 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 17.1 | 2612 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 15.3 | 2331 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 14.9 | 2269 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 14.7 | 2242 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 13.7 | 2086 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 13.0 | 1977 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 11.4 | 1732 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 9.8 | 1491 | `sun.nio.ch.IOUtil.read` |
| 9.7 | 1486 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 9.7 | 1485 | `sun.nio.ch.SocketDispatcher.read` |
| 6.2 | 940 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.5 | 682 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.5 | 537 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 3.5 | 528 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.5 | 526 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 3.3 | 509 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.3 | 508 | `org.xnio.nio.WorkerThread.executeAfter` |
| 3.2 | 489 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 3.2 | 488 | `sun.nio.ch.IOUtil.write` |
| 3.2 | 483 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |

