# Proteus benchmark ab4/u2317-proteus-r4

- host `nautilus`, commit `u2317`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 22.03 18.70 11.65, cpu idle 85.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 325,300 | 0.63 | 3.87 | 7.12 | 0 |
| `/json/response` | 331,825 | 0.62 | 3.72 | 6.46 | 0 |
| `/json/worker` | 176,369 | 0.70 | 8.02 | 14.55 | 0 |

CPU profile: [profile.md](profile.md)
