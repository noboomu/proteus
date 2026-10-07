# Proteus benchmark sweep-20261007T191947Z/io2_platform_w1_directfalse_buf16k_tlc0

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 512 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=2 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=0 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 38.70 35.68 26.93, cpu idle 98.7%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 865,010 | 0.02 | 1.88 | 5.73 | 0 |
| `/plaintext/response` | 854,611 | 0.02 | 1.91 | 5.79 | 0 |
| `/plaintext/blocking` | 427,289 | 0.03 | 4.77 | 9.14 | 0 |
| `/plaintext/worker` | 378,116 | 0.50 | 2.79 | 6.81 | 0 |
| `/plaintext/undertow` | 847,400 | 0.02 | 1.99 | 6.48 | 0 |
| `/json/exchange` | 796,576 | 0.02 | 1.89 | 6.14 | 0 |
| `/json/response` | 820,089 | 0.02 | 1.87 | 6.28 | 0 |
| `/json/blocking` | 421,901 | 0.03 | 4.76 | 9.22 | 0 |
| `/json/worker` | 373,642 | 0.51 | 2.87 | 6.37 | 0 |
| `/json/undertow` | 838,000 | 0.02 | 2.01 | 6.53 | 0 |

CPU profile: [profile.md](profile.md)
