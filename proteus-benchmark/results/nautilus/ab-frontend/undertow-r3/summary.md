# Proteus benchmark ab-fe/undertow-r3

- host `nautilus`, commit `http1`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `-Dundertow.frontEnd=undertow`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 2.95 11.08 8.13, cpu idle 95.3%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 358,147 | 0.58 | 3.61 | 6.55 | 0 |
| `/json/response` | 358,878 | 0.59 | 3.62 | 5.93 | 0 |
| `/plaintext/undertow` | 370,541 | 0.55 | 3.34 | 5.82 | 0 |
| `/json/worker` | 197,984 | 0.67 | 6.78 | 10.76 | 0 |
| `/plaintext/blocking` | 187,852 | 0.64 | 7.90 | 12.06 | 0 |

CPU profile: [profile.md](profile.md)
