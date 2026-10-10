# Proteus benchmark sweep-20261010T195009Z/io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 13.70 12.67 8.77, cpu idle 98.5%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/sleep/worker` | 15,880 | 16.30 | 18.73 | 19.27 | 0 |
| `/sleep/blocking` | 25,307 | 10.09 | 10.29 | 10.96 | 0 |
| `/plaintext/worker` | 695,585 | 0.35 | 0.83 | 1.58 | 0 |

CPU profile: [profile.md](profile.md)
