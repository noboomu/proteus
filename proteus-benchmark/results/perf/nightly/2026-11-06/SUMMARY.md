# Nightly 2026-11-06

- status: REGRESSED
- head: ee86fce
- tests: 215 total, 0 failed, 0 errored
- baseline: 3 better, 5 worse, 4 overlap
- previous: 2026-11-05: 1 better, 1 worse
- blocking_tail: complete p99.9 109503 us, max 325375 us; wrk p99 167.90ms
- rss_peak_mib: 1310
- notes: cells below previous nightly range

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,154k [4,096-4,177] | 8.0 | 0 | 4,057k [4,011-4,086] | 9.2 | 0 | -2.3% | WORSE |
| /plaintext | 16 | 1024 | 4,127k [4,122-4,159] | 11.4 | 0 | 4,074k [4,062-4,149] | 11.1 | 0 | -1.3% | overlap |
| /plaintext | 16 | 4096 | 2,503k [2,447-2,547] | 38.2 | 0 | 4,031k [4,016-4,114] | 30.3 | 0 | +61.0% | better |
| /plaintext | 1 | 256 | 1,828k [1,819-1,851] | 1.9 | 0 | 1,786k [1,781-1,833] | 1.8 | 0 | -2.3% | overlap |
| /plaintext | 1 | 1024 | 1,895k [1,883-1,905] | 4.5 | 0 | 1,894k [1,894-1,899] | 4.4 | 0 | -0.1% | overlap |
| /plaintext/exchange | 1 | 256 | 1,914k [1,898-1,955] | 1.6 | 0 | 1,937k [1,915-1,940] | 1.8 | 0 | +1.2% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,029k [2,028-2,038] | 4.2 | 0 | 2,033k [2,029-2,033] | 4.2 | 0 | +0.2% | overlap |
| /json | 1 | 64 | 811k [793-820] | 0.1 | 0 | 779k [691-808] | 0.1 | 0 | -3.9% | overlap |
| /json | 1 | 256 | 1,825k [1,812-1,853] | 1.7 | 0 | 1,836k [1,775-1,855] | 1.7 | 0 | +0.6% | overlap |
| /json | 1 | 512 | 1,893k [1,874-1,902] | 4.2 | 0 | 1,899k [1,885-1,914] | 4.2 | 0 | +0.3% | overlap |
| /json/blocking | 1 | 256 | 1,184k [1,032-1,196] | 2.9 | 0 | 1,178k [1,158-1,205] | 2.5 | 0 | -0.5% | overlap |
| /json/blocking | 1 | 1024 | 616k [467-641] | 129.9 | 0 | 627k [622-638] | 125.4 | 0 | +1.7% | overlap |

nightly vs previous: improved 1, regressed 1, errors 0 -> REJECT
