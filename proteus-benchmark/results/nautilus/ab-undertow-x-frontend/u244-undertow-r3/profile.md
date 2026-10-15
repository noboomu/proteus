# CPU profile

11973 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.0 | 10655 | `proteus I/O-*` |
| 10.5 | 1256 | `proteus task-*` |
| 0.5 | 56 | `main` |
| 0.0 | 4 | `proteus Accept` |
| 0.0 | 1 | `(unnamed virtual thread)` |
| 0.0 | 1 | `proteus-shutdown` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 10.9 | 1311 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 5.0 | 597 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 4.7 | 560 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 4.1 | 489 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.1 | 488 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 4.0 | 474 | `java.util.regex.Pattern$GroupHead.match` |
| 3.8 | 458 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 3.7 | 439 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.6 | 435 | `java.util.IdentityHashMap.get` |
| 3.4 | 413 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 3.2 | 385 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.9 | 345 | `java.util.regex.Pattern$GroupTail.match` |
| 2.6 | 306 | `java.util.regex.Pattern$Branch.match` |
| 2.4 | 283 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 2.1 | 249 | `io.undertow.util.HttpString.higher` |
| 2.0 | 237 | `java.util.regex.Matcher.reset` |
| 1.7 | 198 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.5 | 179 | `java.util.HashMap.getNode` |
| 1.5 | 177 | `io.undertow.util.Headers.fromCache` |
| 1.3 | 160 | `java.util.TreeMap.put` |
| 1.2 | 144 | `io.undertow.util.HttpString.calcHashCode` |
| 1.2 | 143 | `java.lang.StringBuilder.toString` |
| 1.1 | 135 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 1.1 | 135 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 0.9 | 108 | `java.util.TreeMap.successor` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 22.7 | 2720 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 22.3 | 2666 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 16.7 | 1994 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 16.6 | 1993 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 15.1 | 1809 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 15.0 | 1795 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 13.8 | 1651 | `java.util.regex.Pattern$GroupHead.match` |
| 12.6 | 1506 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 11.4 | 1366 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 10.9 | 1311 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 9.5 | 1141 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 8.5 | 1022 | `java.util.regex.Pattern$Branch.match` |
| 7.5 | 895 | `java.util.regex.Pattern$GroupTail.match` |
| 7.3 | 879 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 5.8 | 699 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 4.8 | 569 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.6 | 556 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.6 | 547 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.5 | 544 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.2 | 506 | `io.undertow.server.handlers.HostHeaderHandler.handleRequest` |
| 4.2 | 501 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 4.1 | 496 | `java.util.regex.Pattern$BranchConn.match` |
| 4.1 | 488 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 4.0 | 474 | `java.lang.AbstractStringBuilder.append` |
| 4.0 | 474 | `java.lang.StringBuilder.append` |

