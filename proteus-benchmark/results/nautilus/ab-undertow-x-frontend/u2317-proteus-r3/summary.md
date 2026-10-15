# Proteus benchmark ab4/u2317-proteus-r3

- host `nautilus`, commit `u2317`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=proteus`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 20.65 16.65 9.32, cpu idle 85.5%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 333,376 | 0.60 | 3.85 | 6.74 | 0 |
| `/json/response` | 315,325 | 0.60 | 4.17 | 11.97 | 0 |
| `/json/worker` | 193,701 | 0.65 | 7.08 | 11.20 | 0 |

CPU profile: [profile.md](profile.md)
