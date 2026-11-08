# Nightly 2026-11-08

- status: REGRESSED
- head: a79150d
- tests: 215 total, 0 failed, 0 errored
- baseline: 1 better, 7 worse, 4 overlap
- previous: 2026-11-07: 0 better, 4 worse
- blocking_tail: complete p99.9 90239 us, max 365055 us; wrk p99 175.10ms
- rss_peak_mib: 1312
- notes: cells below previous nightly range

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,162k [4,122-4,181] | 8.0 | 0 | 4,084k [4,082-4,141] | 8.3 | 0 | -1.9% | overlap |
| /plaintext | 16 | 1024 | 4,180k [2,609-4,197] | 11.1 | 0 | 4,066k [3,914-4,130] | 10.9 | 0 | -2.7% | overlap |
| /plaintext | 16 | 4096 | 4,101k [2,433-4,140] | 29.6 | 0 | 3,790k [2,404-3,879] | 30.8 | 0 | -7.6% | overlap |
| /plaintext | 1 | 256 | 1,800k [1,792-1,836] | 1.8 | 0 | 1,760k [1,412-1,785] | 2.6 | 0 | -2.2% | WORSE |
| /plaintext | 1 | 1024 | 1,885k [1,867-1,901] | 4.4 | 0 | 1,851k [1,428-1,862] | 4.4 | 0 | -1.8% | WORSE |
| /plaintext/exchange | 1 | 256 | 1,925k [1,871-1,928] | 1.8 | 0 | 1,911k [1,500-1,920] | 1.8 | 0 | -0.7% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,028k [1,998-2,032] | 4.1 | 0 | 1,985k [1,528-1,997] | 4.1 | 0 | -2.1% | WORSE |
| /json | 1 | 64 | 811k [727-818] | 0.1 | 0 | 711k [697-789] | 0.1 | 0 | -12.3% | overlap |
| /json | 1 | 256 | 1,802k [1,788-1,840] | 2.0 | 0 | 1,770k [1,433-1,777] | 2.2 | 0 | -1.8% | WORSE |
| /json | 1 | 512 | 1,887k [1,848-1,927] | 4.2 | 0 | 1,837k [1,446-1,854] | 4.1 | 0 | -2.6% | overlap |
| /json/blocking | 1 | 256 | 1,174k [1,063-1,179] | 2.9 | 0 | 1,148k [1,076-1,163] | 2.9 | 0 | -2.2% | overlap |
| /json/blocking | 1 | 1024 | 620k [471-647] | 148.9 | 0 | 627k [493-659] | 162.3 | 0 | +1.2% | overlap |

nightly vs previous: improved 0, regressed 4, errors 0 -> REJECT
