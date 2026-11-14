# Nightly 2026-11-14

- status: OK
- head: 972b463
- tests: 216 total, 0 failed, 0 errored
- baseline: 1 better, 5 worse, 6 overlap
- previous: 2026-11-13: 0 better, 0 worse
- blocking_tail: complete p99.9 43167 us, max 301311 us; wrk p99 133.41ms
- rss_peak_mib: 1343
- notes: complete

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,090k [4,085-4,111] | 7.3 | 0 | 4,121k [3,808-4,179] | 8.1 | 0 | +0.8% | overlap |
| /plaintext | 16 | 1024 | 4,106k [4,090-4,151] | 10.6 | 0 | 4,140k [3,844-4,161] | 10.8 | 0 | +0.8% | overlap |
| /plaintext | 16 | 4096 | 4,033k [4,028-4,077] | 30.8 | 0 | 4,100k [3,772-4,106] | 30.1 | 0 | +1.7% | overlap |
| /plaintext | 1 | 256 | 1,783k [1,766-1,795] | 1.8 | 0 | 1,775k [1,746-1,802] | 1.9 | 0 | -0.4% | overlap |
| /plaintext | 1 | 1024 | 1,879k [1,870-1,882] | 4.5 | 0 | 1,883k [1,842-1,902] | 4.5 | 0 | +0.2% | overlap |
| /plaintext/exchange | 1 | 256 | 1,861k [1,848-1,887] | 1.7 | 0 | 1,895k [1,850-1,898] | 1.8 | 0 | +1.8% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,002k [1,992-2,017] | 4.3 | 0 | 2,010k [1,955-2,021] | 4.4 | 0 | +0.4% | overlap |
| /json | 1 | 64 | 797k [790-816] | 0.1 | 0 | 800k [733-812] | 0.1 | 0 | +0.4% | overlap |
| /json | 1 | 256 | 1,761k [1,756-1,770] | 1.9 | 0 | 1,772k [1,733-1,818] | 1.8 | 0 | +0.6% | overlap |
| /json | 1 | 512 | 1,866k [1,831-1,867] | 4.5 | 0 | 1,871k [1,833-1,884] | 4.3 | 0 | +0.3% | overlap |
| /json/blocking | 1 | 256 | 1,171k [1,152-1,190] | 2.1 | 0 | 1,172k [1,084-1,173] | 2.2 | 0 | +0.1% | overlap |
| /json/blocking | 1 | 1024 | 617k [614-632] | 113.0 | 0 | 629k [534-645] | 147.3 | 0 | +1.9% | overlap |

nightly vs previous: improved 0, regressed 0, errors 0 -> REJECT
