package io.sinistral.proteus.security.jwt;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a JSON Web Token with convenient access to standard and custom claims.
 * This interface provides type-safe access to JWT claims and follows the
 * JWT RFC 7519 specification.
 *
 * <p>Standard JWT claims are provided as dedicated methods for convenience,
 * while custom claims can be accessed via the generic claim methods.
 *
 * @since 1.0
 */
public interface JsonWebToken {

    // Standard JWT Claims (RFC 7519)

    /**
     * Gets the "iss" (issuer) claim.
     *
     * @return The issuer or empty if not present
     */
    Optional<String> getIssuer();

    /**
     * Gets the "sub" (subject) claim.
     *
     * @return The subject or empty if not present
     */
    Optional<String> getSubject();

    /**
     * Gets the "aud" (audience) claim.
     *
     * @return The audience list or empty if not present
     */
    List<String> getAudience();

    /**
     * Gets the "exp" (expiration time) claim.
     *
     * @return The expiration time or empty if not present
     */
    Optional<Instant> getExpirationTime();

    /**
     * Gets the "nbf" (not before) claim.
     *
     * @return The not before time or empty if not present
     */
    Optional<Instant> getNotBefore();

    /**
     * Gets the "iat" (issued at) claim.
     *
     * @return The issued at time or empty if not present
     */
    Optional<Instant> getIssuedAt();

    /**
     * Gets the "jti" (JWT ID) claim.
     *
     * @return The JWT ID or empty if not present
     */
    Optional<String> getJwtId();

    // Generic Claim Access

    /**
     * Gets a claim value as a string.
     *
     * @param claimName The name of the claim
     * @return The claim value or empty if not present
     */
    Optional<String> getClaimAsString(String claimName);

    /**
     * Gets a claim value as a long.
     *
     * @param claimName The name of the claim
     * @return The claim value or empty if not present or not a valid number
     */
    Optional<Long> getClaimAsLong(String claimName);

    /**
     * Gets a claim value as a boolean.
     *
     * @param claimName The name of the claim
     * @return The claim value or empty if not present or not a valid boolean
     */
    Optional<Boolean> getClaimAsBoolean(String claimName);

    /**
     * Gets a claim value as a list of strings.
     *
     * @param claimName The name of the claim
     * @return The claim value as a list or empty list if not present
     */
    List<String> getClaimAsStringList(String claimName);

    /**
     * Gets a raw claim value as an Object.
     *
     * @param claimName The name of the claim
     * @return The claim value or empty if not present
     */
    Optional<Object> getClaim(String claimName);

    /**
     * Gets all claim names present in this token.
     *
     * @return Set of all claim names
     */
    Set<String> getClaimNames();

    // Security and Validation

    /**
     * Performs a local temporal check of the {@code exp} and {@code nbf} claims against the
     * current clock.
     *
     * This method does not authenticate the token. Use JwtService.validateToken
     * or processAuthorizationHeader before trusting claims.
     *
     * @return true when present {@code exp} and {@code nbf} claims pass the local time check
     */
    boolean isValid();

    /**
     * Checks if this token has expired.
     *
     * @return true if the token has expired
     */
    boolean isExpired();

    /**
     * Gets the raw token string.
     *
     * @return The original JWT token string
     */
    String getRawToken();

    // Convenience methods for common patterns

    /**
     * Gets the user ID from either "sub" or "user_id" claims.
     *
     * @return The user ID or empty if not present
     */
    default Optional<String> getUserId() {
        return getSubject().or(() -> getClaimAsString("user_id"));
    }

    /**
     * Gets the roles from common role claim names.
     * Checks "roles", "authorities", "groups" in that order.
     *
     * @return List of roles or empty list if not present
     */
    default List<String> getRoles() {
        List<String> roles = getClaimAsStringList("roles");
        if (!roles.isEmpty()) {
            return roles;
        }

        roles = getClaimAsStringList("authorities");
        if (!roles.isEmpty()) {
            return roles;
        }

        return getClaimAsStringList("groups");
    }

    /**
     * Checks if the token contains any of the specified roles.
     *
     * @param requiredRoles The roles to check for
     * @return true if the token contains at least one of the required roles
     */
    default boolean hasAnyRole(String... requiredRoles) {
        List<String> tokenRoles = getRoles();
        for (String requiredRole : requiredRoles) {
            if (tokenRoles.contains(requiredRole)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the token contains all of the specified roles.
     *
     * @param requiredRoles The roles to check for
     * @return true if the token contains all of the required roles
     */
    default boolean hasAllRoles(String... requiredRoles) {
        List<String> tokenRoles = getRoles();
        for (String requiredRole : requiredRoles) {
            if (!tokenRoles.contains(requiredRole)) {
                return false;
            }
        }
        return true;
    }
}
