# Proteus benchmark gc/zgc-2g-r3

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -XX:+UseZGC -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 49.45 47.53 32.77, cpu idle 98% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 873,261 | 0.26 | 1.18 | 2.47 | 0 |
| `/json/response` | 801,613 | 0.26 | 1.43 | 2.44 | 0 |
| `/json/worker` | 645,573 | 0.09 | 4.04 | 10.53 | 0 |
| `/plaintext/blocking` | 609,973 | 0.13 | 4.76 | 15.36 | 0 |

CPU profile: [profile.md](profile.md)
