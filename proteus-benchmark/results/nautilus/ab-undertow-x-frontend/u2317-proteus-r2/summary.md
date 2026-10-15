# Proteus benchmark ab4/u2317-proteus-r2

- host `nautilus`, commit `u2317`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 18.43 13.23 6.57, cpu idle 96.1%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 319,815 | 0.58 | 4.26 | 10.78 | 0 |
| `/json/response` | 346,914 | 0.61 | 3.65 | 6.50 | 0 |
| `/json/worker` | 200,941 | 0.64 | 6.77 | 10.89 | 0 |

CPU profile: [profile.md](profile.md)
