package io.sinistral.proteus.messaging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method of a Guice singleton bean as an event bus consumer.
 *
 * <p>The annotated method accepts either the raw payload type or {@code Message<PayloadType>}
 * as its single parameter. It may return a value, a {@code CompletableFuture}, or
 * {@code void}; a returned value or completed future is used as the reply body for
 * request-reply messages.
 *
 * @author jbauer
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsumeEvent {
    /** @return the event bus address to consume */
    String value();

    /** @return true to execute on the virtual-thread blocking executor */
    boolean blocking() default false;

    /** @return true to dispatch messages serially in arrival order */
    boolean ordered() default false;

    /** @return true to receive only messages delivered within this process */
    boolean local() default true;

    /** @return empty for the default JSON codec, otherwise a registered codec name */
    String codec() default "";
}
