# Proteus benchmark sweep-20261007T174930Z/io2_platform_w10_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 512 I/O threads, platform task executor with 2560 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=2 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 13.84 17.33 16.57, cpu idle 98.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 472,571 | 0.02 | 4.48 | 6.82 | 0 |
| `/plaintext/response` | 477,653 | 0.02 | 4.33 | 6.63 | 0 |
| `/plaintext/blocking` | 136,904 | 0.11 | 4.39 | 39.99 | 0 |
| `/plaintext/worker` | 365,951 | 0.03 | 4.48 | 6.60 | 0 |
| `/plaintext/undertow` | 486,509 | 0.02 | 4.37 | 6.54 | 0 |
| `/json/exchange` | 468,508 | 0.02 | 4.17 | 6.26 | 0 |
| `/json/response` | 468,205 | 0.02 | 4.23 | 6.34 | 0 |
| `/json/blocking` | 145,927 | 0.06 | 3.95 | 56.68 | 0 |
| `/json/worker` | 368,907 | 0.03 | 4.39 | 6.46 | 0 |
| `/json/undertow` | 498,114 | 0.02 | 4.41 | 6.60 | 0 |

CPU profile: [profile.md](profile.md)
