# Proteus benchmark sweep-20261010T195009Z/io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c1024

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000`
- oha 1.16.0, 15s warmup=5s concurrency=1024
- host load before run: 14.30 13.29 10.50, cpu idle 98.8%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/sleep/worker` | 101,128 | 10.09 | 10.43 | 11.94 | 0 |
| `/sleep/blocking` | 101,003 | 10.09 | 11.53 | 11.76 | 0 |
| `/plaintext/worker` | 699,075 | 1.44 | 2.40 | 3.56 | 0 |

CPU profile: [profile.md](profile.md)
