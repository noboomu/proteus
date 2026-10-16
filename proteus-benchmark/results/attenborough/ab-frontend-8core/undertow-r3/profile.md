# CPU profile

11631 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.9 | 10459 | `proteus I/O-*` |
| 6.2 | 723 | `proteus task-*` |
| 3.2 | 367 | `(unnamed virtual thread)` |
| 0.4 | 45 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 35 | `main` |
| 0.0 | 2 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 22.1 | 2567 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.3 | 733 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.5 | 518 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.3 | 497 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.2 | 486 | `java.util.HashMap.getNode` |
| 3.7 | 431 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 3.3 | 385 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 3.2 | 377 | `java.util.regex.Pattern$GroupHead.match` |
| 2.9 | 333 | `java.util.IdentityHashMap.get` |
| 2.8 | 321 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.6 | 300 | `java.util.regex.Pattern$GroupTail.match` |
| 2.5 | 293 | `java.util.regex.Pattern$Branch.match` |
| 1.9 | 226 | `io.undertow.util.HttpString.higher` |
| 1.7 | 203 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.4 | 158 | `java.util.regex.Matcher.match` |
| 1.3 | 148 | `java.util.TreeMap.put` |
| 1.1 | 127 | `io.undertow.util.HttpString.calcHashCode` |
| 1.1 | 123 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.0 | 122 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.0 | 119 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 119 | `java.util.TreeMap.successor` |
| 1.0 | 115 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 0.9 | 104 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 0.8 | 91 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 0.7 | 78 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 36.8 | 4283 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 27.1 | 3152 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 25.4 | 2955 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 22.1 | 2570 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 18.5 | 2153 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.2 | 1539 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 11.4 | 1325 | `java.util.regex.Pattern$GroupHead.match` |
| 8.1 | 947 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 7.8 | 910 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 7.8 | 907 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 7.4 | 859 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 6.9 | 803 | `java.util.regex.Pattern$Branch.match` |
| 6.0 | 703 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.9 | 687 | `java.util.regex.Pattern$GroupTail.match` |
| 5.2 | 602 | `io.undertow.server.protocol.http.RequestParser.parseFieldName` |
| 4.9 | 574 | `java.lang.StringBuilder.append` |
| 4.9 | 573 | `java.lang.AbstractStringBuilder.append` |
| 4.6 | 534 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 4.5 | 518 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.3 | 504 | `java.util.HashMap.getNode` |
| 4.1 | 480 | `java.util.HashMap.get` |
| 4.1 | 478 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 4.1 | 474 | `io.undertow.server.handlers.HostHeaderHandler.handleRequest` |
| 3.5 | 410 | `io.sinistral.proteus.server.handlers.ServerDefaultHttpHandler.handleRequest` |
| 3.4 | 390 | `java.util.regex.Pattern$BranchConn.match` |

