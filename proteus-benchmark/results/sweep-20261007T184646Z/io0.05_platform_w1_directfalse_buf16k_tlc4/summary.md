# Proteus benchmark sweep-20261007T184646Z/io0.05_platform_w1_directfalse_buf16k_tlc4

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=4 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 13.94 13.36 14.18, cpu idle 98.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 501,131 | 0.48 | 1.12 | 1.47 | 0 |
| `/plaintext/response` | 501,385 | 0.47 | 1.13 | 1.51 | 0 |
| `/plaintext/blocking` | 145,254 | 0.06 | 4.24 | 87.23 | 0 |
| `/plaintext/worker` | 404,792 | 0.56 | 1.85 | 2.65 | 0 |
| `/plaintext/undertow` | 514,429 | 0.46 | 1.11 | 1.38 | 0 |
| `/json/exchange` | 503,571 | 0.48 | 1.01 | 1.25 | 0 |
| `/json/response` | 483,601 | 0.51 | 1.05 | 1.62 | 0 |
| `/json/blocking` | 139,147 | 0.07 | 3.89 | 92.83 | 0 |
| `/json/worker` | 398,361 | 0.57 | 1.95 | 2.81 | 0 |
| `/json/undertow` | 501,300 | 0.47 | 1.17 | 1.48 | 0 |

CPU profile: [profile.md](profile.md)
