# Proteus benchmark gc/g1-default-r3

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 54.30 44.51 28.11, cpu idle 96% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 883,363 | 0.25 | 1.29 | 2.70 | 0 |
| `/json/response` | 893,790 | 0.25 | 1.26 | 2.36 | 0 |
| `/json/worker` | 586,071 | 0.09 | 4.47 | 11.43 | 0 |
| `/plaintext/blocking` | 544,277 | 0.14 | 5.43 | 12.12 | 0 |

CPU profile: [profile.md](profile.md)
