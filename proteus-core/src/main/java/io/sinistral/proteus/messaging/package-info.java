/**
 * Embedded Vert.x event bus exposed as a managed service.
 *
 * <p>{@link io.sinistral.proteus.messaging.EventBusService} provides publish, send, and
 * request-reply with typed consumers; {@link io.sinistral.proteus.messaging.ConsumeEvent}
 * registers consumer methods on Guice singletons; payloads cross the wire as JSON via
 * {@link io.sinistral.proteus.messaging.JsonMessageCodec} and stay by reference locally.
 */
package io.sinistral.proteus.messaging;
