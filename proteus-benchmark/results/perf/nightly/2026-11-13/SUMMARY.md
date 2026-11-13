# Nightly 2026-11-13

- status: REGRESSED
- head: 1d5b020
- tests: 216 total, 0 failed, 0 errored
- baseline: 3 better, 6 worse, 3 overlap
- previous: 2026-11-12: 2 better, 2 worse
- blocking_tail: complete p99.9 68479 us, max 362751 us; wrk p99 163.25ms
- rss_peak_mib: 1306
- notes: cells below previous nightly range

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,149k [4,008-4,163] | 10.4 | 0 | 4,090k [4,085-4,111] | 7.3 | 0 | -1.4% | overlap |
| /plaintext | 16 | 1024 | 4,151k [3,999-4,190] | 11.1 | 0 | 4,106k [4,090-4,151] | 10.6 | 0 | -1.1% | overlap |
| /plaintext | 16 | 4096 | 4,045k [3,981-4,148] | 30.6 | 0 | 4,033k [4,028-4,077] | 30.8 | 0 | -0.3% | overlap |
| /plaintext | 1 | 256 | 1,781k [1,762-1,796] | 1.9 | 0 | 1,783k [1,766-1,795] | 1.8 | 0 | +0.1% | overlap |
| /plaintext | 1 | 1024 | 1,883k [1,882-1,887] | 4.6 | 0 | 1,879k [1,870-1,882] | 4.5 | 0 | -0.2% | overlap |
| /plaintext/exchange | 1 | 256 | 1,907k [1,876-1,921] | 1.8 | 0 | 1,861k [1,848-1,887] | 1.7 | 0 | -2.4% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,022k [2,010-2,029] | 4.2 | 0 | 2,002k [1,992-2,017] | 4.3 | 0 | -1.0% | overlap |
| /json | 1 | 64 | 804k [800-804] | 0.1 | 0 | 797k [790-816] | 0.1 | 0 | -0.9% | overlap |
| /json | 1 | 256 | 1,795k [1,793-1,799] | 1.9 | 0 | 1,761k [1,756-1,770] | 1.9 | 0 | -1.8% | WORSE |
| /json | 1 | 512 | 1,875k [1,869-1,911] | 4.3 | 0 | 1,866k [1,831-1,867] | 4.5 | 0 | -0.5% | WORSE |
| /json/blocking | 1 | 256 | 996k [992-1,011] | 4.0 | 0 | 1,171k [1,152-1,190] | 2.1 | 0 | +17.6% | better |
| /json/blocking | 1 | 1024 | 534k [462-560] | 65.3 | 0 | 617k [614-632] | 113.0 | 0 | +15.5% | better |

nightly vs previous: improved 2, regressed 2, errors 0 -> REJECT
