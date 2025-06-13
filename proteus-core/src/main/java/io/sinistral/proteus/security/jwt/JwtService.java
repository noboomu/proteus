package io.sinistral.proteus.security.jwt;

import io.sinistral.proteus.security.SecurityContext;

import java.security.Key;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.Optional;

/**
 * Service for processing and validating JWT tokens.
 * This service handles token extraction, parsing, validation, and security context creation.
 * 
 * @since 1.0
 */
public interface JwtService {
    
    /**
     * Extracts a JWT token from an Authorization header.
     * Supports Bearer token format: "Bearer {token}"
     * 
     * @param authorizationHeader The Authorization header value
     * @return The extracted token or empty if not found/invalid format
     */
    Optional<String> extractTokenFromHeader(String authorizationHeader);
    
    /**
     * Parses a JWT token string into a JsonWebToken object.
     * 
     * @param tokenString The JWT token string
     * @return The parsed JsonWebToken
     * @throws JwtParsingException if the token cannot be parsed
     */
    JsonWebToken parseToken(String tokenString);
    
    /**
     * Validates a JWT token using the configured keys and validation rules.
     * 
     * @param token The token to validate
     * @return true if the token is valid
     */
    boolean validateToken(JsonWebToken token);
    
    /**
     * Validates a JWT token signature using an RSA public key.
     * 
     * @param token The token to validate
     * @param publicKey The RSA public key
     * @return true if the signature is valid
     */
    boolean validateTokenSignature(JsonWebToken token, RSAPublicKey publicKey);
    
    /**
     * Validates a JWT token signature using an EC public key.
     * 
     * @param token The token to validate
     * @param publicKey The EC public key
     * @return true if the signature is valid
     */
    boolean validateTokenSignature(JsonWebToken token, ECPublicKey publicKey);
    
    /**
     * Validates a JWT token signature using an HMAC secret.
     * 
     * @param token The token to validate
     * @param secret The HMAC secret
     * @return true if the signature is valid
     */
    boolean validateTokenSignature(JsonWebToken token, byte[] secret);
    
    /**
     * Creates a SecurityContext from a validated JWT token.
     * 
     * @param token The validated JWT token
     * @return A SecurityContext containing the token's security information
     */
    SecurityContext createSecurityContext(JsonWebToken token);
    
    /**
     * Processes an Authorization header and creates a SecurityContext if valid.
     * This method combines token extraction, parsing, validation, and context creation.
     * 
     * @param authorizationHeader The Authorization header value
     * @return A SecurityContext if the token is valid, empty otherwise
     */
    Optional<SecurityContext> processAuthorizationHeader(String authorizationHeader);
    
    /**
     * Checks if a token has any of the required roles.
     * 
     * @param token The token to check
     * @param requiredRoles The required roles
     * @return true if the token has at least one of the required roles
     */
    boolean hasAnyRole(JsonWebToken token, String... requiredRoles);
    
    /**
     * Checks if a token has all of the required roles.
     * 
     * @param token The token to check
     * @param requiredRoles The required roles
     * @return true if the token has all of the required roles
     */
    boolean hasAllRoles(JsonWebToken token, String... requiredRoles);
    
    /**
     * Gets the issuer whitelist for token validation.
     * Only tokens from these issuers will be considered valid.
     * 
     * @return List of allowed issuers, empty list allows all issuers
     */
    List<String> getAllowedIssuers();
    
    /**
     * Gets the audience whitelist for token validation.
     * Only tokens with these audiences will be considered valid.
     * 
     * @return List of allowed audiences, empty list allows all audiences
     */
    List<String> getAllowedAudiences();
    
    /**
     * Gets the clock skew tolerance for time-based validations.
     * 
     * @return Clock skew tolerance in seconds
     */
    long getClockSkewToleranceSeconds();
}
