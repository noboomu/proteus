#!/usr/bin/env bash
# Nightly review per specs/proteus_nightly.md. Runs on tachikoma from the repo; drives attenborough (server) and godzilla (load).
# Usage: nightly.sh            (scheduled run: defers when hosts are busy, commits and pushes the result directory)
#        nightly.sh --no-push  (manual run: same, but leaves the result commit local)
set -u
REPO=$(cd "$(dirname "$0")/../.." && pwd)
HARNESS="$REPO/proteus-benchmark/perf-harness"
SERVER=attenborough; LOAD=godzilla; PORT=8080
# Remote login shells are fish; every remote command runs through bash.
rsh() { local host=$1; shift; ssh -o BatchMode=yes -n "$host" "bash -c $(printf '%q' "$*")"; }
JAVA_HOME_LOCAL=/usr/lib/jvm/java-27-openjdk
DAY=$(TZ=America/Los_Angeles date +%F)
OUT="$REPO/proteus-benchmark/results/perf/nightly/$DAY"
PUSH=1; [ "${1:-}" = "--no-push" ] && PUSH=0
DEFER_SECONDS=${PROTEUS_NIGHTLY_DEFER_SECONDS:-1800}
LOCK=/tmp/proteus-nightly.lock

exec 9>"$LOCK"; flock -n 9 || { echo "another nightly run holds $LOCK"; exit 0; }
mkdir -p "$OUT/diag"
cd "$REPO" || exit 1

# Writes SUMMARY.md with the given status and notes, commits the result directory, optionally pushes, posts to the bus.
finish() { local status=$1 notes=$2
  {
    echo "# Nightly $DAY"; echo
    echo "- status: $status"
    echo "- head: $(git rev-parse --short HEAD)"
    echo "- tests: ${TESTS:-n/a}"
    echo "- baseline: ${BASELINE:-n/a}"
    echo "- previous: ${PREVIOUS:-n/a}"
    echo "- blocking_tail: ${TAIL:-n/a}"
    echo "- rss_peak_mib: ${RSS:-n/a}"
    echo "- notes: $notes"
    [ -n "${PREV_TABLE:-}" ] && { echo; echo "## Versus previous nightly"; echo; echo "$PREV_TABLE"; }
  } > "$OUT/SUMMARY.md"
  git add "$OUT" && git commit -q -m "data(benchmark): nightly $DAY: $status [jcode/houses-nightly] 🌙" -- "$OUT"
  [ "$PUSH" = 1 ] && git push -q origin development 2>&1 | grep -v -i "dependabot\|vulnerab\|^remote: *$"
  tachikoma-bus say --project proteus --channel work --session houses-nightly \
    "🌙 [jcode/houses-nightly] proteus nightly $DAY: $status | tests ${TESTS:-n/a} | vs baseline ${BASELINE:-n/a} | blocking tail ${TAIL:-n/a} | rss ${RSS:-n/a} MiB | $notes" >/dev/null 2>&1
  echo "$status: $notes"; exit 0; }

# Returns 0 when both hosts are idle per the spec.
idle() {
  rsh "$SERVER" "ss -ltn | grep -q ':$PORT '" && { BUSY="port $PORT held on $SERVER"; return 1; }
  rsh "$LOAD" "pgrep -x wrk >/dev/null || pgrep -f '^bash (\./|/data/cache/proteus-perf/load/)?(perf-ab|probe|diag-blocking)\.sh' >/dev/null" && { BUSY="load tool running on $LOAD"; return 1; }
  local busy
  busy=$(rsh "$SERVER" 'snap() { awk "/^cpu[0-9]/{n=substr(\$1,4); if (n<=7 || (n>=128 && n<=135)) {t+=\$2+\$3+\$4+\$5+\$6+\$7+\$8; i+=\$5+\$6}} END{print t, i}" /proc/stat; }; set -- $(snap); sleep 5; set -- $1 $2 $(snap); echo $(( 100 - 100 * ($4 - $2) / ($3 - $1) ))')
  [ "${busy:-100}" -gt 20 ] && { BUSY="pinned CPUs ${busy}% busy with no Proteus server running"; return 1; }
  return 0; }

