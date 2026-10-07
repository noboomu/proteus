# Proteus benchmark sweep-20261007T174930Z/io2_virtual_wna_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 512 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=2 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=true -Dundertow.bufferSize=16k`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 13.36 15.70 16.09, cpu idle 99.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 482,708 | 0.02 | 4.60 | 7.01 | 0 |
| `/plaintext/response` | 482,994 | 0.02 | 4.53 | 6.89 | 0 |
| `/plaintext/blocking` | 143,373 | 0.10 | 4.13 | 120.00 | 0 |
| `/plaintext/worker` | 186,421 | 0.03 | 3.62 | 16.23 | 0 |
| `/plaintext/undertow` | 514,479 | 0.02 | 4.42 | 6.65 | 0 |
| `/json/exchange` | 491,213 | 0.02 | 4.02 | 6.04 | 0 |
| `/json/response` | 452,333 | 0.02 | 4.07 | 6.11 | 0 |
| `/json/blocking` | 140,953 | 0.09 | 3.88 | 92.90 | 0 |
| `/json/worker` | 181,648 | 0.04 | 3.55 | 41.00 | 0 |
| `/json/undertow` | 475,657 | 0.02 | 4.30 | 6.43 | 0 |

CPU profile: [profile.md](profile.md)
