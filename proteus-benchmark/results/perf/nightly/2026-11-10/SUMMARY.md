# Nightly 2026-11-10

- status: REGRESSED
- head: 84f5b09
- tests: 216 total, 0 failed, 0 errored
- baseline: 1 better, 6 worse, 5 overlap
- previous: 2026-11-09: 0 better, 3 worse
- blocking_tail: complete p99.9 88575 us, max 289791 us; wrk p99 144.44ms
- rss_peak_mib: 1322
- notes: cells below previous nightly range

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,086k [4,070-4,202] | 7.8 | 0 | 3,996k [3,940-4,153] | 8.7 | 0 | -2.2% | overlap |
| /plaintext | 16 | 1024 | 4,138k [4,105-4,188] | 11.1 | 0 | 4,028k [3,917-4,171] | 11.1 | 0 | -2.7% | overlap |
| /plaintext | 16 | 4096 | 4,080k [4,032-4,175] | 29.4 | 0 | 4,002k [3,833-4,104] | 30.4 | 0 | -1.9% | overlap |
| /plaintext | 1 | 256 | 1,813k [1,798-1,840] | 1.8 | 0 | 1,763k [1,734-1,794] | 2.0 | 0 | -2.8% | WORSE |
| /plaintext | 1 | 1024 | 1,894k [1,884-1,907] | 4.4 | 0 | 1,867k [1,851-1,885] | 4.7 | 0 | -1.4% | overlap |
| /plaintext/exchange | 1 | 256 | 1,918k [1,907-1,924] | 1.8 | 0 | 1,909k [1,872-1,916] | 1.7 | 0 | -0.5% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,032k [2,021-2,033] | 4.1 | 0 | 2,005k [1,976-2,013] | 4.4 | 0 | -1.4% | WORSE |
| /json | 1 | 64 | 749k [710-825] | 0.1 | 0 | 801k [722-808] | 0.1 | 0 | +7.0% | overlap |
| /json | 1 | 256 | 1,828k [1,823-1,838] | 1.8 | 0 | 1,769k [1,744-1,806] | 2.0 | 0 | -3.2% | WORSE |
| /json | 1 | 512 | 1,885k [1,882-1,893] | 4.2 | 0 | 1,855k [1,840-1,882] | 4.3 | 0 | -1.6% | overlap |
| /json/blocking | 1 | 256 | 1,170k [1,056-1,177] | 3.0 | 0 | 1,147k [996-1,155] | 2.1 | 0 | -2.0% | overlap |
| /json/blocking | 1 | 1024 | 640k [472-651] | 157.6 | 0 | 627k [480-637] | 162.5 | 0 | -1.9% | overlap |

nightly vs previous: improved 0, regressed 3, errors 0 -> REJECT
