/**
 *
 */
package io.sinistral.proteus.annotations;

import io.undertow.server.HandlerWrapper;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/** Marks a controller or method for wrapping with the given handler wrappers. */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Chain
{
    /**
     * Returns the wrapper classes to apply, outermost first.
     *
     * @return the wrapper classes
     */
    Class<? extends HandlerWrapper>[] value();
}



