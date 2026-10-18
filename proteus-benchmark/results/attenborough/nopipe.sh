#!/usr/bin/env bash
# Non-pipelined regression check: same jar, proteus front end, bufferPipelinedData true vs false, 3 alternating rounds.
cd /tmp/proteus-bench; export PATH=/tmp/proteus-bench/bin:$PATH
export BENCH_JAVA_HOME=/tmp/proteus-bench/jdk BENCH_SKIP_BUILD=1 BENCH_DURATION=10s BENCH_WARMUP=5s BENCH_REPEAT=1 BENCH_COMMIT=8109097
export BENCH_SERVER_CPUS=0-7,128-135 BENCH_LOAD_CPUS=64-127 BENCH_MIN_IDLE=85
export BENCH_ROUTES="plaintext/response json/response json/worker plaintext/blocking"
for round in 1 2 3; do for buf in true false; do
  BENCH_LABEL="nopipe/buf-$buf-r$round" BENCH_JVM_OPTS="-Dundertow.frontEnd=proteus -Dundertow.server.bufferPipelinedData=$buf" ./run.sh > "nopipe-$buf-r$round.log" 2>&1 || echo "FAILED $buf r$round: $(tail -1 nopipe-$buf-r$round.log)"
done; done
echo NOPIPE_DONE
