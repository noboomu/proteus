# Proteus benchmark ab-fe/undertow-r4

- host `nautilus`, commit `http1`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 18.46 14.78 10.08, cpu idle 88.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 333,003 | 0.62 | 3.77 | 6.36 | 0 |
| `/json/response` | 340,163 | 0.61 | 3.53 | 6.07 | 0 |
| `/plaintext/undertow` | 344,696 | 0.59 | 3.71 | 6.49 | 0 |
| `/json/worker` | 191,130 | 0.66 | 7.22 | 11.94 | 0 |
| `/plaintext/blocking` | 183,752 | 0.65 | 8.18 | 12.36 | 0 |

CPU profile: [profile.md](profile.md)
