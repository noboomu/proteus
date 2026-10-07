# CPU profile

65310 execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).

## Sampled threads

| % | samples | frame |
|---:|---:|---|
| 57.7 | 37684 | `proteus I/O-*` |
| 19.8 | 12902 | `proteus task-*` |
| 13.2 | 8633 | ` ForkJoinPool-1-worker-*` |
| 9.3 | 6059 | `(unnamed virtual thread)` |
| 0.0 | 30 | `main` |
| 0.0 | 7 | `VirtualThread-unblocker` |
| 0.0 | 3 | `JFR Periodic Tasks` |

## Top leaf frames (self time)

| % | samples | frame |
|---:|---:|---|
| 10.0 | 6524 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 6.0 | 3910 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors` |
| 5.8 | 3808 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 5.6 | 3630 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 5.5 | 3614 | `java.util.concurrent.locks.LockSupport.unpark` |
| 5.5 | 3595 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 5.2 | 3398 | `java.lang.Thread.interrupted` |
| 4.7 | 3084 | `java.util.concurrent.ConcurrentLinkedDeque.linkLast` |
| 3.9 | 2542 | `java.util.concurrent.ForkJoinPool.deactivate` |
| 2.8 | 1797 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedPredecessors` |
| 2.7 | 1788 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 2.2 | 1461 | `java.util.TreeMap.put` |
| 2.2 | 1440 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 1.8 | 1180 | `jdk.internal.util.DecimalDigits.uncheckedGetCharsLatin1` |
| 1.8 | 1176 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 1.6 | 1074 | `java.util.WeakHashMap.hash` |
| 1.6 | 1018 | `java.util.Collections$SynchronizedMap.get` |
| 1.6 | 1014 | `java.lang.AbstractStringBuilder.needsNewBuffer` |
| 1.5 | 990 | `java.util.concurrent.atomic.AtomicLongFieldUpdater.updateAndGet` |
| 1.3 | 842 | `java.nio.ByteBuffer.putBuffer` |
| 1.1 | 744 | `io.undertow.server.DefaultByteBufferPool.freeInternal` |
| 1.1 | 744 | `sun.nio.ch.IOUtil.configureBlocking` |
| 1.0 | 667 | `java.util.HashMap.getNode` |
| 1.0 | 642 | `io.undertow.util.HttpString.bytesAreEqual` |
| 1.0 | 640 | `java.util.TreeMap.remove` |

## Top frames by inclusive samples

| % | samples | frame |
|---:|---:|---|
| 16.5 | 10807 | `tools.jackson.core.util.RecyclerPool$ConcurrentDequePoolBase.acquirePooled` |
| 16.5 | 10807 | `java.util.concurrent.ConcurrentLinkedDeque.pollFirst` |
| 16.5 | 10805 | `tools.jackson.core.util.RecyclerPool.acquireAndLinkPooled` |
| 14.3 | 9335 | `io.undertow.server.protocol.http.HttpReadListener.handleEventWithNoRunningRequest` |
| 13.1 | 8545 | `io.undertow.server.protocol.http.HttpResponseConduit.write` |
| 12.9 | 8453 | `io.undertow.conduits.AbstractFixedLengthStreamSinkConduit.write` |
| 12.9 | 8400 | `io.undertow.server.protocol.http.HttpResponseConduit.processWrite` |
| 12.6 | 8214 | `java.util.concurrent.ForkJoinPool.runWorker` |
| 12.5 | 8179 | `java.util.concurrent.ForkJoinWorkerThread.run` |
| 12.4 | 8103 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |
| 11.7 | 7669 | `org.xnio.conduits.ConduitStreamSinkChannel.write` |
| 10.7 | 6999 | `java.util.concurrent.ConcurrentLinkedDeque.unlink` |
| 10.0 | 6524 | `io.undertow.util.HeaderMap.fastIterateNonEmpty` |
| 9.4 | 6135 | `io.undertow.server.protocol.http.HttpReadListener.handleEvent` |
| 7.6 | 4984 | `tools.jackson.core.TokenStreamFactory._getBufferRecycler` |
| 6.4 | 4191 | `org.xnio.ChannelListeners.invokeChannelListener` |
| 6.1 | 3964 | `java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec` |
| 6.0 | 3910 | `java.util.concurrent.ConcurrentLinkedDeque.skipDeletedSuccessors` |
| 6.0 | 3903 | `java.util.concurrent.ForkJoinTask$AdaptedRunnableAction.exec` |
| 6.0 | 3903 | `java.util.concurrent.ForkJoinTask.doExec` |
| 5.9 | 3825 | `org.xnio.conduits.ReadReadyHandler$ChannelListenerHandler.readReady` |
| 5.7 | 3714 | `io.undertow.server.protocol.http.HttpRequestParser.handle` |
| 5.5 | 3624 | `org.jboss.threads.EnhancedQueueExecutor.execute` |
| 5.5 | 3623 | `org.xnio.XnioWorker$EnhancedQueueExecutorTaskPool.execute` |
| 5.5 | 3617 | `org.jboss.threads.EnhancedQueueExecutor.tryExecute` |

