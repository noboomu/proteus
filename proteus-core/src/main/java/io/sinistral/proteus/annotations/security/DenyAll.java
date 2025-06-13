package io.sinistral.proteus.annotations.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Denies access to all roles for the annotated resource.
 * This annotation is used to explicitly restrict access to sensitive resources
 * or methods that should not be accessible via HTTP endpoints.
 * 
 * <p>When applied to a class, all methods in the class are inaccessible.
 * When applied to a method, that specific method is inaccessible,
 * potentially overriding more permissive class-level annotations.
 * 
 * <p>This annotation takes precedence over {@link PermitAll} and {@link RolesAllowed}.
 *
 * <p>Example:
 * <pre>
 * {@code
 * @PermitAll
 * public class Resource {
 *
 *     @DenyAll
 *     public Response sensitiveMethod() {
 *         // Not accessible via HTTP, overrides class permission
 *     }
 *
 *     public Response publicMethod() {
 *         // Accessible to all roles (inherits from class)
 *     }
 * }
 * }
 * </pre>
 *
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DenyAll {
}
