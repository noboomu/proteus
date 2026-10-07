# Proteus benchmark sweep-20261007T174930Z/io0.5_virtual_wna_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 128 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.5 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=true -Dundertow.bufferSize=16k`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 14.90 16.23 14.53, cpu idle 98.1%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 511,405 | 0.03 | 3.49 | 5.78 | 0 |
| `/plaintext/response` | 490,463 | 0.04 | 3.24 | 5.36 | 0 |
| `/plaintext/blocking` | 141,805 | 0.10 | 4.17 | 31.91 | 0 |
| `/plaintext/worker` | 182,414 | 0.04 | 3.54 | 17.62 | 0 |
| `/plaintext/undertow` | 518,919 | 0.03 | 3.50 | 4.90 | 0 |
| `/json/exchange` | 481,850 | 0.04 | 3.22 | 5.18 | 0 |
| `/json/response` | 481,673 | 0.04 | 3.21 | 5.19 | 0 |
| `/json/blocking` | 131,686 | 0.09 | 3.99 | 102.88 | 0 |
| `/json/worker` | 180,777 | 0.04 | 3.48 | 27.56 | 0 |
| `/json/undertow` | 497,574 | 0.03 | 3.43 | 4.76 | 0 |

CPU profile: [profile.md](profile.md)
