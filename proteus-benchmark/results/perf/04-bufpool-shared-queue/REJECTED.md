# 04 rejected

Change: `DefaultByteBufferPool(direct, bufferSize, maximumPoolSize=4096, threadLocalCacheSize=0)` passed via `setByteBufferPool`, replacing Undertow's default `(-1, 4)`.

- 5 cells better, 5 worse, 0 errors: rejected under `specs/proteus_performance.md`.
- Better: pipelined plaintext +4 to +8%; `/json/blocking` +121% (256) and +69% (1024).
- Worse: non-pipelined `/plaintext`, `/plaintext/exchange` 1024, `/json` 256/512 by 1 to 3%, p99 roughly doubled.
- The `/json/blocking` gain comes from the default pool creating a per-thread cache entry (synchronized `WeakHashMap<Thread, …>` insert plus `threadLocalDataList` add) for every new virtual thread that allocates a buffer.
- Patch kept outside the repo; not applied.
