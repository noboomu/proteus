#!/usr/bin/env bash
# Read-only diagnostic for /json/blocking tail latency. Runs on godzilla against diag.jar on attenborough.
# Per concurrency: 5s warmup, reset server histograms, 15s measured wrk with per-thread CPU sampled over the window.
set -u
OUT=$1; N=diag; SERVER=attenborough; IP=192.168.1.209; PORT=8080
WRK=/tmp/proteus-load/wrk-src/wrk; JAVA=/tmp/proteus-bench/jdk27/bin/java
AJ='Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7'
mkdir -p "$OUT"; ulimit -n 1048576 2>/dev/null || ulimit -n "$(ulimit -Hn)"
if ssh "$SERVER" "ss -ltn | grep -q ':$PORT '"; then echo "port busy"; exit 1; fi
ssh "$SERVER" "bash -c 'ulimit -n 1048576; cd /tmp/proteus-perf; nohup taskset -c 0-7,128-135 $JAVA -Xms2g -Xmx2g -XX:+UseParallelGC -Xlog:gc:file=/tmp/proteus-perf/diag-gc.log -jar $N.jar > $N.log 2>&1 & echo \$! > $N.pid'"
for _ in $(seq 1 120); do curl -sf "http://$IP:$PORT/plaintext" >/dev/null 2>&1 && break; sleep 0.5; done
prev=0; for _ in $(seq 1 60); do cur=$(ssh "$SERVER" "ls /proc/\$(cat /tmp/proteus-perf/$N.pid)/task | wc -l"); [ "$cur" -gt 30 ] && [ "$cur" = "$prev" ] && break; prev=$cur; sleep 1; done
# Sums utime+stime ticks per thread-name group.
SNAP='p=$(cat /tmp/proteus-perf/diag.pid); for t in /proc/$p/task/*; do c=$(cat $t/comm); s=$(cut -d")" -f2 $t/stat); set -- $s; echo "$c $(( ${12} + ${13} ))"; done | sed -E "s/[-#]?[0-9]+ / /" | awk "{a[\$1]+=\$2; n[\$1]++} END{for(k in a) print k, n[k], a[k]}" | sort'
for c in 256 1024; do
  t=$(( c > 48 ? 48 : c )); a=(-H 'Host: tfb-server' -H 'Connection: keep-alive' -H "$AJ" --latency -t "$t" -c "$c" "http://$IP:$PORT/json/blocking")
  "$WRK" -d 5s "${a[@]}" > /dev/null 2>&1
  curl -s "http://$IP:$PORT/diag" > /dev/null
  ssh "$SERVER" "bash -c '$SNAP'" > "$OUT/cpu-$c-before.txt"
  "$WRK" -d 15s "${a[@]}" > "$OUT/wrk-$c.txt" 2>&1
  ssh "$SERVER" "bash -c '$SNAP'" > "$OUT/cpu-$c-after.txt"
  curl -s "http://$IP:$PORT/diag" > "$OUT/stages-$c.txt"
done
ssh "$SERVER" "bash -c 'p=\$(cat /tmp/proteus-perf/$N.pid); kill \$p; for i in \$(seq 1 40); do kill -0 \$p 2>/dev/null || break; sleep 0.25; done'" ; scp -q $SERVER:/tmp/proteus-perf/diag-gc.log "$OUT/"
echo DIAG_DONE
