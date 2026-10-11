# Proteus benchmark sweep-20261011T213545Z/io1_platform_w10_directfalse_buf16k_tlc0_rto0_c256_grfalse

- host `nautilus`, commit `7aebaa8`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=0 -Dundertow.gracefulShutdown=false  -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=3
- host load before run: 13.75 9.95 5.56, cpu idle 93.1%

| route | req/s median | min | max | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|---:|---:|
| `/plaintext/response` | 358,734 | 354,975 | 360,598 | 0.57 | 3.64 | 5.97 | 0 |
| `/json/response` | 347,354 | 346,023 | 347,510 | 0.59 | 3.67 | 6.15 | 0 |
| `/plaintext/undertow` | 361,580 | 360,953 | 364,074 | 0.57 | 3.70 | 6.08 | 0 |

CPU profile: [profile.md](profile.md)
