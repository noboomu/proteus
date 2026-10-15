# Proteus benchmark ab4/u244-proteus-r3

- host `nautilus`, commit `u244`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 20.77 17.68 10.51, cpu idle 94.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 338,654 | 0.60 | 3.82 | 6.80 | 0 |
| `/json/response` | 341,907 | 0.60 | 3.69 | 6.30 | 0 |
| `/json/worker` | 218,027 | 0.53 | 6.91 | 11.20 | 0 |

CPU profile: [profile.md](profile.md)
