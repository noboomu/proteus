# Proteus parameter sweep sweep-20261010T194553Z

Sorted by `/json/response` req/s. Per-config details and CPU profiles in each subdirectory.

## Requests per second

| config | worker | idle% | json/response | plaintext/blocking | plaintext/response | plaintext/worker |
|---|---|---:|---:|---:|---:|---:|
| [io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256](io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 4 I/O threads, platform task executor with 40 threads, 0-3 cpus | 98.8% | 405,361 | 143,452 | 411,611 | 253,859 |
| [io0.5_platform_w10_directfalse_buf16k_tlc0_rto90000_c256](io0.5_platform_w10_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 2 I/O threads, platform task executor with 40 threads, 0-3 cpus | 99.0% | 217,904 | 204,208 | 218,683 | 267,854 |
| [io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256](io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 1 I/O threads, platform task executor with 40 threads, 0-3 cpus | 99.4% | 110,720 | 280,785 | 111,230 | 257,072 |

## p99 latency (ms)

| config | json/response | plaintext/blocking | plaintext/response | plaintext/worker |
|---|---:|---:|---:|---:|
| io1_platform_w10_directfalse_buf16k_tlc0_rto90000_c256 | 1.05 | 26.79 | 1.28 | 5.61 |
| io0.5_platform_w10_directfalse_buf16k_tlc0_rto90000_c256 | 1.37 | 9.72 | 1.76 | 3.31 |
| io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256 | 2.97 | 1.80 | 2.91 | 1.75 |
