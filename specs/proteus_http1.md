# Proteus HTTP/1.1 Front End Specification

## Purpose

Serve HTTP/1.1 over plain TCP with a Proteus-owned connection, request parser, and response writer that produce and consume the standard Undertow `HttpServerExchange`, so every existing handler, wrapper, generated controller, form parser, and WebSocket upgrade keeps working while the per-request codec cost drops below Undertow's.

## Scope

- Module `proteus-core`, package `io.sinistral.proteus.server.http1`.
- HTTP/1.1 and HTTP/1.0 over `StreamConnection` from an XNIO `AcceptingChannel`. No TLS, no HTTP/2, no AJP, no proxy protocol. Deployments needing those keep `undertow.frontEnd=undertow`.
- Request bodies: `Content-Length` and `Transfer-Encoding: chunked`. `Expect: 100-continue` is honored.
- Keep-alive and pipelining per RFC 9112. `Connection: close` from either side ends the connection after the current response.
- Upgrade (`Connection: Upgrade`) hands the raw `StreamConnection` to the exchange's upgrade listener; WebSocket works unchanged.

## Configuration

```hocon
undertow {
  frontEnd = undertow   # or proteus
}
```

- `undertow`: `ProteusApplication.buildServer()` uses `Undertow.Builder` as today.
- `proteus`: `buildServer()` opens the HTTP listener itself on the application's XNIO worker with `Http1OpenListener`, applies the same `undertow.socket.*` options, and wraps the root handler identically (graceful shutdown, default handler). Every other `undertow.*` key keeps its meaning; `undertow.server.enableHttp2` is ignored with a WARN.
- `ProteusApplication.getUndertow()` returns `null` under `proteus`; `getListenerInfo()` and `getPorts()` are populated from the owned channel.

## Connection

`Http1ServerConnection extends AbstractServerConnection`.

- Constructed per accepted `StreamConnection` with the application's `ByteBufferPool`, root handler, option map, and buffer size.
- One `Http1ReadListener` per connection drives parsing; one `Http1ResponseConduit` per connection is reused across keep-alive requests.
- `getSinkConduit(exchange, next)` returns the response conduit wrapped by a fixed-length or chunked sink conduit from `io.undertow.conduits` according to the response headers, mirroring `HttpTransferEncoding.createSinkConduit` semantics: `Content-Length` set → fixed length; HTTP/1.1 without length → chunked; HTTP/1.0 without length → close after body.
- `upgradeChannel()` clears conduits and returns the channel with any over-read bytes re-fed through `ReadDataStreamSourceConduit`.
- `isContinueResponseSupported()` is true; `sendOutOfBandResponse` writes `100 Continue` directly.
- `exchangeComplete` re-arms reads for the next pipelined or keep-alive request, or closes when the exchange is not persistent.

## Request parsing

`Http1Parser` is a resumable byte-oriented state machine over `ByteBuffer`s from the pool.

- Request line: method, target, version. Method and version are matched against interned `HttpString` constants (`Methods.*`, `Protocols.*`) by length then byte compare; unknown methods construct one `HttpString`.
- Target is decoded by `Connectors.setExchangeRequestPath`, which splits path, query, and path parameters and applies `undertow.server.decodeUrl`, `urlCharset`, and `allowEncodedSlash` options.
- Header names: a static table of the `Headers.*` constants keyed by length and first byte; a hit reuses the interned `HttpString` with no hashing. A miss allocates one `HttpString`.
- Header values: one `String` per value, Latin-1, trailing whitespace trimmed. Obsolete line folding is rejected with 400.
- Limits from `UndertowOptions`: `MAX_HEADER_SIZE`, `MAX_HEADERS`, `MAX_PARAMETERS`, `MAX_ENTITY_SIZE`, `URL_CHARSET`. Violations send 400 or 413 and close.
- `Host` is required for HTTP/1.1 (400 otherwise); duplicate `Host` is 400. No syntax validation beyond presence and count; a load balancer or `HostHeaderHandler` in the chain owns that.
- After headers: request `Content-Length` → `FixedLengthStreamSourceConduit`; `chunked` → `ChunkedStreamSourceConduit`; neither → request terminated immediately. Leftover bytes in the read buffer are kept as `extraBytes` for the next request.

## Response writing

`Http1ResponseConduit extends AbstractStreamSinkConduit<StreamSinkConduit>`.

- On first write or flush, serializes `HTTP/1.1 <status> <reason>\r\n`, then headers, then `\r\n` into one pooled buffer, followed by as much body as fits, and issues one gathering write.
- Status line uses a static table of status code strings for `StatusCodes` constants.
- Headers are iterated with `HeaderMap.fastIterateNonEmpty` once. Each header name is appended from its `HttpString` bytes; values are written Latin-1.
- `Date` is taken from `DateUtils` cached value when `ALWAYS_SET_DATE`; `Connection: keep-alive` is added for HTTP/1.0 persistent connections when `ALWAYS_SET_KEEP_ALIVE`; `Content-Length: 0` is set for header-only responses that allow a body.
- When the full head does not fit the pool buffer, the conduit continues statefully across writes; it never allocates an unpooled buffer.
- Reason phrase comes from `exchange.getReasonPhrase()` only when non-null; otherwise the static table, avoiding the attachment lookup on the common path.

## Listener

`Http1OpenListener implements OpenListener`.

- `handleEvent(StreamConnection)` applies `TCP_NODELAY`, creates `Http1ServerConnection`, installs the read listener, and resumes reads.
- Root handler and `OptionMap` are settable before the channel is opened; changes after are rejected.
- Connection count and request count are exposed through `ConnectorStatistics` when `ENABLE_STATISTICS`.

## Behavioral contract versus Undertow's codec

Every item below is verified by the existing `proteus-core`, `proteus-openapi`, and `proteus-websocket` test suites running under both `frontEnd` values, plus `Http1ConformanceTest`:

- Identical status, headers, and body for every route in `StandardEndpointsTest`, `SecurityEndpointsTest`, `UploadEndpointsTest`, `BlockingEndpointTest`, OpenAPI document and UI routes, and WebSocket handshake and echo.
- Pipelined requests on one connection are answered in order.
- A request with a body larger than `MAX_ENTITY_SIZE` receives 413 and the connection closes.
- Malformed request lines, header lines with bare CR or LF, and folded headers receive 400 and the connection closes.
- A client that half-closes after its request still receives the full response.
- `Expect: 100-continue` receives `100 Continue` before the body is read when the handler reads the body, and the final status without it when the handler responds without reading.
- Keep-alive connections survive `undertow.socket.readTimeout` idle rearm; an idle connection past the timeout is closed.

## Performance gate

`proteus-benchmark` runs the ten standard routes under both front ends on the same host, pinned, three repeats. The Proteus front end ships as a supported option only when its median on every I/O-thread route is at or above Undertow's with no route more than 2% below, and p99 is not worse. The reference default remains `undertow` until a sweep on at least two hosts shows the Proteus front end ahead on all of them.

## Not in scope

- HTTP/2, TLS, ALPN, proxy protocol, AJP: use the Undertow front end.
- Trailers on chunked request bodies: discarded.
- `CONNECT`: 405.
