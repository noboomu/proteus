# Proteus benchmark sweep-20261010T194553Z/io0.5_platform_w10_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 2 I/O threads, platform task executor with 40 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.5 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 11.34 11.61 6.64, cpu idle 99.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 218,683 | 1.18 | 1.76 | 2.46 | 0 |
| `/json/response` | 217,904 | 1.21 | 1.37 | 2.16 | 0 |
| `/plaintext/worker` | 267,854 | 0.79 | 3.31 | 6.60 | 0 |
| `/plaintext/blocking` | 204,208 | 0.76 | 9.72 | 20.74 | 0 |

CPU profile: [profile.md](profile.md)
