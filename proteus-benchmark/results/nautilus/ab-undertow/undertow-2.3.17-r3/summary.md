# Proteus benchmark ab/undertow-2.3.17-r3

- host `nautilus`, commit `ab-2.3.17`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `defaults`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 20.46 12.95 6.50, cpu idle 82.3%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 335,120 | 0.60 | 3.80 | 7.24 | 0 |
| `/json/response` | 333,413 | 0.62 | 3.82 | 6.44 | 0 |
| `/plaintext/undertow` | 343,021 | 0.58 | 3.79 | 6.76 | 0 |
| `/json/worker` | 193,496 | 0.65 | 7.26 | 11.32 | 0 |
| `/plaintext/blocking` | 183,185 | 0.67 | 8.01 | 12.14 | 0 |

CPU profile: [profile.md](profile.md)
