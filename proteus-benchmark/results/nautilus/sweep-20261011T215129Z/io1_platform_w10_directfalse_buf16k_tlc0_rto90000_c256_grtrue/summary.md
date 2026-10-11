# Proteus benchmark sweep-20261011T215129Z/io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grtrue

- host `nautilus`, commit `wip-recycler`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.gracefulShutdown=true -Djackson.recyclerPool=threadLocal -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=3
- host load before run: 1.21 2.65 4.14, cpu idle 87.4%

| route | req/s median | min | max | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|---:|---:|
| `/json/response` | 375,123 | 367,811 | 377,703 | 0.56 | 3.35 | 5.84 | 0 |
| `/json/blocking` | 174,098 | 172,456 | 176,149 | 0.72 | 8.71 | 13.82 | 0 |
| `/json/worker` | 204,883 | 202,431 | 206,213 | 0.60 | 7.04 | 11.31 | 0 |

CPU profile: [profile.md](profile.md)
