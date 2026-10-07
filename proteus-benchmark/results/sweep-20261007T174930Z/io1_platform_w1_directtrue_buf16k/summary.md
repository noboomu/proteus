# Proteus benchmark sweep-20261007T174930Z/io1_platform_w1_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 256 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 13.57 15.48 14.62, cpu idle 99.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 457,690 | 0.03 | 3.44 | 6.64 | 0 |
| `/plaintext/response` | 497,593 | 0.03 | 3.50 | 6.63 | 0 |
| `/plaintext/blocking` | 134,366 | 0.11 | 4.15 | 244.53 | 0 |
| `/plaintext/worker` | 368,763 | 0.03 | 4.08 | 6.05 | 0 |
| `/plaintext/undertow` | 501,244 | 0.02 | 3.70 | 5.60 | 0 |
| `/json/exchange` | 482,650 | 0.03 | 3.52 | 5.34 | 0 |
| `/json/response` | 480,481 | 0.03 | 3.65 | 5.50 | 0 |
| `/json/blocking` | 138,933 | 0.08 | 4.34 | 152.86 | 0 |
| `/json/worker` | 368,943 | 0.04 | 4.07 | 6.02 | 0 |
| `/json/undertow` | 512,598 | 0.02 | 3.55 | 5.35 | 0 |

CPU profile: [profile.md](profile.md)
