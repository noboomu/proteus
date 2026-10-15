# Proteus benchmark ab-fe/proteus-r2

- host `nautilus`, commit `http1`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 20.73 13.44 6.34, cpu idle 96.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 342,417 | 0.59 | 3.79 | 6.54 | 0 |
| `/json/response` | 342,945 | 0.61 | 3.69 | 6.87 | 0 |
| `/plaintext/undertow` | 353,378 | 0.60 | 3.70 | 5.92 | 0 |
| `/json/worker` | 198,266 | 0.64 | 7.06 | 11.29 | 0 |
| `/plaintext/blocking` | 188,525 | 0.64 | 7.83 | 12.00 | 0 |

CPU profile: [profile.md](profile.md)
