# Nightly 2026-11-17

- status: OK
- head: 72b81db
- tests: 216 total, 0 failed, 0 errored
- baseline: 3 better, 5 worse, 4 overlap
- previous: 2026-11-16: 4 better, 0 worse
- blocking_tail: complete p99.9 61311 us, max 279807 us; wrk p99 146.57ms
- rss_peak_mib: 1301
- notes: complete

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 3,826k [3,741-4,113] | 7.1 | 0 | 4,061k [4,061-4,147] | 8.4 | 0 | +6.2% | overlap |
| /plaintext | 16 | 1024 | 3,927k [3,780-4,123] | 11.0 | 0 | 4,109k [4,079-4,145] | 11.1 | 0 | +4.6% | overlap |
| /plaintext | 16 | 4096 | 3,841k [3,724-4,073] | 31.7 | 0 | 4,041k [4,006-4,070] | 29.5 | 0 | +5.2% | overlap |
| /plaintext | 1 | 256 | 1,761k [1,760-1,769] | 1.8 | 0 | 1,786k [1,775-1,792] | 1.8 | 0 | +1.4% | better |
| /plaintext | 1 | 1024 | 1,835k [1,834-1,887] | 4.6 | 0 | 1,889k [1,877-1,889] | 4.7 | 0 | +2.9% | overlap |
| /plaintext/exchange | 1 | 256 | 1,839k [1,832-1,922] | 1.5 | 0 | 1,896k [1,887-1,901] | 1.6 | 0 | +3.1% | overlap |
| /plaintext/exchange | 1 | 1024 | 1,970k [1,958-2,004] | 4.5 | 0 | 2,017k [2,013-2,025] | 4.5 | 0 | +2.4% | better |
| /json | 1 | 64 | 789k [784-810] | 0.1 | 0 | 823k [711-831] | 0.1 | 0 | +4.4% | overlap |
| /json | 1 | 256 | 1,755k [1,755-1,756] | 2.0 | 0 | 1,802k [1,770-1,812] | 1.7 | 0 | +2.7% | better |
| /json | 1 | 512 | 1,844k [1,836-1,850] | 4.5 | 0 | 1,873k [1,870-1,873] | 4.5 | 0 | +1.6% | better |
| /json/blocking | 1 | 256 | 1,152k [1,150-1,166] | 2.1 | 0 | 1,165k [1,151-1,172] | 2.2 | 0 | +1.1% | overlap |
| /json/blocking | 1 | 1024 | 647k [634-652] | 155.4 | 0 | 644k [621-664] | 138.1 | 0 | -0.5% | overlap |

nightly vs previous: improved 4, regressed 0, errors 0 -> ACCEPT
