# Proteus benchmark sweep-20261007T184646Z/io0.02_platform_w1_directtrue_buf16k_tlc4

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 5 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.02 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=4 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 3.59 12.99 15.69, cpu idle 98.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 594,394 | 0.42 | 0.64 | 1.16 | 0 |
| `/plaintext/response` | 580,582 | 0.44 | 0.60 | 1.10 | 0 |
| `/plaintext/blocking` | 151,270 | 0.11 | 3.78 | 59.90 | 0 |
| `/plaintext/worker` | 408,639 | 0.60 | 1.28 | 1.67 | 0 |
| `/plaintext/undertow` | 550,485 | 0.46 | 0.57 | 0.94 | 0 |
| `/json/exchange` | 432,171 | 0.52 | 0.67 | 1.11 | 0 |
| `/json/response` | 477,362 | 0.54 | 0.70 | 1.01 | 0 |
| `/json/blocking` | 146,592 | 0.09 | 4.14 | 86.97 | 0 |
| `/json/worker` | 394,883 | 0.60 | 1.29 | 1.67 | 0 |
| `/json/undertow` | 557,490 | 0.46 | 0.60 | 1.01 | 0 |

CPU profile: [profile.md](profile.md)
