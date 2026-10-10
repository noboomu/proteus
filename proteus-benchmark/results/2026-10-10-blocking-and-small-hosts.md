# Proteus blocking handlers and small-host I/O threads, 2026-10-10

Host: attenborough, AMD EPYC 9755 (128 physical / 256 logical), idle. Server JVM pinned with `taskset` to the CPU count stated per table; `oha` pinned to CPUs 64 to 127 so the load generator never shares server cores. JDK 27, Undertow 2.3.17.Final, heap buffers, `threadLocalCacheSize=0`, 15s measured after 5s warm-up, commit `5f06186`.

## Blocking handlers: platform pool vs virtual threads

Server pinned to 16 CPUs, 4 I/O threads. `/sleep/worker` parks 10 ms on the XNIO task pool via `exchange.dispatch`; `/sleep/blocking` parks 10 ms on a per-request virtual thread via `@Blocking`. `/plaintext/worker` is the non-blocking control. Theoretical ceiling for a pool of N threads with a 10 ms wait is N × 100 req/s; the ceiling for C open connections is C × 100 req/s.

### Requests per second

| executor | pool | conns | sleep/worker | sleep/blocking | plaintext/worker |
|---|---:|---:|---:|---:|---:|
| platform | 16 | 256 | 1,592 | 25,276 | 700,014 |
| platform | 16 | 1024 | 1,589 | 100,836 | 647,165 |
| platform | 16 | 4096 | 1,589 | 387,258 | 649,937 |
| platform | 160 | 256 | 15,880 | 25,307 | 695,585 |
| platform | 160 | 1024 | 15,916 | 100,742 | 685,631 |
| platform | 160 | 4096 | 15,923 | 387,367 | 580,844 |
| platform | 1024 | 256 | 25,307 | 25,307 | 691,316 |
| platform | 1024 | 1024 | 100,920 | 101,067 | 677,478 |
| platform | 1024 | 4096 | 101,406 | 384,122 | 612,249 |
| **virtual** | per task | 256 | 25,293 | 25,315 | 696,773 |
| **virtual** | per task | 1024 | 101,128 | 101,003 | 699,075 |
| **virtual** | per task | 4096 | **394,375** | 383,724 | 497,375 |

### p99 latency, ms

| executor | pool | conns | sleep/worker | sleep/blocking | plaintext/worker |
|---|---:|---:|---:|---:|---:|
| platform | 16 | 256 | 162 | 10.7 | 0.81 |
| platform | 16 | 1024 | 1,057 | 10.9 | 3.8 |
| platform | 16 | 4096 | **4,924** | 17.8 | 12.5 |
| platform | 160 | 256 | 18.7 | 10.3 | 0.83 |
| platform | 160 | 1024 | 67.5 | 11.8 | 3.0 |
| platform | 160 | 4096 | 268 | 17.0 | 12.8 |
| platform | 1024 | 256 | 10.2 | 10.2 | 0.77 |
| platform | 1024 | 1024 | 11.0 | 10.4 | 2.3 |
| platform | 1024 | 4096 | 43.8 | 17.6 | 12.5 |
| **virtual** | per task | 256 | 10.6 | 10.2 | 0.74 |
| **virtual** | per task | 1024 | 10.4 | 11.5 | 2.4 |
| **virtual** | per task | 4096 | **15.0** | 17.3 | 14.1 |

### Reading

