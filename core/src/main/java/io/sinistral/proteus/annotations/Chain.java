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

/**
 * Decorates all methods of a controller or a single controller method with one or more <code>HandlerWrapper</code> classes.
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Chain
{
    Class<? extends HandlerWrapper>[] value();
}



