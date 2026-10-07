# Proteus parameter sweep sweep-20261007T195059Z

Sorted by `/plaintext/undertow` req/s. Per-config details and CPU profiles in each subdirectory.

## Requests per second

| config | worker | idle% | json/blocking | json/exchange | json/response | json/undertow | json/worker | plaintext/blocking | plaintext/exchange | plaintext/response | plaintext/undertow | plaintext/worker |
|---|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| [io0.05_virtual_wna_directfalse_buf16k_tlc0_rto90000](io0.05_virtual_wna_directfalse_buf16k_tlc0_rto90000/summary.md) | 13 I/O threads, virtual task executor | 98.6% | 479,547 | 955,241 | 928,381 | 995,605 | 607,416 | 448,180 | 853,614 | 936,403 | 936,730 | 641,158 |
| [io0.05_virtual_wna_directfalse_buf16k_tlc0_rto0](io0.05_virtual_wna_directfalse_buf16k_tlc0_rto0/summary.md) | 13 I/O threads, virtual task executor | 98.3% | 471,487 | 934,321 | 900,189 | 1,028,611 | 671,174 | 516,985 | 830,406 | 1,003,338 | 933,802 | 708,638 |
| [io0.05_platform_w1_directfalse_buf16k_tlc0_rto0](io0.05_platform_w1_directfalse_buf16k_tlc0_rto0/summary.md) | 13 I/O threads, platform task executor with 256 threads | 98.9% | 479,256 | 992,696 | 977,690 | 921,111 | 640,468 | 455,295 | 855,533 | 905,941 | 925,067 | 669,177 |
| [io0.05_platform_w1_directfalse_buf16k_tlc0_rto90000](io0.05_platform_w1_directfalse_buf16k_tlc0_rto90000/summary.md) | 13 I/O threads, platform task executor with 256 threads | 98.7% | 486,262 | 971,075 | 904,808 | 988,311 | 482,628 | 460,909 | 898,097 | 981,633 | 877,337 | 687,906 |

## p99 latency (ms)

| config | json/blocking | json/exchange | json/response | json/undertow | json/worker | plaintext/blocking | plaintext/exchange | plaintext/response | plaintext/undertow | plaintext/worker |
|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| io0.05_virtual_wna_directfalse_buf16k_tlc0_rto90000 | 2.07 | 0.90 | 0.99 | 1.42 | 1.46 | 2.15 | 1.63 | 1.49 | 1.53 | 1.28 |
| io0.05_virtual_wna_directfalse_buf16k_tlc0_rto0 | 2.15 | 1.15 | 1.35 | 1.35 | 1.28 | 1.94 | 1.67 | 1.40 | 1.57 | 1.29 |
| io0.05_platform_w1_directfalse_buf16k_tlc0_rto0 | 2.15 | 1.04 | 1.12 | 1.60 | 1.40 | 2.23 | 1.66 | 1.60 | 1.63 | 1.15 |
| io0.05_platform_w1_directfalse_buf16k_tlc0_rto90000 | 2.03 | 0.91 | 1.31 | 1.47 | 1.65 | 2.12 | 1.62 | 1.45 | 1.63 | 0.93 |
