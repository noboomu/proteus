# Proteus benchmark gc/zgc-2g-r4

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -XX:+UseZGC -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 47.94 47.94 36.89, cpu idle 98% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 828,587 | 0.27 | 1.33 | 2.51 | 0 |
| `/json/response` | 653,494 | 0.28 | 1.69 | 2.48 | 0 |
| `/json/worker` | 662,382 | 0.09 | 4.17 | 10.85 | 0 |
| `/plaintext/blocking` | 591,595 | 0.14 | 4.94 | 13.22 | 0 |

CPU profile: [profile.md](profile.md)
