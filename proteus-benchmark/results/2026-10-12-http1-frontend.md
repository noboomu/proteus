# Proteus HTTP/1.1 front end experiment, 2026-10-12

Branch `feature/http1-frontend`. A Proteus-owned HTTP/1.1 codec (`io.sinistral.proteus.server.http1`) that produces the standard Undertow `HttpServerExchange`, selected with `undertow.frontEnd=proteus`.

## Hypothesis

The 2026-10-11 JFR attribution showed `HeaderMap.fastIterateNonEmpty` at 23% and `HttpReadListener.handleEvent` at 11% of execution samples. I read those as the cost of Undertow's header scan and keep-alive re-arm and predicted a replacement codec would recover 25 to 35% of CPU.

## Result

Parity. Nautilus, 4 cores pinned, 4 alternating rounds per front end, idle-gated:

| route | undertow | proteus | paired per-round deltas |
|---|---:|---:|---|
| plaintext/response | 350,110 | 345,352 | -0.6 +0.1 -2.8 +0.3 |
| json/response | 354,146 | 340,995 | +0.6 -1.9 -11.3 -0.3 |
| plaintext/undertow | 365,111 | 350,897 | -4.2 -1.8 -12.0 +1.1 |
| json/worker | 198,211 | 198,209 | -1.0 -0.1 +0.1 +1.8 |
| plaintext/blocking | 188,897 | 188,655 | -1.7 -0.7 +0.5 +2.2 |

Proteus round 3 overlapped a host load blip (the two -11/-12 pairs); the other 17 pairs are within ±3%. p99 identical.

## Why the hypothesis was wrong

JFR `jdk.ExecutionSample` attributes a thread blocked in a syscall to the innermost Java frame, and the JIT had inlined the whole write path into `fastIterateNonEmpty` and the whole read path into `handleEvent`. Those frames were where `writev` and `read` were issued, not where CPU was spent. `jdk.NativeMethodSample` for the same recordings: 43% `SocketDispatcher.writev0`, 39% `EPoll.wait`, 14% `SocketDispatcher.read0`. The codec replaced the Java work around those calls, which was a minority of the sample.

What the Proteus codec does remove is real but small: `HttpString.calcHashCode`/`bytesAreEquivalent` on header-name parsing and the `Long.toString(requestId)` in the exchange, together 5 to 8% of Java samples. Those vanished from the Proteus profile and the total did not move, because the floor is socket I/O plus `HttpServerExchange` and `HeaderMap` allocation, shared by both front ends.

A two-hour spike stubbing the response head to a constant byte array would have shown parity before 1,400 lines were written. That is the step I skipped.

## What the branch is worth

- Correctness: every existing test (230) plus `Http1ConformanceTest` (20 wire-level cases: pipelining, Content-Length and chunked bodies, 413, malformed lines, missing Host, HTTP/1.0, half-close, idle timeout) passes under both front ends. WebSocket upgrade works.
- It bypasses Undertow 2.4's per-request `HostHeaderHandler` wrap, so it would let Proteus take 2.4.x security releases without the measured 10% regression. Not yet verified; see below.
- It is the seam for anything that changes per-connection behavior Undertow hard-codes.

Not merged. `frontEnd=undertow` remains the default. The branch stays as an experiment with an opt-in config switch.

## Open

- Measure Undertow 2.4.4 with `frontEnd=proteus` on nautilus. If parity holds there too, the branch has a concrete payoff.
- `BUFFER_PIPELINED_DATA`: Undertow's pipelining write batching, untested.

Directories: `nautilus/ab-frontend/`.
