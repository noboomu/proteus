package io.sinistral.proteus.annotations.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the resource allows anonymous access.
 * 
 * <p>No authentication is required. Anonymous requests are admitted, and invalid 
 * credentials do not cause rejection. However, if a valid optional Bearer token 
 * is supplied, it will still populate the {@code SecurityContext}.
 *
 * <p>When applied to a class, all methods in the class are anonymous-accessible.
 * When applied to a method, that specific method is accessible, overriding any 
 * restrictive class-level annotations. Multiple security annotations on the same 
 * element are invalid and fail application startup.
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
