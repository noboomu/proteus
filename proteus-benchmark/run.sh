#!/usr/bin/env bash
# Builds the benchmark jar, runs oha against every route under one server configuration,
# records a JFR profile for the server lifetime, writes results/<label>/.
#
# Environment:
#   BENCH_LABEL        result directory name (default: UTC timestamp)
#   BENCH_JAVA_HOME    JDK 27 home (default /usr/lib/jvm/java-27-openjdk)
#   BENCH_HOST/PORT    bind target (default 127.0.0.1:21080)
#   BENCH_DURATION     measured pass per route (default 15s)
#   BENCH_WARMUP       discarded pass per route (default 5s)
#   BENCH_CONCURRENCY  oha connections (default 256)
#   BENCH_ROUTES       space-separated route list override
#   BENCH_JVM_OPTS     extra -D overrides for undertow.* config, e.g. "-Dundertow.workerExecutor=virtual"
#   BENCH_SKIP_BUILD   set to 1 to reuse the jar
#   BENCH_JAR          jar path (default target/proteus-benchmark.jar, or ./proteus-benchmark.jar when target/ is absent)
#   BENCH_CONF         application.conf path (default src/main/resources/application.conf, or ./application.conf)
#   BENCH_COMMIT       git commit label when not running inside the repository
#   BENCH_SERVER_CPUS  taskset CPU list for the server JVM (e.g. "0-7"); availableProcessors follows it
#   BENCH_LOAD_CPUS    taskset CPU list for oha
#   BENCH_SLEEP_MS     bench.sleepMs for the /sleep routes (default 10)
#   BENCH_MIN_IDLE     refuse to run unless host CPU idle percent is at least this (default 90; 0 disables)
#   BENCH_REPEAT       measured passes per route; summary reports median/min/max req/s (default 1)
set -euo pipefail

HERE="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$(cd "$HERE/.." && pwd)"
JAVA_HOME="${BENCH_JAVA_HOME:-/usr/lib/jvm/java-27-openjdk}"
JAVA="$JAVA_HOME/bin/java"
JFR="$JAVA_HOME/bin/jfr"
HOST="${BENCH_HOST:-127.0.0.1}"
PORT="${BENCH_PORT:-21080}"
DURATION="${BENCH_DURATION:-15s}"
WARMUP="${BENCH_WARMUP:-5s}"
CONCURRENCY="${BENCH_CONCURRENCY:-256}"
JVM_OPTS="${BENCH_JVM_OPTS:-}"
DEFAULT_ROUTES="plaintext/exchange plaintext/response plaintext/blocking plaintext/worker plaintext/undertow json/exchange json/response json/blocking json/worker json/undertow"
read -r -a ROUTES <<< "${BENCH_ROUTES:-$DEFAULT_ROUTES}"
if [ -d "$HERE/target" ] || [ -f "$HERE/pom.xml" ]; then
  JAR="${BENCH_JAR:-$HERE/target/proteus-benchmark.jar}"
  CONF="${BENCH_CONF:-$HERE/src/main/resources/application.conf}"
else
  JAR="${BENCH_JAR:-$HERE/proteus-benchmark.jar}"
  CONF="${BENCH_CONF:-$HERE/application.conf}"
fi

command -v oha >/dev/null || { echo "oha not found on PATH (cargo install oha)"; exit 1; }
[ -x "$JAVA" ] || { echo "java not found at $JAVA"; exit 1; }

MIN_IDLE="${BENCH_MIN_IDLE:-90}"
REPEAT="${BENCH_REPEAT:-1}"
IDLE_NOW="$(top -bn2 -d1 | grep '^%Cpu' | tail -1 | sed 's/.*, *\([0-9.]*\) id.*/\1/')"
if [ "$MIN_IDLE" != "0" ] && [ "${IDLE_NOW%.*}" -lt "$MIN_IDLE" ]; then
  echo "host cpu idle ${IDLE_NOW}% < ${MIN_IDLE}%; refusing to measure on a contended host (BENCH_MIN_IDLE=0 to override)"
  exit 2
