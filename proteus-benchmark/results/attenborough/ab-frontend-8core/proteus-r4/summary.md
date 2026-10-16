# Proteus benchmark ab8/proteus-r4

- host `attenborough`, commit `pool-cap`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 46.93 36.78 20.83, cpu idle 97.7%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 916,732 | 0.26 | 1.17 | 2.49 | 0 |
| `/json/response` | 929,157 | 0.25 | 1.10 | 2.34 | 0 |
| `/json/worker` | 686,829 | 0.08 | 4.26 | 10.96 | 0 |
| `/plaintext/blocking` | 621,195 | 0.12 | 5.04 | 13.20 | 0 |

CPU profile: [profile.md](profile.md)
