# CPU profile

15240 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 86.8 | 13231 | `proteus I/O-*` |
| 7.0 | 1066 | `(unnamed virtual thread)` |
| 5.2 | 785 | `proteus task-*` |
| 0.6 | 99 | ` ForkJoinPool-1-worker-*` |
| 0.4 | 58 | `main` |
| 0.0 | 6 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 26.6 | 4061 | `java.nio.ByteBuffer.position` |
| 12.4 | 1886 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 6.2 | 941 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 4.6 | 706 | `java.util.IdentityHashMap.get` |
| 3.1 | 473 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 2.9 | 443 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.0 | 310 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.8 | 276 | `io.sinistral.proteus.server.http1.Http1Parser.valueString` |
| 1.8 | 273 | `java.util.TreeMap.put` |
| 1.7 | 260 | `io.sinistral.proteus.server.http1.Http1Parser.internHeader` |
| 1.5 | 225 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 1.3 | 204 | `java.util.TreeMap.getEntry` |
| 1.2 | 183 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 1.1 | 169 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 152 | `sun.nio.ch.IOUtil.write` |
| 1.0 | 147 | `io.undertow.server.DefaultByteBufferPool$ThreadLocalCache.get` |
| 0.9 | 142 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.append` |
| 0.9 | 138 | `io.undertow.server.Connectors.setExchangeRequestPath` |
| 0.9 | 138 | `org.xnio.SingleOption.cast` |
| 0.9 | 137 | `sun.nio.ch.IOUtil.read` |
| 0.9 | 136 | `java.util.Arrays.copyOfRange` |
| 0.9 | 134 | `sun.nio.ch.SocketDispatcher.read` |
| 0.9 | 133 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.8 | 118 | `io.undertow.util.HttpString.bytesAreEqual` |
| 0.7 | 113 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 28.7 | 4367 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEvent` |
| 26.9 | 4098 | `java.nio.ByteBuffer.position` |
| 26.9 | 4097 | `java.nio.Buffer.<init>` |
| 26.9 | 4097 | `java.nio.ByteBuffer.<init>` |
| 26.7 | 4066 | `java.nio.HeapByteBuffer.<init>` |
| 26.6 | 4048 | `java.nio.ByteBuffer.allocate` |
| 19.6 | 2991 | `io.sinistral.proteus.server.http1.Http1ReadListener.handleEventWithNoRunningRequest` |
| 16.2 | 2473 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 15.5 | 2361 | `io.sinistral.proteus.server.http1.Http1Parser.parse` |
| 13.0 | 1977 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 12.4 | 1894 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 6.6 | 1011 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.processWrite` |
| 6.2 | 941 | `io.sinistral.proteus.server.http1.Http1Parser.internMethod` |
| 5.7 | 870 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.5 | 843 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.write` |
| 4.9 | 742 | `org.xnio.OptionMap.get` |
| 4.9 | 742 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 4.6 | 706 | `java.util.IdentityHashMap.get` |
| 4.6 | 699 | `io.sinistral.proteus.server.http1.Http1ResponseConduit.buildHead` |
| 3.0 | 460 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.resumeReads` |
| 2.9 | 447 | `io.sinistral.proteus.server.http1.Http1Parser.applyTarget` |
| 2.9 | 443 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 2.8 | 422 | `sun.nio.ch.SocketChannelImpl.implWrite` |
| 2.7 | 408 | `java.util.TreeSet.remove` |
| 2.7 | 405 | `java.util.TreeMap.remove` |

