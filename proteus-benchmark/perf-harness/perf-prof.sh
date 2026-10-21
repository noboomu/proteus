#!/usr/bin/env bash
# Runs on godzilla. CPU-profiles one jar on attenborough (pinned 0-7,128-135) under wrk from godzilla.
# Usage: perf-prof.sh <name> <route> <conns> <outdir>
set -u
N=$1; ROUTE=$2; C=$3; OUT=$4; IP=192.168.1.209
mkdir -p "$OUT"
A='Accept: application/json,text/html;q=0.9,application/xhtml+xml;q=0.9,application/xml;q=0.8,*/*;q=0.7'
ssh attenborough "bash -c 'ulimit -n 1048576; cd /tmp/proteus-perf; nohup taskset -c 0-7,128-135 /tmp/proteus-bench/jdk27/bin/java -XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -Xms2g -Xmx2g -XX:+UseParallelGC -jar $N.jar > $N.log 2>&1 & echo \$! > $N.pid'"
for _ in $(seq 1 120); do curl -sf "http://$IP:8080/plaintext" >/dev/null 2>&1 && break; sleep 0.5; done
W=/tmp/proteus-load/wrk-src/wrk
$W -H 'Host: tfb-server' -H "$A" -d 10 -c $C -t 48 "http://$IP:8080/$ROUTE" > /dev/null
$W -H 'Host: tfb-server' -H "$A" --latency -d 16 -c $C -t 48 "http://$IP:8080/$ROUTE" > "$OUT/$N-wrk.txt" &
w=$!; sleep 3
ssh attenborough "bash -c 'p=\$(cat /tmp/proteus-perf/$N.pid); /tmp/proteus-bench/asprof/bin/asprof -e cpu -i 200us --cstack vm -d 10 -o collapsed -f /tmp/proteus-perf/$N-cpu.collapsed \$p > /dev/null 2>&1'"
wait $w
ssh attenborough "bash -c 'p=\$(cat /tmp/proteus-perf/$N.pid); kill \$p; sleep 2'"
scp -q attenborough:/tmp/proteus-perf/$N-cpu.collapsed "$OUT/"
grep Requests/sec "$OUT/$N-wrk.txt"
