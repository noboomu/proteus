package io.sinistral.proteus.annotations.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injects a claim value from the current JWT token into a method parameter.
 * The claim is extracted from the JWT token present in the current request context.
 * 
 * <p>This annotation can be applied to method parameters to automatically
 * inject JWT claim values without manual token parsing.
 *
 * <p>Supported parameter types:
 * <ul>
 *   <li>String - for string claims</li>
 *   <li>Long - for numeric claims</li>
 *   <li>Boolean - for boolean claims</li>
 *   <li>List&lt;String&gt; - for array claims</li>
 *   <li>Optional&lt;T&gt; - for optional claims</li>
 * </ul>
 *
 * <p>Example:
 * <pre>
 * {@code
 * @GET
 * @Path("/profile")
 * @RolesAllowed("user")
 * public Response getUserProfile(@Claim("sub") String userId,
 *                               @Claim("roles") List<String> roles,
 *                               @Claim("exp") Long expiration,
 *                               @Claim(value = "email", defaultValue = "unknown") String email) {
 *     // userId contains the "sub" claim value
 *     // roles contains the "roles" claim as a list
 *     // expiration contains the "exp" claim as a timestamp
 *     // email contains the "email" claim or "unknown" if not present
 * }
 * }
 * </pre>
 *
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Claim {
    
    /**
     * The name of the JWT claim to inject.
     * If not specified, the parameter name will be used as the claim name.
     * 
     * @return The claim name
     */
    String value() default "";
    
    /**
     * Default value to use if the claim is not present in the token.
     * Only applicable for String parameters.
     * 
     * @return The default value
     */
    String defaultValue() default "";
    
    /**
     * Whether this claim is required.
     * If true and the claim is not present, an exception will be thrown.
     * If false and the claim is not present, null (or default value) will be injected.
     * 
     * @return true if the claim is required
     */
    boolean required() default true;
}
