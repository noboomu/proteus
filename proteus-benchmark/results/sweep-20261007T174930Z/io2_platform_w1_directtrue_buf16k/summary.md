# Proteus benchmark sweep-20261007T174930Z/io2_platform_w1_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 512 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=2 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 14.31 17.97 16.45, cpu idle 98.4%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 483,418 | 0.02 | 4.61 | 7.00 | 0 |
| `/plaintext/response` | 486,741 | 0.02 | 4.38 | 6.67 | 0 |
| `/plaintext/blocking` | 138,412 | 0.11 | 4.31 | 165.25 | 0 |
| `/plaintext/worker` | 366,219 | 0.03 | 4.41 | 6.50 | 0 |
| `/plaintext/undertow` | 479,962 | 0.02 | 4.45 | 6.66 | 0 |
| `/json/exchange` | 464,919 | 0.02 | 4.21 | 6.33 | 0 |
| `/json/response` | 470,003 | 0.03 | 4.10 | 6.12 | 0 |
| `/json/blocking` | 146,883 | 0.07 | 3.87 | 62.99 | 0 |
| `/json/worker` | 365,073 | 0.04 | 4.37 | 6.44 | 0 |
| `/json/undertow` | 491,475 | 0.02 | 4.32 | 6.48 | 0 |

CPU profile: [profile.md](profile.md)
