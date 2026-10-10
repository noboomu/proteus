# Proteus benchmark sweep-20261010T195009Z/io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 16.68 13.70 10.45, cpu idle 99.3%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/sleep/worker` | 25,293 | 10.09 | 10.56 | 11.00 | 0 |
| `/sleep/blocking` | 25,315 | 10.09 | 10.19 | 10.86 | 0 |
| `/plaintext/worker` | 696,773 | 0.35 | 0.74 | 1.33 | 0 |

CPU profile: [profile.md](profile.md)
