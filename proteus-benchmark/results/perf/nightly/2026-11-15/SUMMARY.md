# Nightly 2026-11-15

- status: OK
- head: aedbfe5
- tests: 216 total, 0 failed, 0 errored
- baseline: 0 better, 7 worse, 5 overlap
- previous: 2026-11-14: 0 better, 0 worse
- blocking_tail: complete p99.9 84159 us, max 306175 us; wrk p99 159.73ms
- rss_peak_mib: 1297
- notes: complete

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 4,121k [3,808-4,179] | 8.1 | 0 | 4,080k [3,807-4,095] | 8.2 | 0 | -1.0% | overlap |
| /plaintext | 16 | 1024 | 4,140k [3,844-4,161] | 10.8 | 0 | 4,094k [3,818-4,144] | 10.7 | 0 | -1.1% | overlap |
| /plaintext | 16 | 4096 | 4,100k [3,772-4,106] | 30.1 | 0 | 4,025k [3,742-4,090] | 30.8 | 0 | -1.8% | overlap |
| /plaintext | 1 | 256 | 1,775k [1,746-1,802] | 1.9 | 0 | 1,752k [1,735-1,761] | 1.8 | 0 | -1.3% | overlap |
| /plaintext | 1 | 1024 | 1,883k [1,842-1,902] | 4.5 | 0 | 1,863k [1,826-1,887] | 4.7 | 0 | -1.0% | overlap |
| /plaintext/exchange | 1 | 256 | 1,895k [1,850-1,898] | 1.8 | 0 | 1,900k [1,847-1,918] | 1.8 | 0 | +0.3% | overlap |
| /plaintext/exchange | 1 | 1024 | 2,010k [1,955-2,021] | 4.4 | 0 | 1,994k [1,968-2,000] | 4.3 | 0 | -0.8% | overlap |
| /json | 1 | 64 | 800k [733-812] | 0.1 | 0 | 749k [737-796] | 0.1 | 0 | -6.4% | overlap |
| /json | 1 | 256 | 1,772k [1,733-1,818] | 1.8 | 0 | 1,767k [1,759-1,776] | 1.7 | 0 | -0.3% | overlap |
| /json | 1 | 512 | 1,871k [1,833-1,884] | 4.3 | 0 | 1,858k [1,840-1,880] | 4.4 | 0 | -0.7% | overlap |
| /json/blocking | 1 | 256 | 1,172k [1,084-1,173] | 2.2 | 0 | 1,147k [1,002-1,160] | 2.2 | 0 | -2.1% | overlap |
| /json/blocking | 1 | 1024 | 629k [534-645] | 147.3 | 0 | 622k [470-636] | 130.2 | 0 | -1.1% | overlap |

nightly vs previous: improved 0, regressed 0, errors 0 -> REJECT
