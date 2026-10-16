# Proteus benchmark ab8/proteus-r2

- host `attenborough`, commit `pool-cap`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 48.23 26.80 13.21, cpu idle 98.4%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 963,820 | 0.24 | 0.88 | 2.39 | 0 |
| `/json/response` | 951,273 | 0.25 | 1.04 | 2.34 | 0 |
| `/json/worker` | 686,940 | 0.08 | 4.29 | 10.74 | 0 |
| `/plaintext/blocking` | 606,595 | 0.12 | 5.29 | 13.33 | 0 |

CPU profile: [profile.md](profile.md)
