package io.sinistral.proteus.websocket;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a Guice singleton as a WebSocket endpoint at the given path.
 *
 * @author jbauer
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocket {
    /** @return the URL path the endpoint is served at */
    String value();
}
