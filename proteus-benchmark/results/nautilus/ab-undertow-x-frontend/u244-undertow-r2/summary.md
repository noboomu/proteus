# Proteus benchmark ab4/u244-undertow-r2

- host `nautilus`, commit `u244`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 19.01 14.24 7.30, cpu idle 88.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 258,338 | 0.76 | 4.23 | 11.12 | 0 |
| `/json/response` | 292,119 | 0.73 | 2.90 | 5.54 | 0 |
| `/json/worker` | 194,946 | 0.68 | 6.60 | 10.41 | 0 |

CPU profile: [profile.md](profile.md)
