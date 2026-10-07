# Proteus benchmark sweep-20261007T174930Z/io0.02_platform_w1_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 5 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.02 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 4.22 3.20 2.94, cpu idle 98.5%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 589,054 | 0.42 | 0.64 | 1.12 | 0 |
| `/plaintext/response` | 568,897 | 0.44 | 0.62 | 1.19 | 0 |
| `/plaintext/blocking` | 169,972 | 0.09 | 3.74 | 150.62 | 0 |
| `/plaintext/worker` | 632,751 | 0.37 | 0.83 | 1.10 | 0 |
| `/plaintext/undertow` | 629,720 | 0.41 | 0.55 | 0.89 | 0 |
| `/json/exchange` | 516,257 | 0.50 | 0.61 | 1.01 | 0 |
| `/json/response` | 499,603 | 0.52 | 0.71 | 1.10 | 0 |
| `/json/blocking` | 149,317 | 0.08 | 6.60 | 176.23 | 0 |
| `/json/worker` | 427,404 | 0.57 | 1.23 | 1.53 | 0 |
| `/json/undertow` | 596,264 | 0.44 | 0.58 | 0.87 | 0 |

CPU profile: [profile.md](profile.md)
