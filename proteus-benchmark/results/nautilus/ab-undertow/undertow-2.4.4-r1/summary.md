# Proteus benchmark ab/undertow-2.4.4-r1

- host `nautilus`, commit `ab-2.4.4`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `defaults`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 16.14 7.50 3.78, cpu idle 96.0%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 303,747 | 0.72 | 3.58 | 5.72 | 0 |
| `/json/response` | 306,551 | 0.71 | 3.21 | 5.93 | 0 |
| `/plaintext/undertow` | 309,076 | 0.69 | 3.64 | 6.35 | 0 |
| `/json/worker` | 202,006 | 0.62 | 6.82 | 11.28 | 0 |
| `/plaintext/blocking` | 187,379 | 0.70 | 7.29 | 11.43 | 0 |

CPU profile: [profile.md](profile.md)
