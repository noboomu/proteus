# Proteus benchmark sweep-20261007T184646Z/io0.02_platform_w1_directfalse_buf16k_tlc4

- host `attenborough`, commit `f9a529d`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 5 I/O threads, platform task executor with 256 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.02 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=false -Dundertow.bufferSize=16k -Dundertow.bufferPool.threadLocalCacheSize=4 -Dundertow.workerThreadsMultiplier=1`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 10.02 11.81 14.29, cpu idle 98.5%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 555,255 | 0.48 | 0.65 | 1.13 | 0 |
| `/plaintext/response` | 535,866 | 0.47 | 0.61 | 1.14 | 0 |
| `/plaintext/blocking` | 168,412 | 0.06 | 4.11 | 71.50 | 0 |
| `/plaintext/worker` | 634,355 | 0.37 | 1.22 | 2.26 | 0 |
| `/plaintext/undertow` | 556,864 | 0.43 | 1.36 | 2.22 | 0 |
| `/json/exchange` | 493,652 | 0.50 | 0.70 | 1.19 | 0 |
| `/json/response` | 488,267 | 0.49 | 0.76 | 1.40 | 0 |
| `/json/blocking` | 169,005 | 0.06 | 3.94 | 72.34 | 0 |
| `/json/worker` | 635,722 | 0.38 | 0.83 | 1.30 | 0 |
| `/json/undertow` | 551,391 | 0.45 | 1.36 | 2.24 | 0 |

CPU profile: [profile.md](profile.md)
