# Proteus benchmark sweep-20261011T215129Z/io1_virtual_wna_directfalse_buf16k_tlc0_rto90000_c256_grtrue

- host `nautilus`, commit `wip-recycler`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, virtual task executor
- jvm opts: `-Dundertow.ioThreadsMultiplier=1 -Dundertow.workerExecutor=virtual -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.gracefulShutdown=true -Djackson.recyclerPool=threadLocal`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=3
- host load before run: 21.36 9.35 6.37, cpu idle 88.9%

| route | req/s median | min | max | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|---:|---:|
| `/json/response` | 358,166 | 352,624 | 360,074 | 0.58 | 3.57 | 6.29 | 0 |
| `/json/blocking` | 169,661 | 169,393 | 170,470 | 0.75 | 8.99 | 14.41 | 0 |
| `/json/worker` | 193,521 | 189,827 | 193,855 | 0.62 | 7.97 | 11.86 | 0 |

CPU profile: [profile.md](profile.md)
