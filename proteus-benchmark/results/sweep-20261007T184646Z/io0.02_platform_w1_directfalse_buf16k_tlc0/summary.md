# Proteus benchmark sweep-20261007T184646Z/io0.02_platform_w1_directfalse_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 5 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.02 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 13.21 13.63 14.59, cpu idle 98.8%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 530,180 | 0.47 | 0.65 | 3.65 | 0 |
| `/plaintext/response` | 519,355 | 0.43 | 0.81 | 3.62 | 0 |
| `/plaintext/blocking` | 714,902 | 0.33 | 0.75 | 4.26 | 0 |
| `/plaintext/worker` | 708,635 | 0.35 | 0.79 | 4.36 | 0 |
| `/plaintext/undertow` | 530,416 | 0.49 | 0.67 | 4.18 | 0 |
| `/json/exchange` | 450,908 | 0.56 | 0.69 | 4.00 | 0 |
| `/json/response` | 449,593 | 0.53 | 0.80 | 4.24 | 0 |
| `/json/blocking` | 594,267 | 0.39 | 0.95 | 4.65 | 0 |
| `/json/worker` | 715,005 | 0.33 | 0.71 | 4.40 | 0 |
| `/json/undertow` | 530,332 | 0.47 | 0.60 | 3.94 | 0 |

CPU profile: [profile.md](profile.md)
