# Proteus benchmark sweep-20261007T191947Z/io0.05_platform_w10_directfalse_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, platform task executor with 2560 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 20.41 18.22 17.03, cpu idle 99.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 898,703 | 0.20 | 1.57 | 3.34 | 0 |
| `/plaintext/response` | 976,635 | 0.20 | 1.42 | 3.34 | 0 |
| `/plaintext/blocking` | 514,146 | 0.44 | 1.88 | 4.90 | 0 |
| `/plaintext/worker` | 669,381 | 0.34 | 1.02 | 4.53 | 0 |
| `/plaintext/undertow` | 937,461 | 0.20 | 1.52 | 3.82 | 0 |
| `/json/exchange` | 926,206 | 0.25 | 1.19 | 3.96 | 0 |
| `/json/response` | 948,503 | 0.25 | 0.96 | 4.01 | 0 |
| `/json/blocking` | 442,329 | 0.51 | 2.20 | 5.42 | 0 |
| `/json/worker` | 593,147 | 0.37 | 1.43 | 4.57 | 0 |
| `/json/undertow` | 923,917 | 0.19 | 1.55 | 3.95 | 0 |

CPU profile: [profile.md](profile.md)
