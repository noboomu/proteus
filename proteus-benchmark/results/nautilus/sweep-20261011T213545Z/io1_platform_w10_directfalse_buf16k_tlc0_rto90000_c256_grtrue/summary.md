# Proteus benchmark sweep-20261011T213545Z/io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grtrue

- host `nautilus`, commit `7aebaa8`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.gracefulShutdown=true  -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=3
- host load before run: 1.39 3.72 2.68, cpu idle 96.1%

| route | req/s median | min | max | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|---:|---:|
| `/plaintext/response` | 364,868 | 343,082 | 372,570 | 0.56 | 3.34 | 5.77 | 0 |
| `/json/response` | 359,915 | 354,355 | 360,955 | 0.60 | 2.89 | 5.53 | 0 |
| `/plaintext/undertow` | 370,970 | 368,225 | 372,476 | 0.56 | 3.43 | 5.76 | 0 |

CPU profile: [profile.md](profile.md)
