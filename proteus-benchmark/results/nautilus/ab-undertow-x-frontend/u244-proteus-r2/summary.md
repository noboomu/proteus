# Proteus benchmark ab4/u244-proteus-r2

- host `nautilus`, commit `u244`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 18.62 14.89 7.92, cpu idle 84.7%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 336,506 | 0.60 | 3.89 | 6.55 | 0 |
| `/json/response` | 334,477 | 0.59 | 3.92 | 7.59 | 0 |
| `/json/worker` | 211,354 | 0.55 | 7.10 | 11.37 | 0 |

CPU profile: [profile.md](profile.md)
