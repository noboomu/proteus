# Proteus benchmark sweep-20261010T195009Z/io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c1024

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, platform task executor with 16 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=1024
- host load before run: 12.65 12.57 8.17, cpu idle 98.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/sleep/worker` | 1,589 | 645.15 | 1057.42 | 1181.98 | 0 |
| `/sleep/blocking` | 100,836 | 10.09 | 10.93 | 12.66 | 0 |
| `/plaintext/worker` | 647,165 | 1.47 | 3.76 | 5.13 | 0 |

CPU profile: [profile.md](profile.md)
