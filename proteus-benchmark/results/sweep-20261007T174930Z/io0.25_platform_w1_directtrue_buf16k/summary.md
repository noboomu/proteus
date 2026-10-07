# Proteus benchmark sweep-20261007T174930Z/io0.25_platform_w1_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 64 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 18.39 13.80 8.88, cpu idle 98.7%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 567,803 | 0.08 | 2.46 | 3.83 | 0 |
| `/plaintext/response` | 523,977 | 0.09 | 2.63 | 4.14 | 0 |
| `/plaintext/blocking` | 132,099 | 0.11 | 4.18 | 19.37 | 0 |
| `/plaintext/worker` | 367,235 | 0.06 | 3.46 | 5.45 | 0 |
| `/plaintext/undertow` | 531,550 | 0.08 | 2.53 | 3.82 | 0 |
| `/json/exchange` | 513,626 | 0.11 | 2.49 | 3.77 | 0 |
| `/json/response` | 514,365 | 0.16 | 2.42 | 3.63 | 0 |
| `/json/blocking` | 153,128 | 0.06 | 3.91 | 44.08 | 0 |
| `/json/worker` | 380,425 | 0.06 | 3.48 | 4.85 | 0 |
| `/json/undertow` | 537,125 | 0.09 | 2.53 | 3.70 | 0 |

CPU profile: [profile.md](profile.md)
