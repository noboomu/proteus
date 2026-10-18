# Proteus benchmark gc/zgc-2g-r1

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -XX:+UseZGC -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 43.75 28.37 17.50, cpu idle 96% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 787,914 | 0.26 | 1.49 | 2.54 | 0 |
| `/json/response` | 800,628 | 0.28 | 1.45 | 2.47 | 0 |
| `/json/worker` | 642,853 | 0.10 | 3.75 | 9.78 | 0 |
| `/plaintext/blocking` | 583,697 | 0.13 | 5.32 | 13.80 | 0 |

CPU profile: [profile.md](profile.md)
