# Pipelining, buffer pool, and GC on attenborough, 2026-10-18

Branch `feature/http1-frontend`. Host attenborough (AMD EPYC, 256 logical CPUs). Server pinned to 8 physical cores (`0-7,128-135`, 16 logical), load generator on `64-127`. JDK 27, Undertow 2.4.4.Final. The idle gate measured the pinned CPUs at 96 to 99% idle before each run; the rest of the host carried unrelated jobs (load average around 11 on 256 CPUs).

## 1. Buffer pool discarded every freed buffer

async-profiler 4.5 (`--cstack vm`, `-XX:+DebugNonSafepoints`) on the json route put 17% of server CPU in `HeapByteBuffer.<init>` under `DefaultByteBufferPool.allocate`. With `maximumPoolSize=-1`, `queueIfUnderMax` treats every free as over the cap and drops it. Undertow's default relies on its per-thread cache to catch frees; the 2026-10-07 sweep disabled that cache (`threadLocalCacheSize=0`), so every request allocated and zeroed a fresh 16k buffer.

Fix: `undertow.bufferPool.maximumPoolSize` (0 derives the cap from `maximumPoolBytes=64M` / `bufferSize`). Also on `development`.

Front-end A/B with the fix, oha, 256 connections, 4 alternating rounds, median [min-max] req/s:

| front end | plaintext/response | json/response | json/worker | plaintext/blocking |
|---|---:|---:|---:|---:|
| undertow | 745,303 [744k-751k] | 760,889 [680k-769k] | 557,501 [547k-565k] | 503,900 [500k-510k] |
| proteus | 943,382 [917k-964k] | 964,078 [929k-999k] | 682,490 [607k-687k] | 613,895 [507k-624k] |
| delta | +26.6% | +26.7% | +22.4% | +21.8% |

Directory: `attenborough/ab-frontend-8core/`.

## 2. Pipelining: the Proteus codec lost, then won

TechEmpower's plaintext test pipelines 16 requests per write. `oha` cannot pipeline, so this used `wrk` (wg/wrk `a211dd5`, built on the host) with a `pipeline.lua` that concatenates 16 requests, `-t 32`, the TFB `Accept` header, 5 s warm-up and 10 s measured per connection count, 3 alternating rounds.

Before (`attenborough/pipelined-8core-before/`), median [min-max]:

| route | conns | proteus | undertow | delta |
|---|---:|---:|---:|---:|
| /plaintext/exchange | 256 | 1.30M [1.30-1.31] | 1.44M [1.42-1.46] | -10% |
| /plaintext/exchange | 1024 | 1.29M [1.28-1.30] | 1.46M [1.44-1.48] | -12% |
| /plaintext/exchange | 4096 | 1.27M [1.27-1.29] | 1.41M [1.39-1.43] | -10% |
| /plaintext/response | 256 | 1.19M [1.19-1.20] | 1.33M [1.32-1.34] | -11% |
| /plaintext/response | 1024 | 1.17M [1.17-1.18] | 1.33M [1.32-1.34] | -12% |
| /plaintext/response | 4096 | 1.15M [1.15-1.16] | 1.27M [1.26-1.29] | -9% |

async-profiler under the same load: 65% of Proteus server CPU inside `writev`, called once per response. Undertow's listener runs with `BUFFER_PIPELINED_DATA` (its builder forces it on) and spent 8% in `write`. The Proteus codec had no response batching. (The pre-fix Proteus stacks were overwritten by the post-fix profile; the 65% figure is from the console output of that run. The Undertow stacks in `attenborough/pipeline-prof/` are from that run.)

Change: the Proteus connection now layers its response conduit over Undertow's own `PipeliningBufferingStreamSinkConduit` once a bodiless request has further pipelined bytes behind it, and flushes before reads resume. New key `undertow.server.bufferPipelinedData` (default true) applies to both front ends. Spec: `specs/proteus_http1.md`, "Pipelined response buffering".

After (`attenborough/pipelined-8core-buffered/`), zero errors in every cell:

