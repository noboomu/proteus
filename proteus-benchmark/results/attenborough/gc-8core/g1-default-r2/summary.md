# Proteus benchmark gc/g1-default-r2

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 46.55 32.28 19.63, cpu idle 99% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 825,598 | 0.25 | 1.45 | 2.50 | 0 |
| `/json/response` | 895,714 | 0.25 | 1.27 | 2.42 | 0 |
| `/json/worker` | 575,328 | 0.08 | 4.70 | 11.72 | 0 |
| `/plaintext/blocking` | 494,577 | 0.13 | 6.54 | 14.69 | 0 |

CPU profile: [profile.md](profile.md)
