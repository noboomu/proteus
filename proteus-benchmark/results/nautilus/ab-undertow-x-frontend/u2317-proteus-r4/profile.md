# CPU profile

8805 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 90.0 | 7928 | `proteus I/O-*` |
| 9.4 | 826 | `proteus task-*` |
| 0.5 | 43 | `main` |
| 0.0 | 4 | `proteus Accept` |
| 0.0 | 2 | `(unnamed virtual thread)` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 28.1 | 2475 | `java.nio.ByteBuffer.position` |
| 7.0 | 613 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.4 | 302 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 3.4 | 297 | `java.util.TreeMap.put` |
| 3.2 | 285 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.5 | 219 | `java.util.IdentityHashMap.get` |
| 2.2 | 190 | `java.util.TreeMap.getEntry` |
| 2.0 | 172 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.9 | 166 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 1.8 | 161 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.8 | 158 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.7 | 152 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.7 | 148 | `sun.nio.ch.SocketDispatcher.read` |
| 1.5 | 135 | `sun.nio.ch.IOUtil.write` |
| 1.5 | 133 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.5 | 130 | `java.nio.ByteBuffer.putBuffer` |
| 1.5 | 130 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 1.5 | 128 | `java.util.TreeMap.deleteEntry` |
| 1.3 | 117 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 1.3 | 115 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.2 | 104 | `java.util.Arrays.copyOfRange` |
| 1.0 | 91 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.9 | 81 | `io.undertow.util.HeaderValues.isEmpty` |
| 0.9 | 81 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.9 | 80 | `ch.qos.logback.classic.Logger.isTraceEnabled` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 28.5 | 2507 | `java.nio.ByteBuffer.position` |
| 28.5 | 2506 | `java.nio.ByteBuffer.<init>` |
| 28.4 | 2505 | `java.nio.Buffer.<init>` |
| 28.2 | 2484 | `java.nio.HeapByteBuffer.<init>` |
| 28.1 | 2477 | `java.nio.ByteBuffer.allocate` |
| 21.1 | 1861 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 21.0 | 1848 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 16.6 | 1466 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 7.6 | 669 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 7.3 | 645 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 7.0 | 613 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.5 | 572 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 5.7 | 500 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.4 | 474 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 5.0 | 438 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 4.4 | 385 | `java.util.TreeSet.remove` |
| 4.4 | 384 | `java.util.TreeMap.remove` |
| 4.3 | 375 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 4.2 | 366 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 3.9 | 343 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 3.9 | 341 | `sun.nio.ch.IOUtil.write` |
| 3.9 | 339 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 3.8 | 331 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 3.7 | 323 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.cleanup` |
| 3.7 | 322 | `org.xnio.nio.WorkerThread.executeAfter` |

