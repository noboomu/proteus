/**
 * Proteus-owned HTTP/1.1 front end producing standard Undertow {@code HttpServerExchange}s.
 *
 * <p>Selected with {@code undertow.frontEnd=proteus}. {@link io.sinistral.proteus.server.http1.Http1OpenListener}
 * accepts connections; the parser, read listener, and response conduit replace Undertow's
 * HTTP/1.1 codec while every handler-facing type stays the same. Plain TCP only.
 */
package io.sinistral.proteus.server.http1;
