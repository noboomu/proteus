# Proteus parameter sweep sweep-20261011T213545Z

Sorted by `/plaintext/undertow` req/s. Per-config details and CPU profiles in each subdirectory.

## Requests per second

| config | worker | idle% | json/response | plaintext/response | plaintext/undertow |
|---|---|---:|---:|---:|---:|
| [io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grtrue](io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grtrue/summary.md) | 8 I/O threads, platform task executor with 80 threads, 0-3,8-11 cpus | 96.1% | 359,915 | 364,868 | 370,970 |
| [io1_platform_w10_directfalse_buf16k_tlc0_rto0_c256_grfalse](io1_platform_w10_directfalse_buf16k_tlc0_rto0_c256_grfalse/summary.md) | 8 I/O threads, platform task executor with 80 threads, 0-3,8-11 cpus | 93.1% | 347,354 | 358,734 | 361,580 |
| [io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grfalse](io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grfalse/summary.md) | 8 I/O threads, platform task executor with 80 threads, 0-3,8-11 cpus | 90.0% | 322,062 | 321,428 | 348,910 |

## p99 latency (ms)

| config | json/response | plaintext/response | plaintext/undertow |
|---|---:|---:|---:|
| io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grtrue | 2.89 | 3.34 | 3.43 |
| io1_platform_w10_directfalse_buf16k_tlc0_rto0_c256_grfalse | 3.67 | 3.64 | 3.70 |
| io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grfalse | 3.22 | 3.53 | 3.61 |
