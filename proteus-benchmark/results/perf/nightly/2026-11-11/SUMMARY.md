# Nightly 2026-11-11

- status: OK
- head: e8dfd6c
- tests: 216 total, 0 failed, 0 errored
- baseline: 4 better, 4 worse, 4 overlap
- previous: 2026-11-10: 1 better, 0 worse
- blocking_tail: complete p99.9 105599 us, max 328959 us; wrk p99 169.40ms
- rss_peak_mib: 1297
- notes: complete

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 3,996k [3,940-4,153] | 8.7 | 0 | 4,122k [4,081-4,143] | 7.6 | 0 | +3.2% | overlap |
| /plaintext | 16 | 1024 | 4,028k [3,917-4,171] | 11.1 | 0 | 4,104k [4,089-4,104] | 10.7 | 0 | +1.9% | overlap |
| /plaintext | 16 | 4096 | 4,002k [3,833-4,104] | 30.4 | 0 | 4,062k [4,021-4,083] | 29.8 | 0 | +1.5% | overlap |
| /plaintext | 1 | 256 | 1,763k [1,734-1,794] | 2.0 | 0 | 1,797k [1,763-1,806] | 2.0 | 0 | +1.9% | overlap |
| /plaintext | 1 | 1024 | 1,867k [1,851-1,885] | 4.7 | 0 | 1,873k [1,868-1,887] | 4.8 | 0 | +0.3% | overlap |
| /plaintext/exchange | 1 | 256 | 1,909k [1,872-1,916] | 1.7 | 0 | 1,894k [1,848-1,897] | 1.8 | 0 | -0.8% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,005k [1,976-2,013] | 4.4 | 0 | 2,013k [1,986-2,021] | 4.4 | 0 | +0.4% | overlap |
| /json | 1 | 64 | 801k [722-808] | 0.1 | 0 | 785k [746-825] | 0.1 | 0 | -2.0% | overlap |
| /json | 1 | 256 | 1,769k [1,744-1,806] | 2.0 | 0 | 1,780k [1,755-1,806] | 1.9 | 0 | +0.6% | overlap |
| /json | 1 | 512 | 1,855k [1,840-1,882] | 4.3 | 0 | 1,868k [1,865-1,883] | 4.3 | 0 | +0.7% | overlap |
| /json/blocking | 1 | 256 | 1,147k [996-1,155] | 2.1 | 0 | 1,169k [1,163-1,173] | 2.3 | 0 | +1.9% | better |
| /json/blocking | 1 | 1024 | 627k [480-637] | 162.5 | 0 | 624k [622-678] | 133.6 | 0 | -0.5% | overlap |

nightly vs previous: improved 1, regressed 0, errors 0 -> ACCEPT
