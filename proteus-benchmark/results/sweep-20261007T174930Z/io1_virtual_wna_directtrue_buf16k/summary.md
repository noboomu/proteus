# Proteus benchmark sweep-20261007T174930Z/io1_virtual_wna_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 256 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=true -Dundertow.bufferSize=16k`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 15.08 16.41 15.36, cpu idle 98.8%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 477,877 | 0.02 | 3.50 | 6.72 | 0 |
| `/plaintext/response` | 481,479 | 0.02 | 3.40 | 6.51 | 0 |
| `/plaintext/blocking` | 149,075 | 0.10 | 4.05 | 187.15 | 0 |
| `/plaintext/worker` | 201,601 | 0.03 | 3.50 | 20.17 | 0 |
| `/plaintext/undertow` | 514,376 | 0.02 | 3.67 | 5.57 | 0 |
| `/json/exchange` | 486,206 | 0.03 | 3.36 | 6.32 | 0 |
| `/json/response` | 493,775 | 0.03 | 3.36 | 5.19 | 0 |
| `/json/blocking` | 157,317 | 0.07 | 3.88 | 54.94 | 0 |
| `/json/worker` | 215,378 | 0.03 | 3.37 | 30.04 | 0 |
| `/json/undertow` | 499,965 | 0.02 | 3.56 | 5.37 | 0 |

CPU profile: [profile.md](profile.md)
