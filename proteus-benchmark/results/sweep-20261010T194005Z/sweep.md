# Proteus parameter sweep sweep-20261010T194005Z

Sorted by `/json/response` req/s. Per-config details and CPU profiles in each subdirectory.

## Requests per second

| config | worker | idle% | json/response | plaintext/blocking | plaintext/response | plaintext/worker |
|---|---|---:|---:|---:|---:|---:|
| [io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256](io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 8 I/O threads, platform task executor with 80 threads, 0-7 cpus | 98.7% | 665,603 | 220,790 | 669,159 | 375,178 |
| [io0.5_platform_w10_directfalse_buf16k_tlc0_rto90000_c256](io0.5_platform_w10_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 4 I/O threads, platform task executor with 80 threads, 0-7 cpus | 98.2% | 405,041 | 379,633 | 409,307 | 454,620 |
| [io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256](io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 2 I/O threads, platform task executor with 80 threads, 0-7 cpus | 98.3% | 214,237 | 457,593 | 217,840 | 474,674 |
| [io0.125_platform_w10_directfalse_buf16k_tlc0_rto90000_c256](io0.125_platform_w10_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 1 I/O threads, platform task executor with 80 threads, 0-7 cpus | 99.4% | 110,541 | 263,191 | 110,634 | 273,817 |

## p99 latency (ms)

| config | json/response | plaintext/blocking | plaintext/response | plaintext/worker |
|---|---:|---:|---:|---:|
| io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256 | 0.90 | 14.03 | 0.79 | 6.17 |
| io0.5_platform_w10_directfalse_buf16k_tlc0_rto90000_c256 | 0.86 | 4.22 | 0.77 | 3.26 |
| io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256 | 1.44 | 1.12 | 1.47 | 1.17 |
| io0.125_platform_w10_directfalse_buf16k_tlc0_rto90000_c256 | 2.93 | 1.90 | 2.92 | 1.59 |
