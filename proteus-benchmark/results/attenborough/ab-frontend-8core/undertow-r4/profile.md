# CPU profile

11503 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.3 | 10272 | `proteus I/O-*` |
| 6.5 | 746 | `proteus task-*` |
| 3.4 | 395 | `(unnamed virtual thread)` |
| 0.4 | 49 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 40 | `main` |
| 0.0 | 2 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 21.7 | 2493 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.5 | 751 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 5.0 | 579 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.4 | 502 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.2 | 484 | `java.util.HashMap.getNode` |
| 4.0 | 456 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 3.3 | 379 | `java.util.regex.Pattern$GroupHead.match` |
| 3.0 | 341 | `java.util.IdentityHashMap.get` |
| 2.7 | 315 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.6 | 295 | `java.util.regex.Pattern$GroupTail.match` |
| 2.5 | 293 | `java.util.regex.Pattern$Branch.match` |
| 1.7 | 190 | `io.undertow.util.HttpString.higher` |
| 1.5 | 172 | `java.util.TreeMap.successor` |
| 1.3 | 155 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.2 | 139 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.2 | 138 | `io.undertow.util.HttpString.calcHashCode` |
| 1.2 | 135 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 126 | `java.util.regex.Matcher.match` |
| 1.1 | 122 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.0 | 118 | `io.undertow.server.protocol.http.RequestParser.parsePathSegments` |
| 1.0 | 113 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 1.0 | 113 | `org.xnio.nio.NioHandle.resume` |
| 0.9 | 102 | `sun.nio.ch.IOUtil.write` |
| 0.8 | 89 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 0.8 | 88 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 33.9 | 3904 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 24.3 | 2797 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 22.7 | 2615 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 21.7 | 2493 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 16.7 | 1924 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.5 | 1668 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 11.6 | 1332 | `java.util.regex.Pattern$GroupHead.match` |
| 8.5 | 983 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 8.4 | 970 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 8.1 | 934 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 7.5 | 861 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 7.3 | 840 | `java.util.regex.Pattern$Branch.match` |
| 6.3 | 728 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 6.2 | 710 | `java.util.regex.Pattern$GroupTail.match` |
| 5.6 | 645 | `io.undertow.server.protocol.http.RequestParser.parseFieldName` |
| 5.5 | 635 | `java.lang.AbstractStringBuilder.append` |
| 5.5 | 635 | `java.lang.StringBuilder.append` |
| 5.2 | 594 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.0 | 579 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.4 | 501 | `java.util.HashMap.getNode` |
| 4.3 | 496 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 4.2 | 482 | `java.util.HashMap.get` |
| 3.9 | 447 | `io.undertow.server.protocol.http.RequestParser.parsePath` |
| 3.7 | 429 | `io.undertow.server.handlers.HostHeaderHandler.handleRequest` |
| 3.6 | 416 | `java.util.regex.Pattern$BranchConn.match` |

