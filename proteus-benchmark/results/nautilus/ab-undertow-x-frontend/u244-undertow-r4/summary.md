# Proteus benchmark ab4/u244-undertow-r4

- host `nautilus`, commit `u244`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 17.92 18.14 11.91, cpu idle 84.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 281,317 | 0.78 | 3.37 | 6.31 | 0 |
| `/json/response` | 287,904 | 0.77 | 2.98 | 6.01 | 0 |
| `/json/worker` | 191,681 | 0.67 | 6.86 | 10.86 | 0 |

CPU profile: [profile.md](profile.md)
