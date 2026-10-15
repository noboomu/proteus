# Proteus benchmark ab4/u2317-undertow-r2

- host `nautilus`, commit `u2317`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 19.50 12.33 5.89, cpu idle 87.3%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 323,819 | 0.63 | 3.73 | 7.76 | 0 |
| `/json/response` | 339,854 | 0.61 | 3.66 | 7.40 | 0 |
| `/json/worker` | 197,860 | 0.65 | 6.74 | 10.80 | 0 |

CPU profile: [profile.md](profile.md)
