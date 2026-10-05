/**
 * Request and response abstractions over the Undertow exchange.
 *
 * <p>{@link io.sinistral.proteus.server.ServerRequest} exposes headers, parameters, and the
 * buffered body; {@link io.sinistral.proteus.server.ServerResponse} builds status, headers,
 * cookies, and a typed entity; {@link io.sinistral.proteus.server.Extractors} holds the static
 * parameter extraction functions that generated handlers call.
 */
package io.sinistral.proteus.server;
