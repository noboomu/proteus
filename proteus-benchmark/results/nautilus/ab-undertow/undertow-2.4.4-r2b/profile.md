# CPU profile

19487 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 88.0 | 17155 | `proteus I/O-*` |
| 6.6 | 1284 | `proteus task-*` |
| 4.7 | 916 | `(unnamed virtual thread)` |
| 0.4 | 80 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 51 | `main` |
| 0.0 | 4 | `proteus Accept` |
| 0.0 | 1 | `JFR Periodic Tasks` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.5 | 2824 | `java.util.IdentityHashMap.get` |
| 8.5 | 1659 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.8 | 1328 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 4.6 | 889 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 3.7 | 730 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.6 | 705 | `java.util.regex.Pattern$GroupHead.match` |
| 3.3 | 649 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 3.0 | 586 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.7 | 520 | `java.util.regex.Pattern$GroupTail.match` |
| 2.6 | 500 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 2.2 | 434 | `java.util.regex.Pattern$Branch.match` |
| 1.9 | 361 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.7 | 341 | `java.util.regex.Matcher.reset` |
| 1.7 | 329 | `io.undertow.util.HttpString.higher` |
| 1.6 | 313 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.6 | 304 | `io.undertow.server.protocol.http.RequestParser.parseFieldName` |
| 1.4 | 275 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.4 | 271 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 1.3 | 244 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.2 | 238 | `java.util.TreeMap.put` |
| 1.1 | 217 | `java.util.HashMap.getNode` |
| 1.1 | 212 | `java.util.TreeMap.getEntry` |
| 1.1 | 211 | `org.xnio.nio.WorkerThread.setOps` |
| 1.0 | 188 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 0.8 | 162 | `ch.qos.logback.classic.Logger.isTraceEnabled` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 24.7 | 4807 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 19.7 | 3836 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 17.1 | 3336 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 17.1 | 3335 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 17.0 | 3306 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 15.4 | 2994 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 14.5 | 2825 | `java.util.IdentityHashMap.get` |
| 12.7 | 2471 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 12.5 | 2434 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 12.1 | 2366 | `java.util.regex.Pattern$GroupHead.match` |
| 11.3 | 2211 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 8.6 | 1667 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 8.5 | 1648 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 7.6 | 1472 | `java.util.regex.Pattern$Branch.match` |
| 6.7 | 1308 | `java.util.regex.Pattern$GroupTail.match` |
| 4.6 | 905 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.4 | 855 | `java.lang.AbstractStringBuilder.append` |
| 4.4 | 855 | `java.lang.StringBuilder.append` |
| 4.3 | 838 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 4.1 | 797 | `java.util.regex.Pattern$BranchConn.match` |
| 3.9 | 758 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 3.8 | 744 | `io.undertow.server.handlers.HostHeaderHandler.handleRequest` |
| 3.7 | 730 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.4 | 653 | `io.undertow.server.Connectors.executeRootHandler` |
| 3.3 | 643 | `io.undertow.server.protocol.http.RequestParser.parseFieldName` |

