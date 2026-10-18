#!/usr/bin/env bash
# Counts recv() chunks for 16 pipelined requests per front end x bufferPipelinedData; fewer chunks = coalesced writes.
cd /tmp/proteus-bench
printf 'GET /plaintext/exchange HTTP/1.1\r\nHost: x\r\n\r\n%.0s' $(seq 1 15) > /tmp/proteus-bench/req16
printf 'GET /plaintext/exchange HTTP/1.1\r\nHost: x\r\nConnection: close\r\n\r\n' >> /tmp/proteus-bench/req16
for fe in proteus undertow; do for buf in true false; do
  taskset -c 0-7,128-135 jdk/bin/java -Dundertow.frontEnd=$fe -Dundertow.server.bufferPipelinedData=$buf -Dconfig.file=application.conf \
    -Dapplication.ports.http=21080 -jar proteus-benchmark.jar > /tmp/proteus-bench/smoke.log 2>&1 &
  spid=$!
  for _ in $(seq 1 60); do curl -sf http://127.0.0.1:21080/plaintext/exchange >/dev/null 2>&1 && break; sleep 0.5; done
  python3 - "$fe" "$buf" <<'PY'
import socket, sys, time
chunks = []
for trial in range(5):
    s = socket.create_connection(('127.0.0.1', 21080))
    s.sendall(open('/tmp/proteus-bench/req16', 'rb').read())
    time.sleep(0.2)
    d = b''; n = 0
    while True:
        c = s.recv(1 << 20)
        if not c: break
        d += c; n += 1
    chunks.append((n, d.count(b'HTTP/1.1 200')))
print(f"fe={sys.argv[1]} bufferPipelinedData={sys.argv[2]} (recv chunks, responses) per trial: {chunks}")
PY
  kill $spid; wait $spid 2>/dev/null
done; done
