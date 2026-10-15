# CPU profile

11728 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 88.8 | 10419 | `proteus I/O-*` |
| 10.7 | 1257 | `proteus task-*` |
| 0.4 | 47 | `main` |
| 0.0 | 3 | `(unnamed virtual thread)` |
| 0.0 | 2 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 9.5 | 1110 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 8.3 | 969 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 6.2 | 722 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 4.9 | 574 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 4.5 | 528 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 4.3 | 502 | `java.util.regex.Pattern$GroupHead.match` |
| 4.1 | 476 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.6 | 427 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 3.5 | 415 | `java.util.regex.Pattern$GroupTail.match` |
| 2.5 | 294 | `java.util.regex.Pattern$Branch.match` |
| 2.5 | 292 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.5 | 289 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 2.2 | 253 | `java.util.TreeMap.put` |
| 2.1 | 251 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.5 | 180 | `java.util.regex.Matcher.reset` |
| 1.5 | 180 | `io.undertow.server.protocol.http.RequestParser.parseFieldName` |
| 1.4 | 163 | `java.util.TreeMap.getEntry` |
| 1.4 | 162 | `java.util.HashMap.getNode` |
| 1.2 | 136 | `io.undertow.util.HttpString.higher` |
| 1.1 | 133 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 1.1 | 127 | `sun.nio.ch.IOUtil.write` |
| 1.0 | 120 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.0 | 116 | `io.undertow.util.HeaderMap.fiNextNonEmpty` |
| 1.0 | 113 | `io.undertow.util.HttpString.calcHashCode` |
| 0.9 | 100 | `io.sinistral.proteus.server.ServerResponse.send` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 17.3 | 2030 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 17.3 | 2029 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 17.2 | 2012 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 17.0 | 1998 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 15.0 | 1760 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 14.2 | 1665 | `java.util.regex.Pattern$GroupHead.match` |
| 13.1 | 1540 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 12.1 | 1416 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 10.0 | 1174 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 9.9 | 1156 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 8.4 | 990 | `java.lang.ThreadLocal.get` |
| 8.4 | 984 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 8.4 | 981 | `java.util.regex.Pattern$Branch.match` |
| 8.4 | 980 | `java.lang.System$1.getCarrierThreadLocal` |
| 8.3 | 979 | `java.lang.ThreadLocal.getCarrierThreadLocal` |
| 8.3 | 976 | `java.util.regex.Pattern$GroupTail.match` |
| 8.2 | 965 | `jdk.internal.misc.CarrierThreadLocal.get` |
| 6.5 | 766 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 6.2 | 722 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 5.4 | 634 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.3 | 622 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 5.3 | 618 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 5.1 | 602 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.9 | 569 | `java.util.regex.Pattern$BranchConn.match` |
| 4.8 | 558 | `java.lang.StringBuilder.append` |

