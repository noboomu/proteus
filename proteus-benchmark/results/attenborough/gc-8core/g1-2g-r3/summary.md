# Proteus benchmark gc/g1-2g-r3

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Xms2g -Xmx2g -XX:+AlwaysPreTouch`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 55.30 46.80 30.09, cpu idle 97% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 894,082 | 0.26 | 1.29 | 2.57 | 0 |
| `/json/response` | 882,586 | 0.25 | 1.29 | 2.36 | 0 |
| `/json/worker` | 703,475 | 0.08 | 4.15 | 11.37 | 0 |
| `/plaintext/blocking` | 620,962 | 0.12 | 4.99 | 12.95 | 0 |

CPU profile: [profile.md](profile.md)
