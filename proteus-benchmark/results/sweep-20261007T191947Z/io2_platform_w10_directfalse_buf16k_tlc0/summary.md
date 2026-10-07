# Proteus benchmark sweep-20261007T191947Z/io2_platform_w10_directfalse_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 512 I/O threads, platform task executor with 2560 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=2 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 67.88 53.87 36.12, cpu idle 99.1%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 821,746 | 0.02 | 1.97 | 5.77 | 0 |
| `/plaintext/response` | 806,327 | 0.02 | 2.46 | 5.70 | 0 |
| `/plaintext/blocking` | 440,968 | 0.03 | 4.53 | 8.90 | 0 |
| `/plaintext/worker` | 373,144 | 0.51 | 2.78 | 6.90 | 0 |
| `/plaintext/undertow` | 849,265 | 0.02 | 1.93 | 6.58 | 0 |
| `/json/exchange` | 792,585 | 0.02 | 1.95 | 6.53 | 0 |
| `/json/response` | 783,608 | 0.02 | 1.95 | 6.62 | 0 |
| `/json/blocking` | 410,595 | 0.03 | 4.52 | 9.17 | 0 |
| `/json/worker` | 366,242 | 0.51 | 3.00 | 6.68 | 0 |
| `/json/undertow` | 823,509 | 0.02 | 1.94 | 6.57 | 0 |

CPU profile: [profile.md](profile.md)
