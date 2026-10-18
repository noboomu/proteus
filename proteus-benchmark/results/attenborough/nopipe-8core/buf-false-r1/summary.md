# Proteus benchmark nopipe/buf-false-r1

- host `attenborough`, commit `8109097`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Dundertow.server.bufferPipelinedData=false`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 41.90 29.42 23.03, cpu idle 94% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 891,173 | 0.24 | 1.23 | 2.49 | 0 |
| `/json/response` | 902,156 | 0.24 | 1.23 | 2.49 | 0 |
| `/json/worker` | 686,838 | 0.08 | 4.46 | 12.16 | 0 |
| `/plaintext/blocking` | 615,294 | 0.13 | 4.75 | 12.02 | 0 |

CPU profile: [profile.md](profile.md)
