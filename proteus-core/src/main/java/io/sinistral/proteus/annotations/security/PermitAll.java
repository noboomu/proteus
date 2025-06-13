package io.sinistral.proteus.annotations.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that all roles are permitted to access this resource.
 * This annotation is equivalent to a wildcard (*) role specification.
 * 
 * <p>When applied to a class, all methods in the class are accessible to all roles.
 * When applied to a method, that specific method is accessible to all roles,
 * potentially overriding more restrictive class-level annotations.
 *
 * <p>Example:
 * <pre>
 * {@code
 * @RolesAllowed("admin")
 * public class Resource {
 *
 *     @PermitAll
 *     public Response publicMethod() {
 *         // Accessible to all roles, overrides class restriction
 *     }
 *
 *     public Response adminMethod() {
 *         // Only admin role can access (inherits from class)
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
public @interface PermitAll {
}
