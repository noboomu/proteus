# Proteus benchmark sweep-20261010T194005Z/io0.125_platform_w10_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 1 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.125 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 1.28 2.08 2.25, cpu idle 99.4%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 110,634 | 2.29 | 2.92 | 4.54 | 0 |
| `/json/response` | 110,541 | 2.29 | 2.93 | 3.23 | 0 |
| `/plaintext/worker` | 273,817 | 0.91 | 1.59 | 1.86 | 0 |
| `/plaintext/blocking` | 263,191 | 0.95 | 1.90 | 2.34 | 0 |

CPU profile: [profile.md](profile.md)
