/**
 *
 */
package io.sinistral.proteus.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Marks a controller or method for debug logging of request processing. */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Debug
{
    /**
     * Returns true to enable debug logging.
     *
     * @return true when debug logging is enabled
     */
    boolean value() default true;
}



