#!/usr/bin/env bash
# Runs run.sh once per server configuration and collates into results/sweep-<timestamp>/sweep.md.
#
# Environment (space-separated lists):
#   SWEEP_IO        ioThreadsMultiplier values      (default "0.02 0.25 0.5 1 2")  0.02*48 rounds to 1 thread
#   SWEEP_EXEC      workerExecutor values           (default "platform virtual")
#   SWEEP_WORKERS   workerThreadsMultiplier values  (default "1 10"; platform only)
#   SWEEP_DIRECT    directBuffers values            (default "true")
#   SWEEP_BUFFER    bufferSize values               (default "16k")
#   SWEEP_TLC       bufferPool.threadLocalCacheSize (default "4")
#   SWEEP_RTO       socket.readTimeout ms           (default "90000"; 0 disables the timeout conduit)
#   SWEEP_ROUTES    passed to run.sh as BENCH_ROUTES
#   BENCH_DURATION / BENCH_WARMUP / BENCH_CONCURRENCY passed through
set -euo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
STAMP="$(date -u +%Y%m%dT%H%M%SZ)"
SWEEP="sweep-$STAMP"
OUT="$HERE/results/$SWEEP"
mkdir -p "$OUT"

read -r -a IOS     <<< "${SWEEP_IO:-0.02 0.25 0.5 1 2}"
read -r -a EXECS   <<< "${SWEEP_EXEC:-platform virtual}"
read -r -a WORKERS <<< "${SWEEP_WORKERS:-1 10}"
read -r -a DIRECTS <<< "${SWEEP_DIRECT:-true}"
read -r -a BUFFERS <<< "${SWEEP_BUFFER:-16k}"
read -r -a TLCS    <<< "${SWEEP_TLC:-4}"
read -r -a RTOS    <<< "${SWEEP_RTO:-90000}"

if [ -f "$HERE/pom.xml" ] && [ "${BENCH_SKIP_BUILD:-0}" != "1" ]; then
  echo "building once"
  (cd "$HERE/.." && JAVA_HOME="${BENCH_JAVA_HOME:-/usr/lib/jvm/java-27-openjdk}" PATH="${BENCH_JAVA_HOME:-/usr/lib/jvm/java-27-openjdk}/bin:$PATH" mvn -q -pl proteus-benchmark -am install -DskipTests -Dgpg.skip=true)
fi

labels=()
for io in "${IOS[@]}"; do
  for exec in "${EXECS[@]}"; do
    if [ "$exec" = "virtual" ]; then wlist=("na"); else wlist=("${WORKERS[@]}"); fi
    for w in "${wlist[@]}"; do
      for d in "${DIRECTS[@]}"; do
        for b in "${BUFFERS[@]}"; do
         for tlc in "${TLCS[@]}"; do
         for rto in "${RTOS[@]}"; do
          label="io${io}_${exec}_w${w}_direct${d}_buf${b}_tlc${tlc}_rto${rto}"
          opts="-Dundertow.ioThreadsMultiplier=$io -Dundertow.workerExecutor=$exec -Dundertow.directBuffers=$d -Dundertow.bufferSize=$b -Dundertow.bufferPool.threadLocalCacheSize=$tlc -Dundertow.socket.readTimeout=$rto"
          [ "$w" != "na" ] && opts="$opts -Dundertow.workerThreadsMultiplier=$w"
          echo "=== $label"
          if [ -n "${SWEEP_ROUTES:-}" ]; then export BENCH_ROUTES="$SWEEP_ROUTES"; fi
          BENCH_LABEL="$SWEEP/$label" BENCH_JVM_OPTS="$opts" BENCH_SKIP_BUILD=1 \
            "$HERE/run.sh" > "$OUT/$label.log" 2>&1 || echo "FAILED $label (see $OUT/$label.log)"
          labels+=("$label")
         done
         done
        done
      done
    done
  done
done

python3 - "$OUT" "${labels[@]}" <<'PYEOF'
import json, os, sys
out = sys.argv[1]; labels = sys.argv[2:]
routes = None; rows = []
for label in labels:
    d = os.path.join(out, label)
    if not os.path.exists(os.path.join(d, 'summary.md')):
        continue
    env = dict(l.strip().split('=', 1) for l in open(os.path.join(d, 'env.txt')) if '=' in l)
    files = sorted(f for f in os.listdir(d) if f.endswith('.json'))
    if routes is None:
        routes = [f[:-5].replace('-', '/') for f in files]
    rps = {}
    p99 = {}
    for f in files:
        j = json.load(open(os.path.join(d, f)))
        r = f[:-5].replace('-', '/')
        rps[r] = j['summary']['requestsPerSec']
        p99[r] = j['latencyPercentiles']['p99'] * 1000
    rows.append((label, env.get('worker', ''), env.get('cpu_idle_before', ''), rps, p99))
anchor = next((r for r in routes if r == 'plaintext/undertow'), routes[0])
rows.sort(key=lambda x: -x[3].get(anchor, 0))
with open(os.path.join(out, 'sweep.md'), 'w') as f:
    f.write(f"# Proteus parameter sweep {os.path.basename(out)}\n\n")
    f.write(f"Sorted by `/{anchor}` req/s. Per-config details and CPU profiles in each subdirectory.\n\n")
    f.write("## Requests per second\n\n| config | worker | idle% | " + " | ".join(routes) + " |\n")
    f.write("|---|---|---:|" + "---:|" * len(routes) + "\n")
    for label, worker, idle, rps, _ in rows:
        f.write(f"| [{label}]({label}/summary.md) | {worker} | {idle} | " + " | ".join(f"{rps.get(r, 0):,.0f}" for r in routes) + " |\n")
    f.write("\n## p99 latency (ms)\n\n| config | " + " | ".join(routes) + " |\n")
    f.write("|---|" + "---:|" * len(routes) + "\n")
    for label, _, _, _, p99 in rows:
        f.write(f"| {label} | " + " | ".join(f"{p99.get(r, 0):.2f}" for r in routes) + " |\n")
print(open(os.path.join(out, 'sweep.md')).read())
PYEOF
