/**
 *
 */
package io.sinistral.proteus.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Indicates that a route may block. When Undertow invokes the generated handler on an I/O
 * thread, Proteus dispatches the controller invocation to a virtual thread. An invocation that
 * has already been dispatched remains on its current thread. A method-level value overrides a
 * class-level value.
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Blocking
{
    /**
     * Whether blocking dispatch is enabled for the annotated route or controller.
     *
     * @return {@code true} to enable blocking dispatch
     */
    boolean value() default true;
}


