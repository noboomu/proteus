#!/usr/bin/env bash
# Builds the benchmark jar, runs oha against every route, writes results/<timestamp>/.
set -euo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$(cd "$HERE/.." && pwd)"
JAVA_HOME="${BENCH_JAVA_HOME:-/usr/lib/jvm/java-27-openjdk}"
JAVA="$JAVA_HOME/bin/java"
HOST="${BENCH_HOST:-127.0.0.1}"
PORT="${BENCH_PORT:-21080}"
DURATION="${BENCH_DURATION:-15s}"
WARMUP="${BENCH_WARMUP:-5s}"
CONCURRENCY="${BENCH_CONCURRENCY:-256}"
ROUTES=(plaintext/exchange plaintext/response plaintext/undertow json/exchange json/response json/undertow)

command -v oha >/dev/null || { echo "oha not found on PATH (cargo install oha)"; exit 1; }
[ -x "$JAVA" ] || { echo "java not found at $JAVA"; exit 1; }

STAMP="$(date -u +%Y%m%dT%H%M%SZ)"
OUT="$HERE/results/$STAMP"
mkdir -p "$OUT"

echo "building"
(cd "$ROOT" && JAVA_HOME="$JAVA_HOME" PATH="$JAVA_HOME/bin:$PATH" mvn -q -pl proteus-benchmark -am install -DskipTests)

echo "starting server on $HOST:$PORT"
"$JAVA" -Dconfig.file="$HERE/src/main/resources/application.conf" \
  -Dapplication.ports.http="$PORT" \
  -jar "$HERE/target/proteus-benchmark.jar" > "$OUT/server.log" 2>&1 &
SERVER_PID=$!
trap 'kill "$SERVER_PID" 2>/dev/null || true' EXIT

for _ in $(seq 1 60); do
  curl -sf "http://$HOST:$PORT/plaintext/undertow" >/dev/null 2>&1 && break
  sleep 0.5
done
curl -sf "http://$HOST:$PORT/plaintext/undertow" >/dev/null || { echo "server did not start"; cat "$OUT/server.log"; exit 1; }

{
  echo "date_utc=$STAMP"
  echo "git_commit=$(cd "$ROOT" && git rev-parse --short HEAD)"
  echo "java=$("$JAVA" -version 2>&1 | head -1)"
  echo "cpu=$(grep -m1 'model name' /proc/cpuinfo | sed 's/.*: //')"
  echo "cores=$(nproc)"
  echo "undertow=$(grep -m1 '<undertow.version>' "$ROOT/pom.xml" | sed 's/.*>\(.*\)<.*/\1/')"
  echo "oha=$(oha --version)"
  echo "duration=$DURATION warmup=$WARMUP concurrency=$CONCURRENCY"
  echo "load_avg_before=$(cut -d' ' -f1-3 /proc/loadavg)"
  echo "cpu_idle_before=$(top -bn1 | sed -n '3p' | sed 's/.*, *\([0-9.]*\) id.*/\1/')%"
} > "$OUT/env.txt"

for route in "${ROUTES[@]}"; do
  name="${route//\//-}"
  url="http://$HOST:$PORT/$route"
  echo "warmup $route"
  oha -z "$WARMUP" -c "$CONCURRENCY" --no-tui "$url" >/dev/null
  echo "measure $route"
  oha -z "$DURATION" -c "$CONCURRENCY" --no-tui --output-format json -o "$OUT/$name.json" "$url"
done

kill "$SERVER_PID" 2>/dev/null || true
trap - EXIT
rm -f "$OUT/server.log"

python3 - "$OUT" "${ROUTES[@]}" <<'PYEOF'
import json, sys, os
out = sys.argv[1]; routes = sys.argv[2:]
env = dict(l.strip().split('=', 1) for l in open(os.path.join(out, 'env.txt')) if '=' in l)
rows = []; bad = 0
for r in routes:
    d = json.load(open(os.path.join(out, r.replace('/', '-') + '.json')))
    s = d['summary']; lat = d['latencyPercentiles']; codes = d['statusCodeDistribution']
    non200 = sum(v for k, v in codes.items() if k != '200')
    # in-flight requests cut by the -z deadline are not failures
    errs = sum(v for k, v in d.get('errorDistribution', {}).items() if 'deadline' not in k) + non200
    bad += errs
    rows.append((r, s['requestsPerSec'], lat['p50'] * 1000, lat['p99'] * 1000, lat['p99.9'] * 1000, errs))
with open(os.path.join(out, 'summary.md'), 'w') as f:
    f.write(f"# Proteus benchmark {env['date_utc']}\n\n")
    f.write(f"- commit `{env['git_commit']}`\n- {env['java']}\n- {env['cpu']} ({env['cores']} cores)\n")
    f.write(f"- Undertow {env['undertow']}\n- {env['oha']}, {env['duration']}\n")
    f.write(f"- host load before run: {env['load_avg_before']}, cpu idle {env['cpu_idle_before']}\n\n")
    f.write("| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |\n|---|---:|---:|---:|---:|---:|\n")
    for r, rps, p50, p99, p999, e in rows:
        f.write(f"| `/{r}` | {rps:,.0f} | {p50:.2f} | {p99:.2f} | {p999:.2f} | {e} |\n")
print(open(os.path.join(out, 'summary.md')).read())
sys.exit(1 if bad else 0)
PYEOF
