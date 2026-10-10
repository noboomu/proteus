# Proteus benchmark sweep-20261010T194553Z/io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 1 I/O threads, platform task executor with 40 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 26.83 13.46 6.71, cpu idle 99.4%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 111,230 | 2.26 | 2.91 | 3.29 | 0 |
| `/json/response` | 110,720 | 2.28 | 2.97 | 3.46 | 0 |
| `/plaintext/worker` | 257,072 | 0.97 | 1.75 | 2.04 | 0 |
| `/plaintext/blocking` | 280,785 | 0.88 | 1.80 | 2.62 | 0 |

CPU profile: [profile.md](profile.md)
