# Proteus parameter sweep sweep-20261007T191947Z

Sorted by `/plaintext/undertow` req/s. Per-config details and CPU profiles in each subdirectory.

## Requests per second

| config | worker | idle% | json/blocking | json/exchange | json/response | json/undertow | json/worker | plaintext/blocking | plaintext/exchange | plaintext/response | plaintext/undertow | plaintext/worker |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| [io0.05_platform_w10_directfalse_buf16k_tlc0](io0.05_platform_w10_directfalse_buf16k_tlc0/summary.md) | 13 I/O threads, platform task executor with 2560 threads | 99.0% | 442,329 | 926,206 | 948,503 | 923,917 | 593,147 | 514,146 | 898,703 | 976,635 | 937,461 | 669,381 |
| [io0.05_platform_w1_directfalse_buf16k_tlc0](io0.05_platform_w1_directfalse_buf16k_tlc0/summary.md) | 13 I/O threads, platform task executor with 256 threads | 99.3% | 446,831 | 926,904 | 972,302 | 988,442 | 595,632 | 502,884 | 934,931 | 967,886 | 913,699 | 663,199 |
| [io0.1_platform_w1_directfalse_buf16k_tlc0](io0.1_platform_w1_directfalse_buf16k_tlc0/summary.md) | 26 I/O threads, platform task executor with 256 threads | 99.2% | 455,474 | 811,212 | 834,756 | 846,680 | 474,749 | 451,555 | 825,987 | 883,642 | 891,466 | 524,637 |
| [io0.1_platform_w10_directfalse_buf16k_tlc0](io0.1_platform_w10_directfalse_buf16k_tlc0/summary.md) | 26 I/O threads, platform task executor with 2560 threads | 98.9% | 441,995 | 852,418 | 814,029 | 839,662 | 490,622 | 474,574 | 877,275 | 844,172 | 880,673 | 484,130 |
| [io2_platform_w10_directfalse_buf16k_tlc0](io2_platform_w10_directfalse_buf16k_tlc0/summary.md) | 512 I/O threads, platform task executor with 2560 threads | 99.1% | 410,595 | 792,585 | 783,608 | 823,509 | 366,242 | 440,968 | 821,746 | 806,327 | 849,265 | 373,144 |
| [io2_platform_w1_directfalse_buf16k_tlc0](io2_platform_w1_directfalse_buf16k_tlc0/summary.md) | 512 I/O threads, platform task executor with 256 threads | 98.7% | 421,901 | 796,576 | 820,089 | 838,000 | 373,642 | 427,289 | 865,010 | 854,611 | 847,400 | 378,116 |

## p99 latency (ms)

| config | json/blocking | json/exchange | json/response | json/undertow | json/worker | plaintext/blocking | plaintext/exchange | plaintext/response | plaintext/undertow | plaintext/worker |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| io0.05_platform_w10_directfalse_buf16k_tlc0 | 2.20 | 1.19 | 0.96 | 1.55 | 1.43 | 1.88 | 1.57 | 1.42 | 1.52 | 1.02 |
| io0.05_platform_w1_directfalse_buf16k_tlc0 | 2.14 | 1.17 | 0.73 | 1.41 | 1.40 | 1.87 | 1.53 | 1.44 | 1.58 | 1.13 |
| io0.1_platform_w1_directfalse_buf16k_tlc0 | 3.09 | 1.58 | 1.58 | 1.41 | 1.70 | 3.03 | 1.38 | 1.19 | 1.18 | 1.65 |
| io0.1_platform_w10_directfalse_buf16k_tlc0 | 3.02 | 1.40 | 1.46 | 1.32 | 1.55 | 2.97 | 1.23 | 1.17 | 1.22 | 1.55 |
| io2_platform_w10_directfalse_buf16k_tlc0 | 4.52 | 1.95 | 1.95 | 1.94 | 3.00 | 4.53 | 1.97 | 2.46 | 1.93 | 2.78 |
| io2_platform_w1_directfalse_buf16k_tlc0 | 4.76 | 1.89 | 1.87 | 2.01 | 2.87 | 4.77 | 1.88 | 1.91 | 1.99 | 2.79 |
