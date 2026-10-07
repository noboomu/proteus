# Proteus worker and buffer-pool sweep, 2026-10-07

Host: attenborough, AMD EPYC 9755 (128 physical / 256 logical), idle (98 to 99% before each run).
JDK 27 (openjdk 27 2026-09-15), Undertow 2.3.17.Final, oha 1.16.0, HTTP/1.1 keep-alive, 256 connections, 15s measured after 5s warm-up per route.
Load generator on the same host. Raw `oha` JSON, `env.txt`, and JFR summaries (`profile.md`) are in the three sweep directories listed at the end.

## Starting point

Before this work `ProteusApplication.buildServer()` created the XNIO worker with `createWorkerBuilder().build()` and passed it to `Undertow.Builder.setWorker`. Undertow ignores `setIoThreads`/`setWorkerThreads` for an external worker, so the server ran on XNIO's defaults: **1 I/O thread, 4 to 16 task threads**. `undertow.ioThreadsMultiplier` and `undertow.workerThreadsMultiplier` had no effect. That is fixed; the multipliers now size `XnioWorker.Builder` directly, and `undertow.workerExecutor=virtual` can replace the task pool with a virtual-thread-per-task executor.

## Where the time went (JFR, 5 I/O threads, platform pool, Undertow default buffer pool)

| inclusive % | frame |
|---:|---|
| 18.9 | `Collections$SynchronizedMap.get` |
| 18.4 | `DefaultByteBufferPool$ThreadLocalCache.get` |
| 13.4 | `WeakHashMap.get` |
| 9.2 | `DefaultByteBufferPool.freeInternal` |
| 7.3 | `ReadTimeoutStreamSourceConduit.handleReadTimeout` → `WorkerThread.executeAfter` |
| 6.8 | `TreeSet.add` (XNIO timer queue) |
| 4.8 | `tools.jackson.databind.ObjectMapper.writeValueAsBytes` |

Undertow's `DefaultByteBufferPool` keeps a per-thread buffer cache in a `Collections.synchronizedMap(new WeakHashMap<Thread, …>())` and consults it on every allocate and free. On this workload that lookup plus the read-timeout timer rescheduling was roughly a third of I/O-thread CPU. Jackson was under 5%.

## Buffer pool: `directBuffers` × `bufferPool.threadLocalCacheSize` (13 I/O threads, platform pool)

| direct | tlc | plaintext/undertow | plaintext/response | json/response | plaintext/blocking | p99 undertow |
|---|---:|---:|---:|---:|---:|---:|
| true | 4 (Undertow default) | 473k | 473k | 463k | 145k | 2.5 ms |
| false | 4 | 514k | 501k | 484k | 145k | 2.5 ms |
| true | 0 | 277k | 280k | 272k | 169k | 5.7 ms |
| **false** | **0** | **1,030k** | **933k** | **947k** | **506k** | **1.3 ms** |

- `tlc=0` with direct buffers is much worse: misses fall through to `ByteBuffer.allocateDirect` (`Bits.setMemory`, `Bits.tryReserveMemory` dominate).
- `tlc=0` with heap buffers roughly doubles every route and halves p99. Heap allocation is a TLAB bump; the pool's bookkeeping cost more than direct allocation saved.
- After the change the top self-time frame is `HeaderMap.fastIterateNonEmpty` (10.6%), which is response-header serialization. The profile looks like an HTTP server rather than a map benchmark.

Reproduced in the confirmation sweep: 968k to 977k `json/response`, 968k to 982k `plaintext/response` at 13 I/O threads.

## I/O thread count (platform pool, heap buffers, tlc=0)

| I/O threads | multiplier | plaintext/response | json/response | json/worker | p99 json/response |
|---:|---:|---:|---:|---:|---:|
| **13** | **0.05** | **968k to 982k** | **905k to 977k** | 483k to 640k | 1.3 ms |
| 26 | 0.1 | 844k to 884k | 814k to 835k | 475k to 491k | 1.6 ms |
| 512 | 2 (old default) | 806k to 855k | 784k to 820k | 366k to 374k | 1.9 ms |

