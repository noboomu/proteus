# CPU profile

8925 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.9 | 8022 | `proteus I/O-*` |
| 9.3 | 830 | `proteus task-*` |
| 0.7 | 61 | `main` |
| 0.1 | 7 | `proteus Accept` |
| 0.0 | 4 | `(unnamed virtual thread)` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 28.3 | 2524 | `java.nio.ByteBuffer.position` |
| 6.6 | 586 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.4 | 391 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 4.0 | 354 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 3.1 | 274 | `java.util.TreeMap.put` |
| 3.1 | 274 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.2 | 197 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 2.0 | 181 | `java.util.TreeMap.successor` |
| 2.0 | 178 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.9 | 169 | `java.util.TreeMap.deleteEntry` |
| 1.8 | 162 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.7 | 154 | `sun.nio.ch.SocketDispatcher.read` |
| 1.7 | 152 | `java.util.IdentityHashMap.get` |
| 1.7 | 150 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.6 | 146 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.5 | 136 | `java.nio.ByteBuffer.putBuffer` |
| 1.4 | 127 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.2 | 107 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 1.1 | 99 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 1.1 | 98 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.0 | 91 | `java.util.Arrays.copyOfRange` |
| 1.0 | 87 | `sun.nio.ch.IOUtil.write` |
| 1.0 | 86 | `io.undertow.util.HeaderValues.isEmpty` |
| 0.8 | 72 | `org.xnio.nio.WorkerThread.setOps` |
| 0.8 | 67 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 28.6 | 2555 | `java.nio.ByteBuffer.position` |
| 28.6 | 2552 | `java.nio.ByteBuffer.<init>` |
| 28.6 | 2552 | `java.nio.Buffer.<init>` |
| 28.4 | 2533 | `java.nio.HeapByteBuffer.<init>` |
| 28.3 | 2523 | `java.nio.ByteBuffer.allocate` |
| 22.9 | 2043 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 22.4 | 2001 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 17.4 | 1549 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 10.1 | 903 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 7.4 | 662 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 6.9 | 612 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 6.6 | 586 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.0 | 532 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.5 | 489 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 5.3 | 475 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 5.2 | 467 | `org.xnio.nio.WorkerThread.executeAfter` |
| 5.0 | 447 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 4.1 | 362 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 4.0 | 360 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 3.5 | 309 | `java.util.TreeMap.put` |
| 3.4 | 307 | `java.util.TreeSet.add` |
| 3.2 | 290 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.2 | 290 | `sun.nio.ch.IOUtil.write` |
| 2.9 | 256 | `java.util.TreeMap.remove` |
| 2.9 | 256 | `java.util.TreeSet.remove` |

