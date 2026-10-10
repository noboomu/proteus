# Proteus parameter sweep sweep-20261010T195009Z

Sorted by `/plaintext/worker` req/s. Per-config details and CPU profiles in each subdirectory.

## Requests per second

| config | worker | idle% | plaintext/worker | sleep/blocking | sleep/worker |
|---|---|---:|---:|---:|---:|
| [io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c256](io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 4 I/O threads, platform task executor with 16 threads, 0-15 cpus | 99.2% | 700,014 | 25,276 | 1,592 |
| [io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c1024](io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c1024/summary.md) | 4 I/O threads, virtual task executor, 0-15 cpus | 98.8% | 699,075 | 101,003 | 101,128 |
| [io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c256](io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 4 I/O threads, virtual task executor, 0-15 cpus | 99.3% | 696,773 | 25,315 | 25,293 |
| [io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256](io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 4 I/O threads, platform task executor with 160 threads, 0-15 cpus | 98.5% | 695,585 | 25,307 | 15,880 |
| [io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c256](io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c256/summary.md) | 4 I/O threads, platform task executor with 1024 threads, 0-15 cpus | 99.0% | 691,316 | 25,307 | 25,307 |
| [io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c1024](io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c1024/summary.md) | 4 I/O threads, platform task executor with 160 threads, 0-15 cpus | 98.6% | 685,631 | 100,742 | 15,916 |
| [io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c1024](io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c1024/summary.md) | 4 I/O threads, platform task executor with 1024 threads, 0-15 cpus | 99.2% | 677,478 | 101,067 | 100,920 |
| [io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c4096](io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c4096/summary.md) | 4 I/O threads, platform task executor with 16 threads, 0-15 cpus | 98.4% | 649,937 | 387,258 | 1,589 |
| [io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c1024](io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c1024/summary.md) | 4 I/O threads, platform task executor with 16 threads, 0-15 cpus | 98.6% | 647,165 | 100,836 | 1,589 |
| [io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c4096](io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c4096/summary.md) | 4 I/O threads, platform task executor with 1024 threads, 0-15 cpus | 98.8% | 612,249 | 384,122 | 101,406 |
| [io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c4096](io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c4096/summary.md) | 4 I/O threads, platform task executor with 160 threads, 0-15 cpus | 98.8% | 580,844 | 387,367 | 15,923 |
| [io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c4096](io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c4096/summary.md) | 4 I/O threads, virtual task executor, 0-15 cpus | 99.4% | 497,375 | 383,724 | 394,375 |

## p99 latency (ms)

| config | plaintext/worker | sleep/blocking | sleep/worker |
|---|---:|---:|---:|
| io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c256 | 0.81 | 10.71 | 161.85 |
| io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c1024 | 2.40 | 11.53 | 10.43 |
| io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c256 | 0.74 | 10.19 | 10.56 |
| io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c256 | 0.83 | 10.29 | 18.73 |
| io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c256 | 0.77 | 10.16 | 10.21 |
| io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c1024 | 2.97 | 11.76 | 67.48 |
| io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c1024 | 2.25 | 10.40 | 11.02 |
| io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c4096 | 12.45 | 17.80 | 4923.85 |
| io0.25_platform_w1_directfalse_buf16k_tlc0_rto90000_c1024 | 3.76 | 10.93 | 1057.42 |
| io0.25_platform_w64_directfalse_buf16k_tlc0_rto90000_c4096 | 12.50 | 17.61 | 43.84 |
| io0.25_platform_w10_directfalse_buf16k_tlc0_rto90000_c4096 | 12.82 | 16.98 | 268.44 |
| io0.25_virtual_wna_directfalse_buf16k_tlc0_rto90000_c4096 | 14.05 | 17.33 | 15.01 |
