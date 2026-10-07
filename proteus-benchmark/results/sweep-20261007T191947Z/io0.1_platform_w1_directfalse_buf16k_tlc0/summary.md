# Proteus benchmark sweep-20261007T191947Z/io0.1_platform_w1_directfalse_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 26 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 32.07 23.74 19.33, cpu idle 99.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 825,987 | 0.25 | 1.38 | 3.44 | 0 |
| `/plaintext/response` | 883,642 | 0.23 | 1.19 | 3.47 | 0 |
| `/plaintext/blocking` | 451,555 | 0.09 | 3.03 | 5.58 | 0 |
| `/plaintext/worker` | 524,637 | 0.44 | 1.65 | 4.76 | 0 |
| `/plaintext/undertow` | 891,466 | 0.24 | 1.18 | 3.89 | 0 |
| `/json/exchange` | 811,212 | 0.24 | 1.58 | 3.98 | 0 |
| `/json/response` | 834,756 | 0.22 | 1.58 | 3.99 | 0 |
| `/json/blocking` | 455,474 | 0.08 | 3.09 | 6.04 | 0 |
| `/json/worker` | 474,749 | 0.46 | 1.70 | 4.96 | 0 |
| `/json/undertow` | 846,680 | 0.24 | 1.41 | 3.97 | 0 |

CPU profile: [profile.md](profile.md)
