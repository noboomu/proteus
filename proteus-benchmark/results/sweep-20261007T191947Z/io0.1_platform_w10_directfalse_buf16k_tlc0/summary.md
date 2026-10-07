# Proteus benchmark sweep-20261007T191947Z/io0.1_platform_w10_directfalse_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 26 I/O threads, platform task executor with 2560 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 27.82 29.32 22.83, cpu idle 98.9%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 877,275 | 0.24 | 1.23 | 3.45 | 0 |
| `/plaintext/response` | 844,172 | 0.25 | 1.17 | 3.49 | 0 |
| `/plaintext/blocking` | 474,574 | 0.09 | 2.97 | 5.76 | 0 |
| `/plaintext/worker` | 484,130 | 0.48 | 1.55 | 4.95 | 0 |
| `/plaintext/undertow` | 880,673 | 0.24 | 1.22 | 4.09 | 0 |
| `/json/exchange` | 852,418 | 0.24 | 1.40 | 4.07 | 0 |
| `/json/response` | 814,029 | 0.24 | 1.46 | 4.01 | 0 |
| `/json/blocking` | 441,995 | 0.09 | 3.02 | 6.21 | 0 |
| `/json/worker` | 490,622 | 0.47 | 1.55 | 5.15 | 0 |
| `/json/undertow` | 839,662 | 0.24 | 1.32 | 4.08 | 0 |

CPU profile: [profile.md](profile.md)
