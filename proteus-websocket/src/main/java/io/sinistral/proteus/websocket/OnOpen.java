package io.sinistral.proteus.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a connection-open handler method on a {@link WebSocket} endpoint.
 * The method accepts {@code WebSocketConnection}.
 *
 * @author jbauer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnOpen {
}
