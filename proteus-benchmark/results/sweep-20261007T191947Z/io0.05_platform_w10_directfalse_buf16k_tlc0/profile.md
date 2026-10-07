# CPU profile

66825 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 64.8 | 43333 | `proteus I/O-*` |
| 13.1 | 8741 | `proteus task-*` |
| 12.8 | 8542 | ` ForkJoinPool-1-worker-*` |
| 9.2 | 6178 | `(unnamed virtual thread)` |
| 0.0 | 25 | `main` |
| 0.0 | 4 | `proteus Accept` |
| 0.0 | 2 | `JFR Periodic Tasks` |
| 0.0 | 2 | `VirtualThread-unblocker` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 13.5 | 9034 | `java.util.IdentityHashMap.get` |
| 7.4 | 4953 | `java.util.concurrent.locks.LockSupport.unpark` |
| 6.9 | 4607 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 6.8 | 4520 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 4.1 | 2765 | `org.xnio.nio.NioSocketConduit.read` |
| 3.6 | 2430 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 3.3 | 2227 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 3.1 | 2041 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 2.9 | 1960 | `java.util.TreeMap.put` |
| 2.4 | 1631 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 2.1 | 1383 | `java.util.Collections$SynchronizedMap.get` |
| 1.8 | 1218 | `java.util.concurrent.ForkJoinPool$WorkQueue.nextLocalTask` |
| 1.8 | 1212 | `sun.nio.ch.SocketDispatcher.read` |
| 1.8 | 1174 | `java.util.concurrent.ForkJoinPool.signalWork` |
| 1.7 | 1160 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.7 | 1153 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 1.7 | 1151 | `java.util.WeakHashMap.hash` |
| 1.4 | 945 | `java.util.TreeMap.remove` |
| 1.3 | 889 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.1 | 705 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 1.0 | 681 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors` |
| 1.0 | 679 | `java.util.HashMap.getNode` |
| 1.0 | 650 | `sun.nio.ch.SocketChannelImpl.configureSocketNonBlockingIfVirtualThread` |
| 1.0 | 642 | `tools.jackson.core.io.CharTypes$AltQuoteEscapes.altEscapesFor` |
| 0.9 | 618 | `java.lang.AbstractStringBuilder.needsNewBuffer` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 15.5 | 10353 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 15.3 | 10244 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 14.9 | 9932 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.5 | 9034 | `java.util.IdentityHashMap.get` |
| 12.2 | 8156 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 12.1 | 8117 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 11.9 | 7955 | `io.undertow.util.AbstractAttachable.getAttachment` |
| 11.9 | 7933 | `io.undertow.server.HttpServerExchange.getReasonPhrase` |
| 7.5 | 4984 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 7.4 | 4976 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 7.4 | 4966 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |
| 7.4 | 4953 | `java.util.concurrent.locks.LockSupport.unpark` |
| 7.4 | 4953 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.unpark` |
| 7.3 | 4865 | `org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run` |
| 7.3 | 4853 | `java.lang.Thread.run` |
| 7.3 | 4851 | `org.xnio.XnioWorker$WorkerThreadFactory$1$1.run` |
| 7.3 | 4845 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 6.9 | 4610 | `org.jboss.threads.EnhancedQueueExecutor$PoolThreadNode.park` |
| 6.9 | 4607 | `java.util.concurrent.locks.LockSupport.parkNanos` |
| 6.8 | 4570 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 6.6 | 4405 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |
| 6.6 | 4404 | `tools.jackson.core.util.RecyclerPool$ConcurrentDequePoolBase.acquirePooled` |
| 6.6 | 4403 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 6.2 | 4148 | `io.undertow.conduits.ReadTimeoutStreamSourceConduit.read` |
| 5.6 | 3735 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |

