package io.sinistral.proteus.security.jwt;

/**
 * Exception thrown when authentication fails due to JWT-related issues.
 * This includes missing tokens, invalid tokens, and authorization failures.
 *
 * @since 1.0
 */
public class JwtAuthenticationException extends RuntimeException {

    /**
     * Constructs a new JWT authentication exception with the specified detail message.
     *
     * @param message The detail message
     */
    public JwtAuthenticationException(String message) {
        super(message);
    }

    /**
     * Constructs a new JWT authentication exception with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause The cause
     */
    public JwtAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new JWT authentication exception with the specified cause.
     *
     * @param cause The cause
     */
    public JwtAuthenticationException(Throwable cause) {
        super(cause);
    }
}
