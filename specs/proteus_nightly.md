# Proteus Nightly Review

A scheduled, unattended run that verifies the development line, measures it against the pre-agent baseline on fixed hardware, and records the result in the repository.

## Scope

- Repository: the Proteus multi-module Maven project, branch `development`, at `origin/development` head.
- Hosts, roles, and pinning are those of the performance contract: server on attenborough (192.168.1.209) pinned `taskset -c 0-7,128-135`, load from godzilla (192.168.1.99) with `wrk`.
- The run never pushes code changes, never edits `master`, and never changes runtime configuration in `proteus-core`.
- The only commit the run makes is its own result directory under `proteus-benchmark/results/perf/nightly/`.

## Schedule

- Daily at 02:30 America/Los_Angeles.
- A run that finds either host busy defers itself by 30 minutes, at most four times, then records a `SKIPPED` result.

## Preconditions

- Busy means any of:
  - a process listening on server port 8080 on attenborough;
  - a running `wrk`, `perf-ab.sh`, `probe.sh`, or `nightly.sh` process on godzilla;
  - attenborough CPUs 0-7 and 128-135 more than 20% busy on average over a 5 s `/proc/stat` sample while no Proteus server is running.
- Working tree clean on `development` after `git fetch` and fast-forward to `origin/development`; a dirty or diverged tree records `SKIPPED` with the `git status` output.
- Staged artifacts on attenborough, under `/data/cache/proteus-perf/` (persistent disk; `/tmp` is tmpfs and is cleared on reboot):
  - JDK 27 at `jdk27/`
  - pre-agent baseline jar at `run/baseline.jar`, SHA-256 `ce5eb693d6e067dd0911f1cf08e7a9ead1e390ba6463e65128cfbc3545a39e59`
  - run directory `run/` for staged jars, pid files, and server logs
- Staged on godzilla, under `/data/cache/proteus-perf/load/`: `wrk` at `wrk-src/wrk`, drivers, and raw run output.
- A missing artifact records `FAILED` naming the artifact.

## Steps

1. `mvn -o clean install -Dgpg.skip` on JDK 27 for the full reactor.
2. Record total, failed, and errored test counts per module from surefire reports.
3. Build the performance-contract application against the freshly installed `proteus-core` and stage it on attenborough as `/data/cache/proteus-perf/run/nightly.jar`.
4. Run the performance-contract measurement, baseline as side A and `nightly` as side B, three alternating rounds, all cells.
5. Run the blocking tail diagnostic once against a diagnostic build of the same head:
   - `/json/blocking` at 256 and 1024 connections, 5 s warmup, 15 s measured;
   - server-side stage histograms `queue`, `complete`, `total`;
   - ParallelGC log for the run.
6. Record peak resident set size of the `nightly` server process during step 4, sampled every 5 s from `/proc/<pid>/status` `VmRSS`.
7. Compare with the previous nightly result, if any.
8. Write the result directory, commit it on `development`, and push that commit only.
9. Post a summary to bus subject `tachikoma.local.projects.proteus.work`.

## Result Directory

- Path: `proteus-benchmark/results/perf/nightly/<YYYY-MM-DD>/`
- Contents:
  - `SUMMARY.md`
  - `verify.txt`: per-module test counts and the reactor exit status
  - `A-r1.txt` … `B-r3.txt` and `table.md` from the measurement
  - `diag/`: `wrk-<conns>.txt`, `stages-<conns>.txt`, `diag-gc.log`
  - `rss.txt`: one `epoch_seconds rss_kib` line per sample
  - `env.txt`: head commit, JDK version, Undertow and XNIO versions, both hosts' load averages at start
- `SUMMARY.md` fields, in order:
  - `status`: `OK`, `REGRESSED`, `FAILED`, or `SKIPPED`
  - `head`: abbreviated commit
  - `tests`: total, failed, errored across modules
  - `baseline`: cells better, worse, and overlapping, using the performance-contract rules
  - `previous`: for each cell, this night's median versus the previous nightly median, flagged when the ranges do not overlap
  - `blocking_tail`: `complete` p99.9 and max at 1024 connections; wrk p99 at 1024 connections
  - `rss_peak_mib`
  - `notes`: the cause of any `FAILED` or `SKIPPED`

## Status Rules

- `FAILED`: any test failure or error, build failure, missing artifact, or non-2xx/socket errors in the measurement.
- `REGRESSED`: no failure, and at least one cell whose range lies wholly below the previous nightly range.
- `OK`: neither of the above.
- `SKIPPED`: preconditions unmet after four deferrals.

## Verification

- A manual run of the driver with the hosts idle produces a complete result directory whose `SUMMARY.md` has every field populated.
- A manual run while port 8080 is held on attenborough records `SKIPPED` without starting any server or load.
- `table.md` of a nightly run is byte-identical to `perf-table.py` run over the same `A-r*.txt`/`B-r*.txt` files.
- No process started by the run remains on either host after it exits.
