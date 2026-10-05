/**
 * Runtime handler generation and the default Undertow handler chain.
 *
 * <p>{@link io.sinistral.proteus.server.handlers.HandlerGenerator} emits a routing supplier
 * from Jakarta REST annotated controllers, {@link io.sinistral.proteus.server.handlers.TypeHandler}
 * maps parameter types to extraction statements, and the {@code ServerDefault*} classes apply
 * global headers, render fallback and error responses, and route requests.
 */
package io.sinistral.proteus.server.handlers;
