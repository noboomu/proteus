#!/usr/bin/env bash
# GC sweep: 4 collector configs x 4 alternating rounds, proteus front end, 8 cores pinned.
cd /tmp/proteus-bench; export PATH=/tmp/proteus-bench/bin:$PATH
export BENCH_JAVA_HOME=/tmp/proteus-bench/jdk BENCH_SKIP_BUILD=1 BENCH_DURATION=10s BENCH_WARMUP=5s BENCH_REPEAT=1 BENCH_COMMIT=gc-sweep
export BENCH_SERVER_CPUS=0-7,128-135 BENCH_LOAD_CPUS=64-127 BENCH_MIN_IDLE=85
export BENCH_ROUTES="plaintext/response json/response json/worker plaintext/blocking"
declare -A GC=(
  [g1-default]=""
  [g1-2g]="-Xms2g -Xmx2g -XX:+AlwaysPreTouch"
  [parallel-numa-2g]="-XX:+UseParallelGC -XX:+UseNUMA -Xms2g -Xmx2g -XX:+AlwaysPreTouch"
  [zgc-2g]="-XX:+UseZGC -Xms2g -Xmx2g -XX:+AlwaysPreTouch"
)
for round in 1 2 3 4; do for g in g1-default g1-2g parallel-numa-2g zgc-2g; do
  BENCH_LABEL="gc/$g-r$round" BENCH_JVM_OPTS="-Dundertow.frontEnd=proteus ${GC[$g]}" ./run.sh > "gc-$g-r$round.log" 2>&1 || echo "FAILED $g r$round: $(tail -1 gc-$g-r$round.log)"
done; done
echo GC_DONE
