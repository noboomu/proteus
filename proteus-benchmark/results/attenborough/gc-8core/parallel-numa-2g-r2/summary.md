# Proteus benchmark gc/parallel-numa-2g-r2

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -XX:+UseParallelGC -XX:+UseNUMA -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 50.45 39.33 24.09, cpu idle 98% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 794,620 | 0.25 | 1.52 | 2.55 | 0 |
| `/json/response` | 961,341 | 0.24 | 1.08 | 2.35 | 0 |
| `/json/worker` | 695,794 | 0.08 | 3.74 | 9.32 | 0 |
| `/plaintext/blocking` | 625,836 | 0.12 | 5.20 | 12.51 | 0 |

CPU profile: [profile.md](profile.md)
