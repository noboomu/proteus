package io.sinistral.proteus.annotations.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Injects a claim value from the current JWT token into a method parameter.
 * 
 * <p><strong>Security Note:</strong> Any method containing a {@code @Claim} parameter
 * (including Optional or required=false claims) implicitly requires an authenticated JWT. 
 * If the class or method lacks an explicit security policy, the effective policy becomes 
 * {@code AUTHENTICATED}. To override this fallback and allow anonymous access, explicitly
 * annotate the method or class with {@code @PermitAll}.
 *
 * <p>Supported parameter types:
 * <ul>
 *   <li>Scalars: {@code String}, {@code Long}, {@code Boolean}</li>
 *   <li>Lists: {@code List<String>}, {@code List<Long>}, {@code List<Boolean>}</li>
 *   <li>Optionals: {@code Optional<String>}, {@code Optional<Long>}, {@code Optional<Boolean>}</li>
 * </ul>
 *
 * <p><strong>Missing Values:</strong>
 * <ul>
 *   <li>If required=true: The endpoint rejects a request that lacks the claim.</li>
 *   <li>If required=false and the type is a scalar: Primitive targets (like {@code long}) fail startup validation; wrapper types (like {@code Long}) receive {@code null}. A {@code defaultValue} is applied only to {@code String} parameters.</li>
 *   <li>If required=false and the type is an Optional: Receives {@code Optional.empty()}.</li>
 *   <li>If required=false and the type is a List: Receives {@code List.of()}.</li>
 * </ul>
 *
 * <p><strong>Validation:</strong> Unsupported nested generics fail fast during application startup.
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
     * If true and the claim is not present, the endpoint rejects the request. If false,
     * absent wrapper scalar claims receive {@code null}; absent list claims receive an empty
     * list; and {@link java.util.Optional} parameters receive {@code Optional.empty()}.
     * Optional parameters are always optional, so this setting is ignored for them.
     *
     * @return true if the claim is required
     */
    boolean required() default true;
}
