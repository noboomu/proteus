# Proteus benchmark ab/undertow-2.3.17-r2b

- host `nautilus`, commit `ab-2.3.17`
- openjdk version "27" 2026-09-15
- Intel(R) Core(TM) i7-7820X CPU @ 3.60GHz (16 logical, 8 physical cores)
- Undertow 2.3.17.Final, worker: 8 I/O threads, platform task executor with 80 threads
- jvm opts: `defaults`
- oha 1.16.0, 10s warmup=5s concurrency=256 repeat=1
- host load before run: 15.63 10.52 7.46, cpu idle 84.5%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/response` | 353,229 | 0.57 | 3.73 | 5.98 | 0 |
| `/json/response` | 359,075 | 0.60 | 3.53 | 5.96 | 0 |
| `/plaintext/undertow` | 359,339 | 0.55 | 3.66 | 6.00 | 0 |
| `/json/worker` | 200,020 | 0.63 | 7.00 | 11.35 | 0 |
| `/plaintext/blocking` | 192,015 | 0.64 | 7.53 | 11.24 | 0 |

CPU profile: [profile.md](profile.md)
