# Nightly 2026-11-12

- status: REGRESSED
- head: 3531c63
- tests: 216 total, 0 failed, 0 errored
- baseline: 1 better, 6 worse, 5 overlap
- previous: 2026-11-11: 0 better, 2 worse
- blocking_tail: complete p99.9 73279 us, max 343295 us; wrk p99 161.10ms
- rss_peak_mib: 1312
- notes: cells below previous nightly range

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,122k [4,081-4,143] | 7.6 | 0 | 4,149k [4,008-4,163] | 10.4 | 0 | +0.7% | overlap |
| /plaintext | 16 | 1024 | 4,104k [4,089-4,104] | 10.7 | 0 | 4,151k [3,999-4,190] | 11.1 | 0 | +1.2% | overlap |
| /plaintext | 16 | 4096 | 4,062k [4,021-4,083] | 29.8 | 0 | 4,045k [3,981-4,148] | 30.6 | 0 | -0.4% | overlap |
| /plaintext | 1 | 256 | 1,797k [1,763-1,806] | 2.0 | 0 | 1,781k [1,762-1,796] | 1.9 | 0 | -0.9% | overlap |
| /plaintext | 1 | 1024 | 1,873k [1,868-1,887] | 4.8 | 0 | 1,883k [1,882-1,887] | 4.6 | 0 | +0.5% | overlap |
| /plaintext/exchange | 1 | 256 | 1,894k [1,848-1,897] | 1.8 | 0 | 1,907k [1,876-1,921] | 1.8 | 0 | +0.7% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,013k [1,986-2,021] | 4.4 | 0 | 2,022k [2,010-2,029] | 4.2 | 0 | +0.4% | overlap |
| /json | 1 | 64 | 785k [746-825] | 0.1 | 0 | 804k [800-804] | 0.1 | 0 | +2.4% | overlap |
| /json | 1 | 256 | 1,780k [1,755-1,806] | 1.9 | 0 | 1,795k [1,793-1,799] | 1.9 | 0 | +0.8% | overlap |
| /json | 1 | 512 | 1,868k [1,865-1,883] | 4.3 | 0 | 1,875k [1,869-1,911] | 4.3 | 0 | +0.4% | overlap |
| /json/blocking | 1 | 256 | 1,169k [1,163-1,173] | 2.3 | 0 | 996k [992-1,011] | 4.0 | 0 | -14.8% | WORSE |
| /json/blocking | 1 | 1024 | 624k [622-678] | 133.6 | 0 | 534k [462-560] | 65.3 | 0 | -14.4% | WORSE |

nightly vs previous: improved 0, regressed 2, errors 0 -> REJECT
