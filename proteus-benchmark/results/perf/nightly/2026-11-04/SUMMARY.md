# Nightly 2026-11-04

- status: OK
- head: 5954837
- tests: 215 total, 0 failed, 0 errored
- baseline: 1 better, 6 worse, 5 overlap
- previous: 2026-11-03: 0 better, 0 worse
- blocking_tail: complete p99.9 171007 us, max 326399 us; wrk p99 200.41ms
- rss_peak_mib: 1300
- notes: complete

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,033k [3,900-4,111] | 7.2 | 0 | 4,168k [2,865-4,169] | 6.9 | 0 | +3.3% | overlap |
| /plaintext | 16 | 1024 | 3,974k [3,888-4,094] | 11.6 | 0 | 4,142k [3,910-4,152] | 10.9 | 0 | +4.2% | overlap |
| /plaintext | 16 | 4096 | 4,042k [3,850-4,045] | 30.6 | 0 | 4,102k [3,847-4,104] | 29.6 | 0 | +1.5% | overlap |
| /plaintext | 1 | 256 | 1,797k [1,756-1,810] | 1.8 | 0 | 1,831k [1,645-1,834] | 2.2 | 0 | +1.9% | overlap |
| /plaintext | 1 | 1024 | 1,902k [1,846-1,905] | 4.5 | 0 | 1,894k [1,863-1,900] | 4.5 | 0 | -0.5% | overlap |
| /plaintext/exchange | 1 | 256 | 1,891k [1,355-1,921] | 1.7 | 0 | 1,930k [1,907-1,947] | 1.8 | 0 | +2.1% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,044k [2,004-2,052] | 4.1 | 0 | 2,033k [1,993-2,048] | 4.1 | 0 | -0.5% | overlap |
| /json | 1 | 64 | 804k [788-807] | 0.1 | 0 | 765k [725-807] | 0.1 | 0 | -4.9% | overlap |
| /json | 1 | 256 | 1,813k [1,781-1,828] | 1.8 | 0 | 1,793k [1,776-1,834] | 1.8 | 0 | -1.1% | overlap |
| /json | 1 | 512 | 1,878k [1,842-1,902] | 4.3 | 0 | 1,920k [1,841-1,921] | 4.1 | 0 | +2.2% | overlap |
| /json/blocking | 1 | 256 | 1,184k [1,072-1,187] | 2.3 | 0 | 1,168k [1,167-1,170] | 2.8 | 0 | -1.4% | overlap |
| /json/blocking | 1 | 1024 | 659k [622-668] | 147.8 | 0 | 632k [628-665] | 133.9 | 0 | -4.2% | overlap |

nightly vs previous: improved 0, regressed 0, errors 0 -> REJECT
