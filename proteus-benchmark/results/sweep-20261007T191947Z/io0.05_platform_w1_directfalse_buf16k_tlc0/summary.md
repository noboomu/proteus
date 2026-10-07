# Proteus benchmark sweep-20261007T191947Z/io0.05_platform_w1_directfalse_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 3.06 13.75 15.56, cpu idle 99.3%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 934,931 | 0.20 | 1.53 | 3.36 | 0 |
| `/plaintext/response` | 967,886 | 0.20 | 1.44 | 3.29 | 0 |
| `/plaintext/blocking` | 502,884 | 0.45 | 1.87 | 4.88 | 0 |
| `/plaintext/worker` | 663,199 | 0.34 | 1.13 | 4.51 | 0 |
| `/plaintext/undertow` | 913,699 | 0.20 | 1.58 | 3.74 | 0 |
| `/json/exchange` | 926,904 | 0.26 | 1.17 | 3.89 | 0 |
| `/json/response` | 972,302 | 0.25 | 0.73 | 3.95 | 0 |
| `/json/blocking` | 446,831 | 0.50 | 2.14 | 5.40 | 0 |
| `/json/worker` | 595,632 | 0.37 | 1.40 | 4.54 | 0 |
| `/json/undertow` | 988,442 | 0.20 | 1.41 | 3.85 | 0 |

CPU profile: [profile.md](profile.md)
