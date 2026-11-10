#!/usr/bin/env bash
# Multi-level pass on the knobs that can move these routes, 16 CPUs, current defaults as control.
# 5s warmup + 15s per cell. Control at start, twice in the middle, and at the end; factors interleaved.
cd /data/cache/proteus-perf/load
export CELLS="plaintext:1024 plaintext/exchange:1024 json:512 json/blocking:256 json/blocking:1024"
run() { echo "## $1"; bash probe.sh /data/cache/proteus-perf/load/knobs2 "$1" sweep "$2" 2>&1 | grep -v '^$'; }
run control-a ""
run readto-off   "-Dundertow.socket.readTimeout=0"
run io-8         "-Dundertow.ioThreadsMultiplier=0.5"
run buf-4k       "-Dundertow.bufferSize=4k"
run vtpar-8      "-Djdk.virtualThreadScheduler.parallelism=8"
run io-24        "-Dundertow.ioThreadsMultiplier=1.5"
run control-b ""
run writeto-off  "-Dundertow.socket.writeTimeout=0"
run buf-8k       "-Dundertow.bufferSize=8k"
run io-16        "-Dundertow.ioThreadsMultiplier=1"
run http2-off    "-Dundertow.server.enableHttp2=false"
run buf-64k      "-Dundertow.bufferSize=64k"
run control-c ""
run bothto-off   "-Dundertow.socket.readTimeout=0 -Dundertow.socket.writeTimeout=0"
run io-48        "-Dundertow.ioThreadsMultiplier=3"
run vtpar-24     "-Djdk.virtualThreadScheduler.parallelism=24"
run io-12        "-Dundertow.ioThreadsMultiplier=0.75"
run buf-32k      "-Dundertow.bufferSize=32k"
run control-d ""
echo KNOBS_DONE
