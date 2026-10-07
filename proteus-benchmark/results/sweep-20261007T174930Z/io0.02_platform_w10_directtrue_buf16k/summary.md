# Proteus benchmark sweep-20261007T174930Z/io0.02_platform_w10_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 5 I/O threads, platform task executor with 2560 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.02 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 12.91 8.65 5.22, cpu idle 98.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 547,616 | 0.45 | 0.73 | 1.23 | 0 |
| `/plaintext/response` | 533,307 | 0.48 | 0.65 | 1.18 | 0 |
| `/plaintext/blocking` | 145,493 | 0.11 | 3.84 | 76.87 | 0 |
| `/plaintext/worker` | 509,420 | 0.48 | 1.02 | 1.30 | 0 |
| `/plaintext/undertow` | 546,294 | 0.46 | 0.65 | 0.85 | 0 |
| `/json/exchange` | 455,315 | 0.48 | 0.72 | 1.00 | 0 |
| `/json/response` | 474,010 | 0.52 | 0.70 | 0.99 | 0 |
| `/json/blocking` | 158,974 | 0.08 | 5.04 | 75.09 | 0 |
| `/json/worker` | 499,904 | 0.48 | 1.05 | 1.35 | 0 |
| `/json/undertow` | 557,678 | 0.46 | 0.62 | 0.87 | 0 |

CPU profile: [profile.md](profile.md)
