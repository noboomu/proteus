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
 *   <li>Scalars: {@code String}, {@code Long}, {@code long}, {@code Boolean}, {@code boolean}</li>
 *   <li>Lists: {@code List<String>}, {@code List<Long>}, {@code List<Boolean>}</li>
 *   <li>Optionals: {@code Optional<String>}, {@code Optional<Long>}, {@code Optional<Boolean>}</li>
 * </ul>
 *
 * <p><strong>Missing Values:</strong>
 * <ul>
 *   <li>A non-empty {@code defaultValue} supplies a missing {@code String} claim before
 *       {@code required} is considered.</li>
 *   <li>For other scalar and list parameters, {@code required=true} rejects a missing claim.</li>
 *   <li>With {@code required=false}, boxed scalars receive {@code null}, and lists receive
 *       {@code List.of()}; optional primitive scalar targets fail startup validation.</li>
 *   <li>{@code Optional<T>} always receives {@code Optional.empty()} when absent, and ignores
 *       the {@code required} setting.</li>
 * </ul>
 *
 * <p><strong>Validation:</strong> Unsupported scalar types, generic element types, and nested
 * generics fail fast during application startup.
 *
 * <p>Example:
 * <pre>
 * {@code
 * @GET
 * @Path("/profile")
 * @RolesAllowed("user")
 * public ServerResponse<?> getUserProfile(@Claim("sub") String userId,
 *                                         @Claim("roles") List<String> roles,
 *                                         @Claim("exp") Long expiration,
 *                                         @Claim(value = "email", defaultValue = "unknown")
 *                                         String email) {
 *     return response(Map.of("userId", userId, "roles", roles, "email", email))
 *         .applicationJson();
 * }
 * }
 * </pre>
 *
 * @since 0.9.5
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
     * Only applicable for String parameters. A non-empty default takes precedence over
     * {@link #required()} for a missing claim.
     *
     * @return The default value
     */
    String defaultValue() default "";

    /**
     * Whether this claim is required.
     * If true and a non-defaulted scalar or list claim is absent, the endpoint rejects the
     * request. If false, absent wrapper scalar claims receive {@code null} and absent list claims
     * receive an empty list. {@link java.util.Optional} parameters are always optional, so this
     * setting is ignored for them.
     *
     * @return true if the claim is required
     */
    boolean required() default true;
}
