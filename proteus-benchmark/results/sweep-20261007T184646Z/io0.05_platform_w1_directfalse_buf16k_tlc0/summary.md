# Proteus benchmark sweep-20261007T184646Z/io0.05_platform_w1_directfalse_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 13.61 15.43 15.01, cpu idle 98.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 979,392 | 0.21 | 1.39 | 3.36 | 0 |
| `/plaintext/response` | 932,501 | 0.21 | 1.46 | 3.27 | 0 |
| `/plaintext/blocking` | 505,701 | 0.45 | 1.92 | 4.90 | 0 |
| `/plaintext/worker` | 655,106 | 0.34 | 1.16 | 4.41 | 0 |
| `/plaintext/undertow` | 1,030,212 | 0.20 | 1.29 | 3.82 | 0 |
| `/json/exchange` | 955,770 | 0.23 | 0.96 | 3.83 | 0 |
| `/json/response` | 946,953 | 0.24 | 0.93 | 3.86 | 0 |
| `/json/blocking` | 492,643 | 0.47 | 1.91 | 5.30 | 0 |
| `/json/worker` | 567,915 | 0.37 | 1.51 | 4.70 | 0 |
| `/json/undertow` | 969,808 | 0.21 | 1.42 | 3.89 | 0 |

CPU profile: [profile.md](profile.md)
