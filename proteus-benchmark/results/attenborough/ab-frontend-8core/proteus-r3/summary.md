# Proteus benchmark ab8/proteus-r3

- host `attenborough`, commit `pool-cap`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 42.53 31.56 16.88, cpu idle 99.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 958,210 | 0.26 | 0.86 | 2.42 | 0 |
| `/json/response` | 999,355 | 0.26 | 0.36 | 1.12 | 0 |
| `/json/worker` | 607,024 | 0.08 | 4.20 | 10.00 | 0 |
| `/plaintext/blocking` | 507,005 | 0.13 | 6.37 | 13.66 | 0 |

CPU profile: [profile.md](profile.md)
