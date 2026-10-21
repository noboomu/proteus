#!/usr/bin/env python3
# Reduces perf-ab.sh logs (A-r*.txt, B-r*.txt) to the specs/proteus_performance.md result table and verdict.
import re, glob, statistics, sys, os

def parse(f):
    out = {}; key = None
    for line in open(f):
        m = re.match(r"=== (\S+) c=(\d+) depth=(\d+)", line)
        if m:
            key = (m.group(1), int(m.group(3)), int(m.group(2))); out[key] = {'err': 0}; continue
        if key is None: continue
        m = re.match(r"Requests/sec:\s+([\d.]+)", line.strip())
        if m: out[key]['rps'] = float(m.group(1))
        m = re.match(r"\s*99%\s+([\d.]+)(us|ms|s)\b", line)
        if m:
            v = float(m.group(1)); u = m.group(2)
            out[key]['p99'] = v / 1000 if u == 'us' else v * 1000 if u == 's' else v
        m = re.search(r"Non-2xx or 3xx responses: (\d+)", line)
        if m: out[key]['err'] += int(m.group(1))
        m = re.search(r"Socket errors: connect (\d+), read (\d+), write (\d+), timeout (\d+)", line)
        if m: out[key]['err'] += sum(int(x) for x in m.groups()[:3]); out[key]['to'] = int(m.groups()[3])
    return out

d = sys.argv[1]
runs = {s: [parse(f) for f in sorted(glob.glob(f"{d}/{s}-r*.txt"))] for s in 'AB'}
names = {s: open(sorted(glob.glob(f"{d}/{s}-r*.txt"))[0]).readline().split()[1].split('=')[1] for s in 'AB'}
keys = list(runs['A'][0].keys())
print(f"| route | pipeline | conns | {names['A']} median [min-max] | p99 ms | err | {names['B']} median [min-max] | p99 ms | err | delta | verdict |")
print("|---|---:|---:|---:|---:|---:|---:|---:|---:|---:|---|")
improved = regressed = 0; errs = 0
for k in keys:
    cells = []; st = {}
    for s in 'AB':
        r = [x[k]['rps'] for x in runs[s] if 'rps' in x.get(k, {})]
        p = [x[k]['p99'] for x in runs[s] if 'p99' in x.get(k, {})]
        e = sum(x[k]['err'] for x in runs[s] if k in x)
        st[s] = (statistics.median(r), min(r), max(r), e)
        cells.append(f"{statistics.median(r)/1e3:,.0f}k [{min(r)/1e3:,.0f}-{max(r)/1e3:,.0f}] | {statistics.median(p):.1f} | {e}")
    v = 'better' if st['B'][1] > st['A'][2] else 'WORSE' if st['B'][2] < st['A'][1] else 'overlap'
    improved += v == 'better'; regressed += v == 'WORSE'; errs += st['B'][3]
    print(f"| /{k[0]} | {k[1]} | {k[2]} | {cells[0]} | {cells[1]} | {100*(st['B'][0]/st['A'][0]-1):+.1f}% | {v} |")
ok = regressed == 0 and improved > 0 and errs == 0
print(f"\n{names['B']} vs {names['A']}: improved {improved}, regressed {regressed}, errors {errs} -> {'ACCEPT' if ok else 'REJECT'}")
