/**
 * Application bootstrap for Proteus servers.
 *
 * <p>{@link io.sinistral.proteus.ProteusApplication} owns the Guice injector, registers
 * controllers, services, and modules, generates and compiles route handlers at startup, and
 * manages the Undertow listener lifecycle together with Guava's {@code ServiceManager}.
 */
package io.sinistral.proteus;
