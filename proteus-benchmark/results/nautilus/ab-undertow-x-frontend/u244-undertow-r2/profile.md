# CPU profile

11771 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 88.9 | 10459 | `proteus I/O-*` |
| 10.7 | 1255 | `proteus task-*` |
| 0.4 | 48 | `main` |
| 0.0 | 5 | `proteus Accept` |
| 0.0 | 4 | `(unnamed virtual thread)` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 20.2 | 2375 | `java.nio.ByteBuffer.position` |
| 4.5 | 530 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 4.1 | 485 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 4.1 | 477 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 4.0 | 473 | `java.util.regex.Pattern$GroupHead.match` |
| 4.0 | 467 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 3.6 | 420 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.2 | 379 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 3.0 | 350 | `java.util.HashMap.getNode` |
| 2.8 | 332 | `java.util.regex.Pattern$GroupTail.match` |
| 2.3 | 268 | `io.undertow.util.HttpString.higher` |
| 2.2 | 254 | `java.util.regex.Pattern$Branch.match` |
| 1.8 | 209 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.7 | 199 | `java.util.TreeMap.put` |
| 1.6 | 194 | `java.util.IdentityHashMap.get` |
| 1.6 | 192 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.3 | 157 | `java.util.regex.Matcher.reset` |
| 1.3 | 155 | `java.lang.ThreadLocal$ThreadLocalMap.getEntry` |
| 1.3 | 148 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 1.2 | 143 | `java.util.TreeMap.deleteEntry` |
| 1.2 | 138 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.1 | 132 | `java.util.concurrent.atomic.AtomicLongFieldUpdater.updateAndGet` |
| 1.1 | 126 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 1.0 | 122 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 0.9 | 101 | `java.lang.StringBuilder.toString` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 20.4 | 2399 | `java.nio.Buffer.<init>` |
| 20.4 | 2399 | `java.nio.ByteBuffer.position` |
| 20.4 | 2399 | `java.nio.ByteBuffer.<init>` |
| 20.2 | 2382 | `java.nio.HeapByteBuffer.<init>` |
| 20.1 | 2362 | `java.nio.ByteBuffer.allocate` |
| 13.0 | 1532 | `java.util.regex.Pattern$GroupHead.match` |
| 12.5 | 1468 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 12.3 | 1446 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 10.0 | 1178 | `io.undertow.server.protocol.http.RequestParser.handle` |
| 9.0 | 1057 | `java.util.regex.Pattern$BmpCharProperty.match` |
| 7.7 | 909 | `java.util.regex.Pattern$Branch.match` |
| 7.1 | 835 | `java.util.regex.Pattern$GroupTail.match` |
| 5.6 | 654 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 5.5 | 653 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 5.5 | 652 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 5.5 | 646 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.3 | 626 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 5.2 | 612 | `io.undertow.server.protocol.http.RequestParser.parseFieldValue` |
| 4.8 | 563 | `java.lang.StringBuilder.append` |
| 4.8 | 563 | `java.lang.AbstractStringBuilder.append` |
| 4.7 | 557 | `org.jboss.threads.EnhancedQueueExecutor.runThreadBody` |
| 4.5 | 535 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 4.5 | 531 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 4.5 | 526 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 4.2 | 496 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |

