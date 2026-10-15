# Proteus benchmark ab4/u2317-proteus-r1

- host `nautilus`, commit `u2317`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 13.94 6.10 2.97, cpu idle 89.3%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 363,654 | 0.57 | 3.63 | 6.63 | 0 |
| `/json/response` | 367,217 | 0.58 | 3.46 | 6.08 | 0 |
| `/json/worker` | 200,750 | 0.60 | 7.30 | 12.16 | 0 |

CPU profile: [profile.md](profile.md)
