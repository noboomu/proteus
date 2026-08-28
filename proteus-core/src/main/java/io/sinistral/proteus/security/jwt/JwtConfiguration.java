package io.sinistral.proteus.security.jwt;

import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

/**
 * Configuration interface for JWT processing.
 * This interface defines all the configuration options needed for JWT validation
 * including signing keys, validation rules, and security settings.
 *
 * @since 0.9.5
 */
public interface JwtConfiguration {

    /**
     * Gets the list of RSA public keys for signature verification.
     *
     * @return List of RSA public keys, empty if not configured
     */
    List<RSAPublicKey> getRsaPublicKeys();

    /**
     * Gets the list of EC public keys for signature verification.
     *
     * @return List of EC public keys, empty if not configured
     */
    List<ECPublicKey> getEcPublicKeys();

    /**
     * Gets the list of HMAC secrets for signature verification.
     *
     * @return List of HMAC secrets, empty if not configured
     */
    List<byte[]> getHmacSecrets();

    /**
     * Gets the JWS algorithms accepted for token authentication.
     *
     * @return Algorithm names such as {@code RS256}, {@code ES256}, or
     *         {@code HS256}
     */
    default List<String> getAllowedAlgorithms() {
        return List.of("RS256", "ES256", "HS256");
    }

    /**
     * Gets the list of allowed token issuers.
     * Only tokens from these issuers will be considered valid.
     *
     * @return List of allowed issuers, empty list allows all issuers
     */
    List<String> getAllowedIssuers();

    /**
     * Gets the list of allowed token audiences.
     * Only tokens with these audiences will be considered valid.
     *
     * @return List of allowed audiences, empty list allows all audiences
     */
    List<String> getAllowedAudiences();

    /**
     * Gets the clock skew tolerance for time-based validations in seconds.
     * This tolerance is applied to exp (expiration) and nbf (not before) claims
     * to account for clock differences between systems.
     *
     * @return Clock skew tolerance in seconds
     */
    long getClockSkewToleranceSeconds();

    /**
     * Whether to require token expiration claim.
     * If true, tokens without an 'exp' claim will be rejected.
     *
     * @return true if expiration claim is required
     */
    boolean isExpirationRequired();

    /**
     * Whether to require token issuer claim.
     * If true, tokens without an 'iss' claim will be rejected.
     *
     * @return true if issuer claim is required
     */
    boolean isIssuerRequired();

    /**
     * Whether to require token audience claim.
     * If true, tokens without an 'aud' claim will be rejected.
     *
     * @return true if audience claim is required
     */
    boolean isAudienceRequired();

    /**
     * Gets the maximum allowed token age in seconds.
     * This is measured from the 'iat' (issued at) claim.
     *
     * @return Maximum token age in seconds, 0 or negative means no limit
     */
    long getMaxTokenAgeSeconds();

    /**
     * Whether signature verification is required.
     * If true and no keys are configured, tokens will be rejected.
     * If false, signature verification is skipped when no keys are configured.
     *
     * @return true if signature verification is required
     */
    boolean isSignatureVerificationRequired();
}
