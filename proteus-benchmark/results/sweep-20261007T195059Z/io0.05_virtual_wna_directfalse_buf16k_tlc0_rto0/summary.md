# Proteus benchmark sweep-20261007T195059Z/io0.05_virtual_wna_directfalse_buf16k_tlc0_rto0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=0`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 40.88 33.94 31.53, cpu idle 98.3%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 830,406 | 0.21 | 1.67 | 3.28 | 0 |
| `/plaintext/response` | 1,003,338 | 0.19 | 1.40 | 3.28 | 0 |
| `/plaintext/blocking` | 516,985 | 0.44 | 1.94 | 4.96 | 0 |
| `/plaintext/worker` | 708,638 | 0.31 | 1.29 | 4.33 | 0 |
| `/plaintext/undertow` | 933,802 | 0.20 | 1.57 | 3.71 | 0 |
| `/json/exchange` | 934,321 | 0.24 | 1.15 | 3.47 | 0 |
| `/json/response` | 900,189 | 0.23 | 1.35 | 3.45 | 0 |
| `/json/blocking` | 471,487 | 0.47 | 2.15 | 5.01 | 0 |
| `/json/worker` | 671,174 | 0.33 | 1.28 | 4.47 | 0 |
| `/json/undertow` | 1,028,611 | 0.20 | 1.35 | 3.88 | 0 |

CPU profile: [profile.md](profile.md)
