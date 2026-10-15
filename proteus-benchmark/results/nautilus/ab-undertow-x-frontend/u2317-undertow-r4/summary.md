# Proteus benchmark ab4/u2317-undertow-r4

- host `nautilus`, commit `u2317`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 20.95 18.11 11.06, cpu idle 96.1%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 318,908 | 0.64 | 3.88 | 7.80 | 0 |
| `/json/response` | 331,752 | 0.62 | 3.57 | 6.63 | 0 |
| `/json/worker` | 175,246 | 0.72 | 7.89 | 16.60 | 0 |

CPU profile: [profile.md](profile.md)
