# Nightly 2026-11-05

- status: REGRESSED
- head: 867c75d
- tests: 215 total, 0 failed, 0 errored
- baseline: 2 better, 4 worse, 6 overlap
- previous: 2026-11-04: 0 better, 1 worse
- blocking_tail: complete p99.9 113663 us, max 374015 us; wrk p99 177.32ms
- rss_peak_mib: 1303
- notes: cells below previous nightly range

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,168k [2,865-4,169] | 6.9 | 0 | 4,154k [4,096-4,177] | 8.0 | 0 | -0.3% | overlap |
| /plaintext | 16 | 1024 | 4,142k [3,910-4,152] | 10.9 | 0 | 4,127k [4,122-4,159] | 11.4 | 0 | -0.4% | overlap |
| /plaintext | 16 | 4096 | 4,102k [3,847-4,104] | 29.6 | 0 | 2,503k [2,447-2,547] | 38.2 | 0 | -39.0% | WORSE |
| /plaintext | 1 | 256 | 1,831k [1,645-1,834] | 2.2 | 0 | 1,828k [1,819-1,851] | 1.9 | 0 | -0.2% | overlap |
| /plaintext | 1 | 1024 | 1,894k [1,863-1,900] | 4.5 | 0 | 1,895k [1,883-1,905] | 4.5 | 0 | +0.1% | overlap |
| /plaintext/exchange | 1 | 256 | 1,930k [1,907-1,947] | 1.8 | 0 | 1,914k [1,898-1,955] | 1.6 | 0 | -0.8% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,033k [1,993-2,048] | 4.1 | 0 | 2,029k [2,028-2,038] | 4.2 | 0 | -0.2% | overlap |
| /json | 1 | 64 | 765k [725-807] | 0.1 | 0 | 811k [793-820] | 0.1 | 0 | +6.0% | overlap |
| /json | 1 | 256 | 1,793k [1,776-1,834] | 1.8 | 0 | 1,825k [1,812-1,853] | 1.7 | 0 | +1.8% | overlap |
| /json | 1 | 512 | 1,920k [1,841-1,921] | 4.1 | 0 | 1,893k [1,874-1,902] | 4.2 | 0 | -1.4% | overlap |
| /json/blocking | 1 | 256 | 1,168k [1,167-1,170] | 2.8 | 0 | 1,184k [1,032-1,196] | 2.9 | 0 | +1.4% | overlap |
| /json/blocking | 1 | 1024 | 632k [628-665] | 133.9 | 0 | 616k [467-641] | 129.9 | 0 | -2.4% | overlap |

nightly vs previous: improved 0, regressed 1, errors 0 -> REJECT
