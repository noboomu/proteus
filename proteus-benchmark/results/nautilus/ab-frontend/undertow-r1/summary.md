# Proteus benchmark ab-fe/undertow-r1

- host `nautilus`, commit `http1`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 2.37 1.42 1.18, cpu idle 77.7%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 364,764 | 0.53 | 3.69 | 6.77 | 0 |
| `/json/response` | 369,826 | 0.55 | 3.59 | 6.07 | 0 |
| `/plaintext/undertow` | 387,080 | 0.54 | 3.22 | 5.48 | 0 |
| `/json/worker` | 209,072 | 0.59 | 7.00 | 10.68 | 0 |
| `/plaintext/blocking` | 197,710 | 0.60 | 7.68 | 11.72 | 0 |

CPU profile: [profile.md](profile.md)
