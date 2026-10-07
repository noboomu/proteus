# Proteus HTTP Benchmark Specification

## Purpose

- Provide a reproducible throughput and latency measurement of the Proteus request path on the current JDK and Undertow line.
- Replace the unverifiable 2017 TechEmpower claim with a locally reproducible result recorded in the repository.

## Scope

- Module: `proteus-benchmark`, reactor member, not published to Maven Central.
- Workloads mirror the TechEmpower `plaintext` and `json` tests:
  - `GET /plaintext` returns `Hello, World!` as `text/plain`.
  - `GET /json` returns `{"message":"Hello, World!"}` as `application/json` serialized by the application `ObjectMapper`.
- Three handler styles are measured for each workload so the cost of each abstraction layer is visible:
  - `exchange`: controller method takes `HttpServerExchange` and completes it directly.
  - `response`: controller method returns `ServerResponse<T>`.
  - `undertow`: a hand-written `HttpHandler` registered on the same router, as the floor.

## Server

- `BenchmarkApplication` is a `ProteusApplication` with one controller and no services beyond the defaults.
- Listens on `application.ports.http` from `proteus-benchmark/src/main/resources/application.conf`; default port is `21080` on all interfaces.
- `undertow.server.recordRequestStartTime` is `false`.
- Global headers are limited to `Server`.
- Logging level for `io.sinistral` is `WARN` during runs.

## Load Generator

- `oha` (Rust) over HTTP/1.1 with keep-alive.
- Fixed duration per run: `15s`. Concurrency: `256`. Warm-up: one `5s` run discarded before each measured run.
- Command shape: `oha -z 15s -c 256 --no-tui --output-format json -o OUT.json http://HOST:21080/PATH`.

## Runner

- `proteus-benchmark/run.sh` builds the module, starts the server, waits for `/plaintext` to answer, runs the warm-up and measured passes for every route, stops the server, and writes results.
- Routes: `/plaintext/exchange`, `/plaintext/response`, `/plaintext/undertow`, `/json/exchange`, `/json/response`, `/json/undertow`.
- Output: `proteus-benchmark/results/<ISO-8601 UTC timestamp>/` containing one `oha` JSON file per route plus `summary.md` with a table of requests/s, p50, p99, and p99.9 latency, and `env.txt` with JDK version, CPU model, core count, Undertow version, git commit.

## Reporting

- `README.md` links the latest `summary.md` and states the host, JDK, and date beside the numbers.
- Numbers are not compared to other frameworks in repository docs; the result stands as a Proteus-only regression baseline.

## Verification

- `run.sh` exits non-zero if the server fails to start, any route returns a non-200 status during the measured pass, or `oha` reports any error other than `aborted due to deadline` (in-flight requests cut at the duration boundary, one per connection).
- `summary.md` records the error count column; a non-zero value invalidates the run.
