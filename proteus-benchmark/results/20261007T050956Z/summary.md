# Proteus benchmark 20261007T050956Z

- commit `7724a99`
- openjdk version "27" 2026-09-15
- AMD Ryzen Threadripper PRO 9965WX 24-Cores (24 cores)
- Undertow 2.3.17.Final
- oha 1.16.0, 15s warmup=5s concurrency=256
- host load before run: 20.81 21.36 18.28, cpu idle 27.2%

| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |
|---|---:|---:|---:|---:|---:|
| `/plaintext/exchange` | 166,637 | 0.25 | 12.52 | 145.98 | 0 |
| `/plaintext/response` | 172,306 | 0.19 | 11.36 | 95.68 | 0 |
| `/plaintext/undertow` | 170,807 | 0.32 | 13.05 | 128.31 | 0 |
| `/json/exchange` | 164,412 | 0.12 | 19.97 | 166.53 | 0 |
| `/json/response` | 139,225 | 0.27 | 13.90 | 156.50 | 0 |
| `/json/undertow` | 170,584 | 0.25 | 16.49 | 193.14 | 0 |
