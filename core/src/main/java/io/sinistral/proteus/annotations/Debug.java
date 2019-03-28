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
 * Indicates that this route should use a RequestDumpingHandler
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Debug
{
    boolean value() default true;
}



