# Proteus benchmark sweep-20261007T174930Z/io0.25_platform_w10_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 64 I/O threads, platform task executor with 2560 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 13.71 14.37 10.21, cpu idle 98.9%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 528,166 | 0.09 | 2.57 | 4.16 | 0 |
| `/plaintext/response` | 533,096 | 0.08 | 2.62 | 3.93 | 0 |
| `/plaintext/blocking` | 137,451 | 0.11 | 4.32 | 76.09 | 0 |
| `/plaintext/worker` | 397,506 | 0.05 | 3.46 | 4.79 | 0 |
| `/plaintext/undertow` | 555,268 | 0.08 | 2.54 | 3.77 | 0 |
| `/json/exchange` | 529,497 | 0.10 | 2.52 | 3.77 | 0 |
| `/json/response` | 533,053 | 0.11 | 2.43 | 3.55 | 0 |
| `/json/blocking` | 148,490 | 0.06 | 3.95 | 69.37 | 0 |
| `/json/worker` | 396,432 | 0.05 | 3.42 | 4.70 | 0 |
| `/json/undertow` | 564,369 | 0.08 | 2.40 | 3.51 | 0 |

CPU profile: [profile.md](profile.md)
