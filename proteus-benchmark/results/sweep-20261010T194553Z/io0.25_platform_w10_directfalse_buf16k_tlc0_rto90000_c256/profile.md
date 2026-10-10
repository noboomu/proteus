# CPU profile

1437 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 84.1 | 1209 | `proteus I/O-*` |
| 8.2 | 118 | `(unnamed virtual thread)` |
| 3.9 | 56 | ` ForkJoinPool-1-worker-*` |
| 2.4 | 34 | `proteus task-*` |
| 1.6 | 23 | `main` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 14.8 | 213 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 11.2 | 161 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 4.0 | 57 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 3.4 | 49 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.9 | 42 | `java.util.HashMap.getNode` |
| 2.6 | 38 | `io.undertow.util.HttpString.calcHashCode` |
| 2.3 | 33 | `io.undertow.util.HeaderMap.getOrCreateEntry` |
| 2.2 | 32 | `java.util.TreeMap.put` |
| 2.2 | 31 | `java.util.TreeMap.getEntry` |
| 2.1 | 30 | `sun.nio.ch.EPollSelectorImpl.doSelect` |
| 2.0 | 29 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.suspendReads` |
| 1.9 | 28 | `io.undertow.util.HttpString.higher` |
| 1.9 | 27 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 1.8 | 26 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 1.8 | 26 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 1.7 | 24 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 1.5 | 22 | `java.util.TreeMap.deleteEntry` |
| 1.5 | 22 | `java.util.concurrent.locks.LockSupport.unpark` |
| 1.5 | 21 | `java.util.HashMap.putVal` |
| 1.5 | 21 | `org.xnio.Buffers.copy` |
| 1.2 | 17 | `io.undertow.server.Connectors.verifyToken` |
| 1.1 | 16 | `java.lang.AbstractStringBuilder.append` |
| 1.0 | 15 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 15 | `java.util.TreeMap.fixAfterDeletion` |
| 1.0 | 14 | `java.nio.ByteBuffer.putBuffer` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 29.5 | 424 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 28.5 | 409 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 21.7 | 312 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 15.7 | 225 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 15.1 | 217 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 12.2 | 175 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 12.1 | 174 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.0 | 172 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 10.4 | 149 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 10.0 | 143 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 7.0 | 101 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 5.9 | 85 | `sun.nio.ch.EPollSelectorImpl.doSelect` |
| 5.6 | 80 | `sun.nio.ch.SelectorImpl.lockAndDoSelect` |
| 5.1 | 74 | `java.util.TreeSet.remove` |
| 5.1 | 74 | `java.util.TreeMap.remove` |
| 5.0 | 72 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 4.5 | 65 | `org.xnio.nio.WorkerThread.run` |
| 4.5 | 65 | `sun.nio.ch.SelectorImpl.select` |
| 4.5 | 65 | `java.lang.AbstractStringBuilder.append` |
| 4.5 | 65 | `java.lang.StringBuilder.append` |
| 4.5 | 65 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 4.5 | 64 | `io.undertow.util.HttpString.<init>` |
| 4.2 | 61 | `java.util.HashMap.getNode` |
| 4.1 | 59 | `io.undertow.util.HttpString.calcHashCode` |
| 4.0 | 58 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |

