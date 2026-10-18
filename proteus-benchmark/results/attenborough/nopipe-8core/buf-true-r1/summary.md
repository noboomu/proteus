# Proteus benchmark nopipe/buf-true-r1

- host `attenborough`, commit `8109097`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Dundertow.server.bufferPipelinedData=true`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 17.72 23.38 20.74, cpu idle 99% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 893,429 | 0.25 | 1.27 | 2.47 | 0 |
| `/json/response` | 953,652 | 0.25 | 0.95 | 2.36 | 0 |
| `/json/worker` | 679,961 | 0.08 | 4.26 | 11.15 | 0 |
| `/plaintext/blocking` | 610,483 | 0.14 | 4.81 | 11.84 | 0 |

CPU profile: [profile.md](profile.md)
