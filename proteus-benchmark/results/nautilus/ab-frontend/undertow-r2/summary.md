# Proteus benchmark ab-fe/undertow-r2

- host `nautilus`, commit `http1`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 22.00 10.89 4.88, cpu idle 67.4%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 342,074 | 0.62 | 3.69 | 6.07 | 0 |
| `/json/response` | 349,414 | 0.62 | 3.57 | 6.44 | 0 |
| `/plaintext/undertow` | 359,681 | 0.58 | 3.61 | 5.83 | 0 |
| `/json/worker` | 198,438 | 0.65 | 6.92 | 11.54 | 0 |
| `/plaintext/blocking` | 189,943 | 0.65 | 7.63 | 11.48 | 0 |

CPU profile: [profile.md](profile.md)
