# Proteus benchmark sweep-20261011T213545Z/io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grfalse

- host `nautilus`, commit `7aebaa8`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.gracefulShutdown=false  -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=3
- host load before run: 11.78 7.36 4.18, cpu idle 90.0%

| route | req/s median | min | max | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|---:|---:|
| `/plaintext/response` | 321,428 | 312,482 | 348,213 | 0.70 | 3.53 | 5.95 | 0 |
| `/json/response` | 322,062 | 321,410 | 324,354 | 0.66 | 3.22 | 6.08 | 0 |
| `/plaintext/undertow` | 348,910 | 348,040 | 352,122 | 0.59 | 3.61 | 5.97 | 0 |

CPU profile: [profile.md](profile.md)
