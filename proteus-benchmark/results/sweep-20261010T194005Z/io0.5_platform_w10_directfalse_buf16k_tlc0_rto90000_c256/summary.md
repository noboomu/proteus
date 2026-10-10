# Proteus benchmark sweep-20261010T194005Z/io0.5_platform_w10_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.5 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 8.22 4.47 3.11, cpu idle 98.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 409,307 | 0.63 | 0.77 | 1.49 | 0 |
| `/json/response` | 405,041 | 0.66 | 0.86 | 1.44 | 0 |
| `/plaintext/worker` | 454,620 | 0.33 | 3.26 | 6.38 | 0 |
| `/plaintext/blocking` | 379,633 | 0.42 | 4.22 | 6.62 | 0 |

CPU profile: [profile.md](profile.md)
