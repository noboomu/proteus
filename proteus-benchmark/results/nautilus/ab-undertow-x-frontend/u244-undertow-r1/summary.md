# Proteus benchmark ab4/u244-undertow-r1

- host `nautilus`, commit `u244`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 20.53 9.09 4.16, cpu idle 70.4%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 302,298 | 0.74 | 3.40 | 5.96 | 0 |
| `/json/response` | 301,269 | 0.71 | 3.55 | 7.01 | 0 |
| `/json/worker` | 199,593 | 0.62 | 6.91 | 11.21 | 0 |

CPU profile: [profile.md](profile.md)
