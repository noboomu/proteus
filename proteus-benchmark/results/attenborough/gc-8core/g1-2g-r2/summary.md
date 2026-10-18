# Proteus benchmark gc/g1-2g-r2

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 49.36 36.00 21.83, cpu idle 97% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 760,535 | 0.25 | 1.55 | 2.57 | 0 |
| `/json/response` | 777,123 | 0.29 | 1.41 | 2.42 | 0 |
| `/json/worker` | 692,538 | 0.08 | 4.01 | 10.38 | 0 |
| `/plaintext/blocking` | 623,224 | 0.14 | 4.50 | 11.52 | 0 |

CPU profile: [profile.md](profile.md)
