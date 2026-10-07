# Proteus benchmark sweep-20261007T174930Z/io0.5_platform_w10_directtrue_buf16k

- host `attenborough`, commit `d688b58`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.3.17.Final, worker: 128 I/O threads, platform task executor with 2560 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=0.5 -Dundertow.workerExecutor=platform -Dundertow.directBuffers=true -Dundertow.bufferSize=16k -Dundertow.workerThreadsMultiplier=10`
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 14.73 17.75 14.48, cpu idle 99.3%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 490,219 | 0.03 | 3.63 | 6.01 | 0 |
| `/plaintext/response` | 484,277 | 0.03 | 3.69 | 5.89 | 0 |
| `/plaintext/blocking` | 125,806 | 0.11 | 4.37 | 65.07 | 0 |
| `/plaintext/worker` | 350,038 | 0.04 | 4.44 | 6.16 | 0 |
| `/plaintext/undertow` | 497,016 | 0.04 | 3.35 | 5.37 | 0 |
| `/json/exchange` | 470,066 | 0.04 | 3.36 | 5.51 | 0 |
| `/json/response` | 481,935 | 0.04 | 3.15 | 5.10 | 0 |
| `/json/blocking` | 142,162 | 0.08 | 3.89 | 16.13 | 0 |
| `/json/worker` | 351,887 | 0.04 | 3.47 | 5.85 | 0 |
| `/json/undertow` | 495,763 | 0.04 | 3.43 | 4.87 | 0 |

CPU profile: [profile.md](profile.md)
