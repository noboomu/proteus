# Proteus benchmark ab8/undertow-r3

- host `attenborough`, commit `pool-cap`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 39.22 28.58 14.87, cpu idle 97.9%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 743,851 | 0.33 | 0.86 | 2.47 | 0 |
| `/json/response` | 680,306 | 0.37 | 0.66 | 1.63 | 0 |
| `/json/worker` | 562,059 | 0.11 | 4.67 | 12.62 | 0 |
| `/plaintext/blocking` | 509,650 | 0.16 | 6.15 | 13.98 | 0 |

CPU profile: [profile.md](profile.md)
