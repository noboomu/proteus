# Proteus benchmark nopipe/buf-false-r3

- host `attenborough`, commit `8109097`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Dundertow.server.bufferPipelinedData=false`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 49.12 42.06 30.21, cpu idle 98% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 905,726 | 0.25 | 1.25 | 2.45 | 0 |
| `/json/response` | 887,301 | 0.26 | 1.32 | 2.48 | 0 |
| `/json/worker` | 682,764 | 0.08 | 4.19 | 10.79 | 0 |
| `/plaintext/blocking` | 624,909 | 0.14 | 4.59 | 11.84 | 0 |

CPU profile: [profile.md](profile.md)
