# Proteus benchmark nopipe/buf-true-r3

- host `attenborough`, commit `8109097`
- openjdk version "27" 2026-09-15
- AMD EPYC 9755 128-Core Processor (256 logical, 128 physical cores)
- Undertow 2.4.4.Final, worker: 16 I/O threads, platform task executor with 160 threads
- jvm opts: `-Dundertow.frontEnd=proteus -Dundertow.server.bufferPipelinedData=true`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 51.44 40.83 28.88, cpu idle 97% (cpus 0-7,128-135,64-127)

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 886,348 | 0.27 | 1.28 | 2.49 | 0 |
| `/json/response` | 919,687 | 0.26 | 1.07 | 2.33 | 0 |
| `/json/worker` | 591,161 | 0.08 | 4.48 | 11.81 | 0 |
| `/plaintext/blocking` | 528,034 | 0.13 | 6.25 | 14.54 | 0 |

CPU profile: [profile.md](profile.md)
