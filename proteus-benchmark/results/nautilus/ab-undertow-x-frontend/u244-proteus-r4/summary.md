# Proteus benchmark ab4/u244-proteus-r4

- host `nautilus`, commit `u244`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 18.54 18.30 12.32, cpu idle 89.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 330,202 | 0.62 | 3.79 | 6.55 | 0 |
| `/json/response` | 334,057 | 0.62 | 3.67 | 6.62 | 0 |
| `/json/worker` | 210,012 | 0.56 | 7.16 | 11.57 | 0 |

CPU profile: [profile.md](profile.md)
