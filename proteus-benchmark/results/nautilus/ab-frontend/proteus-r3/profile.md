# CPU profile

16597 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 87.8 | 14572 | `proteus I/O-*` |
| 6.3 | 1053 | `(unnamed virtual thread)` |
| 5.0 | 826 | `proteus task-*` |
| 0.6 | 104 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 45 | `main` |
| 0.0 | 5 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 15.2 | 2524 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 11.9 | 1973 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 6.8 | 1131 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 5.3 | 879 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 4.8 | 798 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.5 | 587 | `java.nio.ByteBuffer.allocate` |
| 2.6 | 429 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.5 | 409 | `java.util.TreeMap.put` |
| 2.1 | 346 | `sun.nio.ch.SocketDispatcher.read` |
| 2.0 | 329 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 1.9 | 314 | `java.util.IdentityHashMap.get` |
| 1.8 | 304 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.7 | 276 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.4 | 240 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 1.3 | 209 | `sun.nio.ch.IOUtil.write` |
| 1.2 | 199 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.2 | 193 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.1 | 190 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 1.1 | 189 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 171 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 1.0 | 167 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 164 | `org.xnio.nio.WorkerThread.setOps` |
| 1.0 | 162 | `jdk.internal.misc.Unsafe.checkOffset` |
| 0.9 | 152 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.8 | 140 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 42.0 | 6963 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 33.1 | 5489 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 22.3 | 3709 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 19.6 | 3255 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 19.5 | 3236 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 18.9 | 3131 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 18.7 | 3106 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 14.5 | 2399 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 14.2 | 2358 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 12.1 | 2008 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 11.9 | 1977 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 8.7 | 1437 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 7.4 | 1229 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 7.3 | 1212 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 4.8 | 798 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.2 | 697 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.1 | 686 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 3.6 | 598 | `io.undertow.server.DefaultByteBufferPool.allocate` |
| 3.6 | 597 | `java.nio.ByteBuffer.allocate` |
| 3.4 | 560 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.3 | 554 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.9 | 484 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 2.8 | 473 | `org.xnio.nio.WorkerThread.executeAfter` |
| 2.8 | 463 | `sun.nio.ch.IOUtil.write` |
| 2.7 | 444 | `io.undertow.server.Connectors.executeRootHandler` |

