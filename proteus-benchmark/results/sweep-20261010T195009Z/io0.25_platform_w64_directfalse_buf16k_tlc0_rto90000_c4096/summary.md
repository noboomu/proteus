# Proteus benchmark sweep-20261010T195009Z/io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c4096

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, platform task executor with 1024 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=64`
- oha 1.16.0, 15s warmup=5s concurrency=4096
- host load before run: 12.32 12.49 9.84, cpu idle 98.8%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/sleep/worker` | 101,406 | 40.28 | 43.84 | 46.94 | 0 |
| `/sleep/blocking` | 384,122 | 10.12 | 17.61 | 35.31 | 0 |
| `/plaintext/worker` | 612,249 | 6.42 | 12.50 | 15.73 | 0 |

CPU profile: [profile.md](profile.md)
