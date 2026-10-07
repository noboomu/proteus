# Proteus parameter sweep sweep-20261007T174930Z

Sorted by `/plaintext/undertow` req/s. Per-config details and CPU profiles in each subdirectory.

## Requests per second

| config | worker | idle% | json/blocking | json/exchange | json/response | json/undertow | json/worker | plaintext/blocking | plaintext/exchange | plaintext/response | plaintext/undertow | plaintext/worker |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| [io0.02_platform_w1_directtrue_buf16k](io0.02_platform_w1_directtrue_buf16k/summary.md) | 5 I/O threads, platform task executor with 256 threads | 98.5% | 149,317 | 516,257 | 499,603 | 596,264 | 427,404 | 169,972 | 589,054 | 568,897 | 629,720 | 632,751 |
| [io0.02_virtual_wna_directtrue_buf16k](io0.02_virtual_wna_directtrue_buf16k/summary.md) | 5 I/O threads, virtual task executor | 99.5% | 167,217 | 467,147 | 470,170 | 541,982 | 205,562 | 156,333 | 611,464 | 579,553 | 584,056 | 216,379 |
| [io0.25_platform_w10_directtrue_buf16k](io0.25_platform_w10_directtrue_buf16k/summary.md) | 64 I/O threads, platform task executor with 2560 threads | 98.9% | 148,490 | 529,497 | 533,053 | 564,369 | 396,432 | 137,451 | 528,166 | 533,096 | 555,268 | 397,506 |
| [io0.02_platform_w10_directtrue_buf16k](io0.02_platform_w10_directtrue_buf16k/summary.md) | 5 I/O threads, platform task executor with 2560 threads | 98.2% | 158,974 | 455,315 | 474,010 | 557,678 | 499,904 | 145,493 | 547,616 | 533,307 | 546,294 | 509,420 |
| [io0.25_platform_w1_directtrue_buf16k](io0.25_platform_w1_directtrue_buf16k/summary.md) | 64 I/O threads, platform task executor with 256 threads | 98.7% | 153,128 | 513,626 | 514,365 | 537,125 | 380,425 | 132,099 | 567,803 | 523,977 | 531,550 | 367,235 |
| [io0.5_virtual_wna_directtrue_buf16k](io0.5_virtual_wna_directtrue_buf16k/summary.md) | 128 I/O threads, virtual task executor | 98.1% | 131,686 | 481,850 | 481,673 | 497,574 | 180,777 | 141,805 | 511,405 | 490,463 | 518,919 | 182,414 |
| [io2_virtual_wna_directtrue_buf16k](io2_virtual_wna_directtrue_buf16k/summary.md) | 512 I/O threads, virtual task executor | 99.2% | 140,953 | 491,213 | 452,333 | 475,657 | 181,648 | 143,373 | 482,708 | 482,994 | 514,479 | 186,421 |
| [io1_virtual_wna_directtrue_buf16k](io1_virtual_wna_directtrue_buf16k/summary.md) | 256 I/O threads, virtual task executor | 98.8% | 157,317 | 486,206 | 493,775 | 499,965 | 215,378 | 149,075 | 477,877 | 481,479 | 514,376 | 201,601 |
| [io0.25_virtual_wna_directtrue_buf16k](io0.25_virtual_wna_directtrue_buf16k/summary.md) | 64 I/O threads, virtual task executor | 99.0% | 137,300 | 489,558 | 481,719 | 523,392 | 179,579 | 128,629 | 516,833 | 524,381 | 511,547 | 162,896 |
| [io1_platform_w1_directtrue_buf16k](io1_platform_w1_directtrue_buf16k/summary.md) | 256 I/O threads, platform task executor with 256 threads | 99.0% | 138,933 | 482,650 | 480,481 | 512,598 | 368,943 | 134,366 | 457,690 | 497,593 | 501,244 | 368,763 |
| [io0.5_platform_w1_directtrue_buf16k](io0.5_platform_w1_directtrue_buf16k/summary.md) | 128 I/O threads, platform task executor with 256 threads | 95.2% | 144,648 | 493,180 | 490,814 | 520,152 | 366,933 | 128,815 | 498,294 | 503,963 | 498,831 | 356,590 |
| [io0.5_platform_w10_directtrue_buf16k](io0.5_platform_w10_directtrue_buf16k/summary.md) | 128 I/O threads, platform task executor with 2560 threads | 99.3% | 142,162 | 470,066 | 481,935 | 495,763 | 351,887 | 125,806 | 490,219 | 484,277 | 497,016 | 350,038 |
| [io1_platform_w10_directtrue_buf16k](io1_platform_w10_directtrue_buf16k/summary.md) | 256 I/O threads, platform task executor with 2560 threads | 98.8% | 143,052 | 473,420 | 475,126 | 501,234 | 359,784 | 129,279 | 464,087 | 466,511 | 494,914 | 357,445 |
| [io2_platform_w10_directtrue_buf16k](io2_platform_w10_directtrue_buf16k/summary.md) | 512 I/O threads, platform task executor with 2560 threads | 98.6% | 145,927 | 468,508 | 468,205 | 498,114 | 368,907 | 136,904 | 472,571 | 477,653 | 486,509 | 365,951 |
| [io2_platform_w1_directtrue_buf16k](io2_platform_w1_directtrue_buf16k/summary.md) | 512 I/O threads, platform task executor with 256 threads | 98.4% | 146,883 | 464,919 | 470,003 | 491,475 | 365,073 | 138,412 | 483,418 | 486,741 | 479,962 | 366,219 |

