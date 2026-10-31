# diag-02: what the /json/blocking 1024-connection stall waits on

Read-only diagnostic on development head (thread-routed buffer and recycler pools, XNIO slice pool). Server on attenborough pinned `0-7,128-135`, wrk from godzilla. Instrumentation adds a `commit` stage to diag-01's histograms (`instrumentation-commit-stage.diff`). JFR `profile` settings with `jdk.ThreadPark` ≥ 50 ms, `jdk.VirtualThreadPinned` ≥ 20 ms, `jdk.SocketWrite` ≥ 20 ms, `jdk.JavaMonitorEnter` ≥ 20 ms (`diag-blocking-jfr.sh`).

Host note: attenborough restarted at 2026-10-30 14:03. Absolute throughput on every route is about 1.6–2x the numbers recorded before that (e.g. `/plaintext` pipelined 1024: 2.7M → 6.0M). Cross-day comparisons of absolute numbers are invalid. Same-run A/B comparisons are unaffected.

## Stages at 1024 connections (µs)

| stage | p50 | p99 | p99.9 | max |
|---|---:|---:|---:|---:|
| queue (parsed → handler on VT) | 83 | 496 | 852 | 21,935 |
| commit (handler → response committed) | 0 | 1 | 15 | 25,567 |
| complete (handler → exchange complete) | 5 | 19 | 92,799 | 317,439 |

- Committing the response is fast. The tail is all in the write that follows the commit, until the exchange completes.
- 60 s steady-state run: `complete` p99.9 65.8 ms, max 297 ms, wrk p99 131 ms. The tail doesn't shrink with run length, so it isn't warmup.

## What JFR shows

- All 16 ForkJoin carrier threads park in `ForkJoinPool.awaitWork` at the same instant and stay parked about 290–310 ms (`jfr-threadpark.txt`). This happened three times in 30 s: 18:46:48.537, 18:47:03.941, 18:47:09.351.
- During each window there are no Java execution samples on any thread (`jfr-java-samples-per-100ms.txt`: 18:47:09.3 has 14 samples, 18:47:09.4–.5 has none, 18:47:09.6 has 6). Native samples show only XNIO I/O threads in `EPoll.wait`.
- Not GC: the longest pause is 14.4 ms (`diag-gc.log`, `jfr-vmops-top20.txt`). Not a VM operation: the longest is `CollectForMetadataAllocation` at 14.4 ms.
- Not pinning: 0 `jdk.VirtualThreadPinned` events. No `jdk.VirtualThreadSubmitFailed`.
- No virtual thread is runnable and every I/O thread is idle in `epoll_wait`. The work exists, but the server waits for the kernel to report the sockets writable.

## What it is not (measured)

- `TCP_CORK`: CORK off gave the same 1024 result (597k, p99 120–134 ms vs 599–606k, p99 138–150 ms).
- NIC drops: `rx_missed_errors` on godzilla and `rx_total_ring_discards` on attenborough did not change across a virtual-thread run.
- Retransmits: worker dispatch saw more TCP retransmits (godzilla 1,957, attenborough 894) than virtual threads (933, 299), but without the tail, so retransmits are not the cause.
- Zero-window: about 1,100–1,200 zero-window advertisements from attenborough in both modes.

## Conclusion

Under virtual-thread dispatch at 1024 connections, the server goes completely idle for about 300 ms at intervals of several seconds. No thread is runnable, the I/O threads sit in `epoll_wait`, and the stalled exchanges have already committed their response. About 0.1% of exchanges span such a window. Worker dispatch at the same load shows no such window. Why worker dispatch is immune is UNVERIFIED.

Open hypotheses, none verified:

- The blocking response write on a virtual thread parks in XNIO `SelectorUtils.await`, which uses a `ThreadLocal` temporary selector (`NioXnio.getSelector`). Every virtual thread therefore opens a new epoll instance for its first blocking wait. This explains extra cost, but not by itself a whole-server idle window.
- The roughly 300 ms period is close to a TCP retransmission timeout (200 ms minimum plus RTT/backoff). A synchronized stall on the client (godzilla/wrk) would also make the server look idle. Server-side JFR cannot tell these apart.

Next diagnostic, bounded:

- During a 1024-connection run, record both sides at once:
  - `ss -tin` snapshots every 100 ms on attenborough and godzilla, for `rto`, `unacked`, `retrans`, and send/receive queue at stall times;
  - `strace -c -f -e trace=epoll_create1,epoll_wait,writev` counts on the server PID for 10 s.
- Line both up against JFR park timestamps.
- Then A/B the candidate fix, sending the `@Blocking` response from the I/O thread with the non-blocking sender, with `perf-ab.sh`.
