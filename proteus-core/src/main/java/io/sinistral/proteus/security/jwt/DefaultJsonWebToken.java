package io.sinistral.proteus.security.jwt;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Default implementation of JsonWebToken using Nimbus JOSE + JWT library.
 * This implementation provides efficient JWT parsing and validation with
 * support for RSA, ECDSA, and HMAC signature algorithms.
 *
 * @since 1.0
 */
public class DefaultJsonWebToken implements JsonWebToken {

    private static final Logger logger = LoggerFactory.getLogger(DefaultJsonWebToken.class);

    private final String rawToken;
    private final SignedJWT signedJWT;
    private final JWTClaimsSet claimsSet;

    /**
     * Creates a new JsonWebToken from a raw JWT string.
     *
     * @param rawToken The JWT token string
     * @throws JwtParsingException if the token cannot be parsed
     */
    public DefaultJsonWebToken(String rawToken) {
        this.rawToken = rawToken;
        try {
            this.signedJWT = SignedJWT.parse(rawToken);
            this.claimsSet = signedJWT.getJWTClaimsSet();
        } catch (ParseException e) {
            throw new JwtParsingException("Failed to parse JWT token", e);
        }
    }

    /**
     * Verifies the token signature using the provided verifier.
     *
     * @param verifier The JWS verifier to use
     * @return true if the signature is valid
     * @throws JwtValidationException if verification fails
     */
    public boolean verifySignature(JWSVerifier verifier) {
        try {
            return signedJWT.verify(verifier);
        } catch (JOSEException e) {
            throw new JwtValidationException("Failed to verify JWT signature", e);
        }
    }

    /**
     * Verifies the token signature using an RSA public key.
     *
     * @param publicKey The RSA public key
     * @return true if the signature is valid
     */
    public boolean verifySignature(RSAPublicKey publicKey) {
        try {
            RSASSAVerifier verifier = new RSASSAVerifier(publicKey);
            return verifySignature(verifier);
        } catch (Exception e) {
            throw new JwtValidationException("Failed to create RSA verifier", e);
        }
    }

    /**
     * Verifies the token signature using an EC public key.
     *
     * @param publicKey The EC public key
     * @return true if the signature is valid
     */
    public boolean verifySignature(ECPublicKey publicKey) {
        try {
            ECDSAVerifier verifier = new ECDSAVerifier(publicKey);
            return verifySignature(verifier);
        } catch (Exception e) {
            throw new JwtValidationException("Failed to create ECDSA verifier", e);
        }
    }

    /**
     * Verifies the token signature using an HMAC secret.
     *
     * @param secret The HMAC secret
     * @return true if the signature is valid
     */
    public boolean verifySignature(byte[] secret) {
        try {
            MACVerifier verifier = new MACVerifier(secret);
            return verifySignature(verifier);
        } catch (Exception e) {
            throw new JwtValidationException("Failed to create HMAC verifier", e);
        }
    }

    @Override
    public Optional<String> getIssuer() {
        return Optional.ofNullable(claimsSet.getIssuer());
    }

    @Override
    public Optional<String> getSubject() {
        return Optional.ofNullable(claimsSet.getSubject());
    }

    @Override
    public List<String> getAudience() {
        List<String> audience = claimsSet.getAudience();
        return audience != null ? audience : List.of();
    }

    @Override
    public Optional<Instant> getExpirationTime() {
        Date exp = claimsSet.getExpirationTime();
        return exp != null ? Optional.of(exp.toInstant()) : Optional.empty();
    }

    @Override
    public Optional<Instant> getNotBefore() {
        Date nbf = claimsSet.getNotBeforeTime();
        return nbf != null ? Optional.of(nbf.toInstant()) : Optional.empty();
    }

    @Override
    public Optional<Instant> getIssuedAt() {
        Date iat = claimsSet.getIssueTime();
        return iat != null ? Optional.of(iat.toInstant()) : Optional.empty();
    }

    @Override
    public Optional<String> getJwtId() {
        return Optional.ofNullable(claimsSet.getJWTID());
    }

    @Override
    public Optional<String> getClaimAsString(String claimName) {
        try {
            return Optional.ofNullable(claimsSet.getStringClaim(claimName));
        } catch (ParseException e) {
            logger.debug("Failed to parse claim '{}' as string", claimName, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Long> getClaimAsLong(String claimName) {
        try {
            Long value = claimsSet.getLongClaim(claimName);
            return value != null ? Optional.of(value) : Optional.empty();
        } catch (ParseException e) {
            logger.debug("Failed to parse claim '{}' as long", claimName, e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Boolean> getClaimAsBoolean(String claimName) {
        try {
            Boolean value = claimsSet.getBooleanClaim(claimName);
            return value != null ? Optional.of(value) : Optional.empty();
        } catch (ParseException e) {
            logger.debug("Failed to parse claim '{}' as boolean", claimName, e);
            return Optional.empty();
        }
    }

    @Override
    public List<String> getClaimAsStringList(String claimName) {
        try {
            List<String> values = claimsSet.getStringListClaim(claimName);
            return values != null ? values : List.of();
        } catch (ParseException e) {
            logger.debug("Failed to parse claim '{}' as string list", claimName, e);
            return List.of();
        }
    }

    @Override
    public Optional<Object> getClaim(String claimName) {
        return Optional.ofNullable(claimsSet.getClaim(claimName));
    }

    @Override
    public Set<String> getClaimNames() {
        return claimsSet.getClaims().keySet();
    }

    @Override
    public boolean isValid() {
        Instant now = Instant.now();

        // Check expiration
        Optional<Instant> exp = getExpirationTime();
        if (exp.isPresent() && now.isAfter(exp.get())) {
            return false;
        }

        // Check not before
        Optional<Instant> nbf = getNotBefore();
        if (nbf.isPresent() && now.isBefore(nbf.get())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isExpired() {
        return getExpirationTime()
            .map(exp -> Instant.now().isAfter(exp))
            .orElse(false);
    }

    @Override
    public String getRawToken() {
        return rawToken;
    }

    /**
     * Gets the underlying Nimbus SignedJWT object for advanced operations.
     *
     * @return The SignedJWT object
     */
    public SignedJWT getSignedJWT() {
        return signedJWT;
    }

    /**
     * Gets the underlying Nimbus JWTClaimsSet for advanced operations.
     *
     * @return The JWTClaimsSet object
     */
    public JWTClaimsSet getClaimsSet() {
        return claimsSet;
    }

    @Override
    public String toString() {
        return "JsonWebToken{" +
                "subject=" + getSubject().orElse("none") +
                ", issuer=" + getIssuer().orElse("none") +
                ", expiration=" + getExpirationTime().orElse(null) +
                ", valid=" + isValid() +
                '}';
    }
}
