# Nightly 2026-11-16

- status: REGRESSED
- head: 2a5d330
- tests: 216 total, 0 failed, 0 errored
- baseline: 1 better, 6 worse, 5 overlap
- previous: 2026-11-15: 0 better, 1 worse
- blocking_tail: complete p99.9 118143 us, max 339967 us; wrk p99 180.81ms
- rss_peak_mib: 1305
- notes: cells below previous nightly range

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,080k [3,807-4,095] | 8.2 | 0 | 3,826k [3,741-4,113] | 7.1 | 0 | -6.2% | overlap |
| /plaintext | 16 | 1024 | 4,094k [3,818-4,144] | 10.7 | 0 | 3,927k [3,780-4,123] | 11.0 | 0 | -4.1% | overlap |
| /plaintext | 16 | 4096 | 4,025k [3,742-4,090] | 30.8 | 0 | 3,841k [3,724-4,073] | 31.7 | 0 | -4.6% | overlap |
| /plaintext | 1 | 256 | 1,752k [1,735-1,761] | 1.8 | 0 | 1,761k [1,760-1,769] | 1.8 | 0 | +0.5% | overlap |
| /plaintext | 1 | 1024 | 1,863k [1,826-1,887] | 4.7 | 0 | 1,835k [1,834-1,887] | 4.6 | 0 | -1.5% | overlap |
| /plaintext/exchange | 1 | 256 | 1,900k [1,847-1,918] | 1.8 | 0 | 1,839k [1,832-1,922] | 1.5 | 0 | -3.2% | overlap |
| /plaintext/exchange | 1 | 1024 | 1,994k [1,968-2,000] | 4.3 | 0 | 1,970k [1,958-2,004] | 4.5 | 0 | -1.2% | overlap |
| /json | 1 | 64 | 749k [737-796] | 0.1 | 0 | 789k [784-810] | 0.1 | 0 | +5.4% | overlap |
| /json | 1 | 256 | 1,767k [1,759-1,776] | 1.7 | 0 | 1,755k [1,755-1,756] | 2.0 | 0 | -0.7% | WORSE |
| /json | 1 | 512 | 1,858k [1,840-1,880] | 4.4 | 0 | 1,844k [1,836-1,850] | 4.5 | 0 | -0.8% | overlap |
| /json/blocking | 1 | 256 | 1,147k [1,002-1,160] | 2.2 | 0 | 1,152k [1,150-1,166] | 2.1 | 0 | +0.4% | overlap |
| /json/blocking | 1 | 1024 | 622k [470-636] | 130.2 | 0 | 647k [634-652] | 155.4 | 0 | +4.1% | overlap |

nightly vs previous: improved 0, regressed 1, errors 0 -> REJECT
