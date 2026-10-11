# Proteus benchmark sweep-20261011T215534Z/io1_virtual_wna_directfalse_buf16k_tlc0_rto90000_c256_grtrue

- host `nautilus`, commit `wip-recycler`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.gracefulShutdown=true -Djackson.recyclerPool=concurrentDeque`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=3
- host load before run: 16.72 11.92 7.71, cpu idle 89.4%

| route | req/s median | min | max | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|---:|---:|
| `/json/response` | 336,748 | 207,653 | 342,371 | 0.63 | 3.67 | 6.53 | 0 |
| `/json/blocking` | 184,392 | 182,626 | 185,411 | 0.66 | 8.18 | 13.00 | 0 |
| `/json/worker` | 209,626 | 209,072 | 209,806 | 0.56 | 7.33 | 11.13 | 0 |

CPU profile: [profile.md](profile.md)
