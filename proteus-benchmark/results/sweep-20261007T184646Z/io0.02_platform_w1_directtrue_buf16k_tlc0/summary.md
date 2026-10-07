# Proteus benchmark sweep-20261007T184646Z/io0.02_platform_w1_directtrue_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 5 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.02 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 13.49 13.73 15.43, cpu idle 98.4%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 305,714 | 0.82 | 1.18 | 2.19 | 0 |
| `/plaintext/response` | 306,288 | 0.82 | 1.21 | 2.24 | 0 |
| `/plaintext/blocking` | 171,299 | 1.34 | 4.18 | 5.98 | 0 |
| `/plaintext/worker` | 240,363 | 1.00 | 2.31 | 5.03 | 0 |
| `/plaintext/undertow` | 307,693 | 0.80 | 1.18 | 4.26 | 0 |
| `/json/exchange` | 309,573 | 0.81 | 1.06 | 4.23 | 0 |
| `/json/response` | 310,970 | 0.83 | 1.07 | 4.18 | 0 |
| `/json/blocking` | 167,985 | 1.37 | 4.27 | 6.68 | 0 |
| `/json/worker` | 237,971 | 1.01 | 2.31 | 5.01 | 0 |
| `/json/undertow` | 307,341 | 0.83 | 0.98 | 4.19 | 0 |

CPU profile: [profile.md](profile.md)
