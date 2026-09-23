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
    /** The event bus address to consume.
     *
     * @return the event bus address
     */
    String value();

    /** True to execute on the virtual-thread blocking executor.
     *
     * @return the blocking flag
     */
    boolean blocking() default false;

    /** True to dispatch messages serially in arrival order.
     *
     * @return the ordered flag
     */
    boolean ordered() default false;

    /** True to receive only messages delivered within this process.
     *
     * @return the local-only flag
     */
    boolean local() default true;

    /** Empty for the default JSON codec, otherwise a registered codec name.
     *
     * @return the codec selector
     */
    String codec() default "";
}
