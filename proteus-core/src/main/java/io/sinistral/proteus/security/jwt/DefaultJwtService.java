package io.sinistral.proteus.security.jwt;

import io.sinistral.proteus.security.DefaultSecurityContext;
import io.sinistral.proteus.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of JwtService.
 * This service extracts Bearer credentials, parses tokens, validates time, issuer, audience,
 * algorithm, and signature policy, then creates an explicit request security context.
 * Configuration fails closed by default when signature verification is required but no usable
 * key verifies the token.
 *
 * @since 1.0
 */
@Singleton
public class DefaultJwtService implements JwtService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJwtService.class);
    private final JwtConfiguration configuration;

    /**
     * Creates a service backed by one immutable JWT policy.
     *
     * @param configuration validation and verification configuration
     */
    @Inject
    public DefaultJwtService(JwtConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Optional<String> extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.trim().isEmpty()) {
            return Optional.empty();
        }

        String trimmed = authorizationHeader.trim();
        int separator = trimmed.indexOf(' ');
        if (
            separator > 0 &&
            trimmed.substring(0, separator).equalsIgnoreCase("Bearer")
        ) {
            String token = trimmed.substring(separator + 1).trim();
            return token.isEmpty() ? Optional.empty() : Optional.of(token);
        }

        return Optional.empty();
    }

    @Override
    public JsonWebToken parseToken(String tokenString) {
        if (tokenString == null || tokenString.trim().isEmpty()) {
            throw new JwtParsingException("Token string cannot be null or empty");
        }

        try {
            return new DefaultJsonWebToken(tokenString.trim());
        } catch (Exception e) {
            logger.debug("Failed to parse JWT token", e);
            throw new JwtParsingException("Invalid JWT token format", e);
        }
    }

    @Override
    public boolean validateToken(JsonWebToken token) {
        try {
            // Basic time-based validation
            if (!isTimeValid(token)) {
                logger.debug("Token failed time validation");
                return false;
            }

            // Issuer validation
            if (!isIssuerValid(token)) {
                logger.debug("Token failed issuer validation");
                return false;
            }

            // Audience validation
            if (!isAudienceValid(token)) {
                logger.debug("Token failed audience validation");
                return false;
            }

            if (!isAlgorithmAllowed(token)) {
                logger.debug("Token uses an algorithm that is not allowed");
                return false;
            }

            // Signature validation (if keys are configured)
            if (!isSignatureValid(token)) {
                logger.debug("Token failed signature validation");
                return false;
            }

            return true;
        } catch (Exception e) {
            logger.debug("Token validation failed with exception", e);
            return false;
        }
    }

    private boolean isTimeValid(JsonWebToken token) {
        Instant now = Instant.now();
        long skewSeconds = getClockSkewToleranceSeconds();

        // Check expiration with skew tolerance
        Optional<Instant> exp = token.getExpirationTime();
        if (configuration.isExpirationRequired() && exp.isEmpty()) {
            return false;
        }
        if (exp.isPresent() && now.isAfter(exp.get().plusSeconds(skewSeconds))) {
            return false;
        }

        // Check not before with skew tolerance
        Optional<Instant> nbf = token.getNotBefore();
        if (nbf.isPresent() && now.isBefore(nbf.get().minusSeconds(skewSeconds))) {
            return false;
        }

        Optional<Instant> issuedAt = token.getIssuedAt();
        if (
            issuedAt.isPresent() &&
            issuedAt.get().isAfter(now.plusSeconds(skewSeconds))
        ) {
            return false;
        }

        long maxTokenAgeSeconds = configuration.getMaxTokenAgeSeconds();
        if (maxTokenAgeSeconds > 0) {
            if (
                issuedAt.isEmpty() ||
                now.isAfter(
                    issuedAt.get().plusSeconds(maxTokenAgeSeconds + skewSeconds)
                )
            ) {
                return false;
            }
        }

        return true;
    }

    private boolean isIssuerValid(JsonWebToken token) {
        List<String> allowedIssuers = getAllowedIssuers();
        if (configuration.isIssuerRequired() && token.getIssuer().isEmpty()) {
            return false;
        }
        if (allowedIssuers.isEmpty()) {
            return true; // No issuer restriction
        }

        Optional<String> tokenIssuer = token.getIssuer();
        return tokenIssuer.map(allowedIssuers::contains).orElse(false);
    }

    private boolean isAudienceValid(JsonWebToken token) {
        List<String> allowedAudiences = getAllowedAudiences();
        if (configuration.isAudienceRequired() && token.getAudience().isEmpty()) {
            return false;
        }
        if (allowedAudiences.isEmpty()) {
            return true; // No audience restriction
        }

        List<String> tokenAudiences = token.getAudience();
        return tokenAudiences.stream().anyMatch(allowedAudiences::contains);
    }

    private boolean isAlgorithmAllowed(JsonWebToken token) {
        if (!(token instanceof DefaultJsonWebToken defaultToken)) {
            return !configuration.isSignatureVerificationRequired();
        }
        String algorithm = defaultToken
            .getSignedJWT()
            .getHeader()
            .getAlgorithm()
            .getName();
        return configuration.getAllowedAlgorithms().contains(algorithm);
    }

    private boolean isSignatureValid(JsonWebToken token) {
        if (!(token instanceof DefaultJsonWebToken)) {
            logger.warn("Cannot validate signature for non-default token implementation");
            return !configuration.isSignatureVerificationRequired();
        }

        DefaultJsonWebToken defaultToken = (DefaultJsonWebToken) token;

        // Try RSA keys
        for (RSAPublicKey key : configuration.getRsaPublicKeys()) {
            try {
                if (defaultToken.verifySignature(key)) {
                    return true;
                }
            } catch (Exception e) {
                logger.debug("RSA signature validation failed", e);
            }
        }

        // Try EC keys
        for (ECPublicKey key : configuration.getEcPublicKeys()) {
            try {
                if (defaultToken.verifySignature(key)) {
                    return true;
                }
            } catch (Exception e) {
                logger.debug("EC signature validation failed", e);
            }
        }

        // Try HMAC secrets
        for (byte[] secret : configuration.getHmacSecrets()) {
            try {
                if (defaultToken.verifySignature(secret)) {
                    return true;
                }
            } catch (Exception e) {
                logger.debug("HMAC signature validation failed", e);
            }
        }

        // No configured key can authenticate a token. Fail closed when verification is
        // required; only an explicit signatureVerificationRequired=false permits parsing
        // without signature verification.
        boolean hasKeys = !configuration.getRsaPublicKeys().isEmpty() ||
                         !configuration.getEcPublicKeys().isEmpty() ||
                         !configuration.getHmacSecrets().isEmpty();

        if (!hasKeys) {
            if (configuration.isSignatureVerificationRequired()) {
                logger.warn("No signing keys configured; rejecting signed-token authentication");
                return false;
            }
            logger.warn("No signing keys configured; signature verification is disabled");
            return true;
        }

        return false;
    }

    @Override
    public boolean validateTokenSignature(JsonWebToken token, RSAPublicKey publicKey) {
        if (!(token instanceof DefaultJsonWebToken)) {
            return false;
        }

        try {
            return ((DefaultJsonWebToken) token).verifySignature(publicKey);
        } catch (Exception e) {
            logger.debug("RSA signature validation failed", e);
            return false;
        }
    }

    @Override
    public boolean validateTokenSignature(JsonWebToken token, ECPublicKey publicKey) {
        if (!(token instanceof DefaultJsonWebToken)) {
            return false;
        }

        try {
            return ((DefaultJsonWebToken) token).verifySignature(publicKey);
        } catch (Exception e) {
            logger.debug("EC signature validation failed", e);
            return false;
        }
    }

    @Override
    public boolean validateTokenSignature(JsonWebToken token, byte[] secret) {
        if (!(token instanceof DefaultJsonWebToken)) {
            return false;
        }

        try {
            return ((DefaultJsonWebToken) token).verifySignature(secret);
        } catch (Exception e) {
            logger.debug("HMAC signature validation failed", e);
            return false;
        }
    }

    @Override
    public SecurityContext createSecurityContext(JsonWebToken token) {
        DefaultSecurityContext.Builder builder = DefaultSecurityContext.builder()
                .userId(token.getUserId().orElse(null))
                .username(token.getClaimAsString("preferred_username")
                         .or(() -> token.getClaimAsString("name"))
                         .or(() -> token.getClaimAsString("username"))
                         .orElse(null))
                .roles(token.getRoles())
                .authenticationScheme("Bearer")
                .rawToken(token.getRawToken());

        // Add permissions if present
        List<String> permissions = token.getClaimAsStringList("permissions");
        if (!permissions.isEmpty()) {
            builder.permissions(permissions);
        }

        // Retain all claims for @Claim parameter injection.
        for (String claimName : token.getClaimNames()) {
            token.getClaim(claimName).ifPresent(value ->
                builder.attribute(claimName, value));
        }

        return builder.build();
    }


    @Override
    public Optional<SecurityContext> processAuthorizationHeader(String authorizationHeader) {
        try {
            Optional<String> tokenString = extractTokenFromHeader(authorizationHeader);
            if (tokenString.isEmpty()) {
                return Optional.empty();
            }

            JsonWebToken token = parseToken(tokenString.get());
            if (!validateToken(token)) {
                return Optional.empty();
            }

            return Optional.of(createSecurityContext(token));
        } catch (Exception e) {
            logger.debug("Failed to process authorization header", e);
            return Optional.empty();
        }
    }

    @Override
    public boolean hasAnyRole(JsonWebToken token, String... requiredRoles) {
        return token.hasAnyRole(requiredRoles);
    }

    @Override
    public boolean hasAllRoles(JsonWebToken token, String... requiredRoles) {
        return token.hasAllRoles(requiredRoles);
    }

    @Override
    public List<String> getAllowedIssuers() {
        return configuration.getAllowedIssuers();
    }

    @Override
    public List<String> getAllowedAudiences() {
        return configuration.getAllowedAudiences();
    }

    @Override
    public long getClockSkewToleranceSeconds() {
        return configuration.getClockSkewToleranceSeconds();
    }
}