for attempt in 0 1 2 3 4; do
  idle && break
  [ "$attempt" = 4 ] && finish SKIPPED "hosts busy after four deferrals: $BUSY"
  echo "busy ($BUSY); deferring ${DEFER_SECONDS}s"; sleep "$DEFER_SECONDS"
done

git fetch -q origin
if [ -n "$(git status --porcelain --untracked-files=no)" ] || [ "$(git rev-parse --abbrev-ref HEAD)" != development ]; then
  finish SKIPPED "working tree not clean on development: $(git status -sb | head -5 | tr '\n' ' ')"
fi
git merge -q --ff-only origin/development || finish SKIPPED "development diverged from origin/development"

for a in "$SERVER:/data/cache/proteus-perf/jdk27/bin/java" "$SERVER:/data/cache/proteus-perf/run/baseline.jar" "$LOAD:/data/cache/proteus-perf/load/wrk-src/wrk"; do
  rsh "${a%%:*}" "test -e ${a#*:}" || finish FAILED "missing artifact ${a}"
done

{
  echo "head $(git rev-parse HEAD)"
  echo "jdk $("$JAVA_HOME_LOCAL/bin/java" -version 2>&1 | head -1)"
  echo "undertow $(grep -m1 -o '<undertow.version>[^<]*' pom.xml | cut -d'>' -f2)"
  echo "xnio $(ls ~/.m2/repository/org/jboss/xnio/xnio-api/ | sort -V | tail -1)"
  echo "load $SERVER $(rsh "$SERVER" cat /proc/loadavg)"
  echo "load $LOAD $(rsh "$LOAD" cat /proc/loadavg)"
} > "$OUT/env.txt"

