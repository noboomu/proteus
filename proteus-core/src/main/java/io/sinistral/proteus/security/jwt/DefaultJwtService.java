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
 * This service provides comprehensive JWT processing including token extraction,
 * parsing, validation, and security context creation.
 * 
 * @since 1.0
 */
@Singleton
public class DefaultJwtService implements JwtService {
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultJwtService.class);
    private static final String BEARER_PREFIX = "Bearer ";
    
    private final JwtConfiguration configuration;
    
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
        if (trimmed.startsWith(BEARER_PREFIX)) {
            String token = trimmed.substring(BEARER_PREFIX.length()).trim();
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
        if (exp.isPresent() && now.isAfter(exp.get().plusSeconds(skewSeconds))) {
            return false;
        }
        
        // Check not before with skew tolerance
        Optional<Instant> nbf = token.getNotBefore();
        if (nbf.isPresent() && now.isBefore(nbf.get().minusSeconds(skewSeconds))) {
            return false;
        }
        
        return true;
    }
    
    private boolean isIssuerValid(JsonWebToken token) {
        List<String> allowedIssuers = getAllowedIssuers();
        if (allowedIssuers.isEmpty()) {
            return true; // No issuer restriction
        }
        
        Optional<String> tokenIssuer = token.getIssuer();
        return tokenIssuer.map(allowedIssuers::contains).orElse(false);
    }
    
    private boolean isAudienceValid(JsonWebToken token) {
        List<String> allowedAudiences = getAllowedAudiences();
        if (allowedAudiences.isEmpty()) {
            return true; // No audience restriction
        }
        
        List<String> tokenAudiences = token.getAudience();
        return tokenAudiences.stream().anyMatch(allowedAudiences::contains);
    }
    
    private boolean isSignatureValid(JsonWebToken token) {
        if (!(token instanceof DefaultJsonWebToken)) {
            logger.warn("Cannot validate signature for non-default token implementation");
            return true; // Skip validation for unknown implementations
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
        
        // If no keys are configured, skip signature validation
        boolean hasKeys = !configuration.getRsaPublicKeys().isEmpty() ||
                         !configuration.getEcPublicKeys().isEmpty() ||
                         !configuration.getHmacSecrets().isEmpty();
        
        if (!hasKeys) {
            logger.warn("No signing keys configured, skipping signature validation");
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
        
        // Add custom attributes from token claims
        for (String claimName : token.getClaimNames()) {
            if (!isStandardClaim(claimName)) {
                token.getClaim(claimName).ifPresent(value -> 
                    builder.attribute(claimName, value));
            }
        }
        
        return builder.build();
    }
    
    private boolean isStandardClaim(String claimName) {
        return "iss".equals(claimName) || "sub".equals(claimName) || 
               "aud".equals(claimName) || "exp".equals(claimName) ||
               "nbf".equals(claimName) || "iat".equals(claimName) ||
               "jti".equals(claimName) || "roles".equals(claimName) ||
               "permissions".equals(claimName) || "preferred_username".equals(claimName) ||
               "name".equals(claimName) || "username".equals(claimName);
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
