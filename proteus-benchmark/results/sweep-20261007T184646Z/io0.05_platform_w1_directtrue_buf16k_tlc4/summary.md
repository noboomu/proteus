# Proteus benchmark sweep-20261007T184646Z/io0.05_platform_w1_directtrue_buf16k_tlc4

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=4 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 15.10 14.77 14.84, cpu idle 99.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 497,172 | 0.48 | 1.15 | 1.51 | 0 |
| `/plaintext/response` | 472,522 | 0.51 | 1.15 | 1.52 | 0 |
| `/plaintext/blocking` | 144,507 | 0.09 | 3.95 | 40.98 | 0 |
| `/plaintext/worker` | 390,904 | 0.58 | 1.95 | 2.73 | 0 |
| `/plaintext/undertow` | 472,903 | 0.50 | 1.21 | 1.53 | 0 |
| `/json/exchange` | 467,361 | 0.51 | 1.14 | 1.37 | 0 |
| `/json/response` | 463,081 | 0.52 | 1.14 | 1.40 | 0 |
| `/json/blocking` | 138,026 | 0.08 | 3.81 | 39.24 | 0 |
| `/json/worker` | 392,318 | 0.58 | 1.96 | 2.71 | 0 |
| `/json/undertow` | 477,330 | 0.50 | 1.16 | 1.44 | 0 |

CPU profile: [profile.md](profile.md)
