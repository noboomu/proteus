# diag-01: where /json/blocking p99 latency comes from at 1024 connections

Read-only diagnostic. Development head `c685434` (thread-routed buffer and recycler pools), unchanged runtime defaults except `undertow.server.recordRequestStartTime=true` in the scratch app's `application.conf`. Server on attenborough pinned `0-7,128-135`, wrk from godzilla, 5 s warmup + 15 s measured per cell, one run. Instrumentation in `instrumentation.diff` (scratch app only, not in Proteus); driver `diag-blocking.sh`.

Server-side stages, from `System.nanoTime()`, in microseconds:

- `queue`: request parsed on the I/O thread → handler running on a virtual thread.
- `complete`: handler running → exchange-complete listener (response written).
- `total`: request parsed → exchange complete.

## Result

| conns | wrk req/s | wrk p50 | wrk p90 | wrk p99 | server total p99 | server p99.9 | server max |
|---:|---:|---:|---:|---:|---:|---:|---:|
| 256 | 751k | 0.25 ms | 0.97 ms | 3.18 ms | 0.23 ms | 1.1 ms | 23.7 ms |
| 1024 | 683k | 0.93 ms | 139 ms | 250 ms | 0.52 ms | 233 ms | 395 ms |

Stage breakdown at 1024 (`stages-1024.txt`):

- `queue`: p99 431 µs, p99.9 799 µs, max 9.4 ms. Virtual-thread scheduling delay is not the tail.
- `complete`: p50 7 µs, p99 26 µs, **p99.9 232.6 ms, max 395 ms**. The whole tail lives here.
- At 256 the same stage stays small (p99.9 987 µs, max 23.7 ms).

Supporting evidence:

- Little's law: 1024 / 683k = 1.50 ms mean time in the system. wrk reports a 34.4 ms mean, so most of wrk's tail is synthetic. Its coordinated-omission correction (`stats_correct`, interval ≈ 1.5 ms here) adds about 154 samples for every 232 ms stall, so roughly 0.1% of real requests stalling becomes wrk's p90/p99.
- GC is ruled out: 122 ParallelGC pauses, the longest 7.5 ms (`diag-gc.log`).
- Carrier CPU during the 1024 window: 16 ForkJoin carriers at about 7 cores combined (`cpu-1024-*.txt`), so carriers are not saturated. The XNIO I/O thread counters read 0 because `/proc` reports their CPU against the thread names above; per-I/O-thread CPU is UNVERIFIED.

## Conclusion

The /json/blocking p99 at 1024 connections does not come from virtual-thread scheduling or GC. About 0.1% of exchanges stall 200–400 ms between "handler running" and "exchange complete". That stage covers the write of a response from a virtual thread through Undertow's blocking sender. wrk's coordinated-omission correction turns those rare stalls into a 250 ms p99.

Next diagnostic: find what the stalled exchanges wait on inside `complete`. Record the stack, or JFR `jdk.VirtualThreadPinned` / `jdk.VirtualThreadSubmitFailed` and park events, for exchanges whose `complete` exceeds 50 ms. The leading hypothesis (UNVERIFIED) is the blocking response write parking the virtual thread on `awaitWritable` until the I/O thread's next selector wakeup. If that holds, the fix is to complete the response asynchronously from the virtual thread (non-blocking `Sender` handed back to the I/O thread) instead of blocking-sending.
