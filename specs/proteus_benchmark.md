# Proteus HTTP Benchmark Specification

## Purpose

- Provide a reproducible throughput and latency measurement of the Proteus request path on the current JDK and Undertow line.
- Replace the unverifiable 2017 TechEmpower claim with a locally reproducible result recorded in the repository.

## Scope

- Module: `proteus-benchmark`, reactor member, not published to Maven Central.
- Workloads mirror the TechEmpower `plaintext` and `json` tests:
  - `GET /plaintext` returns `Hello, World!` as `text/plain`.
  - `GET /json` returns `{"message":"Hello, World!"}` as `application/json` serialized by the application `ObjectMapper`.
- Handler styles measured for each workload so the cost of each abstraction layer is visible:
  - `exchange`: controller method takes `HttpServerExchange` and completes it on the I/O thread.
  - `response`: controller method returns `ServerResponse<T>`, completed on the I/O thread.
  - `blocking`: `@Blocking` controller method returning `ServerResponse<T>`; the generated handler dispatches it off the I/O thread to a new virtual thread per request.
  - `worker`: controller method that calls `exchange.dispatch(handler)` so the request runs on the XNIO worker task pool, whose thread type is governed by `undertow.workerExecutor`.
  - `undertow`: a hand-written `HttpHandler` registered on the same router, as the floor.
- Blocking workload: `GET /sleep/worker` and `GET /sleep/blocking` park the handling thread for `bench.sleepMs` (default 10) before responding, as a stand-in for a database or outbound HTTP wait. `/sleep/worker` dispatches to the XNIO task pool; `/sleep/blocking` uses `@Blocking`.

## Worker Threading Contract

- `ProteusApplication.buildServer()` creates the XNIO worker with `Xnio.createWorker(OptionMap)` and passes it to `Undertow.Builder.setWorker`. Because the worker is external, Undertow ignores `setIoThreads` and `setWorkerThreads`; the option map carries the values Undertow would apply to its own worker.
- `undertow.ioThreadsMultiplier` × available processors is `Options.WORKER_IO_THREADS`.
- `undertow.workerThreadsMultiplier` × available processors is `Options.WORKER_TASK_CORE_THREADS` and `Options.WORKER_TASK_MAX_THREADS`.
- `CONNECTION_HIGH_WATER` and `CONNECTION_LOW_WATER` are 1000000, `TCP_NODELAY` and `CORK` are true.
- `@Blocking` routes dispatch with `Thread::startVirtualThread`.
- Startup logs the effective I/O thread count and task thread count at `INFO`.

## Buffer Pool Contract

- `ProteusApplication.buildServer()` passes one `ThreadRoutedByteBufferPool` to `Undertow.Builder.setByteBufferPool`; it holds two `DefaultByteBufferPool` instances with `undertow.directBuffers` and `undertow.bufferSize`.
- An allocation on a platform thread (I/O and worker threads) uses the platform pool: Undertow's own default parameters, per-thread cache of 4 buffers and no shared-queue cap.
- An allocation on a virtual thread uses the virtual pool: per-thread cache of 0, shared lock-free queue capped by `undertow.bufferPool.virtualMaximumPoolSize`.
- A buffer returns to the pool it was allocated from, whichever thread frees it.
- A buffer freed by one virtual thread is reused by the next virtual-thread allocation; a buffer freed while the virtual pool's queue already holds more than the cap is released.
- `undertow.bufferPool.virtualMaximumPoolSize` default is 4096 buffers.
- `getArrayBackedPool()` is the platform pool's array-backed pool; `close()` closes both pools.

## Server

- `BenchmarkApplication` is a `ProteusApplication` with one controller and no services beyond the defaults.
- Listens on `application.ports.http` from `proteus-benchmark/src/main/resources/application.conf`; default port is `21080` on all interfaces.
- `undertow.server.recordRequestStartTime` is `false`.
- Global headers are limited to `Server`.
- Logging level for `io.sinistral` is `WARN` during runs.

## Load Generator

- `oha` (Rust) over HTTP/1.1 with keep-alive.
- Fixed duration per run: `15s`. Concurrency: `256` by default, swept via `SWEEP_CONC`. Warm-up: one `5s` run discarded before each measured run.
- `BENCH_SERVER_CPUS` and `BENCH_LOAD_CPUS` pin the server JVM and `oha` with `taskset` so `availableProcessors()` reflects a smaller machine and the load generator does not share those cores.
- A JFR recording (`settings=profile`) is captured for the full server lifetime of each configuration; `jfr print --events jdk.ExecutionSample` is summarized to the top 25 leaf frames and top 25 frames by inclusive sample count in `profile.md`.
- Command shape: `oha -z 15s -c 256 --no-tui --output-format json -o OUT.json http://HOST:21080/PATH`.

## Parameter Sweep

- `sweep.sh` runs `run.sh` once per configuration and collates all `summary.md` tables into `results/sweep-<timestamp>/sweep.md`, ordered by plaintext/undertow req/s.
- Each configuration is expressed as `-D` system properties overriding `undertow.*` config paths; the server reads them through Typesafe Config's system-property override.
- Default grid: `ioThreadsMultiplier` ∈ {0.02, 0.25, 0.5, 1, 2} × available processors (rounded, min 1), `workerExecutor` ∈ {platform, virtual}, `workerThreadsMultiplier` ∈ {1, 10} (platform only), `directBuffers` ∈ {true, false}, `bufferSize` ∈ {16k, 64k}, `bufferPool.threadLocalCacheSize` ∈ {4, 0}, `socket.readTimeout` ∈ {90000, 0}. Non-product subsets are chosen by the operator via environment variables documented at the top of `sweep.sh`.

## Runner

- `proteus-benchmark/run.sh` builds the module, starts the server, waits for `/plaintext` to answer, runs the warm-up and measured passes for every route, stops the server, and writes results.
- Routes: `/plaintext/{exchange,response,blocking,worker,undertow}` and `/json/{exchange,response,blocking,worker,undertow}`.
- Output: `proteus-benchmark/results/<ISO-8601 UTC timestamp>/` containing one `oha` JSON file per route plus `summary.md` with a table of requests/s, p50, p99, and p99.9 latency, and `env.txt` with JDK version, CPU model, core count, Undertow version, git commit.

## Reporting

- `README.md` links the latest `summary.md` and states the host, JDK, and date beside the numbers.
- Numbers are not compared to other frameworks in repository docs; the result stands as a Proteus-only regression baseline.

## Verification

- `run.sh` exits non-zero if the server fails to start, any route returns a non-200 status during the measured pass, or `oha` reports any error other than `aborted due to deadline` (in-flight requests cut at the duration boundary, one per connection).
- `summary.md` records the error count column; a non-zero value invalidates the run.
