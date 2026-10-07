# CPU profile

48562 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 53.1 | 25768 | `proteus I/O-*` |
| 18.9 | 9167 | ` ForkJoinPool-1-worker-*` |
| 16.0 | 7791 | `proteus task-*` |
| 12.0 | 5820 | `(unnamed virtual thread)` |
| 0.0 | 23 | `main` |
| 0.0 | 2 | `proteus Accept` |
| 0.0 | 1 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 10.6 | 5138 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 9.2 | 4477 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 6.5 | 3175 | `java.util.TreeMap.put` |
| 5.2 | 2504 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 4.8 | 2316 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.2 | 2031 | `java.util.concurrent.locks.LockSupport.unpark` |
| 3.7 | 1776 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.8 | 1348 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 2.7 | 1287 | `io.undertow.util.HttpString.bytesAreEqual` |
| 2.5 | 1226 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.5 | 1208 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.9 | 940 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 1.8 | 889 | `sun.nio.ch.SocketDispatcher.read` |
| 1.6 | 776 | `java.util.TreeMap.getEntry` |
| 1.5 | 740 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.getOrAddNode` |
| 1.5 | 722 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.4 | 685 | `sun.nio.ch.SocketDispatcher.writev` |
| 1.4 | 676 | `java.util.TreeMap.remove` |
| 1.3 | 614 | `org.xnio.Buffers.copy` |
| 1.3 | 609 | `java.util.TreeMap.successor` |
| 1.2 | 599 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.2 | 574 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.1 | 522 | `java.util.TreeMap.fixAfterInsertion` |
| 1.0 | 504 | `java.nio.ByteBuffer.putBuffer` |
| 1.0 | 478 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 17.9 | 8713 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 17.9 | 8686 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 13.7 | 6658 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 13.7 | 6646 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 13.3 | 6479 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.5 | 6075 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 11.9 | 5778 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 10.6 | 5138 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 9.8 | 4746 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 9.8 | 4741 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 9.8 | 4741 | `java.util.concurrent.ForkJoinTask.doExec` |
| 9.5 | 4636 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 8.3 | 4026 | `java.util.TreeSet.add` |
| 8.2 | 3990 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.handleReadTimeout` |
| 8.2 | 3987 | `org.xnio.nio.WorkerThread.executeAfter` |
| 7.6 | 3698 | `java.util.TreeMap.put` |
| 6.5 | 3155 | `org.xnio.conduits.ConduitStreamSourceChannel.read` |
| 6.5 | 3141 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 5.0 | 2435 | `org.xnio.nio.WorkerThread$TimeKey.remove` |
| 5.0 | 2414 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 5.0 | 2404 | `java.util.TreeSet.remove` |
| 4.9 | 2403 | `java.util.TreeMap.remove` |
| 4.9 | 2403 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 4.4 | 2118 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 4.3 | 2073 | `org.jboss.threads.EnhancedQueueExecutor.execute` |

