# Proteus benchmark ab8/undertow-r1

- host `attenborough`, commit `pool-cap`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 6.16 6.00 4.72, cpu idle 98.9%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 746,754 | 0.35 | 0.89 | 2.44 | 0 |
| `/json/response` | 768,538 | 0.33 | 0.53 | 1.38 | 0 |
| `/json/worker` | 547,247 | 0.12 | 4.91 | 14.55 | 0 |
| `/plaintext/blocking` | 500,465 | 0.14 | 6.74 | 14.96 | 0 |

CPU profile: [profile.md](profile.md)
