# Proteus benchmark sweep-20261010T194553Z/io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 4 I/O threads, platform task executor with 40 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 15.96 12.67 7.42, cpu idle 98.8%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 411,611 | 0.60 | 1.28 | 2.91 | 0 |
| `/json/response` | 405,361 | 0.65 | 1.05 | 2.75 | 0 |
| `/plaintext/worker` | 253,859 | 0.68 | 5.61 | 14.70 | 0 |
| `/plaintext/blocking` | 143,452 | 0.56 | 26.79 | 61.87 | 0 |

CPU profile: [profile.md](profile.md)
