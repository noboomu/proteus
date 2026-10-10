# Proteus benchmark sweep-20261010T195009Z/io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, platform task executor with 1024 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=64`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 16.86 13.61 9.79, cpu idle 99.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/sleep/worker` | 25,307 | 10.09 | 10.21 | 10.87 | 0 |
| `/sleep/blocking` | 25,307 | 10.09 | 10.16 | 10.95 | 0 |
| `/plaintext/worker` | 691,316 | 0.37 | 0.77 | 1.45 | 0 |

CPU profile: [profile.md](profile.md)
