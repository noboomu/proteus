# Proteus benchmark ab/undertow-2.4.4-r3b

- host `nautilus`, commit `ab-2.4.4`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.4.4.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `defaults`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 21.27 13.47 8.77, cpu idle 94.6%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 293,373 | 0.76 | 3.24 | 5.85 | 0 |
| `/json/response` | 296,172 | 0.73 | 3.08 | 6.21 | 0 |
| `/plaintext/undertow` | 300,846 | 0.73 | 3.22 | 5.53 | 0 |
| `/json/worker` | 188,180 | 0.71 | 6.83 | 10.74 | 0 |
| `/plaintext/blocking` | 180,284 | 0.71 | 7.65 | 11.79 | 0 |

CPU profile: [profile.md](profile.md)
