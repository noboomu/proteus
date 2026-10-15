# Proteus benchmark ab4/u2317-undertow-r1

- host `nautilus`, commit `u2317`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 7.12 3.48 1.97, cpu idle 87.9%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 362,017 | 0.58 | 3.70 | 6.52 | 0 |
| `/json/response` | 369,431 | 0.58 | 3.53 | 5.68 | 0 |
| `/json/worker` | 193,200 | 0.66 | 7.25 | 13.78 | 0 |

CPU profile: [profile.md](profile.md)
