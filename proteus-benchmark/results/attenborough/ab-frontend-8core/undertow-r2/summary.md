# Proteus benchmark ab8/undertow-r2

- host `attenborough`, commit `pool-cap`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 43.67 21.00 10.46, cpu idle 98.8%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 750,648 | 0.32 | 0.50 | 2.41 | 0 |
| `/json/response` | 763,804 | 0.33 | 0.52 | 1.39 | 0 |
| `/json/worker` | 552,943 | 0.12 | 4.91 | 14.28 | 0 |
| `/plaintext/blocking` | 500,469 | 0.15 | 6.47 | 15.02 | 0 |

CPU profile: [profile.md](profile.md)
