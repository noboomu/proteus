#!/usr/bin/env python3
# Summarises wrk pipelined logs (<fe>-r<N>.txt) in a directory: median/min/max req/s, p99, errors per route x conns.
import re, glob, statistics, sys, os

def parse(f):
    out = {}; key = None
    for line in open(f):
        m = re.match(r"=== (\S+) c=(\d+)", line)
        if m:
            key = (m.group(1), int(m.group(2))); out[key] = {'err': 0}; continue
        if key is None:
            continue
        m = re.match(r"Requests/sec:\s+([\d.]+)", line.strip())
        if m: out[key]['rps'] = float(m.group(1))
        m = re.match(r"\s*99%\s+([\d.]+)(us|ms|s)\b", line)
        if m:
            v = float(m.group(1)); u = m.group(2)
            out[key]['p99'] = v / 1000 if u == 'us' else v * 1000 if u == 's' else v
        m = re.search(r"Non-2xx or 3xx responses: (\d+)", line)
        if m: out[key]['err'] += int(m.group(1))
        m = re.search(r"Socket errors: connect (\d+), read (\d+), write (\d+), timeout (\d+)", line)
        if m: out[key]['err'] += sum(int(x) for x in m.groups())
    return out

d = sys.argv[1]
fes = sorted({os.path.basename(f).split('-r')[0] for f in glob.glob(f"{d}/*-r*.txt")})
data = {fe: [parse(f) for f in sorted(glob.glob(f"{d}/{fe}-r*.txt"))] for fe in fes}
keys = sorted({k for runs in data.values() for r in runs for k in r})
print("| route | conns | " + " | ".join(f"{fe} req/s median [min-max] | p99 ms | errors" for fe in fes) + " |")
print("|---|---:|" + "---:|---:|---:|" * len(fes))
for k in keys:
    cells = []
    for fe in fes:
        r = [run[k]['rps'] for run in data[fe] if 'rps' in run.get(k, {})]
        p = [run[k]['p99'] for run in data[fe] if 'p99' in run.get(k, {})]
        e = sum(run[k]['err'] for run in data[fe] if k in run)
        cells.append(f"{statistics.median(r)/1e6:.2f}M [{min(r)/1e6:.2f}-{max(r)/1e6:.2f}] | {statistics.median(p):.1f} | {e}")
    print(f"| `/{k[0]}` | {k[1]} | " + " | ".join(cells) + " |")
