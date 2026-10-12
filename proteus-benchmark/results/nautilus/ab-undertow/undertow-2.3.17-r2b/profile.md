# CPU profile

15350 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 87.6 | 13439 | `proteus I/O-*` |
| 6.2 | 959 | `(unnamed virtual thread)` |
| 5.2 | 803 | `proteus task-*` |
| 0.6 | 99 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 53 | `main` |
| 0.0 | 4 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 11.7 | 1794 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 10.3 | 1580 | `sun.nio.ch.SocketDispatcher.read` |
| 7.4 | 1139 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.7 | 723 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.6 | 560 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 3.4 | 524 | `ch.qos.logback.classic.Logger.isInfoEnabled` |
| 3.3 | 509 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.7 | 419 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 2.2 | 345 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 2.1 | 324 | `java.nio.ByteBuffer.allocate` |
| 2.1 | 315 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 1.9 | 286 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.9 | 284 | `java.util.TreeMap.put` |
| 1.8 | 279 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.8 | 278 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.6 | 250 | `io.undertow.util.HttpString.calcHashCode` |
| 1.5 | 234 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.4 | 212 | `org.xnio.nio.WorkerThread.setOps` |
| 1.3 | 196 | `io.undertow.util.HttpString.higher` |
| 1.2 | 184 | `java.lang.AbstractStringBuilder.append` |
| 1.2 | 177 | `java.util.TreeMap.successor` |
| 1.1 | 167 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 1.1 | 166 | `sun.nio.ch.IOUtil.write` |
| 1.0 | 158 | `jdk.internal.misc.Unsafe.checkOffset` |
| 1.0 | 151 | `io.undertow.server.Connectors.verifyToken` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 23.5 | 3613 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 20.3 | 3111 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 20.0 | 3067 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 19.9 | 3054 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 19.8 | 3040 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.7 | 2256 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 14.3 | 2197 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 13.0 | 1993 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 11.7 | 1794 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 11.1 | 1700 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 10.8 | 1659 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 10.4 | 1601 | `sun.nio.ch.IOUtil.read` |
| 10.3 | 1582 | `sun.nio.ch.IOUtil.readIntoNativeBuffer` |
| 10.3 | 1580 | `sun.nio.ch.SocketDispatcher.read` |
| 7.4 | 1141 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 5.7 | 878 | `org.xnio.nio.WorkerThread.run` |
| 4.5 | 694 | `java.lang.StringBuilder.append` |
| 4.5 | 693 | `java.lang.AbstractStringBuilder.append` |
| 4.4 | 672 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 4.0 | 615 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.0 | 615 | `io.undertow.util.HttpString.equals` |
| 3.9 | 600 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 3.5 | 533 | `org.jboss.logging.DelegatingBasicLogger.tracef` |
| 3.4 | 526 | `org.jboss.logging.Logger.logf` |
| 3.4 | 526 | `org.jboss.logging.Slf4jLocationAwareLogger.isEnabled` |

