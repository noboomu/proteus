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
 * Indicates that the route may block and should execute on a virtual thread
 * rather than Undertow's I/O thread. A method-level value overrides a
 * class-level value.
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Blocking
{
    boolean value() default true;
}



