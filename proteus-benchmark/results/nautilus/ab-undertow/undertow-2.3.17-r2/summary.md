# Proteus benchmark ab/undertow-2.3.17-r2

- host `nautilus`, commit `ab-2.3.17`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `defaults`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 19.23 10.48 5.15, cpu idle 74.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 335,220 | 0.62 | 3.87 | 7.04 | 0 |
| `/json/response` | 339,822 | 0.59 | 3.80 | 6.88 | 0 |
| `/plaintext/undertow` | 349,056 | 0.59 | 3.73 | 6.55 | 0 |
| `/json/worker` | 195,345 | 0.68 | 6.78 | 10.38 | 0 |
| `/plaintext/blocking` | 186,531 | 0.66 | 7.77 | 11.47 | 0 |

CPU profile: [profile.md](profile.md)
