# Proteus benchmark gc/parallel-numa-2g-r4

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -XX:+UseParallelGC -XX:+UseNUMA -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 46.70 48.00 36.05, cpu idle 96% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 877,234 | 0.24 | 1.30 | 2.45 | 0 |
| `/json/response` | 821,099 | 0.25 | 1.50 | 2.44 | 0 |
| `/json/worker` | 696,649 | 0.08 | 4.17 | 11.09 | 0 |
| `/plaintext/blocking` | 630,576 | 0.12 | 4.99 | 12.02 | 0 |

CPU profile: [profile.md](profile.md)
