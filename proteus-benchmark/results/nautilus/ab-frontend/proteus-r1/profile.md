# CPU profile

15655 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 86.6 | 13558 | `proteus I/O-*` |
| 6.8 | 1058 | `(unnamed virtual thread)` |
| 5.6 | 883 | `proteus task-*` |
| 0.6 | 100 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 49 | `main` |
| 0.0 | 5 | `proteus Accept` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 2 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 26.8 | 4192 | `java.nio.ByteBuffer.position` |
| 12.1 | 1895 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 6.3 | 992 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 3.6 | 557 | `java.util.TreeMap.put` |
| 3.2 | 505 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.6 | 408 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.0 | 319 | `java.util.TreeMap.successor` |
| 1.7 | 268 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.7 | 264 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.6 | 257 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.6 | 251 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.6 | 245 | `java.util.IdentityHashMap.get` |
| 1.5 | 238 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.5 | 229 | `java.util.TreeMap.deleteEntry` |
| 1.4 | 223 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.4 | 214 | `sun.nio.ch.SocketDispatcher.read` |
| 1.2 | 193 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 1.2 | 185 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 152 | `sun.nio.ch.IOUtil.write` |
| 1.0 | 149 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 0.9 | 148 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.9 | 145 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.8 | 122 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 0.8 | 120 | `io.undertow.util.HeaderValues.isEmpty` |
| 0.7 | 117 | `org.xnio.nio.WorkerThread.setOps` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 29.7 | 4647 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 27.1 | 4243 | `java.nio.ByteBuffer.position` |
| 27.1 | 4240 | `java.nio.Buffer.<init>` |
| 27.1 | 4240 | `java.nio.ByteBuffer.<init>` |
| 26.8 | 4198 | `java.nio.HeapByteBuffer.<init>` |
| 26.7 | 4178 | `java.nio.ByteBuffer.allocate` |
| 20.2 | 3162 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 16.8 | 2631 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 16.1 | 2515 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 13.1 | 2052 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 12.2 | 1903 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 6.7 | 1049 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 6.3 | 992 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 6.1 | 951 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 5.8 | 906 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 5.6 | 878 | `org.xnio.nio.WorkerThread.executeAfter` |
| 5.4 | 843 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.4 | 684 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.9 | 608 | `java.util.TreeSet.add` |
| 3.9 | 608 | `java.util.TreeMap.put` |
| 3.5 | 550 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 3.4 | 528 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 3.0 | 464 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 2.8 | 443 | `sun.nio.ch.IOUtil.write` |
| 2.4 | 379 | `java.util.TreeSet.remove` |