fi
LABEL="${BENCH_LABEL:-$(date -u +%Y%m%dT%H%M%SZ)}"
OUT="$HERE/results/$LABEL"
mkdir -p "$OUT"

if [ "${BENCH_SKIP_BUILD:-0}" != "1" ] && [ -f "$ROOT/pom.xml" ]; then
  echo "building"
  (cd "$ROOT" && JAVA_HOME="$JAVA_HOME" PATH="$JAVA_HOME/bin:$PATH" mvn -q -pl proteus-benchmark -am install -DskipTests -Dgpg.skip=true)
fi

SERVER_PIN=(); [ -n "${BENCH_SERVER_CPUS:-}" ] && SERVER_PIN=(taskset -c "$BENCH_SERVER_CPUS")
LOAD_PIN=();   [ -n "${BENCH_LOAD_CPUS:-}" ]   && LOAD_PIN=(taskset -c "$BENCH_LOAD_CPUS")

echo "starting server on $HOST:$PORT ($JVM_OPTS) cpus=${BENCH_SERVER_CPUS:-all}"
# shellcheck disable=SC2086
"${SERVER_PIN[@]}" "$JAVA" $JVM_OPTS -Dbench.sleepMs="${BENCH_SLEEP_MS:-10}" \
  -XX:StartFlightRecording=filename="$OUT/server.jfr",settings=profile,dumponexit=true \
  -Dconfig.file="$CONF" \
  -Dapplication.ports.http="$PORT" \
  -jar "$JAR" > "$OUT/server.log" 2>&1 &
SERVER_PID=$!
trap 'kill "$SERVER_PID" 2>/dev/null || true' EXIT

for _ in $(seq 1 60); do
  curl -sf "http://$HOST:$PORT/plaintext/undertow" >/dev/null 2>&1 && break
  sleep 0.5
done
curl -sf "http://$HOST:$PORT/plaintext/undertow" >/dev/null || { echo "server did not start"; cat "$OUT/server.log"; exit 1; }

{
  echo "label=$LABEL"
  echo "host=$(hostname)"
  echo "date_utc=$(date -u +%Y%m%dT%H%M%SZ)"
  echo "git_commit=${BENCH_COMMIT:-$(cd "$ROOT" && git rev-parse --short HEAD 2>/dev/null || echo unknown)}"
  echo "java=$("$JAVA" -version 2>&1 | head -1)"
  echo "cpu=$(grep -m1 'model name' /proc/cpuinfo | sed 's/.*: //')"
  echo "cores=$(nproc) logical, $(lscpu -p=CORE | grep -v '^#' | sort -u | wc -l) physical"
  echo "undertow=$(unzip -p "$JAR" META-INF/maven/io.undertow/undertow-core/pom.properties 2>/dev/null | grep '^version=' | cut -d= -f2)"
  echo "oha=$(oha --version)"
  echo "duration=$DURATION warmup=$WARMUP concurrency=$CONCURRENCY repeat=$REPEAT"
  echo "jvm_opts=$JVM_OPTS"
  echo "server_cpus=${BENCH_SERVER_CPUS:-all}"
  echo "load_cpus=${BENCH_LOAD_CPUS:-all}"
  echo "sleep_ms=${BENCH_SLEEP_MS:-10}"
  echo "load_avg_before=$(cut -d' ' -f1-3 /proc/loadavg)"
  echo "cpu_idle_before=$(top -bn1 | sed -n '3p' | sed 's/.*, *\([0-9.]*\) id.*/\1/')%"
} > "$OUT/env.txt"

for route in "${ROUTES[@]}"; do
  name="${route//\//-}"
  url="http://$HOST:$PORT/$route"
  echo "warmup $route"
  "${LOAD_PIN[@]}" oha -z "$WARMUP" -c "$CONCURRENCY" --no-tui "$url" >/dev/null
  for i in $(seq 1 "$REPEAT"); do
    echo "measure $route ($i/$REPEAT)"
    suffix=""; [ "$REPEAT" -gt 1 ] && suffix=".$i"
    "${LOAD_PIN[@]}" oha -z "$DURATION" -c "$CONCURRENCY" --no-tui --output-format json -o "$OUT/$name$suffix.json" "$url"
  done
