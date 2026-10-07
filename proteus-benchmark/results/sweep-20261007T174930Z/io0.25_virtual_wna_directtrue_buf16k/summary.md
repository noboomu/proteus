# Proteus benchmark sweep-20261007T174930Z/io0.25_virtual_wna_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 64 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=true -Dundertow.bufferSize=16k`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 15.86 15.72 11.71, cpu idle 99.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 516,833 | 0.09 | 2.60 | 4.20 | 0 |
| `/plaintext/response` | 524,381 | 0.09 | 2.59 | 4.08 | 0 |
| `/plaintext/blocking` | 128,629 | 0.11 | 4.13 | 138.93 | 0 |
| `/plaintext/worker` | 162,896 | 0.04 | 3.55 | 14.99 | 0 |
| `/plaintext/undertow` | 511,547 | 0.09 | 2.63 | 4.02 | 0 |
| `/json/exchange` | 489,558 | 0.15 | 2.52 | 3.77 | 0 |
| `/json/response` | 481,719 | 0.16 | 2.67 | 3.99 | 0 |
| `/json/blocking` | 137,300 | 0.09 | 4.32 | 62.36 | 0 |
| `/json/worker` | 179,579 | 0.04 | 3.48 | 8.84 | 0 |
| `/json/undertow` | 523,392 | 0.09 | 2.54 | 3.88 | 0 |

CPU profile: [profile.md](profile.md)
