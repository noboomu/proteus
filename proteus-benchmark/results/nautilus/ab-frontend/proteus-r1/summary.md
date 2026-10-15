# Proteus benchmark ab-fe/proteus-r1

- host `nautilus`, commit `http1`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 17.40 6.64 3.05, cpu idle 79.7%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 362,654 | 0.57 | 3.67 | 6.21 | 0 |
| `/json/response` | 372,093 | 0.57 | 3.40 | 5.72 | 0 |
| `/plaintext/undertow` | 370,923 | 0.57 | 3.57 | 6.74 | 0 |
| `/json/worker` | 206,905 | 0.60 | 6.83 | 11.23 | 0 |
| `/plaintext/blocking` | 194,397 | 0.60 | 8.01 | 11.70 | 0 |

CPU profile: [profile.md](profile.md)
