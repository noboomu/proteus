# Proteus Performance Contract

The rules a runtime change to Proteus must satisfy before it is merged to the development line, and the exact measurement that decides it.

## Scope

- Applies to any change in `proteus-core`, `proteus-openapi`, or `proteus-websocket` that can alter request throughput, latency, memory, or thread usage.
- Applies to `proteus-core` reference configuration defaults under `undertow`, `jackson`, and `application`.
- Does not apply to documentation, javadoc, or the `proteus-benchmark` module.

## Baseline

- The baseline is the Proteus revision whose `ProteusApplication.buildServer()` lets `Undertow.Builder` create its own XNIO worker, with `setIoThreads` and `setWorkerThreads` taking effect.
- Baseline configuration is that revision's `reference.conf` unchanged.
- The baseline and the candidate are built from source and run with the same JDK as the candidate.
- Where the baseline cannot compile on the candidate JDK, it runs on the newest JDK it compiles on, and the JDK of each side is recorded with the result.

## Workload

- Application: one controller, built the way a Proteus user writes one.
  - `GET /plaintext`: returns `ServerResponse<ByteBuffer>` with body `Hello, World!`, `Content-Type: text/plain`.
  - `GET /json`: returns `ServerResponse<Message>` where `Message` is a record with one field `message = "Hello, World!"`, serialized by the injected `ObjectMapper`, `Content-Type: application/json`.
  - `GET /plaintext/exchange`: writes the same body directly through `HttpServerExchange.getResponseSender()`.
  - `GET /json/blocking`: the `/json` handler annotated `@Blocking`.
- Configuration: `application.conf` sets only `application.host`, `application.path = ""`, `application.ports.http`, and `globalHeaders.Server`. Every other key takes the revision's own default.
- JVM flags: `-Xms2g -Xmx2g -XX:+UseParallelGC` on both sides.

## Hosts

- Server host: attenborough, 192.168.1.209, AMD EPYC 9755.
- Server pinning: `taskset -c 0-7,128-135` (8 physical cores, 16 hardware threads); `availableProcessors()` returns 16.
- Load host: godzilla, 192.168.1.99, 10 GbE to the server on the same switch.
- Load generator: `wrk` built from github.com/wg/wrk, run on godzilla only.
- No other benchmark, build, or container runs on either host during a measurement.

## Measurement

- Request headers on every request: `Host: tfb-server`, `Connection: keep-alive`, and an `Accept` header.
  - Plaintext routes: `Accept: text/plain,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7`.
  - JSON routes: `Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7`.
- Per route and connection count: one 5 s warmup discarded, then one 15 s measured pass.
- Threads: `-t min(connections, 48)`.
- Cells:
  - `/plaintext` with 16-request pipelining via a Lua script concatenating 16 copies of the request: 256, 1024, 4096 connections.
  - `/plaintext` without pipelining: 256, 1024 connections.
  - `/plaintext/exchange` without pipelining: 256, 1024 connections.
  - `/json`: 64, 256, 512 connections.
  - `/json/blocking`: 256, 1024 connections.
- Rounds: baseline and candidate alternate, server restarted each time, three rounds each.
- Reported per cell: median, minimum, maximum requests per second; median p99 latency; total non-2xx responses and socket errors.

## Acceptance

- A cell improves when the candidate minimum exceeds the baseline maximum.
- A cell regresses when the candidate maximum is below the baseline minimum.
- A change is accepted when it regresses no cell and improves at least one.
- A change that regresses any cell is reverted.
- Any non-2xx response or socket error other than end-of-run timeouts on the candidate rejects the change.
- The full reactor test suite passes on the candidate.

## Change discipline

- One change per measurement; a change is a single default value or a single code path.
- Each accepted change is its own commit containing the code, the test that covers its behavior, and the result table for that measurement.
- A rejected change is reverted with a revert commit; the result table is kept in the benchmark results directory.

## Verification

- `mvn clean verify` on the candidate: all tests pass, zero failures.
- Server startup log on the candidate at 16 available processors reports the XNIO I/O thread count and worker pool size implied by its configuration.
- The result table for every accepted change shows each cell's baseline and candidate median, minimum, maximum, p99, and error totals.
- A final measurement of the development line after all accepted changes, against the baseline, satisfies the Acceptance rules.
