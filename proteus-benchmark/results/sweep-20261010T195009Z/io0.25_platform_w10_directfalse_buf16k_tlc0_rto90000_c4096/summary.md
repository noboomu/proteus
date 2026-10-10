# Proteus benchmark sweep-20261010T195009Z/io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c4096

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=4096
- host load before run: 11.57 11.91 8.98, cpu idle 98.8%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/sleep/worker` | 15,923 | 258.04 | 268.44 | 376.37 | 0 |
| `/sleep/blocking` | 387,367 | 10.12 | 16.98 | 28.21 | 0 |
| `/plaintext/worker` | 580,844 | 6.30 | 12.82 | 15.68 | 0 |

CPU profile: [profile.md](profile.md)
