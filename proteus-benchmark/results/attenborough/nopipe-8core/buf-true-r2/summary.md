# Proteus benchmark nopipe/buf-true-r2

- host `attenborough`, commit `8109097`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Dundertow.server.bufferPipelinedData=true`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 49.49 34.09 25.10, cpu idle 97% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 900,653 | 0.25 | 1.16 | 2.43 | 0 |
| `/json/response` | 944,955 | 0.26 | 1.01 | 2.35 | 0 |
| `/json/worker` | 679,943 | 0.08 | 4.24 | 11.33 | 0 |
| `/plaintext/blocking` | 618,830 | 0.14 | 4.41 | 11.39 | 0 |

CPU profile: [profile.md](profile.md)
