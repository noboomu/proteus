# Proteus benchmark gc/g1-default-r1

- host `attenborough`, commit `gc-sweep`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 8.58 9.85 10.07, cpu idle 97% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 869,843 | 0.26 | 1.33 | 2.56 | 0 |
| `/json/response` | 915,191 | 0.25 | 1.16 | 2.39 | 0 |
| `/json/worker` | 685,161 | 0.08 | 4.36 | 11.76 | 0 |
| `/plaintext/blocking` | 617,586 | 0.13 | 4.89 | 12.63 | 0 |

CPU profile: [profile.md](profile.md)
