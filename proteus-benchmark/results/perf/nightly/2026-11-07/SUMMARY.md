# Nightly 2026-11-07

- status: OK
- head: b259dc4
- tests: 215 total, 0 failed, 0 errored
- baseline: 1 better, 5 worse, 6 overlap
- previous: 2026-11-06: 1 better, 0 worse
- blocking_tail: complete p99.9 114559 us, max 304383 us; wrk p99 169.52ms
- rss_peak_mib: 1312
- notes: complete

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,057k [4,011-4,086] | 9.2 | 0 | 4,162k [4,122-4,181] | 8.0 | 0 | +2.6% | better |
| /plaintext | 16 | 1024 | 4,074k [4,062-4,149] | 11.1 | 0 | 4,180k [2,609-4,197] | 11.1 | 0 | +2.6% | overlap |
| /plaintext | 16 | 4096 | 4,031k [4,016-4,114] | 30.3 | 0 | 4,101k [2,433-4,140] | 29.6 | 0 | +1.7% | overlap |
| /plaintext | 1 | 256 | 1,786k [1,781-1,833] | 1.8 | 0 | 1,800k [1,792-1,836] | 1.8 | 0 | +0.8% | overlap |
| /plaintext | 1 | 1024 | 1,894k [1,894-1,899] | 4.4 | 0 | 1,885k [1,867-1,901] | 4.4 | 0 | -0.5% | overlap |
| /plaintext/exchange | 1 | 256 | 1,937k [1,915-1,940] | 1.8 | 0 | 1,925k [1,871-1,928] | 1.8 | 0 | -0.6% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,033k [2,029-2,033] | 4.2 | 0 | 2,028k [1,998-2,032] | 4.1 | 0 | -0.2% | overlap |
| /json | 1 | 64 | 779k [691-808] | 0.1 | 0 | 811k [727-818] | 0.1 | 0 | +4.1% | overlap |
| /json | 1 | 256 | 1,836k [1,775-1,855] | 1.7 | 0 | 1,802k [1,788-1,840] | 2.0 | 0 | -1.9% | overlap |
| /json | 1 | 512 | 1,899k [1,885-1,914] | 4.2 | 0 | 1,887k [1,848-1,927] | 4.2 | 0 | -0.6% | overlap |
| /json/blocking | 1 | 256 | 1,178k [1,158-1,205] | 2.5 | 0 | 1,174k [1,063-1,179] | 2.9 | 0 | -0.4% | overlap |
| /json/blocking | 1 | 1024 | 627k [622-638] | 125.4 | 0 | 620k [471-647] | 148.9 | 0 | -1.1% | overlap |

nightly vs previous: improved 1, regressed 0, errors 0 -> ACCEPT
