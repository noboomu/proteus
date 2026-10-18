# Proteus benchmark gc/parallel-numa-2g-r1

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -XX:+UseParallelGC -XX:+UseNUMA -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 45.43 24.67 15.56, cpu idle 99% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 897,045 | 0.24 | 1.29 | 2.44 | 0 |
| `/json/response` | 848,939 | 0.24 | 1.42 | 2.37 | 0 |
| `/json/worker` | 709,316 | 0.08 | 4.14 | 10.67 | 0 |
| `/plaintext/blocking` | 634,825 | 0.12 | 4.89 | 12.66 | 0 |

CPU profile: [profile.md](profile.md)