# Step 1-2: full reactor verify.
JAVA_HOME=$JAVA_HOME_LOCAL mvn -q -o clean install -Dgpg.skip > "$OUT/verify.log" 2>&1; VERIFY_EXIT=$?
: > "$OUT/verify.txt"; T=0; F=0; E=0
for m in proteus-core proteus-openapi proteus-websocket; do
  line=$(cat $m/target/surefire-reports/*.txt 2>/dev/null | grep -E '^Tests run:' | awk -F'[ ,]+' '{t+=$3;f+=$5;e+=$7} END{print t+0, f+0, e+0}')
  echo "$m $line" >> "$OUT/verify.txt"; set -- $line; T=$((T+$1)); F=$((F+$2)); E=$((E+$3))
done
echo "reactor_exit $VERIFY_EXIT" >> "$OUT/verify.txt"
TESTS="$T total, $F failed, $E errored"
[ "$VERIFY_EXIT" != 0 ] || [ "$F" != 0 ] || [ "$E" != 0 ] && { tail -40 "$OUT/verify.log" > "$OUT/verify-tail.txt"; rm "$OUT/verify.log"; finish FAILED "reactor verify exit $VERIFY_EXIT"; }
rm "$OUT/verify.log"

# Step 3: build and stage both workload apps against the installed proteus-core.
V=$(sed -n 's:.*<version>\(.*\)</version>.*:\1:p' pom.xml | head -1)
for app in app diag-app; do
  (cd "$HARNESS/$app" && JAVA_HOME=$JAVA_HOME_LOCAL mvn -q -o package -Dproteus.version="$V" > /dev/null 2>&1) || finish FAILED "build of perf-harness/$app failed"
done
scp -q "$HARNESS/app/target/proteus-perf.jar" "$SERVER:/data/cache/proteus-perf/run/nightly.jar" || finish FAILED "staging nightly.jar failed"
scp -q "$HARNESS/diag-app/target/proteus-perf-diag.jar" "$SERVER:/data/cache/proteus-perf/run/diag.jar" || finish FAILED "staging diag.jar failed"
scp -q "$HARNESS/perf-ab.sh" "$HARNESS/diag-blocking.sh" "$LOAD:/data/cache/proteus-perf/load/" || finish FAILED "staging drivers failed"

# Step 4 + 6: measurement, with RSS sampling of the nightly server process.
rsh "$SERVER" 'echo $$ > /data/cache/proteus-perf/run/rss-sampler.pid; while :; do p=$(cat /data/cache/proteus-perf/run/nightly.pid 2>/dev/null); [ -n "$p" ] && grep -q nightly.jar /proc/$p/cmdline 2>/dev/null && awk -v t="$(date +%s)" "/VmRSS/{print t, \$2}" /proc/$p/status; sleep 5; done' > "$OUT/rss.txt" &
RSS_SAMPLER=$!
rsh "$LOAD" "bash /data/cache/proteus-perf/load/perf-ab.sh /data/cache/proteus-perf/load/results/perf/nightly-$DAY baseline nightly > /data/cache/proteus-perf/load/nightly-$DAY.log 2>&1"
rsh "$SERVER" 'kill $(cat /data/cache/proteus-perf/run/rss-sampler.pid) 2>/dev/null; true'
kill "$RSS_SAMPLER" 2>/dev/null; wait "$RSS_SAMPLER" 2>/dev/null
rsh "$LOAD" "tail -1 /data/cache/proteus-perf/load/nightly-$DAY.log" | grep -q PERF_DONE || finish FAILED "measurement did not complete: $(rsh "$LOAD" "tail -3 /data/cache/proteus-perf/load/nightly-$DAY.log" | tr '\n' ' ')"
rsync -aq "$LOAD:/data/cache/proteus-perf/load/results/perf/nightly-$DAY/" "$OUT/"
python3 "$HARNESS/perf-table.py" "$OUT" > "$OUT/table.md"
BASELINE=$(awk -F'|' '/^\| \//{v=$12; gsub(/ /,"",v); if (v=="better") b++; else if (v=="WORSE") w++; else o++} END{printf "%d better, %d worse, %d overlap", b, w, o}' "$OUT/table.md")
ERRS=$(awk -F'|' '/^\| \//{gsub(/ /,"",$10); s+=$10} END{print s+0}' "$OUT/table.md")
RSS=$(awk '$2>m{m=$2} END{printf "%d", m/1024}' "$OUT/rss.txt")

# Step 5: blocking tail diagnostic.
rsh "$LOAD" "bash /data/cache/proteus-perf/load/diag-blocking.sh /data/cache/proteus-perf/load/diag/nightly-$DAY > /dev/null 2>&1"
rsync -aq "$LOAD:/data/cache/proteus-perf/load/diag/nightly-$DAY/" "$OUT/diag/"
C=$(grep '^complete' "$OUT/diag/stages-1024.txt" 2>/dev/null)
TAIL="complete p99.9 $(echo "$C" | grep -o 'p999=[0-9]*' | cut -d= -f2) us, max $(echo "$C" | grep -o 'max=[0-9]*' | cut -d= -f2) us; wrk p99 $(grep ' 99%' "$OUT/diag/wrk-1024.txt" 2>/dev/null | awk '{print $2}')"

# Step 7: versus previous nightly, same range rule as the performance contract.
PREV=$(ls -d "$REPO"/proteus-benchmark/results/perf/nightly/*/ 2>/dev/null | grep -v "/$DAY/" | while read -r d; do [ -f "$d/B-r1.txt" ] && echo "$d"; done | sort | tail -1)
REGRESSED=0
if [ -n "$PREV" ]; then
  mkdir -p "$OUT/vs-previous"
  for r in 1 2 3; do
    sed "1s/name=[^ ]*/name=previous/" "$PREV/B-r$r.txt" > "$OUT/vs-previous/A-r$r.txt"
    cp "$OUT/B-r$r.txt" "$OUT/vs-previous/B-r$r.txt"
  done
  PREV_TABLE=$(python3 "$HARNESS/perf-table.py" "$OUT/vs-previous")
  rm -r "$OUT/vs-previous"
  W=$(echo "$PREV_TABLE" | grep -c '| WORSE |'); B=$(echo "$PREV_TABLE" | grep -c '| better |')
  PREVIOUS="$(basename "$PREV"): $B better, $W worse"
  [ "$W" -gt 0 ] && REGRESSED=1
else
  PREVIOUS="none"
fi

[ "$ERRS" != 0 ] && finish FAILED "$ERRS non-2xx or socket errors in measurement"
[ "$REGRESSED" = 1 ] && finish REGRESSED "cells below previous nightly range"
finish OK "complete"
