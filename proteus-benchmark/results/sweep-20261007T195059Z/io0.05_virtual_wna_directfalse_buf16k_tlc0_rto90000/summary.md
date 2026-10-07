# Proteus benchmark sweep-20261007T195059Z/io0.05_virtual_wna_directfalse_buf16k_tlc0_rto90000

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 23.47 27.91 29.54, cpu idle 98.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 853,614 | 0.19 | 1.63 | 3.23 | 0 |
| `/plaintext/response` | 936,403 | 0.20 | 1.49 | 3.21 | 0 |
| `/plaintext/blocking` | 448,180 | 0.50 | 2.15 | 5.05 | 0 |
| `/plaintext/worker` | 641,158 | 0.35 | 1.28 | 4.43 | 0 |
| `/plaintext/undertow` | 936,730 | 0.19 | 1.53 | 3.81 | 0 |
| `/json/exchange` | 955,241 | 0.25 | 0.90 | 3.62 | 0 |
| `/json/response` | 928,381 | 0.24 | 0.99 | 3.57 | 0 |
| `/json/blocking` | 479,547 | 0.47 | 2.07 | 5.00 | 0 |
| `/json/worker` | 607,416 | 0.35 | 1.46 | 4.57 | 0 |
| `/json/undertow` | 995,605 | 0.21 | 1.42 | 3.84 | 0 |

CPU profile: [profile.md](profile.md)
