# Proteus benchmark ab8/proteus-r1

- host `attenborough`, commit `pool-cap`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 34.26 14.32 7.68, cpu idle 98.7%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 928,554 | 0.27 | 1.06 | 2.43 | 0 |
| `/json/response` | 976,883 | 0.26 | 0.63 | 2.31 | 0 |
| `/json/worker` | 678,151 | 0.08 | 4.25 | 12.42 | 0 |
| `/plaintext/blocking` | 623,778 | 0.14 | 4.49 | 12.30 | 0 |

CPU profile: [profile.md](profile.md)
