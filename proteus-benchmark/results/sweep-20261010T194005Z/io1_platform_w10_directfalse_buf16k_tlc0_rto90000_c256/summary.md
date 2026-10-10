# Proteus benchmark sweep-20261010T194005Z/io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 16.85 8.03 4.47, cpu idle 98.7%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 669,159 | 0.38 | 0.79 | 2.51 | 0 |
| `/json/response` | 665,603 | 0.36 | 0.90 | 2.40 | 0 |
| `/plaintext/worker` | 375,178 | 0.19 | 6.17 | 21.53 | 0 |
| `/plaintext/blocking` | 220,790 | 0.31 | 14.03 | 27.84 | 0 |

CPU profile: [profile.md](profile.md)
