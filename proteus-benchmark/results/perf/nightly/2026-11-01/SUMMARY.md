# Nightly 2026-11-01

- status: REGRESSED
- head: 54ec377
- tests: 215 total, 0 failed, 0 errored
- baseline: 3 better, 6 worse, 3 overlap
- previous: 2026-10-31: 1 better, 1 worse
- blocking_tail: complete p99.9 5067 us, max 296191 us; wrk p99 159.08ms
- rss_peak_mib: 1339
- notes: cells below previous nightly range

## Versus previous nightly

| route | pipeline | conns | previous median [min-max] | p99 ms | err | nightly median [min-max] | p99 ms | err | delta | verdict |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|
| /plaintext | 16 | 256 | 6,074k [5,922-6,076] | 0.0 | 0 | 6,000k [5,973-6,050] | 0.0 | 0 | -1.2% | overlap |
| /plaintext | 16 | 1024 | 6,374k [6,312-6,428] | 63.6 | 0 | 6,357k [6,259-6,390] | 98.5 | 0 | -0.3% | overlap |
| /plaintext | 16 | 4096 | 5,578k [5,558-5,648] | 25.8 | 0 | 5,695k [5,667-5,823] | 26.1 | 0 | +2.1% | better |
| /plaintext | 1 | 256 | 1,923k [1,590-1,945] | 1.2 | 0 | 1,908k [1,592-1,917] | 1.3 | 0 | -0.8% | overlap |
| /plaintext | 1 | 1024 | 2,148k [2,138-2,159] | 4.2 | 0 | 2,139k [2,031-2,145] | 4.2 | 0 | -0.5% | overlap |
| /plaintext/exchange | 1 | 256 | 2,002k [1,988-2,014] | 1.1 | 0 | 1,934k [1,906-1,955] | 1.1 | 0 | -3.4% | WORSE |
| /plaintext/exchange | 1 | 1024 | 2,228k [2,228-2,236] | 4.2 | 0 | 2,220k [2,211-2,238] | 4.3 | 0 | -0.4% | overlap |
| /json | 1 | 64 | 822k [488-841] | 0.2 | 0 | 802k [789-815] | 0.2 | 0 | -2.4% | overlap |
| /json | 1 | 256 | 1,935k [1,900-1,940] | 1.2 | 0 | 1,865k [1,633-1,924] | 1.3 | 0 | -3.6% | overlap |
| /json | 1 | 512 | 2,114k [2,106-2,116] | 3.7 | 0 | 2,090k [1,881-2,112] | 3.7 | 0 | -1.1% | overlap |
| /json/blocking | 1 | 256 | 1,220k [1,203-1,229] | 1.6 | 0 | 1,215k [1,202-1,222] | 1.6 | 0 | -0.3% | overlap |
| /json/blocking | 1 | 1024 | 585k [581-587] | 98.3 | 0 | 596k [586-598] | 95.9 | 0 | +1.9% | overlap |

nightly vs previous: improved 1, regressed 1, errors 0 -> REJECT
