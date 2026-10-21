Invalid: round 1 of both sides started measuring before all I/O threads existed (18-19 threads instead of ~70);
perf-ab.sh now waits for a stable thread count and checks the listener pid. Kept for the record; not used.
