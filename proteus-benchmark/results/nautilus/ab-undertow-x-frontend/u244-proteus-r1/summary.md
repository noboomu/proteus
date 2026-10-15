# Proteus benchmark ab4/u244-proteus-r1

- host `nautilus`, commit `u244`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 21.02 11.04 5.10, cpu idle 51.5%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 347,273 | 0.60 | 3.69 | 6.61 | 0 |
| `/json/response` | 351,892 | 0.59 | 3.60 | 5.97 | 0 |
| `/json/worker` | 211,131 | 0.62 | 6.61 | 10.45 | 0 |

CPU profile: [profile.md](profile.md)
