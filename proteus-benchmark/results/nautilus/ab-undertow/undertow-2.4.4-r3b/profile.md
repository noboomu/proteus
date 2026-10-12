# CPU profile

19268 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 88.5 | 17049 | `proteus I/O-*` |
| 6.4 | 1230 | `proteus task-*` |
| 4.5 | 860 | `(unnamed virtual thread)` |
| 0.4 | 81 | ` ForkJoinPool-1-worker-*` |
| 0.3 | 51 | `main` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 9.0 | 1731 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 8.7 | 1670 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 6.1 | 1182 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 4.7 | 907 | `java.util.IdentityHashMap.get` |
| 4.1 | 783 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 3.6 | 698 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 3.5 | 673 | `java.util.regex.Pattern$GroupHead.match` |
| 3.3 | 635 | `java.util.HashMap.getNode` |
| 3.1 | 595 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.7 | 527 | `java.util.regex.Pattern$GroupTail.match` |
| 2.5 | 490 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 2.2 | 431 | `java.util.regex.Pattern$Branch.match` |
| 2.1 | 409 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 2.0 | 387 | `java.util.TreeMap.put` |
| 1.8 | 340 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.7 | 334 | `io.undertow.util.HttpString.higher` |
| 1.7 | 327 | `java.lang.AbstractStringBuilder.append` |
| 1.6 | 308 | `java.util.regex.Matcher.reset` |
| 1.4 | 270 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.3 | 253 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.3 | 251 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.1 | 220 | `io.undertow.server.protocol.http.RequestParser.parsePathSegments` |
| 1.1 | 203 | `org.xnio.nio.WorkerThread.setOps` |
| 1.0 | 185 | `java.util.TreeMap.successor` |
| 0.9 | 164 | `java.lang.StringBuilder.toString` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 17.1 | 3294 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 16.5 | 3178 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 16.2 | 3125 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 13.0 | 2497 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.9 | 2491 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 11.8 | 2269 | `java.util.regex.Pattern$GroupHead.match` |
| 11.7 | 2248 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 10.3 | 1993 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 10.0 | 1925 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 9.5 | 1829 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 9.0 | 1731 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 8.7 | 1681 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 8.2 | 1571 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 7.2 | 1387 | `java.util.regex.Pattern$Branch.match` |
| 6.6 | 1265 | `java.util.regex.Pattern$GroupTail.match` |
| 6.3 | 1217 | `java.lang.ThreadLocal.get` |
| 6.3 | 1208 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 6.2 | 1197 | `java.lang.System$1.getCarrierThreadLocal` |
| 6.2 | 1196 | `java.lang.ThreadLocal.getCarrierThreadLocal` |
| 6.1 | 1171 | `jdk.internal.misc.CarrierThreadLocal.get` |
| 5.0 | 956 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.7 | 907 | `java.util.IdentityHashMap.get` |
| 3.9 | 745 | `java.util.regex.Pattern$BranchConn.match` |
| 3.7 | 721 | `io.undertow.server.protocol.http.RequestParser.parseFieldName` |
| 3.7 | 712 | `io.undertow.server.handlers.HostHeaderHandler.handleRequest` |

