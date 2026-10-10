# CPU profile

8491 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 51.9 | 4409 | `proteus I/O-*` |
| 18.9 | 1602 | `proteus task-*` |
| 15.5 | 1313 | `(unnamed virtual thread)` |
| 11.6 | 984 | ` ForkJoinPool-1-worker-*` |
| 2.1 | 182 | `ForkJoinPool-1-delayScheduler` |
| 0.0 | 4 | `VirtualThread-unblocker` |
| 0.0 | 1 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 15.1 | 1283 | `sun.nio.ch.IOUtil.configureBlocking` |
| 9.4 | 797 | `java.util.TreeMap.put` |
| 5.7 | 487 | `java.util.IdentityHashMap.get` |
| 4.7 | 396 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 4.0 | 343 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 3.0 | 254 | `java.util.concurrent.locks.LockSupport.unpark` |
| 2.7 | 232 | `java.util.HashMap.getNode` |
| 1.9 | 159 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.7 | 147 | `java.util.TreeMap.getEntry` |
| 1.6 | 140 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.6 | 132 | `java.lang.Thread.interrupted` |
| 1.5 | 131 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 1.5 | 129 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.5 | 127 | `java.util.TreeMap.successor` |
| 1.5 | 125 | `java.util.concurrent.ForkJoinPool$WorkQueue.push` |
| 1.4 | 116 | `io.undertow.server.protocol.http.HttpRequestParser.handlePath` |
| 1.3 | 111 | `java.util.TreeMap.remove` |
| 1.2 | 105 | `java.util.ArrayDeque.addLast` |
| 1.1 | 94 | `io.undertow.util.HttpString.calcHashCode` |
| 1.0 | 85 | `org.xnio.nio.NioHandle.suspend` |
| 1.0 | 83 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 0.9 | 79 | `sun.nio.ch.EPollSelectorImpl.processUpdateQueue` |
| 0.9 | 78 | `org.xnio.Buffers.copy` |
| 0.9 | 77 | `ch.qos.logback.classic.Logger.isTraceEnabled` |
| 0.9 | 75 | `java.lang.VirtualThread.parkNanos` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 15.1 | 1283 | `sun.nio.ch.IOUtil.configureBlocking` |
| 15.1 | 1283 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 12.4 | 1057 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 12.4 | 1052 | `sun.nio.ch.SocketChannelImpl.implRead` |
| 12.2 | 1035 | `org.xnio.nio.NioSocketConduit.read` |
| 12.2 | 1035 | `sun.nio.ch.SocketChannelImpl.read` |
| 12.1 | 1027 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 11.2 | 955 | `org.xnio.nio.WorkerThread.executeAfter` |
| 10.0 | 847 | `java.util.TreeMap.put` |
| 9.7 | 820 | `java.util.TreeSet.add` |
| 9.3 | 790 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 8.6 | 731 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 8.5 | 721 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 8.2 | 697 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 6.8 | 580 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 6.7 | 570 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 5.7 | 488 | `java.util.IdentityHashMap.get` |
| 5.3 | 454 | `java.util.TreeMap.remove` |
| 5.3 | 454 | `java.util.TreeSet.remove` |
| 5.3 | 451 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 4.8 | 408 | `io.undertow.server.protocol.http.HttpRequestParser.handleHeaderValue` |
| 4.7 | 403 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 4.7 | 396 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 4.6 | 390 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 3.9 | 327 | `org.jboss.threads.EnhancedQueueExecutor.execute` |

