# Proteus parameter sweep sweep-20261011T215129Z

Sorted by `/json/blocking` req/s. Per-config details and CPU profiles in each subdirectory.

## Requests per second

| config | worker | idle% | json/blocking | json/response | json/worker |
|---|---|---:|---:|---:|---:|
| [io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grtrue](io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grtrue/summary.md) | 8 I/O threads, platform task executor with 80 threads, 0-3,8-11 cpus | 87.4% | 174,098 | 375,123 | 204,883 |
| [io1_virtual_wna_directfalse_buf16k_tlc0_rto90000_c256_grtrue](io1_virtual_wna_directfalse_buf16k_tlc0_rto90000_c256_grtrue/summary.md) | 8 I/O threads, virtual task executor, 0-3,8-11 cpus | 88.9% | 169,661 | 358,166 | 193,521 |

## p99 latency (ms)

| config | json/blocking | json/response | json/worker |
|---|---:|---:|---:|
| io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256_grtrue | 8.71 | 3.35 | 7.04 |
| io1_virtual_wna_directfalse_buf16k_tlc0_rto90000_c256_grtrue | 8.99 | 3.57 | 7.97 |
