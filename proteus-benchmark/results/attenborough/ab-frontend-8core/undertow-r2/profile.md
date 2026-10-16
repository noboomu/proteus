# CPU profile

11778 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.6 | 10553 | `proteus I/O-*` |
| 6.0 | 712 | `proteus task-*` |
| 3.6 | 423 | `(unnamed virtual thread)` |
| 0.5 | 57 | ` ForkJoinPool-1-worker-*` |
| 0.2 | 29 | `main` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 2 | `proteus Accept` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 22.0 | 2593 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.8 | 800 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 5.2 | 610 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.2 | 499 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 3.9 | 459 | `java.util.HashMap.getNode` |
| 3.6 | 420 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.9 | 345 | `java.util.regex.Pattern$GroupHead.match` |
| 2.9 | 340 | `java.util.regex.Pattern$GroupTail.match` |
| 2.6 | 305 | `java.util.regex.Pattern$Branch.match` |
| 2.6 | 302 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.2 | 260 | `java.util.IdentityHashMap.get` |
| 2.0 | 241 | `io.undertow.util.HttpString.higher` |
| 1.4 | 170 | `java.util.TreeMap.successor` |
| 1.4 | 169 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 1.4 | 163 | `java.util.regex.Matcher.match` |
| 1.2 | 139 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.2 | 139 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.1 | 129 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.1 | 129 | `io.undertow.util.HttpString.calcHashCode` |
| 1.0 | 122 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 115 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.9 | 106 | `io.undertow.server.protocol.http.RequestParser.parsePathSegments` |
| 0.9 | 106 | `org.xnio.nio.NioHandle.resume` |
| 0.9 | 101 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 0.9 | 101 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 34.9 | 4106 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 23.8 | 2803 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 23.2 | 2737 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 22.1 | 2598 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 16.8 | 1973 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.1 | 1540 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 11.9 | 1400 | `java.util.regex.Pattern$GroupHead.match` |
| 9.4 | 1111 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 9.0 | 1062 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 8.1 | 951 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 7.8 | 923 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.6 | 897 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 7.5 | 879 | `java.util.regex.Pattern$Branch.match` |
| 6.2 | 735 | `java.util.regex.Pattern$GroupTail.match` |
| 6.1 | 724 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.1 | 603 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 5.0 | 592 | `io.undertow.server.protocol.http.RequestParser.parseFieldName` |
| 4.1 | 484 | `io.undertow.server.handlers.HostHeaderHandler.handleRequest` |
| 4.1 | 481 | `java.lang.StringBuilder.append` |
| 4.1 | 481 | `java.lang.AbstractStringBuilder.append` |
| 4.0 | 473 | `java.util.HashMap.getNode` |
| 3.9 | 455 | `java.util.HashMap.get` |
| 3.8 | 448 | `io.undertow.server.protocol.http.RequestParser.parsePath` |
| 3.6 | 420 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.4 | 395 | `java.util.regex.Pattern$BranchConn.match` |

