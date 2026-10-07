# Proteus benchmark sweep-20261007T184646Z/io0.05_platform_w1_directtrue_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 12.73 14.33 14.70, cpu idle 98.9%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 285,051 | 0.83 | 2.03 | 2.62 | 0 |
| `/plaintext/response` | 280,182 | 0.84 | 2.21 | 2.87 | 0 |
| `/plaintext/blocking` | 169,073 | 1.17 | 5.83 | 8.22 | 0 |
| `/plaintext/worker` | 245,123 | 0.93 | 3.14 | 5.57 | 0 |
| `/plaintext/undertow` | 276,521 | 0.85 | 2.08 | 4.93 | 0 |
| `/json/exchange` | 267,976 | 0.88 | 2.23 | 4.86 | 0 |
| `/json/response` | 271,944 | 0.87 | 2.12 | 4.71 | 0 |
| `/json/blocking` | 163,175 | 1.20 | 6.27 | 9.01 | 0 |
| `/json/worker` | 238,006 | 0.95 | 3.25 | 5.53 | 0 |
| `/json/undertow` | 301,461 | 0.75 | 2.14 | 4.72 | 0 |

CPU profile: [profile.md](profile.md)
