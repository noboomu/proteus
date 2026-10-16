# Proteus benchmark ab8/undertow-r4

- host `attenborough`, commit `pool-cap`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 45.28 34.38 18.90, cpu idle 98.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 743,847 | 0.34 | 0.81 | 2.49 | 0 |
| `/json/response` | 757,974 | 0.32 | 0.64 | 1.88 | 0 |
| `/json/worker` | 564,848 | 0.11 | 4.79 | 13.55 | 0 |
| `/plaintext/blocking` | 507,332 | 0.14 | 6.52 | 14.50 | 0 |

CPU profile: [profile.md](profile.md)
