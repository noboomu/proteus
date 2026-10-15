# Proteus benchmark ab4/u2317-undertow-r3

- host `nautilus`, commit `u2317`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 20.62 15.94 8.67, cpu idle 94.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 328,168 | 0.64 | 3.92 | 6.62 | 0 |
| `/json/response` | 325,030 | 0.61 | 3.81 | 9.07 | 0 |
| `/json/worker` | 187,453 | 0.72 | 7.06 | 11.23 | 0 |

CPU profile: [profile.md](profile.md)
