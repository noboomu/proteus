# Proteus benchmark gc/g1-2g-r1

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 37.90 17.94 12.83, cpu idle 99% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 729,185 | 0.25 | 1.65 | 2.68 | 0 |
| `/json/response` | 843,338 | 0.25 | 1.42 | 2.41 | 0 |
| `/json/worker` | 677,660 | 0.09 | 3.71 | 10.12 | 0 |
| `/plaintext/blocking` | 620,416 | 0.11 | 5.42 | 13.87 | 0 |

CPU profile: [profile.md](profile.md)
