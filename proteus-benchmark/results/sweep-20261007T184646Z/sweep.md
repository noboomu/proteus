# Proteus parameter sweep sweep-20261007T184646Z

Sorted by `/plaintext/undertow` req/s. Per-config details and CPU profiles in each subdirectory.

## Requests per second

| config | worker | idle% | json/blocking | json/exchange | json/response | json/undertow | json/worker | plaintext/blocking | plaintext/exchange | plaintext/response | plaintext/undertow | plaintext/worker |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| [io0.05_platform_w1_directfalse_buf16k_tlc0](io0.05_platform_w1_directfalse_buf16k_tlc0/summary.md) | 13 I/O threads, platform task executor with 256 threads | 98.6% | 492,643 | 955,770 | 946,953 | 969,808 | 567,915 | 505,701 | 979,392 | 932,501 | 1,030,212 | 655,106 |
| [io0.02_platform_w1_directfalse_buf16k_tlc4](io0.02_platform_w1_directfalse_buf16k_tlc4/summary.md) | 5 I/O threads, platform task executor with 256 threads | 98.5% | 169,005 | 493,652 | 488,267 | 551,391 | 635,722 | 168,412 | 555,255 | 535,866 | 556,864 | 634,355 |
| [io0.02_platform_w1_directtrue_buf16k_tlc4](io0.02_platform_w1_directtrue_buf16k_tlc4/summary.md) | 5 I/O threads, platform task executor with 256 threads | 98.2% | 146,592 | 432,171 | 477,362 | 557,490 | 394,883 | 151,270 | 594,394 | 580,582 | 550,485 | 408,639 |
| [io0.02_platform_w1_directfalse_buf16k_tlc0](io0.02_platform_w1_directfalse_buf16k_tlc0/summary.md) | 5 I/O threads, platform task executor with 256 threads | 98.8% | 594,267 | 450,908 | 449,593 | 530,332 | 715,005 | 714,902 | 530,180 | 519,355 | 530,416 | 708,635 |
| [io0.05_platform_w1_directfalse_buf16k_tlc4](io0.05_platform_w1_directfalse_buf16k_tlc4/summary.md) | 13 I/O threads, platform task executor with 256 threads | 98.6% | 139,147 | 503,571 | 483,601 | 501,300 | 398,361 | 145,254 | 501,131 | 501,385 | 514,429 | 404,792 |
| [io0.05_platform_w1_directtrue_buf16k_tlc4](io0.05_platform_w1_directtrue_buf16k_tlc4/summary.md) | 13 I/O threads, platform task executor with 256 threads | 99.0% | 138,026 | 467,361 | 463,081 | 477,330 | 392,318 | 144,507 | 497,172 | 472,522 | 472,903 | 390,904 |
| [io0.02_platform_w1_directtrue_buf16k_tlc0](io0.02_platform_w1_directtrue_buf16k_tlc0/summary.md) | 5 I/O threads, platform task executor with 256 threads | 98.4% | 167,985 | 309,573 | 310,970 | 307,341 | 237,971 | 171,299 | 305,714 | 306,288 | 307,693 | 240,363 |
| [io0.05_platform_w1_directtrue_buf16k_tlc0](io0.05_platform_w1_directtrue_buf16k_tlc0/summary.md) | 13 I/O threads, platform task executor with 256 threads | 98.9% | 163,175 | 267,976 | 271,944 | 301,461 | 238,006 | 169,073 | 285,051 | 280,182 | 276,521 | 245,123 |

## p99 latency (ms)

| config | json/blocking | json/exchange | json/response | json/undertow | json/worker | plaintext/blocking | plaintext/exchange | plaintext/response | plaintext/undertow | plaintext/worker |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| io0.05_platform_w1_directfalse_buf16k_tlc0 | 1.91 | 0.96 | 0.93 | 1.42 | 1.51 | 1.92 | 1.39 | 1.46 | 1.29 | 1.16 |
| io0.02_platform_w1_directfalse_buf16k_tlc4 | 3.94 | 0.70 | 0.76 | 1.36 | 0.83 | 4.11 | 0.65 | 0.61 | 1.36 | 1.22 |
| io0.02_platform_w1_directtrue_buf16k_tlc4 | 4.14 | 0.67 | 0.70 | 0.60 | 1.29 | 3.78 | 0.64 | 0.60 | 0.57 | 1.28 |
| io0.02_platform_w1_directfalse_buf16k_tlc0 | 0.95 | 0.69 | 0.80 | 0.60 | 0.71 | 0.75 | 0.65 | 0.81 | 0.67 | 0.79 |
| io0.05_platform_w1_directfalse_buf16k_tlc4 | 3.89 | 1.01 | 1.05 | 1.17 | 1.95 | 4.24 | 1.12 | 1.13 | 1.11 | 1.85 |
| io0.05_platform_w1_directtrue_buf16k_tlc4 | 3.81 | 1.14 | 1.14 | 1.16 | 1.96 | 3.95 | 1.15 | 1.15 | 1.21 | 1.95 |
| io0.02_platform_w1_directtrue_buf16k_tlc0 | 4.27 | 1.06 | 1.07 | 0.98 | 2.31 | 4.18 | 1.18 | 1.21 | 1.18 | 2.31 |
| io0.05_platform_w1_directtrue_buf16k_tlc0 | 6.27 | 2.23 | 2.12 | 2.14 | 3.25 | 5.83 | 2.03 | 2.21 | 2.08 | 3.14 |
