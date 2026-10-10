# Proteus benchmark sweep-20261010T194005Z/io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256

- host `attenborough`, commit `5f06186`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 2 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.25 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 5.11 3.01 2.56, cpu idle 98.3%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 217,840 | 1.28 | 1.47 | 2.23 | 0 |
| `/json/response` | 214,237 | 1.23 | 1.44 | 2.06 | 0 |
| `/plaintext/worker` | 474,674 | 0.53 | 1.17 | 1.73 | 0 |
| `/plaintext/blocking` | 457,593 | 0.54 | 1.12 | 2.03 | 0 |

CPU profile: [profile.md](profile.md)
