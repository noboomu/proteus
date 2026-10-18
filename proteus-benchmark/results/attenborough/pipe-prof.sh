#!/usr/bin/env bash
# async-profiler CPU profile of each front end under wrk depth-16 pipelined /plaintext/exchange, 8 cores pinned.
cd /tmp/proteus-bench
JAVA=/tmp/proteus-bench/jdk/bin/java
WRK=/tmp/proteus-bench/wrk-src/wrk
PORT=21080
OUT=/tmp/proteus-bench/results/pipe-prof
mkdir -p "$OUT"
for fe in proteus undertow; do
  taskset -c 0-7,128-135 "$JAVA" -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -Dundertow.frontEnd="$fe" \
    -Dconfig.file=/tmp/proteus-bench/application.conf -Dapplication.ports.http="$PORT" -jar /tmp/proteus-bench/proteus-benchmark.jar > "$OUT/server-$fe.log" 2>&1 &
  spid=$!
  for _ in $(seq 1 60); do curl -sf "http://127.0.0.1:$PORT/plaintext/exchange" >/dev/null 2>&1 && break; sleep 0.5; done
  taskset -c 64-127 "$WRK" -t 32 -c 1024 -d 8s -s /tmp/proteus-bench/pipeline.lua "http://127.0.0.1:$PORT/plaintext/exchange" -- 16 > /dev/null 2>&1
  taskset -c 64-127 "$WRK" -t 32 -c 1024 -d 14s -s /tmp/proteus-bench/pipeline.lua "http://127.0.0.1:$PORT/plaintext/exchange" -- 16 > "$OUT/wrk-$fe.txt" 2>&1 &
  wpid=$!
  sleep 2
  /tmp/proteus-bench/asprof/bin/asprof -e cpu -i 200us --cstack vm -d 10 -o collapsed -f "$OUT/$fe.collapsed" "$spid" > "$OUT/asprof-$fe.log" 2>&1
  wait "$wpid"
  kill "$spid"; wait "$spid" 2>/dev/null
done
echo PROF_DONE
