#!/usr/bin/env python3
"""Summarize `jfr print --events jdk.ExecutionSample` output into leaf and inclusive frame tables."""
import re
import sys
from collections import Counter

FRAME = re.compile(r'^\s+([\w$.<>\[\]]+)\((?:[^)]*)\)\s+line:\s*(\d+)')
JAVA_THREAD = re.compile(r'sampledThread = "(.*?)"')

leaf = Counter()
inclusive = Counter()
threads = Counter()
total = 0
stack = []
in_stack = False

def flush():
    global total, stack
    if not stack:
        return
    total += 1
    leaf[stack[0]] += 1
    for f in set(stack):
        inclusive[f] += 1
    stack = []

for line in open(sys.argv[1], errors='replace'):
    m = JAVA_THREAD.search(line)
    if m:
        name = m.group(1)
        # collapse per-thread suffixes so pools aggregate
        name = re.sub(r'[- ]?(I/O-|task-|ForkJoinPool-\d+-worker-|VirtualThread-)\d+.*', r' \1*', name)
        threads[name or '(unnamed virtual thread)'] += 1
    if 'stackTrace = [' in line:
        flush()
        in_stack = True
        continue
    if in_stack:
        if line.strip() == ']':
            in_stack = False
            flush()
            continue
        fm = FRAME.match(line)
        if fm:
            stack.append(fm.group(1))
flush()

def table(title, counter, n=25):
    print(f"## {title}\n")
    print("| % | samples | frame |\n|---:|---:|---|")
    for frame, c in counter.most_common(n):
        print(f"| {100*c/total:.1f} | {c} | `{frame}` |")
    print()

print(f"# CPU profile\n\n{total} execution samples (jdk.ExecutionSample, settings=profile, ~10ms interval, Java frames only).\n")
table("Sampled threads", threads, 12)
table("Top leaf frames (self time)", leaf)
table("Top frames by inclusive samples", inclusive)
