# Proteus benchmark sweep-20261007T174930Z/io0.02_virtual_wna_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 5 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.02 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=true -Dundertow.bufferSize=16k`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 11.95 11.30 7.05, cpu idle 99.5%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 611,464 | 0.41 | 0.60 | 1.12 | 0 |
| `/plaintext/response` | 579,553 | 0.44 | 0.58 | 1.08 | 0 |
| `/plaintext/blocking` | 156,333 | 0.09 | 3.71 | 74.75 | 0 |
| `/plaintext/worker` | 216,379 | 0.05 | 3.31 | 10.10 | 0 |
| `/plaintext/undertow` | 584,056 | 0.44 | 0.57 | 0.81 | 0 |
| `/json/exchange` | 467,147 | 0.55 | 0.65 | 0.94 | 0 |
| `/json/response` | 470,170 | 0.54 | 0.67 | 0.87 | 0 |
| `/json/blocking` | 167,217 | 0.07 | 3.62 | 96.38 | 0 |
| `/json/worker` | 205,562 | 0.05 | 3.56 | 41.73 | 0 |
| `/json/undertow` | 541,982 | 0.43 | 1.60 | 2.43 | 0 |

CPU profile: [profile.md](profile.md)
