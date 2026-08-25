package io.sinistral.proteus.annotations.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies the list of roles permitted to access method(s) in an application.
 * The value of the RolesAllowed annotation is a list of security role names.
 * This annotation can be specified on a class or on method(s). Specifying it
 * at a class level means that it applies to all the methods in the class.
 * Specifying it on a method means that it is applicable to that method only.
 * If applied at both the class and methods level, the method value overrides
 * the class value if the two conflict. Access is granted when the authenticated
 * principal has any declared role. An empty role list denies all access. Blank or
 * duplicate role names, and multiple Proteus security annotations on the same element,
 * are invalid and fail application startup.
 *
 * <p>Example:
 * <pre>
 * {@code
 * @RolesAllowed("user")
 * public class Resource {
 *
 *     @RolesAllowed({"admin", "moderator"})
 *     public Response adminMethod() {
 *         // Only admin or moderator roles can access
 *     }
 *
 *     public Response userMethod() {
 *         // Any user role can access (inherits from class)
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
public @interface RolesAllowed {

    /**
     * List of roles that are permitted access. A caller needs any one declared role.
     * An empty array means deny all access.
     *
     * @return An array of role names
     */
    String[] value();
}
