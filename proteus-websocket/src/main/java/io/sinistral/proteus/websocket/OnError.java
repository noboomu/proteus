package io.sinistral.proteus.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks an error handler method on a {@link WebSocket} endpoint.
 * The method accepts {@code WebSocketConnection} and {@code Throwable} in either order.
 *
 * @author jbauer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnError {
}