With the Undertow-default buffer pool the same shape held (5 threads 630k, 64 threads 555k, 512 threads 480k on `plaintext/undertow`). Fewer I/O threads win on throughput and latency. A single load generator with 256 connections cannot drive 5 threads to saturation on this CPU, so the true optimum is between 5 and 26; 0.25 × CPUs is the new reference default as a conservative middle on smaller hosts (12 on a 48-logical Threadripper, 1 on a 4-CPU container).

## Virtual threads

### `workerExecutor=virtual` (replaces the XNIO task pool)

| | heap/tlc0 platform | heap/tlc0 virtual | default-pool platform | default-pool virtual |
|---|---:|---:|---:|---:|
| plaintext/worker | 655k | 653k to 703k | 633k | 216k |
| json/worker | 483k to 640k | 607k to 671k | 427k | 206k |
| plaintext/response (I/O thread, no dispatch) | 968k | 936k to 1,003k | 569k | 580k |

- With Undertow's default buffer pool, VT dispatch was 3× slower than the platform pool. The JFR profile was 14.8% `ForkJoinPool.signalWork` + 9.7% `ForkJoinPool.deactivate`: a submit/park/unpark cycle per request with nothing blocking to amortize it, on top of the WeakHashMap lookups now happening from short-lived threads.
- With heap buffers and tlc=0, VT and platform are **equivalent within run-to-run noise** on dispatched routes. The buffer pool was most of the VT penalty.
- Routes that never dispatch are unaffected either way.

### `@Blocking` (per-request `Thread.startVirtualThread`)

| | default pool | heap/tlc0 |
|---|---:|---:|
| plaintext/blocking | 145k to 170k | 492k to 506k |
| json/blocking | 137k to 168k | 442k to 486k |

Same story: the per-thread buffer cache punished every fresh virtual thread. Fixed, `@Blocking` costs about half of the direct I/O-thread path, which is the price of a thread handoff and acceptable for routes that actually block.

Default stays `platform`. Virtual is now a reasonable opt-in for applications whose dispatched handlers block on I/O.

## Worker pool size

`workerThreadsMultiplier` 1 vs 10: within noise on every route in every sweep. Only matters when dispatched handlers block.

## Read timeout

`socket.readTimeout` 90000 vs 0 at the winning config: within noise (platform 877k vs 925k, virtual 937k vs 934k on `plaintext/undertow`; other routes split both ways). The timer cost seen in the first profile shrank once the buffer-pool cost was removed. Left at 90000.

## Changes to `reference.conf`

| key | old | new |
|---|---|---|
| `undertow.ioThreadsMultiplier` | 2 | 0.25 |
| `undertow.directBuffers` | true | false |
| `undertow.bufferPool.threadLocalCacheSize` | (Undertow default 4) | 0 |
| `undertow.workerExecutor` | (did not exist) | platform |

## Not measured

- Applications whose handlers block (DB, outbound HTTP). The VT and worker-pool conclusions above are for CPU-bound handlers only.
- HTTP/2, TLS, request bodies, large responses. Buffer-pool behavior with 16k heap buffers under large payloads may differ.
- A separate load-generator host. Numbers here include `oha` on the same machine.
- Threadripper (godzilla) with the final defaults; godzilla was contended during this work and its earlier numbers predate the worker-sizing fix.

## Sweep directories

- `sweep-20261007T174930Z`: io × executor × pool size, Undertow default buffer pool (15 configs)
- `sweep-20261007T184646Z`: direct × tlc at 5 and 13 I/O threads (8 configs)
- `sweep-20261007T191947Z`: io 13/26/512 × pool size, heap/tlc0 (6 configs)
- `sweep-20261007T195059Z`: platform vs virtual × readTimeout, heap/tlc0, 13 I/O threads (4 configs)
