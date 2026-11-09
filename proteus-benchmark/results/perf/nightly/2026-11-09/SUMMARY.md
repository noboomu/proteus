# Nightly 2026-11-09

- status: OK
- head: 8e7973f
- tests: 215 total, 0 failed, 0 errored
- baseline: 2 better, 5 worse, 5 overlap
- previous: 2026-11-08: 6 better, 0 worse
- blocking_tail: complete p99.9 79103 us, max 330495 us; wrk p99 153.88ms
- rss_peak_mib: 1312
- notes: complete

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,084k [4,082-4,141] | 8.3 | 0 | 4,086k [4,070-4,202] | 7.8 | 0 | +0.0% | overlap |
| /plaintext | 16 | 1024 | 4,066k [3,914-4,130] | 10.9 | 0 | 4,138k [4,105-4,188] | 11.1 | 0 | +1.8% | overlap |
| /plaintext | 16 | 4096 | 3,790k [2,404-3,879] | 30.8 | 0 | 4,080k [4,032-4,175] | 29.4 | 0 | +7.7% | better |
| /plaintext | 1 | 256 | 1,760k [1,412-1,785] | 2.6 | 0 | 1,813k [1,798-1,840] | 1.8 | 0 | +3.0% | better |
| /plaintext | 1 | 1024 | 1,851k [1,428-1,862] | 4.4 | 0 | 1,894k [1,884-1,907] | 4.4 | 0 | +2.3% | better |
| /plaintext/exchange | 1 | 256 | 1,911k [1,500-1,920] | 1.8 | 0 | 1,918k [1,907-1,924] | 1.8 | 0 | +0.4% | overlap |
| /plaintext/exchange | 1 | 1024 | 1,985k [1,528-1,997] | 4.1 | 0 | 2,032k [2,021-2,033] | 4.1 | 0 | +2.4% | better |
| /json | 1 | 64 | 711k [697-789] | 0.1 | 0 | 749k [710-825] | 0.1 | 0 | +5.3% | overlap |
| /json | 1 | 256 | 1,770k [1,433-1,777] | 2.2 | 0 | 1,828k [1,823-1,838] | 1.8 | 0 | +3.3% | better |
| /json | 1 | 512 | 1,837k [1,446-1,854] | 4.1 | 0 | 1,885k [1,882-1,893] | 4.2 | 0 | +2.6% | better |
| /json/blocking | 1 | 256 | 1,148k [1,076-1,163] | 2.9 | 0 | 1,170k [1,056-1,177] | 3.0 | 0 | +1.9% | overlap |
| /json/blocking | 1 | 1024 | 627k [493-659] | 162.3 | 0 | 640k [472-651] | 157.6 | 0 | +2.1% | overlap |

nightly vs previous: improved 6, regressed 0, errors 0 -> ACCEPT
