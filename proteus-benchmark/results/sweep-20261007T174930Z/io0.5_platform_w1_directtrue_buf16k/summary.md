# Proteus benchmark sweep-20261007T174930Z/io0.5_platform_w1_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 128 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.5 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 12.77 16.48 13.07, cpu idle 95.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 498,294 | 0.03 | 3.64 | 5.93 | 0 |
| `/plaintext/response` | 503,963 | 0.04 | 3.35 | 5.39 | 0 |
| `/plaintext/blocking` | 128,815 | 0.16 | 4.26 | 36.82 | 0 |
| `/plaintext/worker` | 356,590 | 0.04 | 3.50 | 6.24 | 0 |
| `/plaintext/undertow` | 498,831 | 0.03 | 3.40 | 5.46 | 0 |
| `/json/exchange` | 493,180 | 0.04 | 3.45 | 4.85 | 0 |
| `/json/response` | 490,814 | 0.04 | 3.60 | 5.02 | 0 |
| `/json/blocking` | 144,648 | 0.07 | 3.81 | 96.74 | 0 |
| `/json/worker` | 366,933 | 0.04 | 3.44 | 6.05 | 0 |
| `/json/undertow` | 520,152 | 0.03 | 3.60 | 5.02 | 0 |

CPU profile: [profile.md](profile.md)