| route | conns | proteus | p99 ms | undertow | p99 ms | delta |
|---|---:|---:|---:|---:|---:|---:|
| /plaintext/exchange | 256 | 2.41M [2.40-2.45] | 3.0 | 1.43M [1.41-1.44] | 4.6 | +69% |
| /plaintext/exchange | 1024 | 2.45M [2.43-2.49] | 8.0 | 1.44M [1.42-1.45] | 13.4 | +70% |
| /plaintext/exchange | 4096 | 2.38M [2.37-2.42] | 32.0 | 1.41M [1.38-1.41] | 50.2 | +69% |
| /plaintext/response | 256 | 2.09M [2.08-2.12] | 2.8 | 1.31M [1.30-1.33] | 4.4 | +60% |
| /plaintext/response | 1024 | 2.09M [2.07-2.11] | 9.2 | 1.31M [1.30-1.32] | 14.4 | +60% |
| /plaintext/response | 4096 | 2.03M [2.01-2.04] | 37.7 | 1.27M [1.25-1.28] | 56.1 | +60% |

The Proteus codec went from 1.30M to 2.41M on the same route. Partial attribution from async-profiler on both buffered codecs under the same depth-16 load, 1024 connections, no `-H` headers (`attenborough/pipeline-prof/`, wrk during profiling: proteus 3.22M, undertow 1.96M req/s): in Undertow's profile, `java.util.regex` frames under `HostHeaderHandler` are 23.4% of server CPU, absent from the Proteus profile. The rest of the gap is not attributed.

Correctness found along the way: with buffering on, Undertow 2.4.4's listener does not flush buffered responses when the next request is incomplete. Two complete requests plus half of a third get no response until the read timeout. The Proteus codec flushes before waiting; `Http1ConformanceTest.bufferedPipelinedResponsesFlushBeforeWaitingOnPartialRequest` covers it (proteus only; it fails on the Undertow front end with buffering on and passes with it off).

Without pipelining, the buffer is never engaged. Same jar, proteus front end, `bufferPipelinedData` true vs false, 3 alternating rounds, oha 256 connections (`attenborough/nopipe-8core/`):

| bufferPipelinedData | plaintext/response | json/response | json/worker | plaintext/blocking |
|---|---:|---:|---:|---:|
| true | 893,429 [886k-901k] | 944,955 [920k-954k] | 679,943 [591k-680k] | 610,483 [528k-619k] |
| false | 905,726 [891k-908k] | 902,156 [887k-972k] | 682,764 [546k-687k] | 615,294 [538k-625k] |

Ranges overlap on every route: no measurable effect.

## 3. GC choice is inside the noise at this heap pressure

Proteus front end, 4 alternating rounds per collector, oha 256 connections (`attenborough/gc-8core/`). `2g` = `-Xms2g -Xmx2g -XX:+AlwaysPreTouch`. Compact object headers are on by default on JDK 27 in every run.

| collector | plaintext/response | json/response | json/worker | plaintext/blocking |
|---|---:|---:|---:|---:|
| G1 default heap | 876,603 [826k-950k] | 895,002 [894k-915k] | 634,132 [575k-685k] | 580,932 [495k-629k] |
| G1 2g | 795,241 [729k-894k] | 862,962 [777k-889k] | 685,099 [603k-703k] | 620,689 [536k-623k] |
| Parallel + UseNUMA 2g | 875,897 [795k-897k] | 878,809 [821k-961k] | 697,658 [696k-709k] | 632,700 [626k-647k] |
| ZGC 2g | 808,250 [733k-873k] | 801,120 [653k-821k] | 653,977 [643k-664k] | 592,784 [584k-610k] |

Ranges overlap almost everywhere. ParallelGC + NUMA has the tightest ranges and the best medians on the dispatched routes; ZGC is lowest on the I/O-thread routes. With the buffer pool retaining, per-request allocation is small enough that the collector does not separate configurations at 8 cores. No default changed. TFB's top Java entries ran ParallelGC + UseNUMA with a fixed 2G heap; that choice is not penalized here, but not shown to help either.

## Open

- Attribute the remaining pipelined gap beyond the 23% `HostHeaderHandler` regex share.
- Full-host run with load from a second machine over the LAN.
- The GC result is at one connection count and one heap size; json at 4096 connections might separate them.
