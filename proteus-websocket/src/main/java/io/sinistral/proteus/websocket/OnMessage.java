package io.sinistral.proteus.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a frame handler method on a {@link WebSocket} endpoint.
 * The method accepts {@code String} or {@code byte[]} plus {@code WebSocketConnection}
 * in either order.
 *
 * @author jbauer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OnMessage {
}