done

# SIGTERM triggers dumponexit so the JFR file is complete before we read it.
kill "$SERVER_PID" 2>/dev/null || true
for _ in $(seq 1 40); do kill -0 "$SERVER_PID" 2>/dev/null || break; sleep 0.25; done
trap - EXIT
echo "worker=$(grep -o 'XNIO worker: .*' "$OUT/server.log" | head -1 | sed 's/XNIO worker: //')" >> "$OUT/env.txt"
rm -f "$OUT/server.log"

if [ -s "$OUT/server.jfr" ]; then
  "$JFR" print --events jdk.ExecutionSample "$OUT/server.jfr" > "$OUT/samples.txt"
  python3 "$HERE/profile.py" "$OUT/samples.txt" > "$OUT/profile.md"
  rm -f "$OUT/samples.txt"
fi

python3 - "$OUT" "${ROUTES[@]}" <<'PYEOF'
import json, sys, os
out = sys.argv[1]; routes = sys.argv[2:]
env = dict(l.strip().split('=', 1) for l in open(os.path.join(out, 'env.txt')) if '=' in l)
import glob, statistics
rows = []; bad = 0
for r in routes:
    base = os.path.join(out, r.replace('/', '-'))
    files = sorted(glob.glob(base + '.json') + glob.glob(base + '.[0-9]*.json'))
    rps = []; p50 = []; p99 = []; p999 = []; errs = 0
    for f in files:
        d = json.load(open(f))
        s = d['summary']; lat = d['latencyPercentiles']; codes = d['statusCodeDistribution']
        non200 = sum(v for k, v in codes.items() if k != '200')
        # in-flight requests cut by the -z deadline are not failures
        errs += sum(v for k, v in d.get('errorDistribution', {}).items() if 'deadline' not in k) + non200
        rps.append(s['requestsPerSec']); p50.append(lat['p50'] * 1000); p99.append(lat['p99'] * 1000); p999.append(lat['p99.9'] * 1000)
    bad += errs
    rows.append((r, statistics.median(rps), min(rps), max(rps), statistics.median(p50), statistics.median(p99), statistics.median(p999), errs, len(files)))
with open(os.path.join(out, 'summary.md'), 'w') as f:
    f.write(f"# Proteus benchmark {env['label']}\n\n")
    f.write(f"- host `{env['host']}`, commit `{env['git_commit']}`\n- {env['java']}\n- {env['cpu']} ({env['cores']} cores)\n")
    f.write(f"- Undertow {env['undertow']}, worker: {env['worker']}\n- jvm opts: `{env['jvm_opts'] or 'defaults'}`\n")
    f.write(f"- {env['oha']}, {env['duration']}\n")
    f.write(f"- host load before run: {env['load_avg_before']}, cpu idle {env['cpu_idle_before']}\n\n")
    if any(n > 1 for *_, n in rows):
        f.write("| route | req/s median | min | max | p50 ms | p99 ms | p99.9 ms | errors |\n|---|---:|---:|---:|---:|---:|---:|---:|\n")
        for r, med, lo, hi, p50, p99, p999, e, n in rows:
            f.write(f"| `/{r}` | {med:,.0f} | {lo:,.0f} | {hi:,.0f} | {p50:.2f} | {p99:.2f} | {p999:.2f} | {e} |\n")
    else:
        f.write("| route | req/s | p50 ms | p99 ms | p99.9 ms | errors |\n|---|---:|---:|---:|---:|---:|\n")
        for r, med, lo, hi, p50, p99, p999, e, n in rows:
            f.write(f"| `/{r}` | {med:,.0f} | {p50:.2f} | {p99:.2f} | {p999:.2f} | {e} |\n")
    if os.path.exists(os.path.join(out, 'profile.md')):
        f.write("\nCPU profile: [profile.md](profile.md)\n")
print(open(os.path.join(out, 'summary.md')).read())
sys.exit(1 if bad else 0)
PYEOF
