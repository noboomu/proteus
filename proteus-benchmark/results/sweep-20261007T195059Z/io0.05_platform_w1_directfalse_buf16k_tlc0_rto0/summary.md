# Proteus benchmark sweep-20261007T195059Z/io0.05_platform_w1_directfalse_buf16k_tlc0_rto0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=0 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 59.68 34.80 31.71, cpu idle 98.9%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 855,533 | 0.19 | 1.66 | 3.27 | 0 |
| `/plaintext/response` | 905,941 | 0.19 | 1.60 | 3.26 | 0 |
| `/plaintext/blocking` | 455,295 | 0.49 | 2.23 | 5.08 | 0 |
| `/plaintext/worker` | 669,177 | 0.33 | 1.15 | 4.41 | 0 |
| `/plaintext/undertow` | 925,067 | 0.18 | 1.63 | 3.77 | 0 |
| `/json/exchange` | 992,696 | 0.24 | 1.04 | 3.97 | 0 |
| `/json/response` | 977,690 | 0.23 | 1.12 | 3.96 | 0 |
| `/json/blocking` | 479,256 | 0.47 | 2.15 | 5.40 | 0 |
| `/json/worker` | 640,468 | 0.35 | 1.40 | 4.53 | 0 |
| `/json/undertow` | 921,111 | 0.19 | 1.60 | 3.83 | 0 |

CPU profile: [profile.md](profile.md)
