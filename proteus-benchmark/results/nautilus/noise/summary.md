# Proteus benchmark noise

- host `nautilus`, commit `7aebaa8`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.ioThreadsMultiplier=1`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=3
- host load before run: 6.48 3.35 2.01, cpu idle 89.1%

| route | req/s median | min | max | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|---:|---:|
| `/plaintext/response` | 366,118 | 365,403 | 368,689 | 0.57 | 3.29 | 5.84 | 0 |
| `/json/response` | 353,498 | 352,645 | 358,061 | 0.58 | 3.22 | 5.51 | 0 |

CPU profile: [profile.md](profile.md)
