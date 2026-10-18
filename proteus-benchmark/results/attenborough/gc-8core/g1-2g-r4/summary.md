# Proteus benchmark gc/g1-2g-r4

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 52.56 49.14 35.39, cpu idle 96% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 829,947 | 0.26 | 1.47 | 2.56 | 0 |
| `/json/response` | 888,986 | 0.24 | 1.29 | 2.37 | 0 |
| `/json/worker` | 603,320 | 0.08 | 4.52 | 12.28 | 0 |
| `/plaintext/blocking` | 535,830 | 0.12 | 6.12 | 14.12 | 0 |

CPU profile: [profile.md](profile.md)
