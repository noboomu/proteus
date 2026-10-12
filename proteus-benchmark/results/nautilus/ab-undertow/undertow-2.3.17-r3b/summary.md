# Proteus benchmark ab/undertow-2.3.17-r3b

- host `nautilus`, commit `ab-2.3.17`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `defaults`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 19.26 14.78 9.66, cpu idle 81.9%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 335,381 | 0.60 | 3.82 | 6.46 | 0 |
| `/json/response` | 340,304 | 0.61 | 3.64 | 6.60 | 0 |
| `/plaintext/undertow` | 347,906 | 0.60 | 3.69 | 6.53 | 0 |
| `/json/worker` | 192,341 | 0.67 | 7.11 | 11.57 | 0 |
| `/plaintext/blocking` | 186,143 | 0.69 | 7.48 | 11.50 | 0 |

CPU profile: [profile.md](profile.md)
