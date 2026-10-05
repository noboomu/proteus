/**
 * Guice modules installed by the application at startup.
 *
 * <p>{@code ConfigModule} binds Typesafe Config values, {@code ApplicationModule} binds the
 * router and default handlers, {@code JacksonModule} and {@code XmlModule} bind the JSON and
 * XML mappers, and {@code MessagingModule} binds the event bus and optional NATS bridge.
 */
package io.sinistral.proteus.modules;
