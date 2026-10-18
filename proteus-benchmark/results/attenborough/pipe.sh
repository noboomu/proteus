#!/usr/bin/env bash
# TFB-shaped pipelined plaintext: wrk + pipeline.lua depth 16, 8 server cores pinned.
# Waits for the GC sweep to finish so the two never share cores. Alternates front ends per round.
cd /tmp/proteus-bench
while pgrep -f '/tmp/proteus-bench/gc.sh' >/dev/null || pgrep -f './gc.sh' >/dev/null; do sleep 15; done
JAVA=/tmp/proteus-bench/jdk/bin/java
WRK=/tmp/proteus-bench/wrk-src/wrk
SERVER_CPUS=0-7,128-135
LOAD_CPUS=64-127
PORT=21080
OUT=/tmp/proteus-bench/results/pipe
mkdir -p "$OUT"
cat > /tmp/proteus-bench/pipeline.lua <<'LUA'
-- TFB pipeline.lua: concatenates N copies of the request and sends them in one write.
init = function(args)
  local r = {}
  local depth = tonumber(args[1]) or 1
  for i = 1, depth do r[i] = wrk.format() end
  req = table.concat(r)
end
request = function() return req end
LUA
ACCEPT='Accept: text/plain,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7'
for round in 1 2 3; do for fe in proteus undertow; do
  log="$OUT/$fe-r$round.txt"
  taskset -c "$SERVER_CPUS" "$JAVA" -Dundertow.frontEnd="$fe" -Dconfig.file=/tmp/proteus-bench/application.conf \
    -Dapplication.ports.http="$PORT" -jar /tmp/proteus-bench/proteus-benchmark.jar > "$OUT/server-$fe-r$round.log" 2>&1 &
  spid=$!
  for _ in $(seq 1 60); do curl -sf "http://127.0.0.1:$PORT/plaintext/exchange" >/dev/null 2>&1 && break; sleep 0.5; done
  echo "fe=$fe round=$round $(grep -o 'XNIO worker: .*' "$OUT/server-$fe-r$round.log" | head -1)" > "$log"
  for route in plaintext/exchange plaintext/response; do
    for c in 256 1024 4096; do
      taskset -c "$LOAD_CPUS" "$WRK" -t 32 -c "$c" -d 5s -H "$ACCEPT" -H 'Connection: keep-alive' --latency \
        -s /tmp/proteus-bench/pipeline.lua "http://127.0.0.1:$PORT/$route" -- 16 > /dev/null 2>&1
      echo "=== $route c=$c depth=16" >> "$log"
      taskset -c "$LOAD_CPUS" "$WRK" -t 32 -c "$c" -d 10s -H "$ACCEPT" -H 'Connection: keep-alive' --latency \
        -s /tmp/proteus-bench/pipeline.lua "http://127.0.0.1:$PORT/$route" -- 16 >> "$log" 2>&1
    done
  done
  kill "$spid"; wait "$spid" 2>/dev/null
done; done
echo PIPE_DONE
