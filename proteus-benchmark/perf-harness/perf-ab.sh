#!/usr/bin/env bash
# specs/proteus_performance.md measurement. Runs on godzilla; server on attenborough pinned 0-7,128-135.
# Usage: perf-ab.sh <outdir> <nameA> <nameB> [extra JVM opts for B]
#   Jars are /data/cache/proteus-perf/run/<name>.jar on attenborough. A and B alternate, 3 rounds, server restarted each time.
set -u
OUT="$1"; A="$2"; B="$3"; BOPTS="${4:-}"
SERVER=attenborough; IP=192.168.1.209; PORT=8080
WRK=/data/cache/proteus-perf/load/wrk-src/wrk
JAVA=/data/cache/proteus-perf/jdk27/bin/java
mkdir -p "$OUT"
ulimit -n 1048576 2>/dev/null || ulimit -n "$(ulimit -Hn)"
cat > /data/cache/proteus-perf/load/pipeline.lua <<'LUA'
init = function(args)
  local r = {}
  local depth = tonumber(args[1]) or 1
  for i = 1, depth do r[i] = wrk.format() end
  req = table.concat(r)
end
request = function() return req end
LUA
AP='Accept: text/plain,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7'
AJ='Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7'

start() { local name=$1 opts=$2
  # A stray server on the port would silently take the traffic; refuse to measure.
  if ssh "$SERVER" "ss -ltn | grep -q ':$PORT '"; then echo "port $PORT busy before starting $name"; ssh "$SERVER" "ss -ltnp | grep ':$PORT '"; return 1; fi
  ssh "$SERVER" "bash -c 'ulimit -n 1048576; cd /data/cache/proteus-perf/run; nohup taskset -c 0-7,128-135 $JAVA -Xms2g -Xmx2g -XX:+UseParallelGC $opts -jar $name.jar > $name.log 2>&1 & echo \$! > $name.pid'"
  for _ in $(seq 1 120); do curl -sf "http://$IP:$PORT/plaintext" >/dev/null 2>&1 && break; sleep 0.5; done
  if ! ssh "$SERVER" "ss -ltnp | grep ':$PORT ' | grep -q \"pid=\$(cat /data/cache/proteus-perf/run/$name.pid),\""; then echo "listener on $PORT is not $name"; return 1; fi
  # The listener answers before every I/O thread exists; wait until the thread count stops changing.
  local prev=0 cur=0
  for _ in $(seq 1 60); do
    cur=$(ssh "$SERVER" "ls /proc/\$(cat /data/cache/proteus-perf/run/$name.pid)/task 2>/dev/null | wc -l")
    [ "$cur" -gt 30 ] && [ "$cur" = "$prev" ] && return 0
    prev=$cur; sleep 1
  done
  echo "$name did not start"; ssh "$SERVER" "tail -20 /data/cache/proteus-perf/run/$name.log"; return 1; }
stop() { local name=$1
  ssh "$SERVER" "bash -c 'p=\$(cat /data/cache/proteus-perf/run/$name.pid); kill \$p; for i in \$(seq 1 40); do kill -0 \$p 2>/dev/null || break; sleep 0.25; done'"; sleep 2; }
cell() { local log=$1 route=$2 c=$3 depth=$4 acc=$5 t=$(( $3 > 48 ? 48 : $3 ))
  local a=(-H 'Host: tfb-server' -H 'Connection: keep-alive' -H "$acc" --latency -t "$t" -c "$c" "http://$IP:$PORT/$route")
  [ "$depth" -gt 1 ] && a=(-s /data/cache/proteus-perf/load/pipeline.lua "${a[@]}" -- "$depth")
  "$WRK" -d 5s "${a[@]}" > /dev/null 2>&1
  echo "=== $route c=$c depth=$depth" >> "$log"
  "$WRK" -d 15s "${a[@]}" >> "$log" 2>&1; }

for round in 1 2 3; do for side in A B; do
  if [ $side = A ]; then name=$A; opts=""; else name=$B; opts="$BOPTS"; fi
  log="$OUT/$side-r$round.txt"
  start "$name" "$opts" || exit 1
  { echo "side=$side name=$name round=$round opts='$opts'"
    ssh "$SERVER" "bash -c 'p=\$(cat /data/cache/proteus-perf/run/$name.pid); grep -m3 -oiE \"XNIO worker.*|io threads.*|Creating worker.*\" /data/cache/proteus-perf/run/$name.log; ls /proc/\$p/task | wc -l | sed s/^/threads=/; cut -d\" \" -f1-3 /proc/loadavg'"
  } > "$log"
  for c in 256 1024 4096; do cell "$log" plaintext "$c" 16 "$AP"; done
  for c in 256 1024; do cell "$log" plaintext "$c" 1 "$AP"; done
  for c in 256 1024; do cell "$log" plaintext/exchange "$c" 1 "$AP"; done
  for c in 64 256 512; do cell "$log" json "$c" 1 "$AJ"; done
  for c in 256 1024; do cell "$log" json/blocking "$c" 1 "$AJ"; done
  stop "$name"
done; done
echo PERF_DONE