## p99 latency (ms)

| config | json/blocking | json/exchange | json/response | json/undertow | json/worker | plaintext/blocking | plaintext/exchange | plaintext/response | plaintext/undertow | plaintext/worker |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| io0.02_platform_w1_directtrue_buf16k | 6.60 | 0.61 | 0.71 | 0.58 | 1.23 | 3.74 | 0.64 | 0.62 | 0.55 | 0.83 |
| io0.02_virtual_wna_directtrue_buf16k | 3.62 | 0.65 | 0.67 | 1.60 | 3.56 | 3.71 | 0.60 | 0.58 | 0.57 | 3.31 |
| io0.25_platform_w10_directtrue_buf16k | 3.95 | 2.52 | 2.43 | 2.40 | 3.42 | 4.32 | 2.57 | 2.62 | 2.54 | 3.46 |
| io0.02_platform_w10_directtrue_buf16k | 5.04 | 0.72 | 0.70 | 0.62 | 1.05 | 3.84 | 0.73 | 0.65 | 0.65 | 1.02 |
| io0.25_platform_w1_directtrue_buf16k | 3.91 | 2.49 | 2.42 | 2.53 | 3.48 | 4.18 | 2.46 | 2.63 | 2.53 | 3.46 |
| io0.5_virtual_wna_directtrue_buf16k | 3.99 | 3.22 | 3.21 | 3.43 | 3.48 | 4.17 | 3.49 | 3.24 | 3.50 | 3.54 |
| io2_virtual_wna_directtrue_buf16k | 3.88 | 4.02 | 4.07 | 4.30 | 3.55 | 4.13 | 4.60 | 4.53 | 4.42 | 3.62 |
| io1_virtual_wna_directtrue_buf16k | 3.88 | 3.36 | 3.36 | 3.56 | 3.37 | 4.05 | 3.50 | 3.40 | 3.67 | 3.50 |
| io0.25_virtual_wna_directtrue_buf16k | 4.32 | 2.52 | 2.67 | 2.54 | 3.48 | 4.13 | 2.60 | 2.59 | 2.63 | 3.55 |
| io1_platform_w1_directtrue_buf16k | 4.34 | 3.52 | 3.65 | 3.55 | 4.07 | 4.15 | 3.44 | 3.50 | 3.70 | 4.08 |
| io0.5_platform_w1_directtrue_buf16k | 3.81 | 3.45 | 3.60 | 3.60 | 3.44 | 4.26 | 3.64 | 3.35 | 3.40 | 3.50 |
| io0.5_platform_w10_directtrue_buf16k | 3.89 | 3.36 | 3.15 | 3.43 | 3.47 | 4.37 | 3.63 | 3.69 | 3.35 | 4.44 |
| io1_platform_w10_directtrue_buf16k | 3.85 | 3.46 | 3.45 | 3.37 | 3.86 | 4.27 | 3.54 | 3.47 | 3.46 | 3.94 |
| io2_platform_w10_directtrue_buf16k | 3.95 | 4.17 | 4.23 | 4.41 | 4.39 | 4.39 | 4.48 | 4.33 | 4.37 | 4.48 |
| io2_platform_w1_directtrue_buf16k | 3.87 | 4.21 | 4.10 | 4.32 | 4.37 | 4.31 | 4.61 | 4.38 | 4.45 | 4.41 |
