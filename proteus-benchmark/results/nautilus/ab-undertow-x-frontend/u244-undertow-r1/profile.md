# CPU profile

11654 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 88.7 | 10334 | `proteus I/O-*` |
| 11.0 | 1277 | `proteus task-*` |
| 0.3 | 38 | `main` |
| 0.0 | 5 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 20.1 | 2344 | `java.nio.ByteBuffer.position` |
| 4.5 | 519 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 4.4 | 509 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 4.0 | 467 | `java.util.regex.Pattern$GroupHead.match` |
| 3.9 | 456 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.4 | 402 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 3.4 | 401 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 3.1 | 366 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 3.0 | 350 | `java.util.regex.Pattern$GroupTail.match` |
| 2.8 | 325 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 2.4 | 280 | `java.util.regex.Pattern$Branch.match` |
| 2.3 | 272 | `java.util.TreeMap.put` |
| 1.9 | 217 | `java.util.regex.Matcher.reset` |
| 1.8 | 209 | `io.undertow.server.protocol.http.RequestParser.parseFieldName` |
| 1.6 | 190 | `io.undertow.util.HttpString.higher` |
| 1.5 | 180 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 1.5 | 177 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.3 | 147 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 1.3 | 146 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 1.2 | 142 | `java.lang.AbstractStringBuilder.append` |
| 1.1 | 127 | `sun.nio.ch.IOUtil.write` |
| 1.1 | 124 | `java.util.TreeMap.deleteEntry` |
| 1.0 | 121 | `org.xnio.nio.WorkerThread.run` |
| 1.0 | 117 | `java.util.HashMap.getNode` |
| 1.0 | 114 | `java.util.IdentityHashMap.get` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 20.3 | 2369 | `java.nio.Buffer.<init>` |
| 20.3 | 2369 | `java.nio.ByteBuffer.<init>` |
| 20.3 | 2367 | `java.nio.ByteBuffer.position` |
| 20.2 | 2357 | `java.nio.HeapByteBuffer.<init>` |
| 20.1 | 2343 | `java.nio.ByteBuffer.allocate` |
| 14.9 | 1735 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.0 | 1518 | `java.util.regex.Pattern$GroupHead.match` |
| 12.8 | 1486 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 12.1 | 1405 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 8.8 | 1028 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 7.9 | 924 | `java.util.regex.Pattern$Branch.match` |
| 7.1 | 824 | `java.util.regex.Pattern$GroupTail.match` |
| 6.4 | 744 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.1 | 713 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 6.0 | 699 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.7 | 669 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.3 | 621 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 5.2 | 603 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 5.0 | 584 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.9 | 575 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.7 | 547 | `java.lang.StringBuilder.append` |
| 4.7 | 545 | `java.lang.AbstractStringBuilder.append` |
| 4.6 | 541 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.5 | 529 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 4.5 | 522 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |

