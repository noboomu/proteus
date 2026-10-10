# Proteus benchmark sweep-20261010T195009Z/io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c4096

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, platform task executor with 16 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=4096
- host load before run: 12.75 12.39 8.39, cpu idle 98.4%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/sleep/worker` | 1,589 | 2581.14 | 4923.85 | 5048.16 | 0 |
| `/sleep/blocking` | 387,258 | 10.12 | 17.80 | 36.27 | 0 |
| `/plaintext/worker` | 649,937 | 6.10 | 12.45 | 15.62 | 0 |

CPU profile: [profile.md](profile.md)
