#!/usr/bin/env bash
# One pass per knob on top of current defaults, control at both ends; 5s warmup + 15s per cell.
cd /data/cache/proteus-perf/load
export CELLS="plaintext:1024 plaintext/exchange:1024 json:512 json/blocking:256 json/blocking:1024"
run() { echo "## $1"; GC="${3:--XX:+UseParallelGC}" bash probe.sh /data/cache/proteus-perf/load/knobs "$1" sweep "$2" 2>&1 | grep -v '^$'; }
run control ""
run buf-32k "-Dundertow.bufferSize=32k"
run heapbuf "-Dundertow.directBuffers=false"
run notimeout "-Dundertow.socket.readTimeout=0 -Dundertow.socket.writeTimeout=0"
run vtpar-12 "-Djdk.virtualThreadScheduler.parallelism=12"
run g1 "" "-XX:+UseG1GC"
run zgc "" "-XX:+UseZGC"
run control2 ""
echo KNOBS_DONE
