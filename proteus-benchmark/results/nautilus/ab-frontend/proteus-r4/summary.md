# Proteus benchmark ab-fe/proteus-r4

- host `nautilus`, commit `http1`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 19.72 16.00 10.91, cpu idle 90.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 334,123 | 0.60 | 3.84 | 6.93 | 0 |
| `/json/response` | 339,046 | 0.61 | 3.72 | 6.89 | 0 |
| `/plaintext/undertow` | 348,415 | 0.60 | 3.67 | 6.47 | 0 |
| `/json/worker` | 194,626 | 0.65 | 7.15 | 11.77 | 0 |
| `/plaintext/blocking` | 187,732 | 0.64 | 7.93 | 12.16 | 0 |

CPU profile: [profile.md](profile.md)