- A platform pool is a hard ceiling: 16 threads → 1,590 req/s, 160 → 15,900, 1,024 → 101,000, each exactly pool / 10 ms. Requests above the ceiling queue; p99 goes to seconds.
- Virtual threads track the connection ceiling instead: 256 → 25k, 1024 → 101k, 4096 → 394k, p99 stays at the 10 ms sleep plus a few ms. There is no pool to size.
- `@Blocking` already behaved this way (it was always `Thread::startVirtualThread`). `workerExecutor=virtual` extends it to every `exchange.dispatch(...)` call, including Undertow built-ins and third-party handlers that dispatch to the worker.
- A 1,024-thread platform pool matches VT at 1,024 connections but costs 1,024 parked platform threads (roughly 1 GB of reserved stacks at defaults) and still falls over at 4,096 connections (101k vs 394k, p99 44 ms vs 15 ms).
- Non-blocking cost of VT dispatch at 4,096 connections: `plaintext/worker` 497k vs 612k to 650k platform. VT scheduling has a per-task cost that shows when there is no wait to hide it. At 256 and 1,024 connections the two are equal.

### When VT provides an advantage

1. Handlers that block (JDBC, synchronous HTTP clients, file I/O) with more concurrent requests than the platform pool has threads. Throughput follows open connections, latency stays at the wait time.
2. Services that cannot predict their concurrency and would otherwise oversize the platform pool to be safe.
3. Mixed workloads where a few slow blocking endpoints would otherwise starve fast dispatched ones on a shared pool.

### When VT does not help

1. Routes completed on the I/O thread (`ServerResponse` return, raw exchange). They never dispatch; `workerExecutor` is irrelevant.
2. CPU-bound dispatched work. Platform and VT are equal up to ~1k connections, platform wins slightly beyond.
3. Any handler holding a `synchronized` block across the blocking call on JDKs before 24 (pinning). JDK 27 has JEP 491, so this host did not pin; older runtimes would.

### Recommendation

`workerExecutor=platform` stays the reference default so an upgrade changes nothing. Applications with blocking dispatched handlers should set `undertow.workerExecutor=virtual` and can stop tuning `workerThreadsMultiplier`.

## Small-host I/O thread sizing

Server pinned to 8 and 4 CPUs, platform pool, non-blocking routes plus `@Blocking` plaintext.

| server CPUs | I/O threads | plaintext/response | json/response | plaintext/worker | plaintext/blocking |
|---:|---:|---:|---:|---:|---:|
| 8 | 1 | 110,634 | 110,541 | 273,817 | 263,191 |
| 8 | 2 | 217,840 | 214,237 | 474,674 | 457,593 |
| 8 | 4 | 409,307 | 405,041 | 454,620 | 379,633 |
| 8 | 8 | **669,159** | **665,603** | 375,178 | 220,790 |
| 4 | 1 | 111,230 | 110,720 | 257,072 | 280,785 |
| 4 | 2 | 218,683 | 217,904 | 267,854 | 204,208 |
| 4 | 4 | **411,611** | **405,361** | 253,859 | 143,452 |

- Each I/O thread serves ~110k req/s of routes that complete on it. Throughput is linear in I/O threads up to one per CPU. The `0.25` default I committed on 2026-10-07 was wrong for small hosts: 2 of 8 CPUs would have left 75% of the box idle on the common path. Reverted to `1`.
- Routes that dispatch (`worker`, `blocking`) prefer fewer I/O threads because the dispatched work needs cores too. On 8 CPUs, `@Blocking` peaks at 2 I/O threads (458k) and drops to 221k at 8, when I/O threads and VT carriers compete for the same cores.
- The 256-logical-CPU result that favored 13 to 26 I/O threads is consistent with this: a single `oha` instance cannot generate more than ~1M req/s, so past ~10 threads the server is waiting on the load generator, and extra I/O threads only add wake-up and cache-line cost.

Default `ioThreadsMultiplier=1`. Deployments whose routes are mostly `@Blocking` or worker-dispatched should try 0.25 to 0.5.

## Directories

- `sweep-20261010T194005Z`: 8-CPU pinned, I/O threads 1/2/4/8
- `sweep-20261010T194553Z`: 4-CPU pinned, I/O threads 1/2/4
- `sweep-20261010T195009Z`: 16-CPU pinned, blocking, platform 16/160/1024 vs virtual, 256/1024/4096 connections
