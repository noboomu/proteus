# Where the CPU goes, and what is left to tune, 2026-10-11

Host: nautilus, Intel i7-7820X, server pinned to 4 physical cores (8 threads), `oha` pinned to the other 4, host 90% idle. Three measured passes per route; min and max within 1 to 2% of median, so differences under ~3% are noise. JDK 27, Undertow 2.3.17.Final, heap buffers, `threadLocalCacheSize=0`, 8 I/O threads, platform pool.

Baseline: 366k req/s `/plaintext/response`, 353k `/json/response`, 371k `/plaintext/undertow`.

## Attribution (JFR execution samples, leaf frame walked up to first non-JDK owner)

| owner | share |
|---|---:|
| Undertow + XNIO | 88.6% |
| JDK (socket read/write, StringBuilder, Long.toString) | 5.5% |
| Jackson 3 | 5.2% |
| Proteus (generated handler, `ServerRequest`, `ServerResponse.send`) | **0.7%** |

The Proteus request path is: construct `ServerRequest`, call the controller, `ServerResponse.send`. It does not block and allocates one response object. There is nothing left to remove from it.

## Undertow's share, by frame

| self % | frame | what it is |
|---:|---|---|
| 23.1 | `HeaderMap.fastIterateNonEmpty` | linear scan of a 16-slot hash table to find 4 response headers (Server, Content-Type, Content-Length, Date) when writing the status line |
| 11.4 | `HttpReadListener.handleEventWithNoRunningRequest` | keep-alive re-arm; allocates a new `HttpServerExchange` and two `HeaderMap`s per request |
| 6.1 | `HttpResponseConduit.processWrite` | header bytes into the write buffer |
| 3.7 + 1.3 | `AbstractStringBuilder.needsNewBuffer` / `append` | `HttpRequestParser.handle`, `handlePath`, `handleHeader` building `String`s for path and header values |
| 3.3 + 2.3 + 1.9 | `HttpString.bytesAreEquivalent` / `bytesAreEqual` / `calcHashCode` | header-name interning on parse |
| 3.0 | `DecimalDigits.uncheckedGetCharsLatin1` | `Long.toString(REQUEST_ID_GENERATOR.incrementAndGet())` in `HttpServerExchange.<init>`; a request-ID string allocated per request and never read on this path |
| 2.0 + 1.6 | `ReadTimeoutStreamSourceConduit.read` + `TreeMap.put` | read-timeout timer rescheduled per request |
| 1.8 | `GracefulShutdownHandler.handleRequest` | atomic in-flight counter |

Roughly half of all CPU is Undertow's HTTP/1.1 codec, and the three largest items are design choices inside it: the header table layout, per-request exchange allocation, and the request-ID string.

## Things tried this round

| change | plaintext/response | json/response | verdict |
|---|---:|---:|---|
| baseline | 364,868 | 359,915 | |
| `socket.readTimeout=0` | 358,734 | 347,354 | within noise, no gain; the timer cost shrank once the buffer pool was fixed |
| `gracefulShutdown=false` | 321,428 | 322,062 | **slower**, outside noise, three passes agree; cause not identified, not adopted |
| Jackson `recyclerPool=threadLocal` (new default) | | 375,123 | **+6%** on I/O-thread serialization |
| Jackson `recyclerPool=concurrentDeque`, virtual executor | | 336,748 | threadLocal on virtual: 358,166; VT routes (`json/blocking`, `json/worker`) favor concurrentDeque by 5 to 8% because each virtual thread misses the thread-local cache |

Jackson microbenchmark (2M `writeValueAsBytes` of a one-field record, pinned 4 cores): threadLocal 80 ns, concurrentDeque 102 ns, bounded 101 ns, nonRecycling 646 ns.

`CharTypes$AltQuoteEscapes.altEscapesFor` appears at 1.5% self in JFR but every frame is marked inlined and reflection on a live generator shows `_quoteChar == '"'` and the standard escape table. That attribution is JIT debug-info aliasing inside the inlined `UTF8JsonGenerator` constructor; the real cost is generator construction, which Jackson 3 does per call and which is not reachable from Proteus.

## Where the remaining headroom is

With Undertow 2.3.17's codec, the ceiling on this host is roughly where it sits. Everything tunable from configuration has been swept. The next 1.5 to 2× is inside the codec: a header map sized to the response, no per-request exchange reallocation on keep-alive, no request-ID string, byte-slice header values instead of `String`. That is either a patched Undertow or a replacement HTTP/1.1 front end that still produces `HttpServerExchange` for handlers. See `specs/proteus_codec.md` once written.

## Directories

- `nautilus/noise`: baseline, 3 repeats
- `nautilus/sweep-20261011T213545Z`: readTimeout × gracefulShutdown
- `nautilus/sweep-20261011T215129Z`: recyclerPool=threadLocal, platform and virtual
- `nautilus/sweep-20261011T215534Z`: recyclerPool=concurrentDeque, virtual only (platform pass stopped before completion)
