# Proteus benchmark ab/undertow-2.3.17-r1

- host `nautilus`, commit `ab-2.3.17`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `defaults`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 5.96 3.30 2.18, cpu idle 95.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 366,060 | 0.56 | 3.60 | 5.83 | 0 |
| `/json/response` | 371,409 | 0.57 | 3.50 | 5.70 | 0 |
| `/plaintext/undertow` | 378,456 | 0.57 | 3.20 | 5.32 | 0 |
| `/json/worker` | 205,978 | 0.61 | 6.95 | 11.51 | 0 |
| `/plaintext/blocking` | 194,498 | 0.61 | 7.85 | 12.02 | 0 |

CPU profile: [profile.md](profile.md)
