# Proteus benchmark gc/parallel-numa-2g-r3

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -XX:+UseParallelGC -XX:+UseNUMA -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 55.13 48.09 31.72, cpu idle 97% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 874,560 | 0.25 | 1.35 | 2.52 | 0 |
| `/json/response` | 908,679 | 0.25 | 1.27 | 2.36 | 0 |
| `/json/worker` | 698,666 | 0.08 | 4.15 | 10.65 | 0 |
| `/plaintext/blocking` | 647,287 | 0.13 | 4.38 | 11.62 | 0 |

CPU profile: [profile.md](profile.md)
