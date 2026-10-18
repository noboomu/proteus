# Proteus benchmark gc/g1-default-r4

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 54.61 49.02 34.32, cpu idle 99% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 950,127 | 0.25 | 0.91 | 2.46 | 0 |
| `/json/response` | 894,290 | 0.25 | 1.28 | 2.40 | 0 |
| `/json/worker` | 682,192 | 0.08 | 4.29 | 11.47 | 0 |
| `/plaintext/blocking` | 629,265 | 0.13 | 4.57 | 11.70 | 0 |

CPU profile: [profile.md](profile.md)
