# Proteus benchmark sweep-20261010T195009Z/io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c4096

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000`
- oha 1.16.0, 15s warmup=5s concurrency=4096
- host load before run: 12.60 12.73 10.48, cpu idle 99.4%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/sleep/worker` | 394,375 | 10.09 | 15.01 | 22.79 | 0 |
| `/sleep/blocking` | 383,724 | 10.13 | 17.33 | 37.36 | 0 |
| `/plaintext/worker` | 497,375 | 8.06 | 14.05 | 16.83 | 0 |

CPU profile: [profile.md](profile.md)
