# Proteus benchmark sweep-20261007T174930Z/io1_platform_w10_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 256 I/O threads, platform task executor with 2560 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 15.31 15.40 14.76, cpu idle 98.8%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 464,087 | 0.03 | 3.54 | 6.77 | 0 |
| `/plaintext/response` | 466,511 | 0.03 | 3.47 | 6.42 | 0 |
| `/plaintext/blocking` | 129,279 | 0.11 | 4.27 | 111.64 | 0 |
| `/plaintext/worker` | 357,445 | 0.03 | 3.94 | 5.89 | 0 |
| `/plaintext/undertow` | 494,914 | 0.02 | 3.46 | 6.55 | 0 |
| `/json/exchange` | 473,420 | 0.03 | 3.46 | 6.50 | 0 |
| `/json/response` | 475,126 | 0.03 | 3.45 | 5.84 | 0 |
| `/json/blocking` | 143,052 | 0.08 | 3.85 | 66.52 | 0 |
| `/json/worker` | 359,784 | 0.04 | 3.86 | 5.75 | 0 |
| `/json/undertow` | 501,234 | 0.02 | 3.37 | 6.28 | 0 |

CPU profile: [profile.md](profile.md)
