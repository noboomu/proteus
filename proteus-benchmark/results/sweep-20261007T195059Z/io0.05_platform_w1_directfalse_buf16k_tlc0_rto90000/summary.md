# Proteus benchmark sweep-20261007T195059Z/io0.05_platform_w1_directfalse_buf16k_tlc0_rto90000

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 13 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.05 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.socket.readTimeout=90000 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 3.41 14.47 26.49, cpu idle 98.7%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 898,097 | 0.19 | 1.62 | 3.31 | 0 |
| `/plaintext/response` | 981,633 | 0.19 | 1.45 | 3.30 | 0 |
| `/plaintext/blocking` | 460,909 | 0.49 | 2.12 | 4.98 | 0 |
| `/plaintext/worker` | 687,906 | 0.34 | 0.93 | 4.29 | 0 |
| `/plaintext/undertow` | 877,337 | 0.18 | 1.63 | 3.65 | 0 |
| `/json/exchange` | 971,075 | 0.28 | 0.91 | 3.81 | 0 |
| `/json/response` | 904,808 | 0.24 | 1.31 | 3.77 | 0 |
| `/json/blocking` | 486,262 | 0.47 | 2.03 | 5.33 | 0 |
| `/json/worker` | 482,628 | 0.45 | 1.65 | 5.25 | 0 |
| `/json/undertow` | 988,311 | 0.19 | 1.47 | 3.84 | 0 |

CPU profile: [profile.md](profile.md)
