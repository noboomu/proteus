# CPU profile

11721 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.4 | 10477 | `proteus I/O-*` |
| 6.7 | 786 | `proteus task-*` |
| 3.3 | 381 | `(unnamed virtual thread)` |
| 0.4 | 47 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 31 | `main` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 22.7 | 2665 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.1 | 720 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.7 | 551 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.3 | 501 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 4.1 | 483 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.0 | 469 | `java.util.HashMap.getNode` |
| 3.1 | 369 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 3.0 | 349 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 2.8 | 330 | `java.util.regex.Pattern$Branch.match` |
| 2.8 | 329 | `java.util.regex.Pattern$GroupHead.match` |
| 2.5 | 296 | `io.undertow.util.HttpString.higher` |
| 2.5 | 290 | `java.util.regex.Pattern$GroupTail.match` |
| 1.4 | 166 | `java.util.TreeMap.successor` |
| 1.3 | 150 | `java.util.regex.Matcher.match` |
| 1.2 | 136 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.1 | 133 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 132 | `org.xnio.nio.WorkerThread.setOps` |
| 1.1 | 130 | `io.undertow.util.HttpString.calcHashCode` |
| 1.0 | 119 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.0 | 118 | `java.util.concurrent.ConcurrentLinkedQueue.poll` |
| 0.8 | 96 | `java.util.concurrent.locks.LockSupport.unpark` |
| 0.8 | 94 | `io.undertow.server.protocol.http.RequestParser.parsePathSegments` |
| 0.8 | 93 | `java.util.IdentityHashMap.get` |
| 0.7 | 86 | `sun.nio.ch.SocketDispatcher.read` |
| 0.7 | 84 | `io.undertow.server.protocol.http.RequestParser.handle` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 33.8 | 3962 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 25.0 | 2932 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 23.4 | 2743 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 22.7 | 2665 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 15.1 | 1770 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.2 | 1545 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 11.6 | 1359 | `java.util.regex.Pattern$GroupHead.match` |
| 8.0 | 938 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 8.0 | 933 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 7.9 | 926 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 7.7 | 905 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 7.7 | 902 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 7.4 | 873 | `java.util.regex.Pattern$Branch.match` |
| 6.2 | 724 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 6.0 | 699 | `java.util.regex.Pattern$GroupTail.match` |
| 5.3 | 624 | `java.lang.StringBuilder.append` |
| 5.3 | 622 | `java.lang.AbstractStringBuilder.append` |
| 5.2 | 611 | `io.undertow.server.protocol.http.RequestParser.parseFieldName` |
| 4.7 | 551 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.1 | 479 | `java.util.HashMap.getNode` |
| 4.1 | 477 | `io.undertow.server.handlers.HostHeaderHandler.handleRequest` |
| 4.0 | 473 | `io.sinistral.proteus.server.handlers.ServerDefaultHttpHandler.handleRequest` |
| 3.8 | 441 | `java.util.HashMap.get` |
| 3.7 | 431 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 3.5 | 414 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |

