/**
 * 
 */
package io.sinistral.proteus.annotations;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import io.undertow.server.HandlerWrapper;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
/**
 * @author jbauer
 *
 */
public @interface Chain
{
	 Class<? extends HandlerWrapper>[] value();
}

 