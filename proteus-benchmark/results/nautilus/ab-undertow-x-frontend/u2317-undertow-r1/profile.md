# CPU profile

8976 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 89.2 | 8004 | `proteus I/O-*` |
| 10.3 | 929 | `proteus task-*` |
| 0.5 | 41 | `main` |
| 0.0 | 1 | `(unnamed virtual thread)` |
| 0.0 | 1 | `proteus Accept` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 17.2 | 1540 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 5.5 | 496 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 4.9 | 444 | `sun.nio.ch.SocketChannelImpl.read` |
| 4.6 | 413 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 3.5 | 317 | `sun.nio.ch.SocketDispatcher.read` |
| 3.4 | 307 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 3.1 | 275 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 2.5 | 228 | `io.undertow.util.HttpString.bytesAreEquivalent` |
| 2.5 | 223 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 2.3 | 209 | `io.undertow.server.protocol.http.HttpRequestParser.handleCachedHeader` |
| 2.3 | 207 | `org.xnio.nio.NioSocketConduit.read` |
| 2.0 | 177 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.9 | 175 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.9 | 173 | `io.undertow.util.HttpString.calcHashCode` |
| 1.6 | 148 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 1.6 | 141 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 1.5 | 133 | `java.util.TreeMap.put` |
| 1.5 | 131 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 1.3 | 118 | `java.util.TreeMap.deleteEntry` |
| 1.3 | 117 | `org.xnio.nio.WorkerThread.setOps` |
| 1.1 | 103 | `sun.nio.ch.IOUtil.write` |
| 1.1 | 101 | `io.undertow.server.Connectors.verifyToken` |
| 1.1 | 97 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 1.0 | 93 | `java.lang.AbstractStringBuilder.append` |
| 1.0 | 87 | `ch.qos.logback.classic.Logger.isTraceEnabled` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 23.2 | 2083 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 23.0 | 2067 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 22.8 | 2048 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 22.1 | 1986 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 21.8 | 1959 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 21.2 | 1903 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 17.2 | 1540 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 15.0 | 1349 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 12.5 | 1123 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 11.0 | 988 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 9.4 | 848 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 7.3 | 658 | `org.xnio.nio.NioSocketConduit.read` |
| 7.3 | 656 | `org.xnio.conduits.AbstractStreamSourceConduit.read` |
| 6.7 | 600 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 5.6 | 502 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit$2.readReady` |
| 5.1 | 455 | `sun.nio.ch.SocketChannelImpl.read` |
| 4.8 | 435 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 4.8 | 429 | `io.undertow.channels.DetachableStreamSinkChannel.write` |
| 4.5 | 403 | `io.undertow.util.HttpString.bytesAreEqual` |
| 4.5 | 403 | `io.undertow.util.HttpString.equals` |
| 4.1 | 369 | `java.lang.StringBuilder.append` |
| 4.1 | 368 | `java.lang.AbstractStringBuilder.append` |
| 3.9 | 350 | `sun.nio.ch.EPollSelectorImpl.doSelect` |
| 3.9 | 348 | `sun.nio.ch.SelectorImpl.lockAndDoSelect` |
| 3.7 | 330 | `org.xnio.nio.WorkerThread.run` |

