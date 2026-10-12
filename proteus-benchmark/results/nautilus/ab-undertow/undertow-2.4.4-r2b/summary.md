# Proteus benchmark ab/undertow-2.4.4-r2b

- host `nautilus`, commit `ab-2.4.4`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `defaults`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 1.47 7.46 6.28, cpu idle 89.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 309,905 | 0.72 | 3.65 | 6.09 | 0 |
| `/json/response` | 320,615 | 0.73 | 2.22 | 4.76 | 0 |
| `/plaintext/undertow` | 325,084 | 0.70 | 2.44 | 4.90 | 0 |
| `/json/worker` | 200,560 | 0.64 | 6.75 | 10.85 | 0 |
| `/plaintext/blocking` | 189,364 | 0.66 | 7.35 | 11.32 | 0 |

CPU profile: [profile.md](profile.md)
