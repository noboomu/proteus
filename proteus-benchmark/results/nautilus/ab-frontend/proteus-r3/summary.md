# Proteus benchmark ab-fe/proteus-r3

- host `nautilus`, commit `http1`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 15.14 13.05 9.10, cpu idle 95.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 348,287 | 0.59 | 3.72 | 6.97 | 0 |
| `/json/response` | 318,335 | 0.70 | 2.95 | 6.02 | 0 |
| `/plaintext/undertow` | 325,910 | 0.66 | 3.50 | 5.82 | 0 |
| `/json/worker` | 198,152 | 0.61 | 7.42 | 12.10 | 0 |
| `/plaintext/blocking` | 188,785 | 0.62 | 8.10 | 12.58 | 0 |

CPU profile: [profile.md](profile.md)
