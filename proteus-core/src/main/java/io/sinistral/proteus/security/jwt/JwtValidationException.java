package io.sinistral.proteus.security.jwt;

/**
 * Exception thrown when JWT token validation fails.
 * This includes signature verification failures, key issues, and other validation problems.
 * 
 * @since 1.0
 */
public class JwtValidationException extends RuntimeException {
    
    /**
     * Constructs a new JWT validation exception with the specified detail message.
     * 
     * @param message The detail message
     */
    public JwtValidationException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new JWT validation exception with the specified detail message and cause.
     * 
     * @param message The detail message
     * @param cause The cause
     */
    public JwtValidationException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new JWT validation exception with the specified cause.
     * 
     * @param cause The cause
     */
    public JwtValidationException(Throwable cause) {
        super(cause);
    }
}
