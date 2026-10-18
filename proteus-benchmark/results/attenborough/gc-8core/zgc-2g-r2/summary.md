# Proteus benchmark gc/zgc-2g-r2

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -XX:+UseZGC -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 51.73 41.92 26.08, cpu idle 96% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 733,032 | 0.25 | 1.58 | 2.57 | 0 |
| `/json/response` | 820,884 | 0.29 | 1.33 | 2.43 | 0 |
| `/json/worker` | 663,608 | 0.08 | 4.34 | 11.32 | 0 |
| `/plaintext/blocking` | 593,973 | 0.14 | 4.95 | 13.54 | 0 |

CPU profile: [profile.md](profile.md)
