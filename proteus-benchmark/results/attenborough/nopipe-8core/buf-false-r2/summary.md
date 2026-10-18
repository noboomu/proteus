# Proteus benchmark nopipe/buf-false-r2

- host `attenborough`, commit `8109097`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Dundertow.server.bufferPipelinedData=false`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 54.26 38.53 27.27, cpu idle 99% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 907,712 | 0.25 | 1.21 | 2.44 | 0 |
| `/json/response` | 971,995 | 0.24 | 0.83 | 2.31 | 0 |
| `/json/worker` | 546,112 | 0.09 | 4.39 | 11.90 | 0 |
| `/plaintext/blocking` | 537,632 | 0.13 | 5.77 | 13.06 | 0 |

CPU profile: [profile.md](profile.md)
