# Proteus benchmark ab4/u244-undertow-r3

- host `nautilus`, commit `u244`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 20.50 17.20 9.93, cpu idle 86.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 279,939 | 0.79 | 3.49 | 6.41 | 0 |
| `/json/response` | 276,757 | 0.81 | 2.57 | 7.07 | 0 |
| `/json/worker` | 191,330 | 0.73 | 6.45 | 9.90 | 0 |

CPU profile: [profile.md](profile.md)
